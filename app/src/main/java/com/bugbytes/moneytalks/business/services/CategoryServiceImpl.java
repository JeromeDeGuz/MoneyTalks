package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.CategoryValidator;
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
    public void updateCategory(Category category)
    {
        categoryRepo.updateCategory(category);
    }

    @Override
    public void deleteCategory(Category category)
    {
        CategoryValidator validator = (CategoryValidator) this.validator;
        validator.validateDelete(category, expenseRepo);
        categoryRepo.deleteCategory(category);
    }

    @Override
    public Category getCategory(String categoryName)
    {
        return categoryRepo.getCategoryByName(categoryName);
    }
}