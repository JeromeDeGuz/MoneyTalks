package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.models.Expense;

import com.bugbytes.moneytalks.persistence.ExpenseRepository;
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
        //create temporary non-persistent data
        Expense temp1 = new Expense( "Uber", 15.0, "Transport", "2026/02/01", "Palomino -> Crib");
        Expense temp2 = new Expense( "Date", 45.0, "Food", "2026/02/04", "Tinder date at IGI, he split the bill...");
        Expense temp3 = new Expense( "Sportchek", 20.0, "Shopping", "2026/02/06", "Nidecker supermatic bindings, and new Salomon snowboard");
        addExpense(temp1);
        addExpense(temp2);
        addExpense(temp3);
    }

    //Adds an expense (@param: expense to add)
    @Override
    public void addExpense(Expense expense)
    {
        expense.setId(autoIncrementId); //Assigns unique id
        expenses.add(expense); //stores in list
        autoIncrementId++; //increment id for next expense
    }

    //Deletes an expense (@param: expense to delete, @return: true if removed, false otherwise)
    @Override
    public boolean deleteExpense(Expense expense)
    {
        System.out.println("PRINTING: "+expense.getName());
        return expenses.remove(expense);
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
        return expenses;
    }
}