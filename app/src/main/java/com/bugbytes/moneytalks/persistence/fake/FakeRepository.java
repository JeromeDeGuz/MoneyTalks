package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeRepository implements ExpenseRepository
{
    //Static list so data persists while the app is running
    private static final List<Expense> expenses = new ArrayList<>();
    private static int autoIncrementId = 100;

    //Constructor: creates temporary, non-persistent sample data
    public FakeRepository()
    {

        // Only add sample data if the list is empty to prevent duplicates on every instance creation
        if (isEmpty())
        {
            Expense temp1 = new Expense(0, "Uber", new BigDecimal("15.0"), "Transport", LocalDate.of(2026, 2, 1), "Palomino -> Crib");
            Expense temp2 = new Expense(0, "Date", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 4), "Tinder date at IGI, he split the bill...");
            Expense temp3 = new Expense(0, "Sportchek", new BigDecimal("20.0"), "Shopping", LocalDate.of(2026, 2, 6), "Nidecker supermatic bindings, and new Salomon snowboard");
            addExpense(temp1);
            addExpense(temp2);
            addExpense(temp3);
        }
    }

    //Adds an expense (@param: expense to add)
    @Override
    public void addExpense(Expense expense)
    {
        // Re-creating the object to assign the final id through the constructor
        Expense expenseWithId = new Expense(
                autoIncrementId,
                expense.getName(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate(),
                expense.getNote()
        );
        expenses.add(expenseWithId); //stores in list
        autoIncrementId++; //increment id for next expense
    }

    //Deletes an expense (@param: expense to delete, @return: true if removed, false otherwise)
    @Override
    public boolean deleteExpense(Expense expense)
    {
        // Find by ID because the object instance might be different (e.g. recreated from DB/List)
        for (int i = 0; i < expenses.size(); i++)
        {
            if (expenses.get(i).getId() == expense.getId())
            {
                expenses.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateExpense(Expense expense)
    {
        for (int i = 0; i < expenses.size(); i++)
        {
            if (expenses.get(i).getId() == expense.getId())
            {
                expenses.set(i, expense);
                return true;
            }
        }
        return false;
    }

    //Returns all expenses (@return: list of all stored expenses)
    @Override
    public List<Expense> getAllExpenses()
    {
        // Return a copy to avoid external modification of the internal list
        return new ArrayList<>(expenses);
    }

    @Override
    public boolean isEmpty(){
        return expenses.isEmpty();
    }
}