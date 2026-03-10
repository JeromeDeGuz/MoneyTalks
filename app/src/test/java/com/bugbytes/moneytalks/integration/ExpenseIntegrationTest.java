package com.bugbytes.moneytalks.integration;

import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests verifying interaction between:
 * Service → Validator → Repository
 */
public class ExpenseIntegrationTest {

    private ExpenseService expenseService;
    private ExpenseRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new FakeRepository();
        ExpenseValidator validator = new ExpenseValidator();
        expenseService = new ExpenseServiceImpl(repository, validator);

        // Clear repository to isolate tests
        for (Expense e : repository.getAllExpenses()) {
            repository.deleteExpense(e);
        }
    }

    /**
     * Tests the full lifecycle of an expense:
     * Add → Retrieve → Update → Delete
     */
    @Test
    public void testExpenseLifecycle() {

        Expense expense = new Expense(
                0,
                "Groceries",
                new BigDecimal("50.00"),
                "Food",
                LocalDate.now(),
                "Weekly shopping"
        );

        expenseService.addExpense(expense);

        List<Expense> expenses = expenseService.getAllExpenses();
        assertEquals(1, expenses.size());

        Expense saved = expenses.get(0);
        assertEquals("Groceries", saved.getName());

        saved.setName("Updated Groceries");

        boolean updated = expenseService.updateExpense(saved);
        assertTrue(updated);

        Expense updatedExpense = expenseService.getAllExpenses().get(0);
        assertEquals("Updated Groceries", updatedExpense.getName());

        boolean deleted = expenseService.deleteExpense(updatedExpense);
        assertTrue(deleted);

        assertTrue(expenseService.getAllExpenses().isEmpty());
    }

    /**
     * Ensures validation prevents invalid expenses from being stored.
     */
    @Test
    public void testValidationPreventsPersistence() {

        Expense invalidExpense = new Expense(
                0,
                "Future Trip",
                new BigDecimal("100.00"),
                "Travel",
                LocalDate.now().plusDays(2),
                ""
        );

        assertThrows(
                ValidationException.class,
                () -> expenseService.addExpense(invalidExpense)
        );

        assertTrue(expenseService.getAllExpenses().isEmpty());
    }

    /**
     * Tests filtering by category and sorting by newest date.
     */
    @Test
    public void testSortingAndFilteringIntegration() {

        expenseService.addExpense(
                new Expense(0, "Expense A", new BigDecimal("10"), "Food", LocalDate.now().minusDays(2), "")
        );

        expenseService.addExpense(
                new Expense(0, "Expense B", new BigDecimal("20"), "Food", LocalDate.now(), "")
        );

        expenseService.addExpense(
                new Expense(0, "Expense C", new BigDecimal("30"), "Transport", LocalDate.now().minusDays(1), "")
        );

        List<Expense> result =
                expenseService.getExpensesByCategorySortedByDate("Food", true);

        assertEquals(2, result.size());
        assertEquals("Expense B", result.get(0).getName());
        assertEquals("Expense A", result.get(1).getName());
    }

    /**
     * Tests sorting oldest first.
     */
    @Test
    public void testSortingOldestFirst() {

        expenseService.addExpense(
                new Expense(0, "Old Expense", new BigDecimal("10"), "Food", LocalDate.now().minusDays(2), "")
        );

        expenseService.addExpense(
                new Expense(0, "New Expense", new BigDecimal("20"), "Food", LocalDate.now(), "")
        );

        List<Expense> result = expenseService.getExpensesSortedByDate(false);

        assertEquals("Old Expense", result.get(0).getName());
    }

    /**
     * Tests retrieving an expense by ID.
     */
    @Test
    public void testGetExpenseByIdIntegration() {

        Expense expense = new Expense(
                0,
                "Coffee",
                new BigDecimal("5.00"),
                "Food",
                LocalDate.now(),
                ""
        );

        expenseService.addExpense(expense);

        Expense saved = expenseService.getAllExpenses().get(0);

        Expense result = expenseService.getExpenseById(saved.getId());

        assertNotNull(result);
        assertEquals("Coffee", result.getName());
    }

    /**
     * Ensures deleting null returns false.
     */
    @Test
    public void testDeleteNullExpense() {

        boolean result = expenseService.deleteExpense(null);

        assertFalse(result);
    }

    /**
     * Ensures updating null returns false.
     */
    @Test
    public void testUpdateNullExpense() {

        boolean result = expenseService.updateExpense(null);

        assertFalse(result);
    }
}
