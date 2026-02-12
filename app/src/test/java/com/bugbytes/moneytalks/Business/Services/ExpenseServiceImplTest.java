package com.bugbytes.moneytalks.Business.Services;

import static org.junit.jupiter.api.Assertions.*;

import com.bugbytes.moneytalks.Business.Validation.ExpenseValidationException;
import com.bugbytes.moneytalks.Business.Validation.ExpenseValidator;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpenseServiceImplTest {

    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp() {

        // Simple in-memory fake repository
        ExpenseRepository repository = new ExpenseRepository() {
            private final List<Expense> expenses = new ArrayList<>();

            @Override
            public void addExpense(Expense expense) {
                expenses.add(expense);
            }

            @Override
            public List<Expense> getAllExpenses() {
                return expenses;
            }

//            @Override
//            public void deleteExpense(int id) {
//                expenses.removeIf(e -> e.getId() == id);
//            }
       };

        ExpenseValidator validator = new ExpenseValidator();
        expenseService = new ExpenseServiceImpl(repository, validator);
    }

    @Test
    void addValidExpense_succeeds() {
        Expense expense = new Expense(
                0,
                "Groceries",
                50.0,
                "Food",
                "2024-11-01",
                "Weekly shop"
        );

        assertDoesNotThrow(() -> expenseService.addExpense(expense));

        List<Expense> allExpenses = expenseService.getAllExpenses();
        assertEquals(1, allExpenses.size());
        assertEquals("Groceries", allExpenses.get(0).getName());
    }

    @Test
    void addNullExpense_throwsException() {
        ExpenseValidationException exception = assertThrows(
                ExpenseValidationException.class,
                () -> expenseService.addExpense(null)
        );

        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("null"));
    }

    @Test
    void addExpenseWithEmptyName_throwsException() {
        Expense expense = new Expense(
                0,
                " ",
                10.0,
                "Other",
                "2023-11-01",
                ""
        );

        assertThrows(ExpenseValidationException.class,
                () -> expenseService.addExpense(expense));
    }

    @Test
    void addExpenseWithZeroAmount_throwsException() {
        Expense expense = new Expense(
                0,
                "Gift",
                0.0,
                "Other",
                "2023-11-01",
                ""
        );

        assertThrows(ExpenseValidationException.class,
                () -> expenseService.addExpense(expense));
    }

    @Test
    void addExpenseWithNegativeAmount_throwsException() {
        Expense expense = new Expense(
                0,
                "Gift",
                -10.0,
                "Other",
                "2023-11-01",
                ""
        );

        assertThrows(ExpenseValidationException.class,
                () -> expenseService.addExpense(expense));
    }

    @Test
    void addExpenseWithEmptyDate_throwsException() {
        Expense expense = new Expense(
                0,
                "Coffee",
                5.0,
                "Food",
                "",
                ""
        );

        assertThrows(ExpenseValidationException.class,
                () -> expenseService.addExpense(expense));
    }
}
