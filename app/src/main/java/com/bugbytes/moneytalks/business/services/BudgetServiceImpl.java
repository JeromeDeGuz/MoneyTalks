package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.BudgetSummary;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BudgetServiceImpl implements BudgetService
{
    private final CategoryService categoryService;
    private final ExpenseService expenseService;

    //BudgetServiceImpl: Constructor to initialize the service with category and expense services. Takes in @param categoryService and expenseService.
    public BudgetServiceImpl(CategoryService categoryService, ExpenseService expenseService)
    {
        this.categoryService = categoryService;
        this.expenseService = expenseService;
    }

    //getMonthlyBudgetSummary: Returns the budget overview for all categories in the selected year and month. Takes in @param year and month.
    @Override
    public List<BudgetSummary> getMonthlyBudgetSummary(int year, int month)
    {
        List<Category> categories = categoryService.getAllCategories();
        List<Expense> expenses = expenseService.getAllExpenses();
        List<BudgetSummary> summaries = new ArrayList<>();

        for (Category category : categories)
        {
            BigDecimal spentThisMonth = BigDecimal.ZERO;

            for (Expense expense : expenses)
            {
                if (expense.getCategory().equals(category.getName())
                        && expense.getDate().getYear() == year
                        && expense.getDate().getMonthValue() == month)
                {
                    spentThisMonth = spentThisMonth.add(expense.getAmount());
                }
            }

            summaries.add(new BudgetSummary(
                    category.getName(),
                    category.getBudget(),
                    spentThisMonth
            ));
        }

        return summaries;
    }

    //getCategoryBudgetSummary: Returns the budget summary for one category in the selected year and month. Takes in @param categoryName, year, and month.
    @Override
    public BudgetSummary getCategoryBudgetSummary(String categoryName, int year, int month)
    {
        List<BudgetSummary> summaries = getMonthlyBudgetSummary(year, month);

        for (BudgetSummary summary : summaries)
        {
            if (summary.getCategoryName().equals(categoryName))
            {
                return summary;
            }
        }

        return new BudgetSummary(categoryName, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    //updateCategoryBudget: Updates only the budget amount for the specified category. Takes in @param categoryName and newBudget. @throws IllegalArgumentException if category is not found.
    @Override
    public void updateCategoryBudget(String categoryName, BigDecimal newBudget)
    {
        Category oldCategory = categoryService.getCategory(categoryName);

        if (oldCategory == null)
        {
            throw new IllegalArgumentException("Category not found.");
        }

        Category updatedCategory = new Category(oldCategory.getName(), newBudget);

        try
        {
            categoryService.updateCategory(oldCategory, updatedCategory);
        }
        catch (ValidationException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}