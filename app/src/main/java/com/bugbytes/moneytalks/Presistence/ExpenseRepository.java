package com.bugbytes.moneytalks.Presistence;

import com.bugbytes.moneytalks.Models.Expense;
import java.util.List;

public interface ExpenseRepository
{
    void addExpense(Expense expense);
    List<Expense> getAllExpenses();
}