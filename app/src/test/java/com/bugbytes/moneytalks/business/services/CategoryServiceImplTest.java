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
public class CategoryServiceImplTest
{
    private CategoryServiceImpl service;

    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private CategoryValidator validator;

    //setUp: Prepares the service with mocked dependencies before each test. Returns nothing.
    @BeforeEach
    public void setUp()
    {
        service = new CategoryServiceImpl(categoryRepo, validator, expenseRepo);
    }

    //testConstructorNullRepositoryThrowsException: Verifies exception when category repository is null. Returns nothing.
    @Test
    public void testConstructorNullRepositoryThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(null, validator, expenseRepo));
    }

    //testConstructorNullValidatorThrowsException: Verifies exception when validator is null. Returns nothing.
    @Test
    public void testConstructorNullValidatorThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, null, expenseRepo));
    }

    //testConstructorNullExpenseRepositoryThrowsException: Verifies exception when expense repository is null. Returns nothing.
    @Test
    public void testConstructorNullExpenseRepositoryThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, validator, null));
    }

    //testAddCategoryNullCategoryThrowsValidationException: Verifies exception for adding a null category. Returns nothing.
    @Test
    public void testAddCategoryNullCategoryThrowsValidationException()
    {
        assertThrows(ValidationException.class, () -> service.addCategory(null));
        verify(categoryRepo, never()).addCategory(any());
    }

    //testAddCategoryValidSuccess: Verifies successful category addition. Returns nothing.
    @Test
    public void testAddCategoryValidSuccess() throws ValidationException
    {
        Category category = new Category("Bills");
        service.addCategory(category);
        verify(validator).validate(category);
        verify(categoryRepo).addCategory(category);
    }

    //testAddCategoryInvalidThrowsException: Verifies exception for invalid category data. Returns nothing.
    @Test
    public void testAddCategoryInvalidThrowsException() throws ValidationException
    {
        Category category = new Category("");
        doThrow(new ValidationException("Category name cannot be empty")).when(validator).validate(category);
        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    //testAddCategoryDuplicateThrowsException: Verifies exception when a duplicate category is added. Returns nothing.
    @Test
    public void testAddCategoryDuplicateThrowsException() throws ValidationException
    {
        Category category = new Category("Food");
        doThrow(new ValidationException("Category name already exists")).when(validator).validate(category);
        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    //testGetAllCategoriesSuccess: Verifies retrieval of all categories. Returns nothing.
    @Test
    public void testGetAllCategoriesSuccess()
    {
        List<Category> mockList = Arrays.asList(new Category("Food"), new Category("Bills"));
        when(categoryRepo.getAllCategories()).thenReturn(mockList);
        List<Category> result = service.getAllCategories();
        assertEquals(2, result.size());
        verify(categoryRepo).getAllCategories();
    }

    //testUpdateCategoryNullOldCategoryThrowsValidationException: Verifies exception when old category is null. Returns nothing.
    @Test
    public void testUpdateCategoryNullOldCategoryThrowsValidationException()
    {
        assertThrows(ValidationException.class,
                () -> service.updateCategory(null, new Category("New")));
        verify(categoryRepo, never()).updateCategory(any(), any());
    }

    //testUpdateCategoryNullNewCategoryThrowsValidationException: Verifies exception when new category is null. Returns nothing.
    @Test
    public void testUpdateCategoryNullNewCategoryThrowsValidationException()
    {
        assertThrows(ValidationException.class,
                () -> service.updateCategory(new Category("Old"), null));
        verify(categoryRepo, never()).updateCategory(any(), any());
    }

    //testUpdateCategorySuccess: Verifies successful category update. Returns nothing.
    @Test
    public void testUpdateCategorySuccess() throws ValidationException
    {
        Category oldCategory = new Category(1, "Health", BigDecimal.ZERO);
        Category newCategory = new Category(1, "Wellness", BigDecimal.ZERO);
        service.updateCategory(oldCategory, newCategory);
        verify(expenseRepo).updateExpenseCategory(oldCategory, newCategory);
        verify(categoryRepo).updateCategory(oldCategory, newCategory);
    }

    //testDeleteCategoryNullCategoryThrowsValidationException: Verifies exception when deleting a null category. Returns nothing.
    @Test
    public void testDeleteCategoryNullCategoryThrowsValidationException()
    {
        assertThrows(ValidationException.class, () -> service.deleteCategory(null));
        verify(categoryRepo, never()).deleteCategory(any());
    }

    //testDeleteCategorySuccess: Verifies successful category deletion. Returns nothing.
    @Test
    public void testDeleteCategorySuccess() throws ValidationException
    {
        Category category = new Category(1, "Shopping", BigDecimal.ZERO);
        when(expenseRepo.categoryExists(category)).thenReturn(false);
        service.deleteCategory(category);
        verify(categoryRepo).deleteCategory(category);
    }

    //testDeleteCategoryUsedInExpenseThrowsException: Verifies deletion failure if category is in use. Returns nothing.
    @Test
    public void testDeleteCategoryUsedInExpenseThrowsException()
    {
        Category category = new Category(1, "Gas", BigDecimal.ZERO);
        when(expenseRepo.categoryExists(category)).thenReturn(true);
        assertThrows(ValidationException.class, () -> service.deleteCategory(category));
        verify(categoryRepo, never()).deleteCategory(any());
    }

    //testGetCategoryNullNameReturnsNull: Verifies null return for null category name. Returns nothing.
    @Test
    public void testGetCategoryNullNameReturnsNull()
    {
        assertNull(service.getCategory(null));
        verify(categoryRepo, never()).getCategoryByName(any());
    }

    //testGetCategoryEmptyNameReturnsNull: Verifies null return for empty category name. Returns nothing.
    @Test
    public void testGetCategoryEmptyNameReturnsNull()
    {
        assertNull(service.getCategory(""));
        verify(categoryRepo, never()).getCategoryByName(any());
    }

    //testGetCategoryWhitespaceNameReturnsNull: Verifies null return for whitespace category name. Returns nothing.
    @Test
    public void testGetCategoryWhitespaceNameReturnsNull()
    {
        assertNull(service.getCategory("   "));
        verify(categoryRepo, never()).getCategoryByName(any());
    }

    //testGetCategoryFound: Verifies retrieval of an existing category. Returns nothing.
    @Test
    public void testGetCategoryFound()
    {
        Category mockCat = new Category(1, "Travel", BigDecimal.ZERO);
        when(categoryRepo.getCategoryByName("Travel")).thenReturn(mockCat);
        Category result = service.getCategory("Travel");
        assertNotNull(result);
        assertEquals("Travel", result.getName());
    }

    //testGetCategoryNotFoundReturnsNull: Verifies null return for non-existent category. Returns nothing.
    @Test
    public void testGetCategoryNotFoundReturnsNull()
    {
        when(categoryRepo.getCategoryByName("Unknown")).thenReturn(null);
        assertNull(service.getCategory("Unknown"));
    }

    //testGetMonthSpentNullCategoryNameReturnsZero: Verifies zero spent for null category. Returns nothing.
    @Test
    public void testGetMonthSpentNullCategoryNameReturnsZero()
    {
        assertEquals(BigDecimal.ZERO, service.getMonthSpent(null, LocalDate.now()));
    }

    //testGetMonthSpentNullDateReturnsZero: Verifies zero spent for null date. Returns nothing.
    @Test
    public void testGetMonthSpentNullDateReturnsZero()
    {
        assertEquals(BigDecimal.ZERO, service.getMonthSpent("Food", null));
    }

    //testGetMonthSpentNoMatchingExpensesReturnsZero: Verifies zero spent when no expenses match. Returns nothing.
    @Test
    public void testGetMonthSpentNoMatchingExpensesReturnsZero()
    {
        when(expenseRepo.getAllExpenses()).thenReturn(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, service.getMonthSpent("Food", LocalDate.of(2026, 3, 1)));
    }

    //testGetMonthSpentMatchingExpensesSumsCorrectly: Verifies correct summation of monthly expenses. Returns nothing.
    @Test
    public void testGetMonthSpentMatchingExpensesSumsCorrectly()
    {
        Expense e1 = new Expense(1, "Lunch", new BigDecimal("25.00"), "Food", LocalDate.of(2026, 3, 10), "");
        Expense e2 = new Expense(2, "Dinner", new BigDecimal("35.50"), "Food", LocalDate.of(2026, 3, 15), "");
        Expense e3 = new Expense(3, "Bus", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 3, 5), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e1, e2, e3));

        BigDecimal result = service.getMonthSpent("Food", LocalDate.of(2026, 3, 1));
        assertEquals(new BigDecimal("60.50"), result);
    }

    //testGetMonthSpentDifferentMonthReturnsZero: Verifies expenses from other months are excluded. Returns nothing.
    @Test
    public void testGetMonthSpentDifferentMonthReturnsZero()
    {
        Expense e = new Expense(1, "Lunch", new BigDecimal("25.00"), "Food", LocalDate.of(2026, 2, 10), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        BigDecimal result = service.getMonthSpent("Food", LocalDate.of(2026, 3, 1));
        assertEquals(BigDecimal.ZERO, result);
    }

    //testGetMonthSpentExpenseWithNullCategorySkipped: Verifies expenses with null categories are ignored. Returns nothing.
    @Test
    public void testGetMonthSpentExpenseWithNullCategorySkipped()
    {
        Expense e = new Expense(1, "Unknown", new BigDecimal("10.00"), null, LocalDate.of(2026, 3, 1), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        BigDecimal result = service.getMonthSpent("Food", LocalDate.of(2026, 3, 1));
        assertEquals(BigDecimal.ZERO, result);
    }

    //testHasExceededBudgetCategoryNotFoundReturnsFalse: Verifies budget check returns false if category is missing. Returns nothing.
    @Test
    public void testHasExceededBudgetCategoryNotFoundReturnsFalse()
    {
        when(categoryRepo.getCategoryByName("Ghost")).thenReturn(null);
        assertFalse(service.hasExceededBudget("Ghost", LocalDate.now()));
    }

    //testHasExceededBudgetUnderBudgetReturnsFalse: Verifies false when expenses are within budget. Returns nothing.
    @Test
    public void testHasExceededBudgetUnderBudgetReturnsFalse()
    {
        Category cat = new Category(1, "Food", new BigDecimal("200.00"));
        when(categoryRepo.getCategoryByName("Food")).thenReturn(cat);

        Expense e = new Expense(1, "Lunch", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 3, 1), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        assertFalse(service.hasExceededBudget("Food", LocalDate.of(2026, 3, 1)));
    }

    //testHasExceededBudgetOverBudgetReturnsTrue: Verifies true when expenses exceed budget. Returns nothing.
    @Test
    public void testHasExceededBudgetOverBudgetReturnsTrue()
    {
        Category cat = new Category(1, "Food", new BigDecimal("30.00"));
        when(categoryRepo.getCategoryByName("Food")).thenReturn(cat);

        Expense e = new Expense(1, "Dinner", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 3, 5), "");
        when(expenseRepo.getAllExpenses()).thenReturn(Arrays.asList(e));

        assertTrue(service.hasExceededBudget("Food", LocalDate.of(2026, 3, 1)));
    }

    //testHasExceededBudgetNullNameReturnsFalse: Verifies budget check returns false for null name. Returns nothing.
    @Test
    public void testHasExceededBudgetNullNameReturnsFalse()
    {
        assertFalse(service.hasExceededBudget(null, LocalDate.now()));
    }
}