package com.bugbytes.moneytalks.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqlExpenseRepository implements ExpenseRepository
{
    private final AppDbHelper dbHelper;

    //SqlExpenseRepository: Constructor that initializes the database helper and populates default expenses. Takes in @param context.
    public SqlExpenseRepository(Context context)
    {
        this.dbHelper = new AppDbHelper(context);
    }

    //addExpense: It inserts a new expense record into the SQLite database. Takes in @param expense.
    @Override
    public void addExpense(Expense expense)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ExpenseEntry.COLUMN_NAME, expense.getName());
        values.put(DbContract.ExpenseEntry.COLUMN_AMOUNT, expense.getAmount().toPlainString());
        values.put(DbContract.ExpenseEntry.COLUMN_CATEGORY, expense.getCategory());
        values.put(DbContract.ExpenseEntry.COLUMN_DATE, expense.getDate().toString());
        values.put(DbContract.ExpenseEntry.COLUMN_NOTE, expense.getNote());

        db.insert(DbContract.ExpenseEntry.TABLE_NAME, null, values);
    }

    //deleteExpense: It removes an expense record matching the provided ID. Takes in @param expense and @return boolean result.
    @Override
    public boolean deleteExpense(Expense expense)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DbContract.ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expense.getId())};

        int deletedRows = db.delete(DbContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    //getAllExpenses: It retrieves all expense records from the database. Takes in nothing and @return List of expenses.
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

        //try-with-resources handles automatic cursor closure
        try (Cursor cursor = db.query(
                DbContract.ExpenseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        ))
        {
            while (cursor != null && cursor.moveToNext())
            {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NAME));
                String amountStr = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_AMOUNT));
                BigDecimal amount = new BigDecimal(amountStr);
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_CATEGORY));
                LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_DATE)));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NOTE));

                expenses.add(new Expense(id, name, amount, category, date, note));
            }
        }

        return expenses;
    }

    //updateExpense: It updates an existing expense record with new data. Takes in @param expense and @return boolean result.
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

    //getExpenseById: It fetches a single expense record using its unique ID. Takes in @param id and @return Expense object.
    @Override
    public Expense getExpenseById(long id)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Expense expense = null;

        String selection = DbContract.ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        try (Cursor cursor = db.query(
                DbContract.ExpenseEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null, null, null
        ))
        {
            if (cursor != null && cursor.moveToFirst())
            {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NAME));
                BigDecimal amount = new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_AMOUNT)));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_CATEGORY));
                LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_DATE)));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ExpenseEntry.COLUMN_NOTE));

                expense = new Expense(id, name, amount, category, date, note);
            }
        }
        return expense;
    }

    //isEmpty: It checks if the expense table is currently empty. Takes in nothing and @return boolean result.
    @Override
    public boolean isEmpty()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DbContract.ExpenseEntry.TABLE_NAME, null))
        {
            if (cursor != null && cursor.moveToFirst())
            {
                return cursor.getInt(0) == 0;
            }
        }
        return true;
    }

    //categoryExists: It checks if any expense record is using the specified category name. Takes in @param category and @return boolean result.
    @Override
    public boolean categoryExists(Category category)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "UPPER(" + DbContract.ExpenseEntry.COLUMN_CATEGORY + ") = UPPER(?)";
        String[] selectionArgs = {category.getName()};

        try (Cursor cursor = db.query(DbContract.ExpenseEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null))
        {
            return cursor != null && cursor.getCount() > 0;
        }
    }

    //updateExpenseCategory: It updates the category name for all expenses using an efficient SQL command. Takes in @param oldCategory and newCategory.
    @Override
    public void updateExpenseCategory(Category oldCategory, Category newCategory)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ExpenseEntry.COLUMN_CATEGORY, newCategory.getName());

        String selection = DbContract.ExpenseEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {oldCategory.getName()};

        //This replaces the Java loop with a single database-level update call
        db.update(DbContract.ExpenseEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}