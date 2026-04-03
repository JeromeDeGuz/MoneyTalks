package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CategoryService
{
    //addCategory: It validates and adds a new category. Takes in @param category. @throws ValidationException if data is invalid.
    void addCategory(Category category) throws ValidationException;

    //updateCategory: It edits an existing category. Takes in @param oldCategory and @param newCategory. @throws ValidationException if update fails.
    void updateCategory(Category oldCategory, Category newCategory) throws ValidationException;

    //deleteCategory: It removes a category from persistence. Takes in @param category. @throws ValidationException if deletion is restricted.
    void deleteCategory(Category category) throws ValidationException;

    //getCategory: Search for a specific category based on the string name. Takes in @param categoryName. Returns the Category object.
    Category getCategory(String categoryName);

    //getAllCategories: Returns a list of all categories currently in the system.
    List<Category> getAllCategories();

    //getMonthSpent: Calculates the total amount spent in a specific category for a given month. Takes in @param categoryName and @param targetDate. Returns BigDecimal.
    BigDecimal getMonthSpent(String categoryName, LocalDate targetDate);

    //hasExceededBudget: Checks if the spending in a category has crossed its set budget for the month. Takes in @param categoryName and @param targetDate. Returns boolean.
    boolean hasExceededBudget(String categoryName, LocalDate targetDate);
}