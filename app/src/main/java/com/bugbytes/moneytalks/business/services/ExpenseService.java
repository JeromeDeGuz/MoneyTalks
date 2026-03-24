package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Expense;
import java.util.List;

//This defines business rule for our app, acts as contract that any ExpenseService implementation must follow.
public interface ExpenseService
{
    //addExpense: Adds a new expense to the system. Takes in @param expense.
    void addExpense(Expense expense) throws ValidationException;

    //getAllExpenses: Retrieves all stored expenses. Takes in nothing and @return List<Expense>.
    List<Expense> getAllExpenses();

    //deleteExpense: Removes an expense from persistence. Takes in @param expense and @return true if successful.
    boolean deleteExpense(Expense expense);

    //updateExpense: Edits an existing expense record. Takes in @param expense and @return true if successful.
    boolean updateExpense(Expense expense) throws ValidationException;

    //getExpensesSortedByDate: Sorts expenses based on date. Takes in @param newestFirst and @return List<Expense>.
    List<Expense> getExpensesSortedByDate(boolean newestFirst);

    //getExpensesByCategorySortedByDate: Filters by category then sorts. Takes in @param categoryName and newestFirst and @return List<Expense>.
    List<Expense> getExpensesByCategorySortedByDate(String categoryName, boolean newestFirst);

    //getExpenseById: Finds a specific expense by unique ID. Takes in @param id and @return Expense.
    Expense getExpenseById(long id);

    // new string-based versions for presentation layer
    void addExpense(String name, String amountStr, String category, String dateStr, String notes) throws ValidationException;
    boolean updateExpense(long id, String name, String amountStr, String category, String dateStr, String notes) throws ValidationException;


}