package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Category;

public interface CategoryService
{
    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Category category);

    // Change parameter type to String for name-based lookup
    Category getCategory(String categoryName);
}