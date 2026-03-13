package com.bugbytes.moneytalks.models;

import java.io.Serializable;
import java.math.BigDecimal;

//Serializable is important if you want to pass Category between Activities via Intent
public class Category implements Serializable
{
    private int id;
    private String name;

    //Note: Budget is not being implemented in i2, we will do it in i3.
    private BigDecimal budget;

    //Standard constructor for new categories (ID will be assigned by DB)
    public Category(String name)
    {
        this.name = name;
        this.budget = BigDecimal.ZERO;
    }


    //Overloaded constructor for database retrieval (includes ID)
    public Category(int id, String name, BigDecimal budget)
    {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    //Getters
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return this.name;
    }

    public BigDecimal getBudget()
    {
        return this.budget;
    }

    //Setters
    public void setName(String name)
    {
        this.name = name;
    }

}