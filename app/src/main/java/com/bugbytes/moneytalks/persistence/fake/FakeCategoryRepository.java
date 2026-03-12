package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FakeCategoryRepository implements CategoryRepository {
    private final List<Category> categories = new ArrayList<>();
    private int nextId = 1;

    public FakeCategoryRepository() {
        // Initial data
        addCategory(new Category(nextId++, "Food", BigDecimal.ZERO));
        addCategory(new Category(nextId++, "Transport", BigDecimal.ZERO));
    }

    @Override
    public void addCategory(Category category) {
        Category toAdd = new Category(nextId++, category.getName(), category.getBudget());
        categories.add(toAdd);
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    @Override
    public void updateCategory(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == category.getId()) {
                categories.set(i, category);
                return;
            }
        }
    }

    @Override
    public void deleteCategory(Category category) {
        categories.removeIf(c -> c.getId() == category.getId());
    }

    @Override
    public Category getCategoryByName(String name) {
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return categories.isEmpty();
    }
}
