package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    private CategoryServiceImpl service;

    @Mock private CategoryRepository categoryRepo;
    @Mock private ExpenseRepository expenseRepo;
    @Mock private CategoryValidator validator;

    @BeforeEach
    public void setUp() {
        service = new CategoryServiceImpl(categoryRepo, validator, expenseRepo);
    }

    // ---------------- Constructor ----------------

    @Test
    public void testConstructorNullRepositoryThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(null, validator, expenseRepo));
    }

    @Test
    public void testConstructorNullValidatorThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, null, expenseRepo));
    }

    @Test
    public void testConstructorNullExpenseRepositoryThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, validator, null));
    }

    // ---------------- addCategory ----------------

    @Test
    public void testAddCategory_NullCategory_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> service.addCategory(null));
        verify(categoryRepo, never()).addCategory(any());
    }

    @Test
    public void testAddCategoryValidSuccess() throws ValidationException {
        Category category = new Category("Bills");
        service.addCategory(category);
        verify(validator).validate(category);
        verify(categoryRepo).addCategory(category);
    }

    @Test
    public void testAddCategoryInvalidThrowsException() throws ValidationException {
        Category category = new Category("");
        doThrow(new ValidationException("Category name cannot be empty")).when(validator).validate(category);
        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    @Test
    public void testAddCategoryDuplicateThrowsException() throws ValidationException {
        Category category = new Category("Food");
        doThrow(new ValidationException("Category name already exists")).when(validator).validate(category);
        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    // ---------------- getAllCategories ----------------

    @Test
    public void testGetAllCategoriesSuccess() {
        List<Category> mockList = Arrays.asList(new Category("Food"), new Category("Bills"));
        when(categoryRepo.getAllCategories()).thenReturn(mockList);
        List<Category> result = service.getAllCategories();
        assertEquals(2, result.size());
        verify(categoryRepo).getAllCategories();
    }

    // ---------------- updateCategory ----------------

    @Test
    public void testUpdateCategory_NullOldCategory_ThrowsValidationException() {
        assertThrows(ValidationException.class,
                () -> service.updateCategory(null, new Category("New")));
        verify(categoryRepo, never()).updateCategory(any(), any());
    }

    @Test
    public void testUpdateCategory_NullNewCategory_ThrowsValidationException() {
        assertThrows(ValidationException.class,
                () -> service.updateCategory(new Category("Old"), null));
        verify(categoryRepo, never()).updateCategory(any(), any());
    }

    @Test
    public void testUpdateCategorySuccess() throws ValidationException {
        Category oldCategory = new Category(1, "Health", BigDecimal.ZERO);
        Category newCategory = new Category(1, "Wellness", BigDecimal.ZERO);
        service.updateCategory(oldCategory, newCategory);
        verify(expenseRepo).updateExpenseCategory(oldCategory, newCategory);
        verify(categoryRepo).updateCategory(oldCategory, newCategory);
    }

    // ---------------- deleteCategory ----------------

    @Test
    public void testDeleteCategory_NullCategory_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> service.deleteCategory(null));
        verify(categoryRepo, never()).deleteCategory(any());
    }

    @Test
    public void testDeleteCategorySuccess() throws ValidationException {
        Category category = new Category(1, "Shopping", BigDecimal.ZERO);
        when(expenseRepo.categoryExists(category)).thenReturn(false);
        service.deleteCategory(category);
        verify(categoryRepo).deleteCategory(category);
    }

    @Test
    public void testDeleteCategoryUsedInExpenseThrowsException() {
        Category category = new Category(1, "Gas", BigDecimal.ZERO);
        when(expenseRepo.categoryExists(category)).thenReturn(true);
        assertThrows(ValidationException.class, () -> service.deleteCategory(category));
        verify(categoryRepo, never()).deleteCategory(any());
    }

    // ---------------- getCategory ----------------

    @Test
    public void testGetCategory_NullName_ReturnsNull() {
        assertNull(service.getCategory(null));
        verify(categoryRepo, never()).getCategoryByName(any());
    }

    @Test
    public void testGetCategory_EmptyName_ReturnsNull() {
        assertNull(service.getCategory(""));
        verify(categoryRepo, never()).getCategoryByName(any());
    }

    @Test
    public void testGetCategory_WhitespaceName_ReturnsNull() {
        assertNull(service.getCategory("   "));
        verify(categoryRepo, never()).getCategoryByName(any());
    }

    @Test
    public void testGetCategoryFound() {
        Category mockCat = new Category(1, "Travel", BigDecimal.ZERO);
        when(categoryRepo.getCategoryByName("Travel")).thenReturn(mockCat);
        Category result = service.getCategory("Travel");
        assertNotNull(result);
        assertEquals("Travel", result.getName());
    }

    @Test
    public void testGetCategoryNotFoundReturnsNull() {
        when(categoryRepo.getCategoryByName("Unknown")).thenReturn(null);
        assertNull(service.getCategory("Unknown"));
    }

    // ---------------- getMonthSpent ----------------

    @Test
    public void testGetMonthSpent_NullCategoryName_ReturnsZero() {
        assertEquals(BigDecimal.ZERO, service.getMonthSpent(null, LocalDate.now()));
    }

    @Test
    public void testGetMonthSpent_NullDate_ReturnsZero() {
        assertEquals(BigDecimal.ZERO, service.getMonthSpent("Food", null));
    }

    @Test
    public void testGetMonthSpent_NoMatchingExpenses_ReturnsZero() {
        when(expenseRepo.getAllExpenses()).thenReturn(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, service.getMonthSpent("Food", LocalDate.of(2026, 3, 1)));
    }

    @Test
    public void testGetMonthSpent_MatchingExpenses_SumsCorrectly() {
        Expense e1 = new Expense(1, "Lunch", new BigDecimal("25.00"), "Food", LocalDate.of(2026, 3, 10), "");
        Expense e2 = new Expense(2, "Dinner", new BigDecimal("35.50"), "Food", LocalDate.of(2026, 3, 15), "");
        Expense e3 = new Expense(3, "Bus", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 3, 5), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e1, e2, e3));

        BigDecimal result = service.getMonthSpent("Food", LocalDate.of(2026, 3, 1));
        assertEquals(new BigDecimal("60.50"), result);
    }

    @Test
    public void testGetMonthSpent_DifferentMonth_ReturnsZero() {
        Expense e = new Expense(1, "Lunch", new BigDecimal("25.00"), "Food", LocalDate.of(2026, 2, 10), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        BigDecimal result = service.getMonthSpent("Food", LocalDate.of(2026, 3, 1));
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void testGetMonthSpent_ExpenseWithNullCategory_Skipped() {
        Expense e = new Expense(1, "Unknown", new BigDecimal("10.00"), null, LocalDate.of(2026, 3, 1), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        BigDecimal result = service.getMonthSpent("Food", LocalDate.of(2026, 3, 1));
        assertEquals(BigDecimal.ZERO, result);
    }

    // ---------------- hasExceededBudget ----------------

    @Test
    public void testHasExceededBudget_CategoryNotFound_ReturnsFalse() {
        when(categoryRepo.getCategoryByName("Ghost")).thenReturn(null);
        assertFalse(service.hasExceededBudget("Ghost", LocalDate.now()));
    }

    @Test
    public void testHasExceededBudget_UnderBudget_ReturnsFalse() {
        Category cat = new Category(1, "Food", new BigDecimal("200.00"));
        when(categoryRepo.getCategoryByName("Food")).thenReturn(cat);

        Expense e = new Expense(1, "Lunch", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 3, 1), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        assertFalse(service.hasExceededBudget("Food", LocalDate.of(2026, 3, 1)));
    }

    @Test
    public void testHasExceededBudget_OverBudget_ReturnsTrue() {
        Category cat = new Category(1, "Food", new BigDecimal("30.00"));
        when(categoryRepo.getCategoryByName("Food")).thenReturn(cat);

        Expense e = new Expense(1, "Dinner", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 3, 5), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        assertTrue(service.hasExceededBudget("Food", LocalDate.of(2026, 3, 1)));
    }

    @Test
    public void testHasExceededBudget_NullName_ReturnsFalse() {
        assertFalse(service.hasExceededBudget(null, LocalDate.now()));
    }
}