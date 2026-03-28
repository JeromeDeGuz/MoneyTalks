package com.bugbytes.moneytalks.business.models;

import java.math.BigDecimal;

public class BudgetSummary
{
    private final String categoryName;
    private final BigDecimal budget;
    private final BigDecimal spentThisMonth;

    //BudgetSummary: Constructor to store the budget page data for one category. Takes in @param categoryName and budget and spentThisMonth.
    public BudgetSummary(String categoryName, BigDecimal budget, BigDecimal spentThisMonth)
    {
        this.categoryName = categoryName;
        this.budget = budget;
        this.spentThisMonth = spentThisMonth;
    }

    //getCategoryName: Returns the category name for this row. Takes in nothing and @return String categoryName.
    public String getCategoryName()
    {
        return categoryName;
    }

    //getBudget: Returns the budget amount for this row. Takes in nothing and @return BigDecimal budget.
    public BigDecimal getBudget()
    {
        return budget;
    }

    //getSpentThisMonth: Returns the spent amount for the selected month. Takes in nothing and @return BigDecimal spentThisMonth.
    public BigDecimal getSpentThisMonth()
    {
        return spentThisMonth;
    }
}