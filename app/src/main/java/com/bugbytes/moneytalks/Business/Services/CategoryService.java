package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Models.Category;

public interface CategoryService {

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Category category);

    Category getCategory(Category categoryName);

}