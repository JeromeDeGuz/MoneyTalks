package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Business.Validation.ExpenseValidator;
import com.bugbytes.moneytalks.Models.Expense;

import com.bugbytes.moneytalks.Persistence.ExpenseRepository;
import java.util.List;

//Core implementation of ExpenseService interface.
public class ExpenseServiceImpl implements ExpenseService
{
    private final ExpenseRepository repository;
    private final ExpenseValidator validator;

    //Constructor for dependency injection (@param: repository, validator).
    public ExpenseServiceImpl(ExpenseRepository repository, ExpenseValidator validator)
    {
        this.repository = repository;
        this.validator = validator;
    }

    //Adds a new expense (@param: expense to add).
    @Override
    public void addExpense(Expense expense)
    {
        //Delegate validation to the validator class
        validator.validate(expense);

        //If validation passes, save to repository
        repository.addExpense(expense);
    }

    //Retrieves all stored expenses (@return: list of expenses).
    @Override
    public List<Expense> getAllExpenses()
    {
        return repository.getAllExpenses();
    }

    //Deletes an expense (@param: expense to delete, @return: true if successful).
    @Override
    public boolean deleteExpense(Expense expense)
    {
        return repository.deleteExpense(expense);
    }
}