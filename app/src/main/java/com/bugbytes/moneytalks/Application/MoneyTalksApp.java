package com.bugbytes.moneytalks.Application;

import android.app.Application;

import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Business.Services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;


public class MoneyTalksApp extends Application {

    private ExpenseService expenseService;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
