package com.bugbytes.moneytalks.Application;

import android.app.Application;

import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Business.Services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
import com.bugbytes.moneytalks.Presistence.Fake.FakeRepository;




public class MoneyTalksApp extends Application {

    private ExpenseService expenseService;

    @Override
    public void onCreate() {
        super.onCreate();

        ExpenseRepository expenseRepository = new FakeRepository();
        expenseService = new ExpenseServiceImpl(expenseRepository);
        }

    public ExpenseService getExpenseService() {
        return expenseService;
    }
}
