package com.bugbytes.moneytalks.application; // Lowercase package as per feedback

import android.app.Application;
import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.business.services.ExpenseServiceImpl;
import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;

// Creates once when app starts, acts as main setup/manager for whole app.
public class MoneyTalksApp extends Application
{
    // Shared services used across entire app - Defined by Interface
    private ExpenseService expenseService;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Setup dependencies
        ExpenseRepository expenseRepository = new FakeRepository();
        ExpenseValidator expenseValidator = new ExpenseValidator();

        // Wiring concrete classes here is fine, but assigned to the interface field
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseValidator);
    }

    /**
     * Provides access to the expense service.
     * Includes a null check to prevent crashes if accessed before initialization.
     */
    public ExpenseService getExpenseService()
    {
        // Simple null check/assert for safer failure
        if (expenseService == null) {
            throw new IllegalStateException("ExpenseService accessed before onCreate() or initialization failed.");
        }
        return expenseService;
    }
}