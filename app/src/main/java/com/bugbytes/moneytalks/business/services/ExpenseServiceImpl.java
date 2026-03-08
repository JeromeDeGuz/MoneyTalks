package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ExpenseValidationException;
import com.bugbytes.moneytalks.business.validation.Validator;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

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
        List<Expense> result = repository.getAllExpenses();
        if (result == null)
        {
            throw new IllegalStateException("Repository returned null instead of a list.");
        }
        return result;
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

    @Override
    public boolean updateExpense(Expense expense)
    {
        if (expense == null)
        {
            throw new ExpenseValidationException("Cannot update a null expense.");
        }
        validator.validate(expense);
        return repository.updateExpense(expense);
    }

    //Retrieves expenses sorted by date (@param: newestFirst toggles sort order, @return: sorted list).
    @Override
    public List<Expense> getExpensesSortedByDate(boolean newestFirst)
    {
        // Create a copy of the list to avoid modifying the persistence layer's original data
        List<Expense> sortedList = new ArrayList<>(getAllExpenses());



        sortedList.sort((e1, e2) -> {
            if (newestFirst)
            {
                // Descending order: Newest dates first
                return e2.getDate().compareTo(e1.getDate());
            }
            else
            {
                // Ascending order: Oldest dates first
                return e1.getDate().compareTo(e2.getDate());
            }
        });

        return sortedList;
    }
}