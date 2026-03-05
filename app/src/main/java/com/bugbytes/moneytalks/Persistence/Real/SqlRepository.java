package com.bugbytes.moneytalks.Persistence.Real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.Persistence.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;

public class SqlRepository implements ExpenseRepository {
    private final AppDbHelper dbHelper;

    public SqlRepository(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    @Override
    public void addExpense(Expense expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ExpenseEntry.COLUMN_NAME, expense.getName());
        values.put(DbContract.ExpenseEntry.COLUMN_AMOUNT, expense.getAmount());
        values.put(DbContract.ExpenseEntry.COLUMN_CATEGORY, expense.getCategory());
        values.put(DbContract.ExpenseEntry.COLUMN_DATE, expense.getDate());
        values.put(DbContract.ExpenseEntry.COLUMN_NOTE, expense.getNote());

        long newRowId = db.insert(DbContract.ExpenseEntry.TABLE_NAME, null, values);
        expense.setId((int) newRowId);
    }

    @Override
    public boolean deleteExpense(Expense expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DbContract.ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(expense.getId()) };
        int deletedRows = db.delete(DbContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    @Override
    public List<Expense> getAllExpenses() {
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

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NAME));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_AMOUNT));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_CATEGORY));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_DATE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NOTE));

            Expense expense = new Expense(name, amount, category, date, note);
            expense.setId(id);
            expenses.add(expense);
        }
        cursor.close();
        return expenses;
    }
}
