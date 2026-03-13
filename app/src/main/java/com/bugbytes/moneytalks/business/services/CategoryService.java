package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Category;

import java.util.List;

public interface CategoryService
{
    void addCategory(Category category);

    void updateCategory(Category oldCategory, Category newCategory);

    void deleteCategory(Category category);

    // Change parameter type to String for name-based lookup
    Category getCategory(String categoryName);

    // This is crucial for your Spinner/AutoComplete list in the UI
    List<Category> getAllCategories();
}