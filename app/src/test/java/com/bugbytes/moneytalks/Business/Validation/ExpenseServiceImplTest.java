package com.bugbytes.moneytalks.Business.Validation;

import com.bugbytes.moneytalks.Persistence.Fake.FakeRepository;
import com.bugbytes.moneytalks.Persistence.ExpenseRepository;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.Business.Services.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ExpenseServiceImplTest {

    private ExpenseServiceImpl expenseService;
    private ExpenseRepository repo;

    @BeforeEach
    void setUp() {
        // Initialize the fake repository and the service
        repo = new FakeRepository();
        expenseService = new ExpenseServiceImpl(repo);

        // Crucial: Since FakeRepository uses a static list, clear it before every test
        repo.getAllExpenses().clear();
    }

    @Test
    void addExpense_ShouldDelegateToRepository() {
        Expense expense = new Expense("Groceries", 45.0, "Food", "2026/02/12", "Weekly shop");

        expenseService.addExpense(expense);

        List<Expense> result = expenseService.getAllExpenses();
        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getName());
    }

    @Test
    void getAllExpenses_ShouldReturnAllStoredItems() {
        expenseService.addExpense(new Expense("Rent", 1200.0, "Housing", "2026/02/01", "Feb Rent"));
        expenseService.addExpense(new Expense("Coffee", 5.0, "Food", "2026/02/02", "Latte"));

        List<Expense> list = expenseService.getAllExpenses();

        assertEquals(2, list.size());
    }

    @Test
    void deleteExpense_ShouldReturnTrueOnSuccess() {
        Expense gas = new Expense("Gas", 60.0, "Transport", "2026/02/10", "Full tank");
        expenseService.addExpense(gas);

        boolean deleted = expenseService.deleteExpense(gas);

        assertTrue(deleted);
        assertEquals(0, expenseService.getAllExpenses().size());
    }

    @Test
    void deleteExpense_ShouldReturnFalseIfNotFound() {
        Expense nonExistent = new Expense("Missing", 0.0, "None", "2026/01/01", "");

        boolean deleted = expenseService.deleteExpense(nonExistent);

        assertFalse(deleted);
    }
}