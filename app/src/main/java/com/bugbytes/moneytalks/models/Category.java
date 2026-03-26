package com.bugbytes.moneytalks.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class Category implements Serializable
{
    private int id;
    private String name;
    private BigDecimal budget;

    // 1. Constructor for new categories (No budget provided)
    public Category(String name)
    {
        this.name = name;
        this.budget = BigDecimal.ZERO;
    }

    // 2. MISSING CONSTRUCTOR: For adding new categories with a budget (Fixes your error!)
    public Category(String name, BigDecimal budget)
    {
        this.name = name;
        this.budget = (budget != null) ? budget : BigDecimal.ZERO;
    }

    // 3. Constructor for database retrieval (Existing)
    public Category(int id, String name, BigDecimal budget)
    {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    public int getId() { return id; }
    public String getName() { return this.name; }
    public BigDecimal getBudget() { return this.budget; }

    public void setName(String name) { this.name = name; }

    // 4.
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    @Override
    public String toString() { return name; }
}