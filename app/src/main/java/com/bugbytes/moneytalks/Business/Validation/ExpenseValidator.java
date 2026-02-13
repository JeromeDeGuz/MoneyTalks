package com.bugbytes.moneytalks.Business.Validation;

import com.bugbytes.moneytalks.Models.Expense;


public class ExpenseValidator
{
    public void validate(Expense expense)
    {
        if (expense == null) {
            throw new ExpenseValidationException("Expense object cannot be null.");
        }

        if (expense.getName() == null || expense.getName().trim().isEmpty()) {
            throw new ExpenseValidationException("Expense name is required and cannot be empty.");
        }

        if (expense.getName().trim().matches("^\\d+$")) {
            throw new ExpenseValidationException("Expense name cannot be only numbers.");
        }

        if (expense.getAmount() <= 0) {
            throw new ExpenseValidationException("Expense amount must be a positive value greater than zero.");
        }

        if (expense.getDate() == null || expense.getDate().trim().isEmpty()) {
            throw new ExpenseValidationException("Expense date is required.");
        }
    }
}