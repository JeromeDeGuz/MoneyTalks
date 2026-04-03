package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepo;
    private final ExpenseRepository expenseRepo;
    private final CategoryValidator validator;

    //CategoryServiceImpl: Constructor to initialize repositories and validator. Takes in @param categoryRepo, validator, and expenseRepo.
    public CategoryServiceImpl(CategoryRepository categoryRepo, CategoryValidator validator, ExpenseRepository expenseRepo)
    {
        this.categoryRepo = Objects.requireNonNull(categoryRepo, "Category Repository cannot be null");
        this.validator = Objects.requireNonNull(validator, "Category Validator cannot be null");
        this.expenseRepo = Objects.requireNonNull(expenseRepo, "Expense Repository cannot be null");
    }

    //addCategory: It validates and adds a new category. Takes in @param category. @throws ValidationException if category is null or invalid.
    @Override
    public void addCategory(Category category) throws ValidationException
    {
        //Ensure inputs aren't null before proceeding to business logic
        try
        {
            Objects.requireNonNull(category, "Cannot add a null category");
        }
        catch (NullPointerException e)
        {
            throw new ValidationException("Cannot add a null category");
        }

        validator.validate(category);
        categoryRepo.addCategory(category);
    }

    //getAllCategories: Returns a list of all categories. Returns @return List<Category>.
    @Override
    public List<Category> getAllCategories()
    {
        return categoryRepo.getAllCategories();
    }

    //updateCategory: It updates category data and syncs with expenses. Takes in @param oldCategory and newCategory. @throws ValidationException if null.
    @Override
    public void updateCategory(Category oldCategory, Category newCategory) throws ValidationException
    {
        try
        {
            Objects.requireNonNull(oldCategory, "Old category cannot be null");
            Objects.requireNonNull(newCategory, "New category cannot be null");
        }
        catch (NullPointerException e)
        {
            throw new ValidationException("Old or new category cannot be null");
        }

        //Per feedback: Ensure business logic handles synchronization between layers
        expenseRepo.updateExpenseCategory(oldCategory, newCategory);
        categoryRepo.updateCategory(oldCategory, newCategory);
    }

    //deleteCategory: It removes category if no expenses are linked. Takes in @param category. @throws ValidationException if expenses exist.
    @Override
    public void deleteCategory(Category category) throws ValidationException
    {
        try
        {
            Objects.requireNonNull(category, "Category to delete cannot be null");
        }
        catch (NullPointerException e)
        {
            throw new ValidationException("Category to delete cannot be null");
        }

        //Logic check: prevent deletion if expenses are still linked (richer error handling)
        if (expenseRepo.categoryExists(category))
        {
            throw new ValidationException("Cannot delete category while it still has associated expenses.");
        }

        categoryRepo.deleteCategory(category);
    }

    //getCategory: Search for specific category based on string name. Takes in @param categoryName. Returns @return Category object.
    @Override
    public Category getCategory(String categoryName)
    {
        if (categoryName == null || categoryName.trim().isEmpty())
        {
            return null;
        }
        return categoryRepo.getCategoryByName(categoryName);
    }

    //getMonthSpent: Calculates total spending for a category in a given month. Takes in @param categoryName and targetDate. Returns @return BigDecimal.
    @Override
    public BigDecimal getMonthSpent(String categoryName, LocalDate targetDate)
    {
        try
        {
            Objects.requireNonNull(categoryName, "Category name cannot be null");
            Objects.requireNonNull(targetDate, "Target date cannot be null");
        }
        catch (NullPointerException e)
        {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenseRepo.getAllExpenses())
        {
            if (expense.getCategory() != null
                    && expense.getCategory().equalsIgnoreCase(categoryName)
                    && expense.getDate() != null)
            {
                LocalDate expenseDate = expense.getDate();

                if (expenseDate.getYear() == targetDate.getYear()
                        && expenseDate.getMonthValue() == targetDate.getMonthValue())
                {
                    total = total.add(expense.getAmount());
                }
            }
        }

        return total;
    }

    //hasExceededBudget: Checks if monthly spending exceeds category budget. Takes in @param categoryName and targetDate. Returns @return boolean.
    @Override
    public boolean hasExceededBudget(String categoryName, LocalDate targetDate)
    {
        Category category = getCategory(categoryName);
        if (category == null || category.getBudget() == null)
        {
            return false;
        }

        BigDecimal spent = getMonthSpent(categoryName, targetDate);
        return spent.compareTo(category.getBudget()) > 0;
    }
}