package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Expense;
import java.util.List;

public interface ExpenseRepository
{
    //Adds a new expense (@param: expense to add)
    void addExpense(Expense expense);

    //Deletes an expense (@param: expense to delete, @return: true if removed, false otherwise)
    boolean deleteExpense(Expense expense);

    //Returns all stored expenses (@return: list of expenses)
    List<Expense> getAllExpenses();

    boolean updateExpense(Expense expense);
}