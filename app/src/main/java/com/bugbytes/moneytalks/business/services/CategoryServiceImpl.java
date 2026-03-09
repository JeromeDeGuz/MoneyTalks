package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import java.util.List;
import java.util.Objects;

public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository)
    {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
    }
    @Override
    public void addCategory(Category category)
    {
        // Business Rule 1: Check if null or empty
        if (category == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        // Business Rule 2: Case-insensitive duplicate check
        List<Category> existing = repository.getAllCategories();
        for (Category c : existing) {
            if (c.getName().equalsIgnoreCase(category.getName())) {
                //agar pehle se maujood hai toh add nahi karenge
                return;
            }
        }

        repository.addCategory(category);
    }
    @Override
    public List<Category> getAllCategories()
    {
        return repository.getAllCategories();
    }

    @Override
    public void updateCategory(Category category)
    {
        repository.updateCategory(category);
    }

    @Override
    public void deleteCategory(Category category)
    {
        repository.deleteCategory(category);
    }

    @Override
    public Category getCategory(String categoryName)
    {
        return repository.getCategoryByName(categoryName);
    }
}