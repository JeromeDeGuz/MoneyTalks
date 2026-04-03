package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Category;

import java.util.List;

public interface CategoryRepository
{
    //addCategory: It adds a new category to the persistence layer. Takes in @param category.
    void addCategory(Category category);

    //getAllCategories: It retrieves all categories currently stored. Takes in nothing and @return List of categories.
    List<Category> getAllCategories();

    //updateCategory: It modifies an existing category's details. Takes in @param oldCategory and newCategory.
    void updateCategory(Category oldCategory, Category newCategory);

    //deleteCategory: It removes a specific category from persistence. Takes in @param category.
    void deleteCategory(Category category);

    //getCategoryByName: It searches for a category by its name string. Takes in @param name and @return Category object.
    Category getCategoryByName(String name);

    //isEmpty: It checks if the repository has no categories. Takes in nothing and @return boolean result.
    boolean isEmpty();
}