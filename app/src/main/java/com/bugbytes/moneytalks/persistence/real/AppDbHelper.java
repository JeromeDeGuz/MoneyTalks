package com.bugbytes.moneytalks.persistence.real;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "moneytalks.db";
    private static final int DATABASE_VERSION = 1;

    //AppDbHelper: Constructor to initialize the database helper. Takes in @param context.
    public AppDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //onCreate: It initializes the database schema. Takes in @param db.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createExpenseTable(db);
        createCategoriesTable(db);
    }

    //onUpgrade: It handles database version changes by dropping and recreating tables. Takes in @param db and oldVersion and newVersion.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ExpenseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.CategoryEntry.TABLE_NAME);
        onCreate(db);
    }

    //createExpenseTable: It executes the SQL query to create the expense table. Takes in @param db.
    private void createExpenseTable(SQLiteDatabase db)
    {
        String createExpenseTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.ExpenseEntry.TABLE_NAME + " (" +
                DbContract.ExpenseEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.ExpenseEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.ExpenseEntry.COLUMN_AMOUNT + " TEXT NOT NULL, " +
                DbContract.ExpenseEntry.COLUMN_CATEGORY + " TEXT, " +
                DbContract.ExpenseEntry.COLUMN_DATE + " TEXT, " +
                DbContract.ExpenseEntry.COLUMN_NOTE + " TEXT)";

        db.execSQL(createExpenseTableQuery);
    }

    //createCategoriesTable: It executes the SQL query to create the categories table. Takes in @param db.
    public void createCategoriesTable(SQLiteDatabase db)
    {
        String createCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.CategoryEntry.TABLE_NAME + " (" +
                DbContract.CategoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.CategoryEntry.COLUMN_BUDGET + " TEXT NOT NULL)";

        db.execSQL(createCategoriesTableQuery);
    }
}
