package com.bugbytes.moneytalks.models;

import java.io.Serializable;
import java.math.BigDecimal;

//Serializable is important if you want to pass Category between Activities via Intent
public class Category implements Serializable
{
    private int id;
    private String name;
    private BigDecimal budget;
    public static final BigDecimal DEFAULT_BUDGET = new BigDecimal("500.00");

    //Category: Constructor for new categories where ID is assigned by DB. Takes in @param name.
    public Category(String name)
    {
        this.name = name;
        this.budget = DEFAULT_BUDGET;
    }

    // Category: constructor for name + budget
    public Category(String name, BigDecimal budget)
    {
        this.name = name;
        this.budget = budget;
    }

    //Category: Overloaded constructor for database retrieval. Takes in @param id and name and budget.
    public Category(int id, String name, BigDecimal budget)
    {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    //getId: Returns the unique identifier. Takes in nothing and @return int id.
    public int getId()
    {
        return id;
    }

    //getName: Returns the category name. Takes in nothing and @return String name.
    public String getName()
    {
        return this.name;
    }

    //getBudget: Returns the allocated budget amount. Takes in nothing and @return BigDecimal budget.
    public BigDecimal getBudget()
    {
        return this.budget;
    }

    public void setBudget(BigDecimal budget) { this.budget = budget; }

    //setName: Updates the category name. Takes in @param name.
    public void setName(String name)
    {
        this.name = name;
    }
}