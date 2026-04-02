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
public class BudgetServiceImplTest {

    private BudgetServiceImpl budgetService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ExpenseService expenseService;

    @BeforeEach
    public void setUp() {
        budgetService = new BudgetServiceImpl(categoryService, expenseService);
    }

    // ---------------- getMonthlyBudgetSummary ----------------

    @Test
    public void getMonthlyBudgetSummary_NoCategories_ReturnsEmptyList() {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getMonthlyBudgetSummary_NoExpenses_ReturnsZeroSpent() {
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

    @Test
    public void getMonthlyBudgetSummary_CorrectMonth_SumsExpenses() {
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

    @Test
    public void getMonthlyBudgetSummary_DifferentMonth_ExcludesExpenses() {
        when(categoryService.getAllCategories()).thenReturn(
                Arrays.asList(new Category(1, "Food", new BigDecimal("200.00")))
        );
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(
                new Expense(1, "Old Lunch", new BigDecimal("99.00"), "Food", LocalDate.of(2026, 2, 28), "")
        ));

        List<BudgetSummary> result = budgetService.getMonthlyBudgetSummary(2026, 3);

        assertEquals(BigDecimal.ZERO, result.get(0).getSpentThisMonth());
    }

    @Test
    public void getMonthlyBudgetSummary_MultipleCategories_SumsSeparately() {
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
        for (BudgetSummary s : result) {
            if (s.getCategoryName().equals("Food")) foodSummary = s;
            else if (s.getCategoryName().equals("Transport")) transportSummary = s;
        }

        assertNotNull(foodSummary);
        assertNotNull(transportSummary);
        assertEquals(new BigDecimal("25.00"), foodSummary.getSpentThisMonth());
        assertEquals(new BigDecimal("15.00"), transportSummary.getSpentThisMonth());
    }

    // ---------------- getCategoryBudgetSummary ----------------

    @Test
    public void getCategoryBudgetSummary_Found_ReturnsCorrectSummary() {
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

    @Test
    public void getCategoryBudgetSummary_NotFound_ReturnsZeroSummary() {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        BudgetSummary result = budgetService.getCategoryBudgetSummary("Unknown", 2026, 3);

        assertNotNull(result);
        assertEquals("Unknown", result.getCategoryName());
        assertEquals(BigDecimal.ZERO, result.getBudget());
        assertEquals(BigDecimal.ZERO, result.getSpentThisMonth());
    }

    // ---------------- isOverBudget / getOverAmount ----------------

    @Test
    public void getCategoryBudgetSummary_OverBudget_DetectedCorrectly() {
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

    @Test
    public void getCategoryBudgetSummary_UnderBudget_NotOverBudget() {
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

    // ---------------- updateCategoryBudget ----------------

    @Test
    public void updateCategoryBudget_CategoryExists_CallsUpdateCategory() throws Exception {
        Category existing = new Category(1, "Food", new BigDecimal("100.00"));
        when(categoryService.getCategory("Food")).thenReturn(existing);

        budgetService.updateCategoryBudget("Food", new BigDecimal("250.00"));

        verify(categoryService).updateCategory(
                eq(existing),
                argThat(c -> c.getName().equals("Food") && c.getBudget().compareTo(new BigDecimal("250.00")) == 0)
        );
    }

    @Test
    public void updateCategoryBudget_CategoryNotFound_ThrowsException() throws Exception {
        when(categoryService.getCategory("Ghost")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> budgetService.updateCategoryBudget("Ghost", new BigDecimal("100.00")));

        verify(categoryService, never()).updateCategory(any(), any());
    }
}
