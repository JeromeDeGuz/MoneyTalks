package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeExpenseRepository implements ExpenseRepository
{
    private static final List<Expense> expenses = new ArrayList<>();
    private static long autoIncrementId = 100;

    //FakeExpenseRepository: Constructor for the fake repository. Takes in nothing.
    public FakeExpenseRepository()
    {
    }

    //addExpense: It adds a new expense with a generated ID. Takes in @param expense.
    @Override
    public void addExpense(Expense expense)
    {
        //Re-creating the object to assign the final id through the constructor
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

    //deleteExpense: It removes an expense by matching its ID. Takes in @param expense and @return boolean result.
    @Override
    public boolean deleteExpense(Expense expense)
    {
        //Find by ID because the object instance might be different (e.g. recreated from DB/List)
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

    //updateExpense: It replaces an existing expense record. Takes in @param expense and @return boolean result.
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

    //getAllExpenses: It retrieves the full list of expenses. Takes in nothing and @return List of expenses.
    @Override
    public List<Expense> getAllExpenses()
    {
        //Return a copy to avoid external modification of the internal list
        return new ArrayList<>(expenses);
    }

    //getExpenseById: It fetches a single expense by its unique ID. Takes in @param id and @return Expense object.
    @Override
    public Expense getExpenseById(long id)
    {
        for (Expense e : expenses)
        {
            if (e.getId() == id)
            {
                return e;
            }
        }
        return null;
    }

    //isEmpty: It checks if the repository is empty. Takes in nothing and @return boolean result.
    @Override
    public boolean isEmpty()
    {
        return expenses.isEmpty();
    }

    //categoryExists: It checks if a category name is currently used by any expense. Takes in @param category and @return boolean result.
    @Override
    public boolean categoryExists(Category category)
    {
        for (Expense e : expenses)
        {
            if (e.getCategory().equalsIgnoreCase(category.getName()))
            {
                return true;
            }
        }
        return false;
    }

    //updateExpenseCategory: It updates the category string for all matching expenses. Takes in @param oldCategory and newCategory.
    @Override
    public void updateExpenseCategory(Category oldCategory, Category newCategory)
    {
        for (Expense e : expenses)
        {
            if (e.getCategory().equalsIgnoreCase(oldCategory.getName()))
            {
                e.setCategory(newCategory.getName());
            }
        }
    }
}
