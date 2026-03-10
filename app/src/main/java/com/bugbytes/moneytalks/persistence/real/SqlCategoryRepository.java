package com.bugbytes.moneytalks.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SqlCategoryRepository implements CategoryRepository
{
    private final AppDbHelper dbHelper;

    public SqlCategoryRepository(Context context)
    {
        this.dbHelper = new AppDbHelper(context);
        if(isEmpty()){
            addCategory(new Category("Transport"));
            addCategory(new Category("Food"));
            addCategory(new Category("Shopping"));
        }

    }

    @Override
    public void addCategory(Category category)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.CategoryEntry.COLUMN_NAME, category.getName());
        values.put(DbContract.CategoryEntry.COLUMN_BUDGET, category.getBudget().toPlainString());

        db.insert(DbContract.CategoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DbContract.CategoryEntry.TABLE_NAME,
                null, null, null, null, null, null
        );

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_NAME));
            BigDecimal budget = new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_BUDGET)));

            categories.add(new Category(id, name, budget));
        }
        cursor.close();
        return categories;
    }

    @Override
    public void updateCategory(Category oldCategory, Category newCategory)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.CategoryEntry.COLUMN_NAME, newCategory.getName());
        values.put(DbContract.CategoryEntry.COLUMN_BUDGET, newCategory.getBudget().toPlainString());

        String selection = DbContract.CategoryEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(oldCategory.getId())};

        db.update(DbContract.CategoryEntry.TABLE_NAME, values, selection, selectionArgs);

    }

    @Override
    public void deleteCategory(Category category)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbContract.CategoryEntry.TABLE_NAME,
                DbContract.CategoryEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    @Override
    public Category getCategoryByName(String name)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.CategoryEntry.TABLE_NAME, null,
                DbContract.CategoryEntry.COLUMN_NAME + " = ?", new String[]{name},
                null, null, null);

        Category category = null;
        if (cursor.moveToFirst())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_ID));
            BigDecimal budget = new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_BUDGET)));
            category = new Category(id, name, budget);
        }
        cursor.close();
        return category;
    }

    @Override
    public boolean isEmpty() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.CategoryEntry.TABLE_NAME, null, null, null, null, null, null);
        boolean isEmpty = cursor.getCount() == 0;
        cursor.close();
        return isEmpty;
    }

}