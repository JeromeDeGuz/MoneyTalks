package com.bugbytes.moneytalks.Presistence.Fake;

//Model layer
import com.bugbytes.moneytalks.Models.Expense;
//Presistence layer
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
import java.util.ArrayList;
import java.util.List;

public class FakeRepository implements ExpenseRepository
{
    //We use a static list so data persists while the app is running
    private static final List<Expense> expenses = new ArrayList<>();

    @Override
    public void addExpense(Expense expense)
    {
        expenses.add(expense);
    }

    @Override
    public List<Expense> getAllExpenses()
    {
        return expenses;
    }
}