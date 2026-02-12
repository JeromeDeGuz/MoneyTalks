package com.bugbytes.moneytalks.Application;

import android.app.Application;
//business layer
import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Business.Services.ExpenseServiceImpl;
//presistence layer
import com.bugbytes.moneytalks.Business.Validation.ExpenseValidator;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
import com.bugbytes.moneytalks.Presistence.Fake.FakeRepository;

//Note: creates once when app starts, acts as main setup/manager for whole app.
public class MoneyTalksApp extends Application
{
    //shared services used across entire app
    private ExpenseService expenseService;

    //inititalises layersssssss
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

    //allows activities that ziya & jenna created to access expenseService (keeps business logc seperate from our UI)
    public ExpenseService getExpenseService()
    {
        return expenseService;
    }
}

//Optional test:
//1. test ExpenseService is not null when app starts?