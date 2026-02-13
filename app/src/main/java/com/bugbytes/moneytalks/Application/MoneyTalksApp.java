package com.bugbytes.moneytalks.Application;

import android.app.Application;
import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Business.Services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.Business.Validation.ExpenseValidator;
import com.bugbytes.moneytalks.Persistence.ExpenseRepository;
import com.bugbytes.moneytalks.Persistence.Fake.FakeRepository;

//Note: creates once when app starts, acts as main setup/manager for whole app.
public class MoneyTalksApp extends Application
{
    //shared services used across entire app
    private ExpenseService expenseService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        //create repo using our fake db
        ExpenseRepository expenseRepository = new FakeRepository();
        ExpenseValidator expenseValidator = new ExpenseValidator();
        //create service and connect it to repo
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseValidator);
    }
    public ExpenseService getExpenseService()
    {
        return expenseService;
    }
}
