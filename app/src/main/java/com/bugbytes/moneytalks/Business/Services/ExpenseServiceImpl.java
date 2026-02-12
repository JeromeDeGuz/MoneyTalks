package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Business.Validation.ExpenseValidator;
import com.bugbytes.moneytalks.Models.Expense;
//Presistent layer
import com.bugbytes.moneytalks.Persistence.ExpenseRepository;
import java.util.List;

//core implementation of ExpenseService interface.
public class ExpenseServiceImpl implements ExpenseService
{
    private final ExpenseRepository repository;
    private final ExpenseValidator validator;

    // constructor used to inject dependencies
    public ExpenseServiceImpl(ExpenseRepository repository, ExpenseValidator validator)
    {
        this.repository = repository;
        this.validator = validator;
    }

    //adds new expense after validation
    @Override
    public void addExpense(Expense expense)
    {
        // Delegate validation to the validator class
        validator.validate(expense);

        // If validation passes, save to repository
        repository.addExpense(expense);
    }

    //returns all stored expenses
    @Override
    public List<Expense> getAllExpenses()
    {
        return repository.getAllExpenses();
    }

    @Override
    public boolean deleteExpense(Expense expense) {
        return repository.deleteExpense(expense);
    }
}