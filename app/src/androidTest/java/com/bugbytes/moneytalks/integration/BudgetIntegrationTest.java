package com.bugbytes.moneytalks.integration;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.business.services.BudgetService;
import com.bugbytes.moneytalks.business.services.BudgetServiceImpl;
import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.business.services.CategoryServiceImpl;
import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.BudgetSummary;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.real.SqlCategoryRepository;
import com.bugbytes.moneytalks.persistence.real.SqlExpenseRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//Integration tests for budget features ensuring business logic works correctly with SQLite persistence.
@RunWith(AndroidJUnit4.class)
public class BudgetIntegrationTest
{
    private static final String TEST_DB_NAME = "moneytalks.db";

    private Context context;
    private CategoryRepository categoryRepo;
    private ExpenseRepository expenseRepo;

    private CategoryService categoryService;
    private ExpenseService expenseService;
    private BudgetService budgetService;

    //setup: It prepares the Android context, resets the database, and initializes the real SQLite-backed services. Takes in nothing.
    @Before
    public void setup()
    {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(TEST_DB_NAME);

        categoryRepo = new SqlCategoryRepository(context);
        expenseRepo = new SqlExpenseRepository(context);

        categoryService = new CategoryServiceImpl(
                categoryRepo,
                new CategoryValidator(categoryRepo),
                expenseRepo
        );

        expenseService = new ExpenseServiceImpl(
                expenseRepo,
                new ExpenseValidator()
        );

        budgetService = new BudgetServiceImpl(categoryService, expenseService);
    }

    //tearDown: It deletes the test database after each test to keep the environment clean. Takes in nothing.
    @After
    public void tearDown()
    {
        context.deleteDatabase(TEST_DB_NAME);
    }

    //getMonthlyBudgetSummaryReturnsCorrectSpentAmountsFromSqlite: It verifies that monthly summaries combine real category and expense data from SQLite. Takes in nothing.
    @Test
    public void getMonthlyBudgetSummaryReturnsCorrectSpentAmountsFromSqlite() throws ValidationException
    {
        final long timestamp = System.currentTimeMillis();
        final String foodCategory = "FoodBudget" + timestamp;
        final String transportCategory = "TransportBudget" + timestamp;

        categoryService.addCategory(new Category(foodCategory, new BigDecimal("200.00")));
        categoryService.addCategory(new Category(transportCategory, new BigDecimal("100.00")));

        expenseService.addExpense(new Expense(
                0,
                "Lunch " + timestamp,
                new BigDecimal("25.00"),
                foodCategory,
                LocalDate.of(2026, 3, 10),
                "Lunch"
        ));

        expenseService.addExpense(new Expense(
                0,
                "Dinner " + timestamp,
                new BigDecimal("35.50"),
                foodCategory,
                LocalDate.of(2026, 3, 15),
                "Dinner"
        ));

        expenseService.addExpense(new Expense(
                0,
                "Bus " + timestamp,
                new BigDecimal("15.00"),
                transportCategory,
                LocalDate.of(2026, 3, 12),
                "Bus fare"
        ));

        //Different month, so it should not be counted in March summary
        expenseService.addExpense(new Expense(
                0,
                "Old Food " + timestamp,
                new BigDecimal("99.00"),
                foodCategory,
                LocalDate.of(2026, 2, 28),
                "Previous month"
        ));

        List<BudgetSummary> summaries = budgetService.getMonthlyBudgetSummary(2026, 3);

        BudgetSummary foodSummary = null;
        BudgetSummary transportSummary = null;

        for (BudgetSummary summary : summaries)
        {
            if (summary.getCategoryName().equals(foodCategory))
            {
                foodSummary = summary;
            }
            else if (summary.getCategoryName().equals(transportCategory))
            {
                transportSummary = summary;
            }
        }

        assertNotNull(foodSummary);
        assertNotNull(transportSummary);

        assertEquals(new BigDecimal("200.00"), foodSummary.getBudget());
        assertEquals(new BigDecimal("60.50"), foodSummary.getSpentThisMonth());

        assertEquals(new BigDecimal("100.00"), transportSummary.getBudget());
        assertEquals(new BigDecimal("15.00"), transportSummary.getSpentThisMonth());
    }

    //getCategoryBudgetSummaryReturnsOnlyRequestedCategoryFromSqlite: It verifies that one category summary is returned with the correct budget and spent total from SQLite. Takes in nothing.
    @Test
    public void getCategoryBudgetSummaryReturnsOnlyRequestedCategoryFromSqlite() throws ValidationException
    {
        final long timestamp = System.currentTimeMillis();
        final String categoryName = "SchoolBudget" + timestamp;

        categoryService.addCategory(new Category(categoryName, new BigDecimal("500.00")));

        expenseService.addExpense(new Expense(
                0,
                "Book " + timestamp,
                new BigDecimal("80.00"),
                categoryName,
                LocalDate.of(2026, 3, 5),
                "Textbook"
        ));

        expenseService.addExpense(new Expense(
                0,
                "Supplies " + timestamp,
                new BigDecimal("20.00"),
                categoryName,
                LocalDate.of(2026, 3, 8),
                "School supplies"
        ));

        BudgetSummary summary = budgetService.getCategoryBudgetSummary(categoryName, 2026, 3);

        assertNotNull(summary);
        assertEquals(categoryName, summary.getCategoryName());
        assertEquals(new BigDecimal("500.00"), summary.getBudget());
        assertEquals(new BigDecimal("100.00"), summary.getSpentThisMonth());
    }

    //updateCategoryBudgetPersistsNewBudgetToSqlite: It verifies that updating a category budget through the budget service persists the new amount in SQLite. Takes in nothing.
    @Test
    public void updateCategoryBudgetPersistsNewBudgetToSqlite() throws ValidationException
    {
        final long timestamp = System.currentTimeMillis();
        final String categoryName = "SubscriptionsBudget" + timestamp;

        categoryService.addCategory(new Category(categoryName, new BigDecimal("30.00")));

        budgetService.updateCategoryBudget(categoryName, new BigDecimal("75.00"));

        Category updatedCategory = categoryService.getCategory(categoryName);

        assertNotNull(updatedCategory);
        assertEquals(new BigDecimal("75.00"), updatedCategory.getBudget());
    }

    //getCategoryBudgetSummaryDetectsOverBudgetFromSqlite: It verifies that over-budget status and over amount are calculated correctly using SQLite-backed data. Takes in nothing.
    @Test
    public void getCategoryBudgetSummaryDetectsOverBudgetFromSqlite() throws ValidationException
    {
        final long timestamp = System.currentTimeMillis();
        final String categoryName = "ShoppingBudget" + timestamp;

        categoryService.addCategory(new Category(categoryName, new BigDecimal("50.00")));

        expenseService.addExpense(new Expense(
                0,
                "Shoes " + timestamp,
                new BigDecimal("40.00"),
                categoryName,
                LocalDate.of(2026, 3, 2),
                "Shoes"
        ));

        expenseService.addExpense(new Expense(
                0,
                "Hat " + timestamp,
                new BigDecimal("25.00"),
                categoryName,
                LocalDate.of(2026, 3, 6),
                "Hat"
        ));

        BudgetSummary summary = budgetService.getCategoryBudgetSummary(categoryName, 2026, 3);

        assertNotNull(summary);
        assertTrue(summary.isOverBudget());
        assertEquals(new BigDecimal("15.00"), summary.getOverAmount());
    }
}