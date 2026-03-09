package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class defaultContent {
    //declaration of default expenses
    List<Expense> defaultExpenses;

    public defaultContent() {
        //initialize list of default expenses
        defaultExpenses.add(new Expense(0,"Uber", new BigDecimal("15.0"), "Transport", LocalDate.of(2026, 2, 1), "Palomino -> Crib"));
        defaultExpenses.add(new Expense( 0,"Date", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 4), "Tinder date at IGI, he split the bill..."));
        defaultExpenses.add(new Expense(0, "Sportchek", new BigDecimal("20.0"), "Shopping", LocalDate.of(2026, 2, 6), "Nidecker supermatic bindings, and new Salomon snowboard"));
    }

    public List<Expense> getDefaultExpenses() {
        return defaultExpenses;
    }

    public void setDefaultExpenses(ExpenseRepository database) {
        for(Expense expense : defaultExpenses){
            database.addExpense(expense);
        }
    }
}
