package com.bugbytes.moneytalks.application;

//Android Application class import

import android.app.Application;

//Business layer imports: services and validators
import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;

//Persistence layer imports: repositories (both fake and real)
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeCategoryRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeExpenseRepository;
import com.bugbytes.moneytalks.persistence.real.SqlExpenseRepository;

//Category feature imports (new feature)
import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.business.services.CategoryServiceImpl;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.real.SqlCategoryRepository;

//This class is created once when the app starts and acts as a central place to initialize al shared services and repositories.
public class MoneyTalksApp extends Application
{
    //shared services accessible throughout the entire app
    private ExpenseService expenseService;      //Handles business logic for expenses
    private CategoryService categoryService;    //Handles business logic for categories

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Expense Repository Setup: You can switch between a fake repository (for testing) and a real SQL repository (for production)

        //Fake repository (for testing without database) – currently commented out
        //ExpenseRepository expenseRepository = new FakeExpenseRepository();

        //Real repository using SQLite – currently active
        ExpenseRepository expenseRepository = new SqlExpenseRepository(this);

        // Category Repository Setup: Similar approach for categories as fake vs real repository

        //Fake repository (for testing) – currently commented out
        //CategoryRepository categoryRepository = new FakeCategoryRepository();

        //Real repository using SQLite – currently active
        CategoryRepository categoryRepository = new SqlCategoryRepository(this);


        // Category Validator Setup: Validators check that data is correct before saving it
        CategoryValidator categoryValidator = new CategoryValidator(categoryRepository);

        // Category Service Setup: Service connects repository and validator, providing business logic
        categoryService = new CategoryServiceImpl(categoryRepository, categoryValidator, expenseRepository);

        ExpenseValidator expenseValidator = new ExpenseValidator();

        expenseService = new ExpenseServiceImpl(expenseRepository, expenseValidator);
    }

    //Getters for Services
    public ExpenseService getExpenseService()
    {
        return expenseService;
    }

    public CategoryService getCategoryService()
    {
        return categoryService;
    }
}