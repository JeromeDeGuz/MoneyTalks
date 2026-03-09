package com.bugbytes.moneytalks.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqlRepository implements ExpenseRepository
{
    private final AppDbHelper dbHelper;

    public SqlRepository(Context context)
    {
        this.dbHelper = new AppDbHelper(context);
        // Only add sample data if the database is empty to prevent duplicates on every instance creation
        if (isEmpty())
        {
            addExpense(new Expense(0, "Uber", new BigDecimal("15.0"), "Transport", LocalDate.of(2026, 2, 1), "Palomino -> Crib"));
            addExpense(new Expense(0, "Date", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 4), "Tinder date at IGI, he split the bill..."));
            addExpense(new Expense(0, "Sportchek", new BigDecimal("20.0"), "Shopping", LocalDate.of(2026, 2, 6), "Nidecker supermatic bindings, and new Salomon snowboard"));
        }
    }

    @Override
    public void addExpense(Expense expense)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ExpenseEntry.COLUMN_NAME, expense.getName());
        // Store BigDecimal as String to prevent precision loss
        values.put(DbContract.ExpenseEntry.COLUMN_AMOUNT, expense.getAmount().toPlainString());
        values.put(DbContract.ExpenseEntry.COLUMN_CATEGORY, expense.getCategory());
        values.put(DbContract.ExpenseEntry.COLUMN_DATE, expense.getDate().toString());
        values.put(DbContract.ExpenseEntry.COLUMN_NOTE, expense.getNote());

        db.insert(DbContract.ExpenseEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean deleteExpense(Expense expense)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DbContract.ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expense.getId())};

        int deletedRows = db.delete(DbContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    @Override
    public List<Expense> getAllExpenses()
    {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DbContract.ExpenseEntry.COLUMN_ID,
                DbContract.ExpenseEntry.COLUMN_NAME,
                DbContract.ExpenseEntry.COLUMN_AMOUNT,
                DbContract.ExpenseEntry.COLUMN_CATEGORY,
                DbContract.ExpenseEntry.COLUMN_DATE,
                DbContract.ExpenseEntry.COLUMN_NOTE
        };

        Cursor cursor = db.query(
                DbContract.ExpenseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NAME));
            // Read as String and convert back to BigDecimal for full precision
            String amountStr = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_AMOUNT));
            BigDecimal amount = new BigDecimal(amountStr);
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_CATEGORY));
            LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_DATE)));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NOTE));

            expenses.add(new Expense(id, name, amount, category, date, note));
        }
        cursor.close();

        return expenses;
    }

    @Override
    public boolean updateExpense(Expense expense)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ExpenseEntry.COLUMN_NAME, expense.getName());
        values.put(DbContract.ExpenseEntry.COLUMN_AMOUNT, expense.getAmount().toPlainString());
        values.put(DbContract.ExpenseEntry.COLUMN_CATEGORY, expense.getCategory());
        values.put(DbContract.ExpenseEntry.COLUMN_DATE, expense.getDate().toString());
        values.put(DbContract.ExpenseEntry.COLUMN_NOTE, expense.getNote());

        String selection = DbContract.ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expense.getId())};

        int count = db.update(
                DbContract.ExpenseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count > 0;
    }

    @Override
    public boolean isEmpty()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DbContract.ExpenseEntry.TABLE_NAME);
        return count == 0;
    }
}
