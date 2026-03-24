package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Category;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface CategoryService
{
    //addCategory: It adds a new category.Takes in @param category.
    void addCategory(Category category);

    //updateCategory: It edits an existing category. Takes in @param oldCategory, @param newCategory.
    void updateCategory(Category oldCategory, Category newCategory);

    //deleteCategory: It removes a category from persistence. Takes in @param category.
    void deleteCategory(Category category);

    //getCategory: Search for specific category based on string name.
    Category getCategory(String categoryName);

    //getAllCategories: Returns list of all categories.
    List<Category> getAllCategories();

    BigDecimal getMonthSpent(String categoryName, LocalDate targetDate);
    boolean hasExceededBudget(String categoryName, LocalDate targetDate);
}