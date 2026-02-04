package com.bugbytes.moneytalks.persistence.real;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class SqlItemRepository implements ItemRepository {

    private final AppDbHelper dbHelper;

    public SqlItemRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<Expense> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                AppDbHelper.TABLE_ITEM,
                new String[]{AppDbHelper.COL_ITEM_ID, AppDbHelper.COL_ITEM_TITLE, AppDbHelper.COL_ITEM_DESC},
                null, null, null, null,
                AppDbHelper.COL_ITEM_ID + " DESC"
        );

        List<Expense> result = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(AppDbHelper.COL_ITEM_ID));
                String title = c.getString(c.getColumnIndexOrThrow(AppDbHelper.COL_ITEM_TITLE));
                String desc = c.getString(c.getColumnIndexOrThrow(AppDbHelper.COL_ITEM_DESC));
                result.add(new Expense(id, title, desc));
            }
        } finally {
            c.close();
        }
        return result;
    }

    @Override
    public Expense getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                AppDbHelper.TABLE_ITEM,
                new String[]{AppDbHelper.COL_ITEM_ID, AppDbHelper.COL_ITEM_TITLE, AppDbHelper.COL_ITEM_DESC},
                AppDbHelper.COL_ITEM_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        try {
            if (!c.moveToFirst()) return null;
            String title = c.getString(c.getColumnIndexOrThrow(AppDbHelper.COL_ITEM_TITLE));
            String desc = c.getString(c.getColumnIndexOrThrow(AppDbHelper.COL_ITEM_DESC));
            return new Expense(id, title, desc);
        } finally {
            c.close();
        }
    }

    @Override
    public Expense add(Expense item) {
        if (item == null) return null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(AppDbHelper.COL_ITEM_TITLE, item.getTitle());
            values.put(AppDbHelper.COL_ITEM_DESC, item.getDescription());

            long newId = db.insert(AppDbHelper.TABLE_ITEM, null, values);
            if (newId == -1) return null;

            int itemId = (int) newId;

            db.setTransactionSuccessful();
            return getById(itemId);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean update(Expense item) {
        if (item == null) return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(AppDbHelper.COL_ITEM_TITLE, item.getTitle());
            values.put(AppDbHelper.COL_ITEM_DESC, item.getDescription());

            int rows = db.update(
                    AppDbHelper.TABLE_ITEM,
                    values,
                    AppDbHelper.COL_ITEM_ID + "=?",
                    new String[]{String.valueOf(item.getId())}
            );

            if (rows == 0) return false;

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                AppDbHelper.TABLE_ITEM,
                AppDbHelper.COL_ITEM_ID + "=?",
                new String[]{String.valueOf(id)}
        );
        return rows > 0;
    }
}
