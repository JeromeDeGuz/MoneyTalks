package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Models.Expense;
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

    boolean updateExpense(Expense expense);
}

