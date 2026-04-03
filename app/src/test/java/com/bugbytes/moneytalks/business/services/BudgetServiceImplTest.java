package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.BudgetSummary;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;

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

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceImplTest
{
    private BudgetServiceImpl budgetService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ExpenseService expenseService;

    //setup: Prepares the service with mocked dependencies before each test. Returns nothing.
    @BeforeEach
    public void setUp()
    {
        budgetService = new BudgetServiceImpl(categoryService, expenseService);
    }

    //getMonthlyBudgetSummaryNoCategoriesReturnsEmptyList: Verifies summary is empty when no categories exist. Returns nothing.
    @Test
    public void getMonthlyBudgetSummaryNoCategoriesReturnsEmptyList()
    {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertTrue(result.isEmpty());
    }

    //getMonthlyBudgetSummaryNoExpensesReturnsZeroSpent: Verifies spent amount is zero when no expenses exist. Returns nothing.
    @Test
    public void getMonthlyBudgetSummaryNoExpensesReturnsZeroSpent()
    {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Food", new BigDecimal("200.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getCategoryName());
        assertEquals(new BigDecimal("200.00"), result.get(0).getBudget());
        assertEquals(BigDecimal.ZERO, result.get(0).getSpentThisMonth());
    }

    //getMonthlyBudgetSummaryCorrectMonthSumsExpenses: Verifies expenses are summed correctly for the target month. Returns nothing.
    @Test
    public void getMonthlyBudgetSummaryCorrectMonthSumsExpenses()
    {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Food", new BigDecimal("200.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Lunch", new BigDecimal("25.00"), "Food", LocalDate.of(2026, 3, 10), ""),
                new Expense(2, "Dinner", new BigDecimal("35.50"), "Food", LocalDate.of(2026, 3, 15), "")
        ));

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertEquals(new BigDecimal("60.50"), result.get(0).getSpentThisMonth());
    }

    //getMonthlyBudgetSummaryDifferentMonthExcludesExpenses: Verifies expenses from other months are ignored. Returns nothing.
    @Test
    public void getMonthlyBudgetSummaryDifferentMonthExcludesExpenses()
    {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Food", new BigDecimal("200.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Old Lunch", new BigDecimal("99.00"), "Food", LocalDate.of(2026, 2, 28), "")
        ));

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertEquals(BigDecimal.ZERO, result.get(0).getSpentThisMonth());
    }

    //getMonthlyBudgetSummaryMultipleCategoriesSumsSeparately: Verifies totals are calculated per category. Returns nothing.
    @Test
    public void getMonthlyBudgetSummaryMultipleCategoriesSumsSeparately()
    {
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(
                new Category(1, "Food", new BigDecimal("200.00")),
                new Category(2, "Transport", new BigDecimal("100.00"))
        ));
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Lunch", new BigDecimal("25.00"), "Food", LocalDate.of(2026, 3, 1), ""),
                new Expense(2, "Bus", new BigDecimal("15.00"), "Transport", LocalDate.of(2026, 3, 2), "")
        ));

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertEquals(2, result.size());

        BudgetSummary foodSummary = null;
        BudgetSummary transportSummary = null;
        for (BudgetSummary s : result)
        {
            if (s.getCategoryName().equals("Food"))
            {
                foodSummary = s;
            }
            else if (s.getCategoryName().equals("Transport"))
            {
                transportSummary = s;
            }
        }

        assertNotNull(foodSummary);
        assertNotNull(transportSummary);
        assertEquals(new BigDecimal("25.00"), foodSummary.getSpentThisMonth());
        assertEquals(new BigDecimal("15.00"), transportSummary.getSpentThisMonth());
    }

    //getCategoryBudgetSummaryFoundReturnsCorrectSummary: Verifies summary for a specific existing category. Returns nothing.
    @Test
    public void getCategoryBudgetSummaryFoundReturnsCorrectSummary()
    {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Food", new BigDecimal("500.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Book", new BigDecimal("80.00"), "Food", LocalDate.of(2026, 3, 5), "")
        ));

        BudgetSummary result = budgetService.getCategoryBudgetSummary("Food", 2026, 3);

        assertNotNull(result);
        assertEquals("Food", result.getCategoryName());
        assertEquals(new BigDecimal("500.00"), result.getBudget());
        assertEquals(new BigDecimal("80.00"), result.getSpentThisMonth());
    }

    //getCategoryBudgetSummaryNotFoundReturnsZeroSummary: Verifies zero values when category is not found. Returns nothing.
    @Test
    public void getCategoryBudgetSummaryNotFoundReturnsZeroSummary()
    {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        BudgetSummary result = budgetService.getCategoryBudgetSummary("Unknown", 2026, 3);

        assertNotNull(result);
        assertEquals("Unknown", result.getCategoryName());
        assertEquals(BigDecimal.ZERO, result.getBudget());
        assertEquals(BigDecimal.ZERO, result.getSpentThisMonth());
    }

    //getCategoryBudgetSummaryOverBudgetDetectedCorrectly: Verifies over budget flag and amount. Returns nothing.
    @Test
    public void getCategoryBudgetSummaryOverBudgetDetectedCorrectly()
    {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Shopping", new BigDecimal("50.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Shoes", new BigDecimal("40.00"), "Shopping", LocalDate.of(2026, 3, 2), ""),
                new Expense(2, "Hat", new BigDecimal("25.00"), "Shopping", LocalDate.of(2026, 3, 6), "")
        ));

        BudgetSummary result = budgetService.getCategoryBudgetSummary("Shopping", 2026, 3);

        assertTrue(result.isOverBudget());
        assertEquals(new BigDecimal("15.00"), result.getOverAmount());
    }

    //getCategoryBudgetSummaryUnderBudgetNotOverBudget: Verifies under budget status. Returns nothing.
    @Test
    public void getCategoryBudgetSummaryUnderBudgetNotOverBudget()
    {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Food", new BigDecimal("200.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Lunch", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 3, 1), "")
        ));

        BudgetSummary result = budgetService.getCategoryBudgetSummary("Food", 2026, 3);

        assertFalse(result.isOverBudget());
        assertEquals(BigDecimal.ZERO, result.getOverAmount());
    }

    //updateCategoryBudgetCategoryExistsCallsUpdateCategory: Verifies category update call when category exists. Returns nothing.
    @Test
    public void updateCategoryBudgetCategoryExistsCallsUpdateCategory() throws Exception
    {
        Category existing = new Category(1, "Food", new BigDecimal("100.00"));
        when(categoryService.getCategory("Food")).thenReturn(existing);

        budgetService.updateCategoryBudget("Food", new BigDecimal("250.00"));

        verify(categoryService).updateCategory(
                eq(existing),
                argThat(c -> c.getName().equals("Food") && c.getBudget().compareTo(new BigDecimal("250.00")) == 0)
        );
    }

    //updateCategoryBudgetCategoryNotFoundThrowsException: Verifies exception is thrown for missing category. Returns nothing.
    @Test
    public void updateCategoryBudgetCategoryNotFoundThrowsException() throws Exception
    {
        when(categoryService.getCategory("Ghost")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> budgetService.updateCategoryBudget("Ghost", new BigDecimal("100.00")));

        verify(categoryService, never()).updateCategory(any(), any());
    }
}