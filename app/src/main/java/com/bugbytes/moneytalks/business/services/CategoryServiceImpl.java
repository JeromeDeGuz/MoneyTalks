package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.business.validation.Validator;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.util.List;
import java.util.Objects;

public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepo;
    private final ExpenseRepository expenseRepo;
    private final Validator<Category> validator;


    public CategoryServiceImpl(CategoryRepository categoryRepo, Validator<Category> validator, ExpenseRepository expenseRepo)
    {
        this.categoryRepo = Objects.requireNonNull(categoryRepo, "Repository cannot be null");
        this.validator = Objects.requireNonNull(validator, "Validator cannot be null");
        this.expenseRepo = Objects.requireNonNull(expenseRepo, "Expense Repository cannot be null");
    }
    @Override
    public void addCategory(Category category)
    {
        validator.validate(category);
        categoryRepo.addCategory(category);
    }
    @Override
    public List<Category> getAllCategories()
    {
        return categoryRepo.getAllCategories();
    }

    @Override
    public void updateCategory(Category oldCategory, Category newCategory)
    {

        expenseRepo.updateExpenseCategory(oldCategory, newCategory);
        categoryRepo.updateCategory(oldCategory, newCategory);
    }

    @Override
    public void deleteCategory(Category category)
    {
        if(expenseRepo.categoryExists(category)){
            throw new ValidationException("Cannot delete category with expenses");
        }
        categoryRepo.deleteCategory(category);
    }

    @Override
    public Category getCategory(String categoryName)
    {
        return categoryRepo.getCategoryByName(categoryName);
    }
}