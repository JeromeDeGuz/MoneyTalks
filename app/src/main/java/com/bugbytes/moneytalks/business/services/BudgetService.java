package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.models.BudgetSummary;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetService
{
    //getMonthlyBudgetSummary: Returns the budget overview for all categories in the selected year and month.
    List<BudgetSummary> getMonthlyBudgetSummary(int year, int month);

    //getCategoryBudgetSummary: Returns the budget summary for one category in the selected year and month. Takes in @param categoryName and year and month.
    BudgetSummary getCategoryBudgetSummary(String categoryName, int year, int month);

    //updateCategoryBudget: Updates only the budget amount for the specified category. Takes in @param categoryName and newBudget.
    void updateCategoryBudget(String categoryName, BigDecimal newBudget);
}