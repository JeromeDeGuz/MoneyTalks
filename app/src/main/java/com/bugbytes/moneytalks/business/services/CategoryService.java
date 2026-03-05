package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Category;

public interface CategoryService {

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Category category);

    Category getCategory(Category categoryName);

}