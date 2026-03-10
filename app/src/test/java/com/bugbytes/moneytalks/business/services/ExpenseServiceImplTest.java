package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpenseServiceImplTest
{
    private ExpenseServiceImpl expenseService;
    private ExpenseRepository repo;
    private ExpenseValidator validator;

    @BeforeEach
    public void setUp()
    {
        //Initialize the fake repository and the service
        repo = new FakeRepository();
        validator = new ExpenseValidator();
        expenseService = new ExpenseServiceImpl(repo, validator);

        // Clearing the static list by removing items individually since getAllExpenses() returns a copy
        List<Expense> current = repo.getAllExpenses();
        for (Expense e : current)
        {
            repo.deleteExpense(e);
        }
    }

    //Verify that adding an expense stores it in repository
    @Test
    public void addExpenseShouldDelegateToRepository()
    {
        final Expense expense = new Expense(0, "Groceries", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 12), "Weekly shop");

        expenseService.addExpense(expense);

        final List<Expense> result = expenseService.getAllExpenses();
        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getName());
    }

    @Test
    public void addExpenseShouldThrowExceptionIfInvalid()
    {
        // Name is purely numeric, which should fail validation
        final Expense invalid = new Expense(0, "123", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 12), "");

        assertThrows(ValidationException.class, () -> {
            expenseService.addExpense(invalid);
        });
    }

    //Add multiple expenses and verify retrieval returns all items
    @Test
    public void getAllExpensesShouldReturnAllStoredItems()
    {
        expenseService.addExpense(new Expense(0, "Rent", new BigDecimal("1200.0"), "Housing", LocalDate.of(2026, 2, 1), "Feb Rent"));
        expenseService.addExpense(new Expense(0, "Coffee", new BigDecimal("5.0"), "Food", LocalDate.of(2026, 2, 2), "Latte"));

        final List<Expense> list = expenseService.getAllExpenses();

        assertEquals(2, list.size());
    }

    //Ensure delete returns true when expense exists
    @Test
    public void deleteExpenseShouldReturnTrueOnSuccess()
    {
        final Expense gas = new Expense(0, "Gas", new BigDecimal("60.0"), "Transport", LocalDate.of(2026, 2, 10), "Full tank");
        expenseService.addExpense(gas);

        // Fetching the assigned ID from repo to ensure a clean match
        Expense storedGas = expenseService.getAllExpenses().get(0);

        final boolean deleted = expenseService.deleteExpense(storedGas);

        assertTrue(deleted);
        assertEquals(0, expenseService.getAllExpenses().size());
    }

    //Ensure delete returns false when expense is not in repository
    @Test
    public void deleteExpenseShouldReturnFalseIfNotFound()
    {
        final Expense nonExistent = new Expense(0, "Missing", BigDecimal.ZERO, "None", LocalDate.of(2026, 1, 1), "");

        final boolean deleted = expenseService.deleteExpense(nonExistent);

        assertFalse(deleted);
    }

    @Test
    public void getExpensesSortedByDateShouldReturnCorrectOrder()
    {
        expenseService.addExpense(new Expense(0, "Old", new BigDecimal("10"), "Food", LocalDate.of(2020, 1, 1), ""));
        expenseService.addExpense(new Expense(0, "New", new BigDecimal("10"), "Food", LocalDate.of(2025, 1, 1), ""));

        // Newest first
        List<Expense> result = expenseService.getExpensesSortedByDate(true);
        assertEquals("New", result.get(0).getName());

        // Oldest first
        result = expenseService.getExpensesSortedByDate(false);
        assertEquals("Old", result.get(0).getName());
    }

    @Test
    public void getExpensesByCategoryShouldFilterCorrectly()
    {
        expenseService.addExpense(new Expense(0, "Burger", new BigDecimal("10"), "Food", LocalDate.now(), ""));
        expenseService.addExpense(new Expense(0, "Bus", new BigDecimal("2"), "Transport", LocalDate.now(), ""));

        List<Expense> foodOnly = expenseService.getExpensesByCategorySortedByDate("Food", true);
        assertEquals(1, foodOnly.size());
        assertEquals("Burger", foodOnly.get(0).getName());

        // Category "All" should return everything
        List<Expense> all = expenseService.getExpensesByCategorySortedByDate("All", true);
        assertEquals(2, all.size());
    }

    @Test
    public void updateExpenseShouldReturnFalseIfNotFound()
    {
        final Expense nonExistent = new Expense(999, "Missing", new BigDecimal("10"), "Food", LocalDate.now(), "");
        assertFalse(expenseService.updateExpense(nonExistent));
    }

    @Test
    public void getExpenseByIdShouldReturnNullIfNotFound()
    {
        assertNull(expenseService.getExpenseById(9999));
    }
}
