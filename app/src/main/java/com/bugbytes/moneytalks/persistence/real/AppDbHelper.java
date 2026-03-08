package com.bugbytes.moneytalks.persistence.real;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "moneytalks.db";
    private static final int DATABASE_VERSION = 1;

    public AppDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createExpenseTable(db);
        createCategoriesTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ExpenseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.CategoryEntry.TABLE_NAME);
        onCreate(db);
    }

    private void createExpenseTable(SQLiteDatabase db)
    {
        // Using TEXT for amount to maintain BigDecimal precision in SQLite
        String createExpenseTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.ExpenseEntry.TABLE_NAME + " (" +
                DbContract.ExpenseEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.ExpenseEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.ExpenseEntry.COLUMN_AMOUNT + " TEXT NOT NULL, " +
                DbContract.ExpenseEntry.COLUMN_CATEGORY + " TEXT, " +
                DbContract.ExpenseEntry.COLUMN_DATE + " TEXT, " +
                DbContract.ExpenseEntry.COLUMN_NOTE + " TEXT)";

        db.execSQL(createExpenseTableQuery);
    }

    public void createCategoriesTable(SQLiteDatabase db)
    {
        // Using TEXT for budget to ensure financial precision
        String createCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.CategoryEntry.TABLE_NAME + " (" +
                DbContract.CategoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.CategoryEntry.COLUMN_BUDGET + " TEXT NOT NULL)";

        db.execSQL(createCategoriesTableQuery);
    }
}