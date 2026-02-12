package com.bugbytes.moneytalks.Business.Services;

//Model layer
import com.bugbytes.moneytalks.Models.Expense;
//Presistent layer
import com.bugbytes.moneytalks.Persistence.ExpenseRepository;
import java.util.List;

//core implementation of ExpenseService interface.
public class ExpenseServiceImpl implements ExpenseService
{
    private final ExpenseRepository repository; //repo to store/retrive data

    //constructor used to pass repository into service
    public ExpenseServiceImpl(ExpenseRepository repository)
    {
        this.repository = repository;
    }

    //adds new expense + all rules related to it
    @Override
    public void addExpense(Expense expense)
    {
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