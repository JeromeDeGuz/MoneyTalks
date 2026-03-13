package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.models.Category;

import java.util.List;


public interface ExpenseRepository
{
    //Adds a new expense (@param: expense to add)
    void addExpense(Expense expense);

    //Deletes an expense (@param: expense to delete, @return: true if removed, false otherwise)
    boolean deleteExpense(Expense expense);

    //Returns all stored expenses (@return: list of expenses)
    List<Expense> getAllExpenses();

    //Plan for an update path early to support future editing
    boolean updateExpense(Expense expense);

    Expense getExpenseById(long id);

    boolean isEmpty();

    boolean categoryExists(Category category);

    void updateExpenseCategory(Category oldCategory, Category newCategory);
}


