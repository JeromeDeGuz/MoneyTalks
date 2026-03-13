package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ValidationException;
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
            return new ArrayList<>();
        }
        return result;
    }

    //Deletes an expense (@param: expense to delete).
    //Returns true if deleted, false otherwise.
    @Override
    public boolean deleteExpense(Expense expense)
    {
        if (expense == null)
        {
            return false;
        }

        return repository.deleteExpense(expense);
    }

    @Override
    public boolean updateExpense(Expense expense)
    {
        if (expense == null)
        {
            return false;
        }
        validator.validate(expense);
        return repository.updateExpense(expense);
    }

    // Implementing the missing sorting method for the main list
    @Override
    public List<Expense> getExpensesSortedByDate(boolean newestFirst)
    {
        List<Expense> newList = new ArrayList<>(getAllExpenses());
        sortListByDate(newList, newestFirst);
        return newList;
    }

    @Override
    public List<Expense> getExpensesByCategorySortedByDate(String categoryName, boolean newestFirst)
    {
        List<Expense> newList = new ArrayList<>(getAllExpenses());

        // 1) Filter
        if (categoryName != null && !categoryName.equalsIgnoreCase("All"))
        {
            newList.removeIf(e ->
                    e == null
                            || e.getCategory() == null
                            || !categoryName.equals(e.getCategory())
            );
        }

        // 2) Sort
        sortListByDate(newList, newestFirst);

        return newList;
    }

    // New method to fetch a single expense by ID for the Edit feature
    @Override
    public Expense getExpenseById(long id)
    {
        return repository.getExpenseById(id);
    }

    // Helper method to keep the code DRY (Don't Repeat Yourself)
    private void sortListByDate(List<Expense> list, boolean newestFirst)
    {
        list.sort((e1, e2) -> {
            if (newestFirst)
            {
                return e2.getDate().compareTo(e1.getDate());
            }
            else
            {
               return e1.getDate().compareTo(e2.getDate());
            }
        });
    }
}
