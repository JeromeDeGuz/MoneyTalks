package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Business.Validation.ExpenseValidationException;
import com.bugbytes.moneytalks.Business.Validation.Validator;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.Persistence.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Core implementation of ExpenseService interface.
public class ExpenseServiceImpl implements ExpenseService
{
    private final ExpenseRepository repository;
    private final Validator<Expense> validator;

    //Constructor for dependency injection (@param: repository, validator).
    public ExpenseServiceImpl(ExpenseRepository repository, Validator<Expense> validator)
    {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.validator = Objects.requireNonNull(validator, "Validator cannot be null");
    }

    //Adds a new expense (@param: expense to add).
    @Override
    public void addExpense(Expense expense)
    {
        //Delegate validation to the validator interface
        validator.validate(expense);

        //If validation passes, save to repository
        repository.addExpense(expense);
    }

    //Retrieves all stored expenses (@return: list of expenses).
    @Override
    public List<Expense> getAllExpenses()
    {
        List<Expense> expenses = repository.getAllExpenses();
        if (expenses == null)
        {
            // Safeguard: return an empty list instead of null to prevent downstream NPEs
            return new ArrayList<>();
        }
        return expenses;
    }

    //Deletes an expense (@param: expense to delete).
    //Throws ExpenseValidationException if deletion fails (e.g. not found).
    @Override
    public boolean deleteExpense(Expense expense)
    {
        if (expense == null)
        {
            throw new ExpenseValidationException("Cannot delete a null expense.");
        }

        boolean removed = repository.deleteExpense(expense);

        if (!removed)
        {
            throw new ExpenseValidationException("Expense not found: " + expense.getName());
        }
        return removed;
    }

    //Updates an existing expense (@param: expense to update).
    //Throws ExpenseValidationException if update fails or expense is null.
    @Override
    public boolean updateExpense(Expense expense)
    {
        if (expense == null)
        {
            throw new ExpenseValidationException("Cannot update a null expense.");
        }

        validator.validate(expense);

        boolean updated = repository.updateExpense(expense);

        if (!updated)
        {
            throw new ExpenseValidationException("Expense not found: " + expense.getName());
        }

        return updated;
    }
}
