package com.bugbytes.moneytalks.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SqlCategoryRepository implements CategoryRepository
{
    private final AppDbHelper dbHelper;

    //SqlCategoryRepository: Constructor that initializes the database helper and populates default categories. Takes in @param context.
    public SqlCategoryRepository(Context context)
    {
        this.dbHelper = new AppDbHelper(context);
    }

    //addCategory: It inserts a new category record into the SQLite database. Takes in @param category.
    @Override
    public void addCategory(Category category)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.CategoryEntry.COLUMN_NAME, category.getName());
        values.put(DbContract.CategoryEntry.COLUMN_BUDGET, category.getBudget().toPlainString());

        db.insert(DbContract.CategoryEntry.TABLE_NAME, null, values);
    }

    //getAllCategories: It queries the database for all category records and returns them as a list. Takes in nothing and @return List of categories.
    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //try-with-resources ensures the cursor is closed even if an error occurs
        try (Cursor cursor = db.query(
                DbContract.CategoryEntry.TABLE_NAME,
                null, null, null, null, null, null
        ))
        {
            while (cursor != null && cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_NAME));
                BigDecimal budget = new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_BUDGET)));

                categories.add(new Category(id, name, budget));
            }
        }
        return categories;
    }

    //updateCategory: It updates an existing category record based on its unique identifier. Takes in @param oldCategory and newCategory.
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

    //deleteCategory: It removes a specific category record from the database. Takes in @param category.
    @Override
    public void deleteCategory(Category category)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbContract.CategoryEntry.TABLE_NAME,
                DbContract.CategoryEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    //getCategoryByName: It retrieves a single category matching the specified name. Takes in @param name and @return Category object.
    @Override
    public Category getCategoryByName(String name)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Category category = null;

        try (Cursor cursor = db.query(DbContract.CategoryEntry.TABLE_NAME, null,
                DbContract.CategoryEntry.COLUMN_NAME + " = ?", new String[]{name},
                null, null, null))
        {
            if (cursor != null && cursor.moveToFirst())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_ID));
                BigDecimal budget = new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_BUDGET)));
                category = new Category(id, name, budget);
            }
        }
        return category;
    }

    //isEmpty: It checks the database to see if the categories table has any records. Takes in nothing and @return boolean result.
    @Override
    public boolean isEmpty()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DbContract.CategoryEntry.TABLE_NAME, null))
        {
            if (cursor != null && cursor.moveToFirst())
            {
                return cursor.getInt(0) == 0;
            }
        }
        return true;
    }
}
