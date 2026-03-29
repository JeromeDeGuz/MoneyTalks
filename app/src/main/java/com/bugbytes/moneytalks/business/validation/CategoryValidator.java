package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.util.List;
import java.util.Objects;

public class CategoryValidator implements Validator<Category>
{
    private final CategoryRepository repo;

    public CategoryValidator(CategoryRepository repo)
    {
        //Added null check as per Iteration 1 feedback to prevent initialization with null repo
        this.repo = Objects.requireNonNull(repo, "Repository cannot be null");
    }

    //validate: It runs all business rule checks for a category. Takes in @param category.
    @Override
    public void validate(Category category) throws ValidationException
    {
        isNullorEmpty(category);
        isCaseDuplicate(category);
        isNotNumbers(category);
        isValidBudget(category);
    }

    //isNullorEmpty: It checks if category is null or has no text. Takes in @param category.
    private void isNullorEmpty(Category category) throws ValidationException
    {
        if (category == null || category.getName().trim().isEmpty())
        {
            throw new ValidationException("Category name cannot be empty");
        }
    }

    //isCaseDuplicate: It prevents duplicate names regardless of capitalization. Takes in @param category.
    private void isCaseDuplicate(Category category) throws ValidationException
    {
        List<Category> existing = repo.getAllCategories();
        for (Category c : existing)
        {
            if (c.getName().equalsIgnoreCase(category.getName()))
            {
                throw new ValidationException("Category name already exists");
            }
        }
    }

    //validateDelete: It ensures a category isn't used by expenses before removal. Takes in @param category and expenseRepo.
    public void validateDelete(Category category, ExpenseRepository expenseRepo) throws ValidationException
    {
        if (expenseRepo.categoryExists(category))
        {
            throw new ValidationException("Cannot delete category that exists within an expense");
        }
    }

    //isNotNumbers: It checks that the name isn't just a string of digits. Takes in @param category.
    private void isNotNumbers(Category category) throws ValidationException
    {
        if (category.getName().trim().matches("^\\d+$"))
        {
            throw new ValidationException("Category name cannot be only numbers.");
        }
    }

    private void isValidBudget(Category category) throws ValidationException
    {
        if (category.getBudget() == null)
        {
            throw new ValidationException("Budget cannot be null.");
        }

        if (category.getBudget().compareTo(java.math.BigDecimal.ZERO) < 0)
        {
            throw new ValidationException("Budget cannot be negative.");
        }
    }
}