package com.bugbytes.moneytalks.integration;

import static org.junit.Assert.*;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.business.services.CategoryServiceImpl;
import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.persistence.real.SqlCategoryRepository;
import com.bugbytes.moneytalks.persistence.real.SqlExpenseRepository;
import com.bugbytes.moneytalks.models.Category;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CategoryIntegrationTest {
    private CategoryService categoryService;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // 1. Initialize repositories
        SqlCategoryRepository categoryRepo = new SqlCategoryRepository(context);
        SqlExpenseRepository expenseRepo = new SqlExpenseRepository(context);

        // 2. Initialize validator with required repo
        CategoryValidator validator = new CategoryValidator(categoryRepo);

        // 3. Initialize service with all 3 dependencies
        categoryService = new CategoryServiceImpl(categoryRepo, validator, expenseRepo);
    } // Pehle yahan bracket missing thi

    @Test
    public void testAddAndRetrieveCategory() {
        // Unique name to avoid validation conflicts
        String uniqueName = "TestCat_" + System.currentTimeMillis();
        Category newCat = new Category(uniqueName);

        categoryService.addCategory(newCat);

        List<Category> allCategories = categoryService.getAllCategories();
        boolean found = allCategories.stream()
                .anyMatch(c -> c.getName().equals(uniqueName));

        assertTrue("Category should be successfully added to the database", found);
    }

    @Test
    public void testUpdateCategory() {
        String oldName = "OldName_" + System.currentTimeMillis();
        categoryService.addCategory(new Category(oldName));

        Category fetchedOld = categoryService.getCategory(oldName);
        assertNotNull("Category must be in DB before update", fetchedOld);

        // Creating the new category object for update
        String newName = "NewName_" + System.currentTimeMillis();
        Category newCat = new Category(fetchedOld.getId(), newName, fetchedOld.getBudget());

        // Calling updateCategory(old, new) as per your Service implementation
        categoryService.updateCategory(fetchedOld, newCat);

        Category updated = categoryService.getCategory(newName);
        assertNotNull("Updated category should be found by its new name", updated);
        assertEquals("Name should match the new name", newName, updated.getName());
    }

    @Test
    public void testDeleteCategory() {
        String tempName = "DeleteMe_" + System.currentTimeMillis();
        categoryService.addCategory(new Category(tempName));

        Category toDelete = categoryService.getCategory(tempName);
        assertNotNull(toDelete);

        categoryService.deleteCategory(toDelete);

        Category results = categoryService.getCategory(tempName);
        assertNull("Category should no longer exist in DB", results);
    }
}