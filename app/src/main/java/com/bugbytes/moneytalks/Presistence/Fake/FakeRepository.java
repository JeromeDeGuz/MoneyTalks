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
    private static int autoIncrementId = 100;


    public FakeRepository(){
        //create temporary non-persistent data
        Expense temp1 = new Expense( "Uber", 15.0, "Transport", "2026/02/01", "Palomino -> Crib");
        Expense temp2 = new Expense( "Date", 45.0, "Food", "2026/02/04", "Tinder date at IGI, he split the bill...");
        Expense temp3 = new Expense( "Sportchek", 20.0, "Shopping", "2026/02/06", "Nidecker supermatic bindings, and new Salomon snowboard");
        addExpense(temp1);
        addExpense(temp2);
        addExpense(temp3);
    }

    @Override
    public void addExpense(Expense expense)
    {
        expense.setId(autoIncrementId);
        expenses.add(expense);
        autoIncrementId++;
    }

    @Override
    public void deleteExpense(Expense expense){
        expenses.remove(expense);
    }

    @Override
    public List<Expense> getAllExpenses()
    {
        return expenses;
    }
}