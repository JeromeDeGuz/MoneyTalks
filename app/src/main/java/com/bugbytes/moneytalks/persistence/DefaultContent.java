package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DefaultContent {
    //centralize where we will have default content populate in

    public void populateExpenses(ExpenseRepository expenseRepo){
        if(expenseRepo.isEmpty()){
            expenseRepo.addExpense(new Expense(0, "Uber", new BigDecimal("15.0"), "Transport", LocalDate.of(2026, 2, 1), "Palomino -> Crib"));
            expenseRepo.addExpense(new Expense(0, "Date", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 4), "Tinder date at IGI, he split the bill..."));
            expenseRepo.addExpense(new Expense(0, "Sportchek", new BigDecimal("20.0"), "Shopping", LocalDate.of(2026, 2, 6), "Nidecker supermatic bindings, and new Salomon snowboard"));
            expenseRepo.addExpense(new Expense(0, "Doordash", new BigDecimal("25.0"), "Food", LocalDate.of(2026, 2, 11), "Ramallah Shawarma"));
            expenseRepo.addExpense(new Expense(0, "Rent", new BigDecimal("1200.0"), "Housing", LocalDate.of(2026, 3, 4), "Feb Rent"));
            expenseRepo.addExpense(new Expense(0, "Gas", new BigDecimal("60.0"), "Transport", LocalDate.of(2026, 3, 7), "$1.79 per litre"));
            expenseRepo.addExpense(new Expense(0, "Monthly Car Payment", new BigDecimal("1000.0"), "Car", LocalDate.of(2026, 3, 12), "2026 Audi A5 Sportback"));
            expenseRepo.addExpense(new Expense(0, "Spotify", new BigDecimal("9.99"), "Subscriptions", LocalDate.of(2026, 2, 15), ""));
            expenseRepo.addExpense(new Expense(0, "Netflix", new BigDecimal("15.0"), "Subscriptions", LocalDate.of(2026, 2, 20), ""));
            expenseRepo.addExpense(new Expense(0, "Textbook", new BigDecimal("100.0"), "School", LocalDate.of(2026, 2, 23), "Clean Code"));
            expenseRepo.addExpense(new Expense(0, "Winter Tuition", new BigDecimal("8750.0"), "School", LocalDate.of(2026, 2, 5), "Comp 3350, Comp 4620, Comp 3190, Comp 3430"));

        }
    }

    public void populateCategories(CategoryRepository categoryRepo) {
        if (categoryRepo.isEmpty()) {
            categoryRepo.addCategory(new Category("Transport"));
            categoryRepo.addCategory(new Category("Food"));
            categoryRepo.addCategory(new Category("Shopping"));
            categoryRepo.addCategory(new Category("Housing"));
            categoryRepo.addCategory(new Category("Car"));
            categoryRepo.addCategory(new Category("Subscriptions"));
            categoryRepo.addCategory(new Category("School"));

        }
    }
}
