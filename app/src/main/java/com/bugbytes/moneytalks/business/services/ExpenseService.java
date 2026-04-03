package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Expense;

import java.util.List;

//This defines business rule for our app, acts as contract that any ExpenseService implementation must follow.
public interface ExpenseService
{
    //addExpense: Adds a new expense to the system. Takes in @param expense. @throws ValidationException if data is invalid.
    void addExpense(Expense expense) throws ValidationException;

    //getAllExpenses: Retrieves all stored expenses from the system. Returns @return List<Expense>.
    List<Expense> getAllExpenses();

    //deleteExpense: Removes an expense record from persistence. Takes in @param expense. Returns @return true if successful.
    boolean deleteExpense(Expense expense);

    //updateExpense: Edits an existing expense record. Takes in @param expense. Returns @return true if successful. @throws ValidationException if update fails.
    boolean updateExpense(Expense expense) throws ValidationException;

    //getExpensesSortedByDate: Sorts expenses based on their date. Takes in @param newestFirst. Returns @return List<Expense>.
    List<Expense> getExpensesSortedByDate(boolean newestFirst);

    //getExpensesByCategorySortedByDate: Filters expenses by category then sorts by date. Takes in @param categoryName and @param newestFirst. Returns @return List<Expense>.
    List<Expense> getExpensesByCategorySortedByDate(String categoryName, boolean newestFirst);

    //getExpenseById: Finds a specific expense by its unique identifier. Takes in @param id. Returns @return the Expense object.
    Expense getExpenseById(long id);

    //addExpense: Helper method for string-based inputs from UI. Takes in @param name, amountStr, category, dateStr, and notes. @throws ValidationException if parsing or validation fails.
    void addExpense(String name, String amountStr, String category, String dateStr, String notes) throws ValidationException;

    //updateExpense: Helper method to update expense using string-based inputs. Takes in @param id, name, amountStr, category, dateStr, and notes. Returns @return true if successful. @throws ValidationException if parsing fails.
    boolean updateExpense(long id, String name, String amountStr, String category, String dateStr, String notes) throws ValidationException;
}