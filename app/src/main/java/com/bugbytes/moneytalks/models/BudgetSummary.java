package com.bugbytes.moneytalks.models;

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
        this.budget = (budget != null) ? budget : BigDecimal.ZERO;
        this.spentThisMonth = (spentThisMonth != null) ? spentThisMonth : BigDecimal.ZERO;
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

    //isOverBudget: Returns whether the spent amount is greater than the budget. Takes in nothing and @return boolean.
    public boolean isOverBudget()
    {
        return spentThisMonth.compareTo(budget) > 0;
    }

    //getOverAmount: Returns how much the category is over budget. Takes in nothing and @return BigDecimal overAmount.
    public BigDecimal getOverAmount()
    {
        if (!isOverBudget())
        {
            return BigDecimal.ZERO;
        }

        return spentThisMonth.subtract(budget);
    }
}