package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;

import java.util.List;

public interface ExpenseRepository
{
    //addExpense: It adds a new expense record to the persistence layer. Takes in @param expense.
    void addExpense(Expense expense);

    //deleteExpense: It removes an expense record from the system. Takes in @param expense and @return boolean result.
     boolean deleteExpense(Expense expense);

    //getAllExpenses: It retrieves the full list of stored expenses. Takes in nothing and @return List of expenses.
    List<Expense> getAllExpenses();

    //updateExpense: It modifies an existing expense record. Takes in @param expense and @return boolean result.
    boolean updateExpense(Expense expense);

    //getExpenseById: It searches for a specific expense using its unique identifier. Takes in @param id and @return Expense object.
    Expense getExpenseById(long id);

    //isEmpty: It checks if the repository contains no expense records. Takes in nothing and @return boolean result.
    boolean isEmpty();

    //categoryExists: It verifies if a specific category is linked to any expenses. Takes in @param category and @return boolean result.
    boolean categoryExists(Category category);

    //updateExpenseCategory: It updates the category reference for all matching expenses. Takes in @param oldCategory and newCategory.
    void updateExpenseCategory(Category oldCategory, Category newCategory);
}