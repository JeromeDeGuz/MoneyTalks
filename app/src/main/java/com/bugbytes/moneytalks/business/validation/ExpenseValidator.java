package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;

//Validates expense data before processing.
public class ExpenseValidator implements Validator<Expense>
{
    //Validates an expense object (@param: expense to validate).
    @Override
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

        if (expense.getName().trim().matches("^\\d+$"))
        {
            throw new ExpenseValidationException("Expense name cannot be only numbers.");
        }

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ExpenseValidationException("Expense amount must be greater than zero.");
        }

        if (expense.getDate() == null)
        {
            throw new ExpenseValidationException("Expense date is required.");
        }
    }
}