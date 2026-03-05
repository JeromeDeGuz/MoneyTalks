package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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

        repo.getAllExpenses().clear(); //Since FakeRepository uses a static list, clear it before every test
    }

    //Verify that adding an expense stores it in repository
    @Test
    public void addExpenseShouldDelegateToRepository()
    {
        final Expense expense = new Expense("Groceries", 45.0, "Food", "2026/02/12", "Weekly shop");

        expenseService.addExpense(expense);

        final List<Expense> result = expenseService.getAllExpenses();
        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getName());
    }

    //Add multiple expenses and verify retrieval returns all items
    @Test
    public void getAllExpensesShouldReturnAllStoredItems()
    {
        expenseService.addExpense(new Expense("Rent", 1200.0, "Housing", "2026/02/01", "Feb Rent"));
        expenseService.addExpense(new Expense("Coffee", 5.0, "Food", "2026/02/02", "Latte"));

        final List<Expense> list = expenseService.getAllExpenses();

        assertEquals(2, list.size());
    }

    //Ensure delete returns true when expense exists
    @Test
    public void deleteExpenseShouldReturnTrueOnSuccess()
    {
        final Expense gas = new Expense("Gas", 60.0, "Transport", "2026/02/10", "Full tank");
        expenseService.addExpense(gas);

        final boolean deleted = expenseService.deleteExpense(gas);

        assertTrue(deleted);
        assertEquals(0, expenseService.getAllExpenses().size());
    }

    //Ensure delete returns false when expense is not in repository
    @Test
    public void deleteExpenseShouldReturnFalseIfNotFound()
    {
        final Expense nonExistent = new Expense("Missing", 0.0, "None", "2026/01/01", "");

        final boolean deleted = expenseService.deleteExpense(nonExistent);

        assertFalse(deleted);
    }
}