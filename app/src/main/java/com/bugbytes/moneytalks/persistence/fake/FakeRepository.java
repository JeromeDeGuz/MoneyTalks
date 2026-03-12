package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.PersistenceException;
import com.bugbytes.moneytalks.persistence.DefaultContent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeRepository implements ExpenseRepository {
    //Static list so data persists while the app is running
    private static final List<Expense> expenses = new ArrayList<>();
    private static long autoIncrementId = 100; // Updated to long to match Expense model

    //Constructor: creates temporary, non-persistent sample data
    public FakeRepository() {

        // Only add sample data if the list is empty to prevent duplicates on every instance creation
        DefaultContent defaultContent = new DefaultContent();
        defaultContent.populateExpenses(this);
    }

    //Adds an expense (@param: expense to add)
    @Override
    public void addExpense(Expense expense) {
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
    public boolean deleteExpense(Expense expense) {
        // Find by ID because the object instance might be different (e.g. recreated from DB/List)
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == expense.getId()) {
                expenses.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateExpense(Expense expense) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == expense.getId()) {
                expenses.set(i, expense);
                return true;
            }
        }
        return false;
    }

    //Returns all expenses (@return: list of all stored expenses)
    @Override
    public List<Expense> getAllExpenses() {
        // Return a copy to avoid external modification of the internal list
        return new ArrayList<>(expenses);
    }


    // NEW: Implementation for getExpenseById to fix interface mismatch error
    @Override
    public Expense getExpenseById(long id) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return expenses.isEmpty();
    }

    @Override
    public boolean categoryExists(Category category) {
        for (Expense e : expenses) {
            if (e.getCategory().equals(category.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateExpenseCategory(Category oldCategory, Category newCategory) {
        for (Expense e : expenses) {
            if (e.getCategory().equals(oldCategory.getName())) {
                e.setCategory(newCategory.getName());
                updateExpense(e);
            }
        }
        throw new PersistenceException("Failed to update category");
    }
}