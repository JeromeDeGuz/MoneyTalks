package com.bugbytes.moneytalks.Persistence;

import com.bugbytes.moneytalks.Models.Expense;
import java.util.List;

public interface ExpenseRepository
{
    void addExpense(Expense expense);

    boolean deleteExpense(Expense expense);
    List<Expense> getAllExpenses();
}