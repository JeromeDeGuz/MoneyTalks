package com.bugbytes.moneytalks.application;

//Android Application class import
import android.app.Application;

//Business layer imports: services and validators
import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.services.BudgetService;
import com.bugbytes.moneytalks.business.services.BudgetServiceImpl;
import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;

//Persistence layer imports: repositories (both fake and real)
import com.bugbytes.moneytalks.persistence.DefaultContent;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeCategoryRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeExpenseRepository;
import com.bugbytes.moneytalks.persistence.real.SqlExpenseRepository;

//Category feature imports (new feature)
import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.business.services.CategoryServiceImpl;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.real.SqlCategoryRepository;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

//This class is created once when the app starts and acts as a central place to initialize al shared services and repositories.
public class MoneyTalksApp extends Application
{
    //shared services accessible throughout the entire app
    private ExpenseService expenseService;      //Handles business logic for expenses
    private CategoryService categoryService;    //Handles business logic for categories
    private BudgetService budgetService;        //Handles business logic for monthly budget overview

    @Override
    public void onCreate()
    {

        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("moneytalks_prefs", MODE_PRIVATE);
        int savedMode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        final boolean useSqliteDB = true;

        ExpenseRepository expenseRepository;
        CategoryRepository categoryRepository;

        if (useSqliteDB)
        {
            expenseRepository = new SqlExpenseRepository(this);
            categoryRepository = new SqlCategoryRepository(this);
        }
        else
        {
            expenseRepository = new FakeExpenseRepository();
            categoryRepository = new FakeCategoryRepository();
        }

        //centralize default content population, only if both are empty
        DefaultContent defaultContent = new DefaultContent();
        defaultContent.populate(expenseRepository, categoryRepository);

        CategoryValidator categoryValidator = new CategoryValidator(categoryRepository);
        categoryService = new CategoryServiceImpl(categoryRepository, categoryValidator, expenseRepository);

        ExpenseValidator expenseValidator = new ExpenseValidator();
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseValidator);

        budgetService = new BudgetServiceImpl(categoryService, expenseService);


    }

    //Getters for Services
    public ExpenseService getExpenseService()
    {
        //Added null check as per Iteration 1 feedback to ensure app doesn't access before onCreate
        if (expenseService == null)
        {
            throw new IllegalStateException("expenseService was accessed before initialization in MoneyTalksApp.");
        }
        return expenseService;
    }

    public CategoryService getCategoryService()
    {
        //Added null check as per Iteration 1 feedback
        if (categoryService == null)
        {
            throw new IllegalStateException("categoryService was accessed before initialization in MoneyTalksApp.");
        }
        return categoryService;
    }

    public BudgetService getBudgetService()
    {
        if (budgetService == null)
        {
            throw new IllegalStateException("budgetService was accessed before initialization in MoneyTalksApp.");
        }
        return budgetService;
    }
}
