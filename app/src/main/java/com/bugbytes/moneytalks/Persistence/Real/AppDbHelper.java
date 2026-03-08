package com.bugbytes.moneytalks.Persistence.Real;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moneytalks.db";
    private static final int DATABASE_VERSION = 1;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createExpenseTable(db);
        createCategoriesTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ExpenseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.CategoryEntry.TABLE_NAME);

        //TODO: What do do with oldVersion/newVersion?
        onCreate(db);
    }

    private void createExpenseTable(SQLiteDatabase db) {
        String createExpenseTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.ExpenseEntry.TABLE_NAME + " (" +
                DbContract.ExpenseEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.ExpenseEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.ExpenseEntry.COLUMN_AMOUNT + " REAL CHECK(" + DbContract.ExpenseEntry.COLUMN_AMOUNT + " >= 0), " +
                //TODO: What are our assumptions when it comes to amount for an expense? Are we allowed negative expenses?
                DbContract.ExpenseEntry.COLUMN_CATEGORY + " TEXT, " +
                DbContract.ExpenseEntry.COLUMN_DATE + " TEXT, " +
                DbContract.ExpenseEntry.COLUMN_NOTE + " TEXT)";

        db.execSQL(createExpenseTableQuery);
    }

    public void createCategoriesTable(SQLiteDatabase db) {
        String createCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.CategoryEntry.TABLE_NAME + " (" +
                DbContract.CategoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.CategoryEntry.COLUMN_BUDGET + " REAL CHECK(" + DbContract.CategoryEntry.COLUMN_BUDGET + " >= 0))";

        db.execSQL(createCategoriesTableQuery);
    }
}
