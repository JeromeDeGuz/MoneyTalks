package com.bugbytes.moneytalks.Business.Services;

//Model layer
import com.bugbytes.moneytalks.Models.Expense;
//Presistent layer
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
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
        // Business Rules / Validations
        if (expense == null) {
            throw new IllegalArgumentException("Expense object cannot be null.");
        }

        if (expense.getName() == null || expense.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name is required and cannot be empty.");
        }

        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be a positive value greater than zero.");
        }

        if (expense.getDate() == null || expense.getDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense date is required.");
        }

        repository.addExpense(expense);
    }

    //returns all stored expenses
    @Override
    public List<Expense> getAllExpenses()
    {
        return repository.getAllExpenses();
    }
}