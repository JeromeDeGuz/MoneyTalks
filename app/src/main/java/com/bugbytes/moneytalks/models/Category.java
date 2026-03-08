package com.bugbytes.moneytalks.models;

import java.math.BigDecimal;

public class Category
{
    private int id;
    private String categoryName;
    private BigDecimal categoryBudget;

    public Category(String categoryName)
    {
        this.categoryName = categoryName;
        this.categoryBudget = BigDecimal.ZERO;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    //allows for editing of categories
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public BigDecimal getCategoryBudget()
    {
        return this.categoryBudget;
    }

    public void setCategoryBudget(BigDecimal categoryBudget)
    {
        this.categoryBudget = categoryBudget;
    }
}