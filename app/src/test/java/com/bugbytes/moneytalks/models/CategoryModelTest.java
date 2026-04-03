package com.bugbytes.moneytalks.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryModelTest
{
    //constructorNameOnlyDefaultsBudgetToZero: Verifies that creating a category with only a name sets the budget to zero. Returns nothing.
    @Test
    public void constructorNameOnlyDefaultsBudgetToZero()
    {
        Category c = new Category("Food");
        assertEquals("Food", c.getName());
        assertEquals(BigDecimal.ZERO, c.getBudget());
    }

    //constructorNameAndBudgetSetsBoth: Verifies that both name and budget are correctly assigned during construction. Returns nothing.
    @Test
    public void constructorNameAndBudgetSetsBoth()
    {
        Category c = new Category("Food", new BigDecimal("100.00"));
        assertEquals("Food", c.getName());
        assertEquals(new BigDecimal("100.00"), c.getBudget());
    }

    //constructorNameAndNullBudgetDefaultsToZero: Ensures that passing a null budget results in a default value of zero. Returns nothing.
    @Test
    public void constructorNameAndNullBudgetDefaultsToZero()
    {
        Category c = new Category("Food", null);
        assertEquals(BigDecimal.ZERO, c.getBudget());
    }

    //constructorIdNameBudgetSetsAll: Verifies that ID, name, and budget are all correctly initialized. Returns nothing.
    @Test
    public void constructorIdNameBudgetSetsAll()
    {
        Category c = new Category(1, "Food", new BigDecimal("50.00"));
        assertEquals(1, c.getId());
        assertEquals("Food", c.getName());
        assertEquals(new BigDecimal("50.00"), c.getBudget());
    }

    //setNameUpdatesName: Confirms that the category name can be updated via the setter method. Returns nothing.
    @Test
    public void setNameUpdatesName()
    {
        Category c = new Category("Food");
        c.setName("Transport");
        assertEquals("Transport", c.getName());
    }

    //setBudgetUpdatesBudget: Confirms that the budget amount can be updated via the setter method. Returns nothing.
    @Test
    public void setBudgetUpdatesBudget()
    {
        Category c = new Category("Food");
        c.setBudget(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), c.getBudget());
    }

    //toStringReturnsName: Verifies that the toString method returns the category name string. Returns nothing.
    @Test
    public void toStringReturnsName()
    {
        Category c = new Category("Food");
        assertEquals("Food", c.toString());
    }
}