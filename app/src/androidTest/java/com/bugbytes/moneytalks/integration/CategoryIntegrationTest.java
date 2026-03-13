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

    @Before
    public void setup()
    {
        context = ApplicationProvider.getApplicationContext();

        // Reset the real database before each test.
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

    @After
    public void tearDown()
    {
        // Return the database to its default state after each test.
        context.deleteDatabase(TEST_DB_NAME);
    }

    @Test
    public void addUpdate_flow_worksAcrossLogicAndSQLite()
    {
        final String originalCategoryName = "Category IT " + System.currentTimeMillis();
        final String updatedCategoryName = "Category IT Updated " + System.currentTimeMillis();
        final String expenseName = "Expense Linked To Category " + System.currentTimeMillis();

        final int categoryCountBefore = categoryService.getAllCategories().size();

        // Add a category through the service layer.
        categoryService.addCategory(new Category(originalCategoryName));

        List<Category> afterAdd = categoryService.getAllCategories();
        assertEquals(categoryCountBefore + 1, afterAdd.size());

        Category savedCategory = categoryService.getCategory(originalCategoryName);
        assertNotNull(savedCategory);
        assertTrue(savedCategory.getId() > 0);
        assertEquals(originalCategoryName, savedCategory.getName());

        // Create an expense that uses the original category.
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

        // Update the category through the service layer using the saved category with real id.
        categoryService.updateCategory(
                savedCategory,
                new Category(updatedCategoryName)
        );

        // Verify the category table was updated in SQLite.
        assertNull(categoryService.getCategory(originalCategoryName));

        Category updatedCategory = categoryService.getCategory(updatedCategoryName);
        assertNotNull(updatedCategory);
        assertEquals(updatedCategoryName, updatedCategory.getName());

        // Verify related expenses were also updated across the seam.
        Expense reloadedExpense = expenseRepo.getExpenseById(linkedExpense.getId());
        assertNotNull(reloadedExpense);
        assertEquals(updatedCategoryName, reloadedExpense.getCategory());
    }

    @Test
    public void delete_unusedCategory_removesItFromSQLite()
    {
        final String categoryName = "Category Delete IT " + System.currentTimeMillis();

        // Add a category that is not used by any expense.
        categoryService.addCategory(new Category(categoryName));

        Category savedCategory = categoryService.getCategory(categoryName);
        assertNotNull(savedCategory);

        // Delete through the service layer.
        categoryService.deleteCategory(savedCategory);

        // Verify the category is gone from SQLite.
        Category deletedCategory = categoryService.getCategory(categoryName);
        assertNull(deletedCategory);
    }

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