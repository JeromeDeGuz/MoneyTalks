package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeCategoryRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceImplTest {

    private CategoryServiceImpl service;
    private CategoryRepository categoryRepo;
    private ExpenseRepository expenseRepo;
    private CategoryValidator validator;

    @BeforeEach
    public void setUp() {
        categoryRepo = new FakeCategoryRepository();
        expenseRepo = new FakeRepository();
        validator = new CategoryValidator(categoryRepo);
        service = new CategoryServiceImpl(categoryRepo, validator, expenseRepo);

        // Reset repositories for each test
        for (Category c : categoryRepo.getAllCategories()) {
            categoryRepo.deleteCategory(c);
        }
        for (Expense e : expenseRepo.getAllExpenses()) {
            expenseRepo.deleteExpense(e);
        }
    }

    @Test
    public void testConstructor_NullRepository_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(null, validator, expenseRepo));
    }

    @Test
    public void testConstructor_NullValidator_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, null, expenseRepo));
    }

    @Test
    public void testConstructor_NullExpenseRepository_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, validator, null));
    }

    @Test
    public void testAddCategory_Valid_Success() {
        Category category = new Category("Bills");
        service.addCategory(category);
        
        List<Category> all = service.getAllCategories();
        assertEquals(1, all.size());
        assertEquals("Bills", all.get(0).getName());
    }

    @Test
    public void testAddCategory_Duplicate_ThrowsException() {
        service.addCategory(new Category("Food"));
        assertThrows(ValidationException.class, () -> service.addCategory(new Category("Food")));
    }

    @Test
    public void testGetAllCategories_Empty_ReturnsEmptyList() {
        List<Category> all = service.getAllCategories();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    public void testUpdateCategory_Success() {
        service.addCategory(new Category("Health"));
        Category saved = service.getCategory("Health");
        saved.setName("Medical");
        
        service.updateCategory(saved);
        assertEquals("Medical", service.getCategory("Medical").getName());
    }

    @Test
    public void testDeleteCategory_Success() {
        service.addCategory(new Category("Shopping"));
        Category saved = service.getCategory("Shopping");
        
        service.deleteCategory(saved);
        assertNull(service.getCategory("Shopping"));
    }

    @Test
    public void testDeleteCategory_UsedInExpense_ThrowsException() {
        service.addCategory(new Category("Gas"));
        Category saved = service.getCategory("Gas");
        
        // Simulate category being used
        expenseRepo.addExpense(new Expense(0, "Fuel", BigDecimal.TEN, "Gas", LocalDate.now(), ""));
        
        assertThrows(ValidationException.class, () -> service.deleteCategory(saved));
    }

    @Test
    public void testGetCategory_Found() {
        service.addCategory(new Category("Travel"));
        assertNotNull(service.getCategory("Travel"));
    }

    @Test
    public void testGetCategory_NotFound_ReturnsNull() {
        assertNull(service.getCategory("Unknown"));
    }
}
