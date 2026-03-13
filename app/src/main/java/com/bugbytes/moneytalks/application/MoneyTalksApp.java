package com.bugbytes.moneytalks.application;

import android.app.Application;

import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeCategoryRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeExpenseRepository;
import com.bugbytes.moneytalks.persistence.real.SqlExpenseRepository;
// Adding category-specific imports for the new feature
import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.business.services.CategoryServiceImpl;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.real.SqlCategoryRepository;

//Creates once when app starts, acts as main setup/manager for whole app.
public class MoneyTalksApp extends Application
{
    //shared services used across entire app
    private ExpenseService expenseService;
    private CategoryService categoryService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        //create repo using our fake db
//        ExpenseRepository expenseRepository = new FakeExpenseRepository();                      // Uncomment this to use FakeRepository (Dependency Injection)
        ExpenseRepository expenseRepository = new SqlExpenseRepository(this);           // Comment this to use FakeRepository (Dependency Injection)

        // Setup for the dynamic category feature
//        CategoryRepository categoryRepository = new FakeCategoryRepository();                   // Uncomment this to use FakeRepository (Dependency Injection)
        CategoryRepository categoryRepository = new SqlCategoryRepository(this);        // Comment this to use FakeRepository (Dependency Injection)


        CategoryValidator categoryValidator = new CategoryValidator(categoryRepository);

        categoryService = new CategoryServiceImpl(categoryRepository, categoryValidator, expenseRepository);

        ExpenseValidator expenseValidator = new ExpenseValidator();
        //create service and connect it to repo
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseValidator);
    }

    public ExpenseService getExpenseService()
    {
        return expenseService;
    }

    // New getter to allow Activities to access category management
    public CategoryService getCategoryService()
    {
        return categoryService;
    }
}