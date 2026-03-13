package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Category;

import java.util.List;

public interface CategoryRepository
{
    void addCategory(Category category);

    List<Category> getAllCategories();

    void updateCategory(Category oldCategory, Category newCategory);

    void deleteCategory(Category category);

    Category getCategoryByName(String name);

    boolean isEmpty();

}