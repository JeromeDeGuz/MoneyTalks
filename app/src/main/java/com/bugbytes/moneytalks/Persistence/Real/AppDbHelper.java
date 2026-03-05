package com.bugbytes.moneytalks.Persistence.Real;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoneyTalks.db";

    private static final String SQL_CREATE_EXPENSES =
            "CREATE TABLE " + DbContract.ExpenseEntry.TABLE_NAME + " (" +
                    DbContract.ExpenseEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DbContract.ExpenseEntry.COLUMN_NAME + " TEXT," +
                    DbContract.ExpenseEntry.COLUMN_AMOUNT + " REAL," +
                    DbContract.ExpenseEntry.COLUMN_CATEGORY + " TEXT," +
                    DbContract.ExpenseEntry.COLUMN_DATE + " TEXT," +
                    DbContract.ExpenseEntry.COLUMN_NOTE + " TEXT)";

    private static final String SQL_DELETE_EXPENSES =
            "DROP TABLE IF EXISTS " + DbContract.ExpenseEntry.TABLE_NAME;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EXPENSES);
        onCreate(db);
    }
}
