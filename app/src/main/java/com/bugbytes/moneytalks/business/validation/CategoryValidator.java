package com.bugbytes.moneytalks.business.validation;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import java.util.List;


public class CategoryValidator implements Validator<Category>{

    CategoryRepository repo;
    public CategoryValidator(CategoryRepository repo){
        this.repo = repo;
    }

    @Override
    public void validate(Category category){
       isNullorEmpty(category);
       isCaseDuplicate(category);
       isNotNumbers(category);
    }

    //Check for null or empty
    private void isNullorEmpty(Category category){
        if (category == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }
    }

    //Check for duplicates case-insensitive
    private void isCaseDuplicate(Category category){
        List<Category> existing = repo.getAllCategories();
        for (Category c : existing) {
            if (c.getName().equalsIgnoreCase(category.getName())) {
                throw new ValidationException("Category name already exists");
            }
        }
    }

    //Check that Category name isn't all numbers e.g. "123124125"
    private void isNotNumbers(Category category){
            if (category.getName().trim().matches("^\\d+$"))
            {
                throw new ValidationException("Category name cannot be only numbers.");
            }
    }

    //do not delete a category that exists within any expense
    public void validateDelete(Category category, ExpenseRepository expenseRepo){
        if(expenseRepo.categoryExists(category)){
            throw new ValidationException("Cannot delete category that exists within an expense");
        }
    }
}
