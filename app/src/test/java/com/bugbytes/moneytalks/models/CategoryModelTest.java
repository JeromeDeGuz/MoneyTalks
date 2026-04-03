package com.bugbytes.moneytalks.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryModelTest
{

    @Test
    public void constructor_NameOnly_DefaultsBudgetToZero()
    {
        Category c = new Category("Food");
        assertEquals("Food", c.getName());
        assertEquals(BigDecimal.ZERO, c.getBudget());
    }

    @Test
    public void constructor_NameAndBudget_SetsBoth()
    {
        Category c = new Category("Food", new BigDecimal("100.00"));
        assertEquals("Food", c.getName());
        assertEquals(new BigDecimal("100.00"), c.getBudget());
    }

    @Test
    public void constructor_NameAndNullBudget_DefaultsToZero()
    {
        Category c = new Category("Food", null);
        assertEquals(BigDecimal.ZERO, c.getBudget());
    }

    @Test
    public void constructor_IdNameBudget_SetsAll()
    {
        Category c = new Category(1, "Food", new BigDecimal("50.00"));
        assertEquals(1, c.getId());
        assertEquals("Food", c.getName());
        assertEquals(new BigDecimal("50.00"), c.getBudget());
    }

    @Test
    public void setName_UpdatesName()
    {
        Category c = new Category("Food");
        c.setName("Transport");
        assertEquals("Transport", c.getName());
    }

    @Test
    public void setBudget_UpdatesBudget()
    {
        Category c = new Category("Food");
        c.setBudget(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), c.getBudget());
    }

    @Test
    public void toString_ReturnsName()
    {
        Category c = new Category("Food");
        assertEquals("Food", c.toString());
    }
}