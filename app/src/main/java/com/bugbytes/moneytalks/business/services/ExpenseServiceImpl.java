package com.bugbytes.moneytalks.business.services;

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

    //Constructor for dependency injection. Takes in @param repository and validator.
    public ExpenseServiceImpl(ExpenseRepository repository, Validator<Expense> validator)
    {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.validator = Objects.requireNonNull(validator, "Validator cannot be null");
    }

    //addExpense: It validates and adds a new expense to the repository. Takes in @param expense.
    @Override
    public void addExpense(Expense expense)
    {
        Objects.requireNonNull(expense, "Expense cannot be null");
        //Delegate validation to the validator interface
        validator.validate(expense);

        //If validation passes, save to repository
        repository.addExpense(expense);
    }

    //getAllExpenses: It retrieves all stored expenses. Takes in nothing and @return List of expenses.
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

    //deleteExpense: It removes an expense from the system. Takes in @param expense and @return boolean result.
    @Override
    public boolean deleteExpense(Expense expense)
    {
        if (expense == null)
        {
            return false;
        }

        return repository.deleteExpense(expense);
    }

    //updateExpense: It validates and updates an existing expense. Takes in @param expense and @return boolean result.
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

    //getExpensesSortedByDate: It fetches expenses sorted by their date. Takes in @param newestFirst and @return Sorted list of expenses.
    @Override
    public List<Expense> getExpensesSortedByDate(boolean newestFirst)
    {
        List<Expense> newList = new ArrayList<>(getAllExpenses());
        sortListByDate(newList, newestFirst);
        return newList;
    }

    //getExpensesByCategorySortedByDate: It filters by category then sorts by date. Takes in @param categoryName and newestFirst and @return Filtered sorted list.
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

        //2) Sort
        sortListByDate(newList, newestFirst);

        return newList;
    }

    //getExpenseById: It retrieves a single expense record using its ID. Takes in @param id and @return Expense object.
    @Override
    public Expense getExpenseById(long id)
    {
        return repository.getExpenseById(id);
    }

    //sortListByDate: Helper method to sort a list in place. Takes in @param list and newestFirst.
    private void sortListByDate(List<Expense> list, boolean newestFirst)
    {
        list.sort((e1, e2) ->
        {
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