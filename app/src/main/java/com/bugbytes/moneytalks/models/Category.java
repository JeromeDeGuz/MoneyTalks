package com.bugbytes.moneytalks.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class Category implements Serializable
{
    private int id;
    private String name;
    private BigDecimal budget;

    //Category: Constructor for new categories with a default zero budget. Takes in @param name.
    public Category(String name)
    {
        this.name = name;
        this.budget = BigDecimal.ZERO;
    }

    //Category: Constructor for adding new categories with a specific budget. Takes in @param name and budget.
    public Category(String name, BigDecimal budget)
    {
        this.name = name;
        this.budget = (budget != null) ? budget : BigDecimal.ZERO;
    }

    //Category: Constructor for database retrieval including unique ID. Takes in @param id, name, and budget.
    public Category(int id, String name, BigDecimal budget)
    {
        this.id = id;
        this.name = name;
        this.budget = (budget != null) ? budget : BigDecimal.ZERO;
    }

    //getId: Returns the unique database ID for the category. Returns @return int id.
    public int getId()
    {
        return id;
    }

    //getName: Returns the string name of the category. Returns @return String name.
    public String getName()
    {
        return this.name;
    }

    //getBudget: Returns the allocated budget amount. Returns @return BigDecimal budget.
    public BigDecimal getBudget()
    {
        return this.budget;
    }

    //setName: Updates the category name. Takes in @param name.
    public void setName(String name)
    {
        this.name = name;
    }

    //setBudget: Updates the category budget. Takes in @param budget.
    public void setBudget(BigDecimal budget)
    {
        this.budget = budget;
    }

    //toString: Returns the string representation of the category (name). Returns @return String name.
    @Override
    public String toString()
    {
        return name;
    }
}