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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FilterSortIntegrationTest
{
    private static final String TEST_DB_NAME = "moneytalks.db";

    private Context context;
    private ExpenseRepository repo;
    private ExpenseService service;

    //setup: Prepares the Android context and resets the real database before each test. Returns nothing.
    @Before
    public void setup()
    {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(TEST_DB_NAME);

        repo = new SqlExpenseRepository(context);
        service = new ExpenseServiceImpl(repo, new ExpenseValidator());
    }

    //tearDown: Ensures the database is wiped after tests to maintain environment cleanliness. Returns nothing.
    @After
    public void tearDown()
    {
        context.deleteDatabase(TEST_DB_NAME);
    }

    //filterByCategoryReturnsOnlyMatchingExpensesFromSqlite: Verifies that filtering logic correctly narrows results from the database. Returns nothing.
    @Test
    public void filterByCategoryReturnsOnlyMatchingExpensesFromSqlite() throws ValidationException
    {
        final long timestamp = System.currentTimeMillis();
        final String targetCategory = "FilterCat" + timestamp;
        final String otherCategory = "OtherCat" + timestamp;

        service.addExpense(new Expense(
                0,
                "Filter Match 1 " + timestamp,
                new BigDecimal("11.00"),
                targetCategory,
                LocalDate.now().minusDays(3),
                "First matching expense"
        ));

        service.addExpense(new Expense(
                0,
                "Filter Match 2 " + timestamp,
                new BigDecimal("15.50"),
                targetCategory,
                LocalDate.now().minusDays(1),
                "Second matching expense"
        ));

        service.addExpense(new Expense(
                0,
                "Filter Other " + timestamp,
                new BigDecimal("20.00"),
                otherCategory,
                LocalDate.now().minusDays(2),
                "Non-matching expense"
        ));

        List<Expense> filtered = service.getExpensesByCategorySortedByDate(targetCategory, true);

        assertEquals(2, filtered.size());
        assertEquals(targetCategory, filtered.get(0).getCategory());
        assertEquals(targetCategory, filtered.get(1).getCategory());

        assertTrue(filtered.get(0).getDate().isAfter(filtered.get(1).getDate()));
    }

    //sortByDateReturnsNewestFirstAndOldestFirstFromSqlite: Confirms data retrieved from SQLite follows requested date ordering. Returns nothing.
    @Test
    public void sortByDateReturnsNewestFirstAndOldestFirstFromSqlite() throws ValidationException
    {
        final long timestamp = System.currentTimeMillis();
        final String category = "SortCat" + timestamp;

        final String oldName = "Old Expense " + timestamp;
        final String middleName = "Middle Expense " + timestamp;
        final String newName = "New Expense " + timestamp;

        service.addExpense(new Expense(
                0,
                oldName,
                new BigDecimal("9.99"),
                category,
                LocalDate.now().minusDays(10),
                "Oldest record"
        ));

        service.addExpense(new Expense(
                0,
                middleName,
                new BigDecimal("12.99"),
                category,
                LocalDate.now().minusDays(5),
                "Middle record"
        ));

        service.addExpense(new Expense(
                0,
                newName,
                new BigDecimal("19.99"),
                category,
                LocalDate.now().minusDays(1),
                "Newest record"
        ));

        List<Expense> newestFirst = service.getExpensesByCategorySortedByDate(category, true);
        List<Expense> oldestFirst = service.getExpensesByCategorySortedByDate(category, false);

        assertEquals(3, newestFirst.size());
        assertEquals(3, oldestFirst.size());

        assertEquals(newName, newestFirst.get(0).getName());
        assertEquals(middleName, newestFirst.get(1).getName());
        assertEquals(oldName, newestFirst.get(2).getName());

        assertEquals(oldName, oldestFirst.get(0).getName());
        assertEquals(middleName, oldestFirst.get(1).getName());
        assertEquals(newName, oldestFirst.get(2).getName());
    }
}