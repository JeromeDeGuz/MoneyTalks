package com.bugbytes.moneytalks.persistence.real;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "todo.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_ITEM = "items";
    public static final String COL_ITEM_ID = "id";
    public static final String COL_ITEM_TITLE = "title";
    public static final String COL_ITEM_DESC = "description";
    public static final String COL_IK_ITEM_ID = "item_id";

    public AppDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_ITEM + " (" +
                        COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_ITEM_TITLE + " TEXT NOT NULL, " +
                        COL_ITEM_DESC + " TEXT" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple sample: drop & recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);
    }
}
