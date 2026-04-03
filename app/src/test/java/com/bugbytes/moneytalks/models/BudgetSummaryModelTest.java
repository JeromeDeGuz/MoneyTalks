package com.bugbytes.moneytalks.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetSummaryModelTest
{
    //constructorNullBudgetDefaultsToZero: Verifies that a null budget is handled by defaulting to zero. Returns nothing.
    @Test
    public void constructorNullBudgetDefaultsToZero()
    {
        BudgetSummary s = new BudgetSummary("Food", null, new BigDecimal("10.00"));
        assertEquals(BigDecimal.ZERO, s.getBudget());
    }

    //constructorNullSpentDefaultsToZero: Verifies that null spent amount is handled by defaulting to zero. Returns nothing.
    @Test
    public void constructorNullSpentDefaultsToZero()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("100.00"), null);
        assertEquals(BigDecimal.ZERO, s.getSpentThisMonth());
    }

    //isOverBudgetUnderBudgetReturnsFalse: Confirms that being under budget correctly returns false. Returns nothing.
    @Test
    public void isOverBudgetUnderBudgetReturnsFalse()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));
        assertFalse(s.isOverBudget());
    }

    //isOverBudgetOverBudgetReturnsTrue: Confirms that exceeding the budget correctly returns true. Returns nothing.
    @Test
    public void isOverBudgetOverBudgetReturnsTrue()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("50.00"), new BigDecimal("75.00"));
        assertTrue(s.isOverBudget());
    }

    //isOverBudgetExactlyAtBudgetReturnsFalse: Verifies that spending exactly the budget amount does not count as over budget. Returns nothing.
    @Test
    public void isOverBudgetExactlyAtBudgetReturnsFalse()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("50.00"), new BigDecimal("50.00"));
        assertFalse(s.isOverBudget());
    }

    //getOverAmountNotOverBudgetReturnsZero: Confirms that the overage amount is zero when within budget. Returns nothing.
    @Test
    public void getOverAmountNotOverBudgetReturnsZero()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));
        assertEquals(BigDecimal.ZERO, s.getOverAmount());
    }

    //getOverAmountOverBudgetReturnsCorrectAmount: Verifies the calculation of the amount spent over the budget. Returns nothing.
    @Test
    public void getOverAmountOverBudgetReturnsCorrectAmount()
    {
        BudgetSummary s = new BudgetSummary("Food", new BigDecimal("50.00"), new BigDecimal("65.00"));
        assertEquals(new BigDecimal("15.00"), s.getOverAmount());
    }

    //getCategoryNameReturnsCorrectName: Verifies that the category name is correctly stored and retrieved. Returns nothing.
    @Test
    public void getCategoryNameReturnsCorrectName()
    {
        BudgetSummary s = new BudgetSummary("Transport", new BigDecimal("100.00"), BigDecimal.ZERO);
        assertEquals("Transport", s.getCategoryName());
    }
}