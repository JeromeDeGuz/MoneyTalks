package com.bugbytes.moneytalks.integration;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.business.services.CategoryServiceImpl;
import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
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

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CategoryIntegrationTest
{
    private static final String TEST_DB_NAME = "moneytalks.db";

    private Context context;
    private CategoryRepository categoryRepo;
    private ExpenseRepository expenseRepo;
    private CategoryService categoryService;
    private ExpenseService expenseService;

    //setup: It prepares the test environment by resetting the database and initializing services. Returns nothing.
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
    }

    //tearDown: It cleans up the database after each test to ensure isolation. Returns nothing.
    @After
    public void tearDown()
    {
        context.deleteDatabase(TEST_DB_NAME);
    }

    //addUpdateFlowWorksAcrossLogicAndSqlite: Verifies that adding and updating a category correctly reflects in the database. Returns nothing.
    @Test
    public void addUpdateFlowWorksAcrossLogicAndSqlite() throws ValidationException
    {
        final String originalCategoryName = "Category IT " + System.currentTimeMillis();
        final String updatedCategoryName = "Category IT Updated " + System.currentTimeMillis();
        final String expenseName = "Expense Linked To Category " + System.currentTimeMillis();

        final int categoryCountBefore = categoryService.getAllCategories().size();

        categoryService.addCategory(new Category(originalCategoryName));

        List<Category> afterAdd = categoryService.getAllCategories();
        assertEquals(categoryCountBefore + 1, afterAdd.size());

        Category savedCategory = categoryService.getCategory(originalCategoryName);
        assertNotNull(savedCategory);
        assertTrue(savedCategory.getId() > 0);
        assertEquals(originalCategoryName, savedCategory.getName());

        expenseService.addExpense(new Expense(
                0,
                expenseName,
                new BigDecimal("22.40"),
                originalCategoryName,
                LocalDate.now().minusDays(1),
                "Used to verify category update"
        ));

        Expense linkedExpense = findExpenseByName(expenseService.getAllExpenses(), expenseName);
        assertNotNull(linkedExpense);

        categoryService.updateCategory(
                savedCategory,
                new Category(updatedCategoryName)
        );

        assertNull(categoryService.getCategory(originalCategoryName));

        Category updatedCategory = categoryService.getCategory(updatedCategoryName);
        assertNotNull(updatedCategory);
        assertEquals(updatedCategoryName, updatedCategory.getName());

        Expense reloadedExpense = expenseRepo.getExpenseById(linkedExpense.getId());
        assertNotNull(reloadedExpense);
        assertEquals(updatedCategoryName, reloadedExpense.getCategory());
    }

    //deleteUnusedCategoryRemovesItFromSqlite: Confirms that deleting a category removes the record from SQLite. Returns nothing.
    @Test
    public void deleteUnusedCategoryRemovesItFromSqlite() throws ValidationException
    {
        final String categoryName = "Category Delete IT " + System.currentTimeMillis();

        categoryService.addCategory(new Category(categoryName));

        Category savedCategory = categoryService.getCategory(categoryName);
        assertNotNull(savedCategory);

        categoryService.deleteCategory(savedCategory);

        Category deletedCategory = categoryService.getCategory(categoryName);
        assertNull(deletedCategory);
    }

    //findExpenseByName: Searches through a list of expenses for a specific name match. Takes in @param expenses and targetName. Returns @return Expense.
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