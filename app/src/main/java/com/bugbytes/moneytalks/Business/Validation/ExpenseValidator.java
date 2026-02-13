package com.bugbytes.moneytalks.Business.Validation;

import com.bugbytes.moneytalks.Models.Expense;

//Validates expense data before processing.
public class ExpenseValidator
{
    //Validates an expense object (@param: expense to validate).
    public void validate(Expense expense)
    {
        if (expense == null)
        {
            throw new ExpenseValidationException("Expense object cannot be null.");
        }

        if (expense.getName() == null || expense.getName().trim().isEmpty())
        {
            throw new ExpenseValidationException("Expense name is required and cannot be empty.");
        }

        if (expense.getAmount() <= 0)
        {
            throw new ExpenseValidationException("Expense amount must be greater than zero.");
        }

        if (expense.getDate() == null || expense.getDate().trim().isEmpty())
        {
            throw new ExpenseValidationException("Expense date is required.");
        }
    }
}