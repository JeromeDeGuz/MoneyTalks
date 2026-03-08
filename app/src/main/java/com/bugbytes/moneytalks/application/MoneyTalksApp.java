package com.bugbytes.moneytalks.application;

import android.app.Application;

import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.real.SqlRepository;

//Creates once when app starts, acts as main setup/manager for whole app.
public class MoneyTalksApp extends Application
{
    //shared services used across entire app
    private ExpenseService expenseService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        //create repo using our fake db
//        ExpenseRepository expenseRepository = new FakeRepository();
        ExpenseRepository expenseRepository = new SqlRepository(this);
        ExpenseValidator expenseValidator = new ExpenseValidator();
        //create service and connect it to repo
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseValidator);
    }

    public ExpenseService getExpenseService()
    {
        return expenseService;
    }
}
