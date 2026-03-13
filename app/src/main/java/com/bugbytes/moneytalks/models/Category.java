package com.bugbytes.moneytalks.models;

import java.io.Serializable;
import java.math.BigDecimal;
// Serializable is important if you want to pass Category between Activities via Intent
public class Category implements Serializable
{
    private int id;
    private String name; // Consistent naming with getter/setter
    private BigDecimal budget;

    // Standard constructor for new categories (ID will be assigned by DB)
    public Category(String name)
    {
        this.name = name;
        this.budget = BigDecimal.ZERO;
    }



    // Overloaded constructor for database retrieval (includes ID)
    public Category(int id, String name, BigDecimal budget)
    {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    public int getId() { return id; }

    // We use getName to keep it simple and clean
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getBudget()
    {
        return this.budget;
    }

    public void setBudget(BigDecimal budget)
    {
        this.budget = budget;
    }

    // Helpful for debugging
    @Override
    public String toString() {
        return name;
    }
}