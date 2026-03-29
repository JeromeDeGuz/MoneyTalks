package com.bugbytes.moneytalks.integration;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.real.SqlExpenseRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

//Integration tests for Expense flows ensuring Business logic and SQLite layers interact correctly.
@RunWith(AndroidJUnit4.class)
public class ExpenseIntegrationTest
{
    private static final String TEST_DB_NAME = "moneytalks.db";

    private Context context;
    private ExpenseRepository repo;
    private ExpenseService service;

    //setup: It initializes the test context and resets the real database. Takes in nothing.
    @Before
    public void setup()
    {
        context = ApplicationProvider.getApplicationContext();

        //Reset the real database before each test.
        context.deleteDatabase(TEST_DB_NAME);

        repo = new SqlExpenseRepository(context);
        service = new ExpenseServiceImpl(repo, new ExpenseValidator());
    }

    //tearDown: It cleans up the database after each test execution. Takes in nothing.
    @After
    public void tearDown()
    {
        //Return the database to its default state after each test.
        context.deleteDatabase(TEST_DB_NAME);
    }

    //addUpdateDeleteFlowWorksAcrossLogicAndSqlite: It verifies the full CRUD lifecycle through the service and database layers. Takes in nothing.
    @Test
    public void addUpdateDeleteFlowWorksAcrossLogicAndSqlite() throws ValidationException {
        final String originalName = "Expense IT " + System.currentTimeMillis();
        final String updatedName = "Expense IT Updated " + System.currentTimeMillis();

        //Record the initial database state so the test remains deterministic.
        final int countBefore = service.getAllExpenses().size();

        //Add through the service layer.
        Expense created = new Expense(
                0,
                originalName,
                new BigDecimal("12.50"),
                "Food",
                LocalDate.now().minusDays(1),
                "Created by integration test"
        );
        service.addExpense(created);

        //Verify the record exists after persisting to SQLite.
        List<Expense> afterAdd = service.getAllExpenses();
        assertEquals(countBefore + 1, afterAdd.size());

        Expense saved = findExpenseByName(afterAdd, originalName);
        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("12.50", saved.getAmount().toPlainString());
        assertEquals("Food", saved.getCategory());

        //Update through the service layer.
        Expense updated = new Expense(
                saved.getId(),
                updatedName,
                new BigDecimal("18.75"),
                "Food",
                saved.getDate(),
                "Updated by integration test"
        );

        boolean updateResult = service.updateExpense(updated);
        assertTrue(updateResult);

        //Reload directly from the real repository to verify SQLite was updated.
        Expense reloaded = repo.getExpenseById(saved.getId());
        assertNotNull(reloaded);
        assertEquals(updatedName, reloaded.getName());
        assertEquals("18.75", reloaded.getAmount().toPlainString());
        assertEquals("Updated by integration test", reloaded.getNote());

        //Delete through the service layer.
        boolean deleteResult = service.deleteExpense(reloaded);
        assertTrue(deleteResult);

        //Verify the record is gone from SQLite.
        Expense deleted = repo.getExpenseById(saved.getId());
        assertNull(deleted);
        assertEquals(countBefore, service.getAllExpenses().size());
    }

    //findExpenseByName: It searches for a specific expense in a list by its name. Takes in @param expenses, @param targetName.
    private Expense findExpenseByName(List<Expense> expenses, String targetName)
    {
        for (Expense expense : expenses)
        {
            if (expense != null && targetName.equals(expense.getName()))
            {
                return expense;
            }
        }
        return null;
    }
}