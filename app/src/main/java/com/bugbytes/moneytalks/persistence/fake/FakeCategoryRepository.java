package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeCategoryRepository implements CategoryRepository
{
    private final List<Category> categories = new ArrayList<>();

    //FakeCategoryRepository: Constructor that populates initial data using DefaultContent. Takes in nothing.
    public FakeCategoryRepository()
    {
    }

    //addCategory: It adds a new category object to the in-memory list. Takes in @param category.
    @Override
    public void addCategory(Category category)
    {
        categories.add(category);
    }

    //getAllCategories: It returns a copy of the list containing all categories. Takes in nothing and @return List of categories.
    @Override
    public List<Category> getAllCategories()
    {
        return new ArrayList<>(categories);
    }

    //updateCategory: It finds the old category by name and updates it. Takes in @param oldCategory and newCategory.
    @Override
    public void updateCategory(Category oldCategory, Category newCategory)
    {
        for (Category x : categories)
        {
            if (x.getName().equals(oldCategory.getName()))
            {
                x.setName(newCategory.getName());
            }
        }
    }

    //deleteCategory: It removes the specified category from the list. Takes in @param category.
    @Override
    public void deleteCategory(Category category)
    {
        categories.remove(category);
    }

    //getCategoryByName: It searches for a category with a matching name string. Takes in @param name and @return Category object.
    @Override
    public Category getCategoryByName(String name)
    {
        for (Category x : categories)
        {
            if (x.getName().equals(name))
            {
                return x;
            }
        }
        return null;
    }

    //isEmpty: It checks if the category list is currently empty. Takes in nothing and @return boolean result.
    @Override
    public boolean isEmpty()
    {
        return categories.isEmpty();
    }
}
