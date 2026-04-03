package com.bugbytes.moneytalks.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetSummaryModelTest
{

    @Test
    public void constructor_NullBudget_DefaultsToZero()
    {
        BudgetSummary s = new BudgetSummary("Food", null, new BigDecimal("10.00"));
        assertEquals(BigDecimal.ZERO, s.getBudget());
    }

    @Test
    public void constructor_NullSpent_DefaultsToZero()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("100.00"), null);
        assertEquals(BigDecimal.ZERO, s.getSpentThisMonth());
    }

    @Test
    public void isOverBudget_UnderBudget_ReturnsFalse()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));
        assertFalse(s.isOverBudget());
    }

    @Test
    public void isOverBudget_OverBudget_ReturnsTrue()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("50.00"), new BigDecimal("75.00"));
        assertTrue(s.isOverBudget());
    }

    @Test
    public void isOverBudget_ExactlyAtBudget_ReturnsFalse()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("50.00"), new BigDecimal("50.00"));
        assertFalse(s.isOverBudget());
    }

    @Test
    public void getOverAmount_NotOverBudget_ReturnsZero()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));
        assertEquals(BigDecimal.ZERO, s.getOverAmount());
    }

    @Test
    public void getOverAmount_OverBudget_ReturnsCorrectAmount()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("50.00"), new BigDecimal("65.00"));
        assertEquals(new BigDecimal("15.00"), s.getOverAmount());
    }

    @Test
    public void getCategoryName_ReturnsCorrectName()
    {
        BudgetSummary s = new BudgetSummary("Transport", new BigDecimal("100.00"), BigDecimal.ZERO);
        assertEquals("Transport", s.getCategoryName());
    }
}