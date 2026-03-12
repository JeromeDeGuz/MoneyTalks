package com.bugbytes.moneytalks.persistence.fake;

import android.content.Context;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.DefaultContent;

import java.util.ArrayList;
import java.util.List;

public class FakeCategoryRepository implements CategoryRepository
{
    private final List<Category> categories = new ArrayList<>();

    public FakeCategoryRepository()
    {
        DefaultContent defaultContent = new DefaultContent();
        defaultContent.populateCategories(this);
    }

    @Override
    public void addCategory(Category category) {
        categories.add(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    @Override
    public void updateCategory(Category oldCategory, Category newCategory) {
        for(Category x : categories){
            if(x.getName().equals(oldCategory.getName())) {
                x.setName(newCategory.getName());
            }
        }
    }

    @Override
    public void deleteCategory(Category category) {
        categories.remove(category);
    }

    @Override
    public Category getCategoryByName(String name) {
        for(Category x : categories){
            if(x.getName().equals(name)) {
                return x;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return categories.isEmpty();
    }


}
