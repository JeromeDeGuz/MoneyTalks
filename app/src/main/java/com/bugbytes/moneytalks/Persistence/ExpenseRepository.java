package com.bugbytes.moneytalks.Persistence;

import com.bugbytes.moneytalks.Models.Expense;
import java.util.List;

public interface ExpenseRepository
{
    //Adds a new expense (@param: expense to add)
    void addExpense(Expense expense);

    //Deletes an expense (@param: expense to delete, @return: true if removed, false otherwise)
    boolean deleteExpense(Expense expense);

    //Returns all stored expenses (@return: list of expenses)
    List<Expense> getAllExpenses();
}