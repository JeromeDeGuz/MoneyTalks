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
        // Added 0 as the first parameter for the id
        final Expense expense = new Expense(0, "Groceries", new BigDecimal("45.0"), "Food", LocalDate.of(2026, 2, 12), "Weekly shop");

        expenseService.addExpense(expense);

        final List<Expense> result = expenseService.getAllExpenses();
        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getName());
    }

    //Add multiple expenses and verify retrieval returns all items
    @Test
    public void getAllExpensesShouldReturnAllStoredItems()
    {
        // Added 0 as the first parameter for the id
        expenseService.addExpense(new Expense(0, "Rent", new BigDecimal("1200.0"), "Housing", LocalDate.of(2026, 2, 1), "Feb Rent"));
        expenseService.addExpense(new Expense(0, "Coffee", new BigDecimal("5.0"), "Food", LocalDate.of(2026, 2, 2), "Latte"));

        final List<Expense> list = expenseService.getAllExpenses();

        assertEquals(2, list.size());
    }

    //Ensure delete returns true when expense exists
    @Test
    public void deleteExpenseShouldReturnTrueOnSuccess()
    {
        // Added 0 as the first parameter for the id
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
    public void deleteExpenseShouldReturnExceptionIfNotFound()
    {
        // Added 0 as the first parameter for the id
        final Expense nonExistent = new Expense(0, "Missing", BigDecimal.ZERO, "None", LocalDate.of(2026, 1, 1), "");


        assertThrows(ValidationException.class, () -> expenseService.deleteExpense(nonExistent));


    }
}