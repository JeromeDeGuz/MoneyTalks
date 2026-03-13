package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Expense;

import java.util.List;

//This defines business rule for our app, acts as contract that any ExpenseService implementation must follow.
public interface ExpenseService
{
    //Adds a new expense (@param: expense to add).
    void addExpense(Expense expense);

    //Retrieves all expenses (@return: list of expenses).
    List<Expense> getAllExpenses();

    //Deletes an expense (@param: expense to delete, @return: true if successful).
    boolean deleteExpense(Expense expense);

    // Updates an existing expense (@param: updated expense, @return: true if successful)
    boolean updateExpense(Expense expense);

    // Retrieves expenses sorted by date (@param: newestFirst toggles sort order, @return: sorted list).
    List<Expense> getExpensesSortedByDate(boolean newestFirst);

    //Retrieves expenses filtered by category and sorted by date (@param: categoryName, newestFirst toggles sort order, @return: sorted list).
    List<Expense> getExpensesByCategorySortedByDate(String categoryName, boolean newestFirst);

    // Retrieves a single expense by its ID for editing purposes (@param: id, @return: the found expense or null)
    Expense getExpenseById(long id);
}