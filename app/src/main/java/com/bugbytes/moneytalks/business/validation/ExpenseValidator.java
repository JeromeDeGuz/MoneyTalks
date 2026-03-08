package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;
import java.math.BigDecimal;

/**
 * Validates expense data before processing.
 * Implementation follows the Validator interface for the Business Layer.
 */
public class ExpenseValidator implements Validator<Expense>
{
    /**
     * Validates an expense object (@param: expense to validate).
     * Ensures all required fields are present and logically sound.
     */
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

        // Logic check: Expense names should not be purely numeric
        if (expense.getName().trim().matches("^\\d+$"))
        {
            throw new ExpenseValidationException("Expense name cannot be only numbers.");
        }

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ExpenseValidationException("Expense amount must be greater than zero.");
        }

        // Integrated Category check from Development branch
        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty())
        {
            throw new ExpenseValidationException("Expense category is required.");
        }

        // Strict Date check to ensure data integrity
        if (expense.getDate() == null)
        {
            throw new ExpenseValidationException("Expense date is required.");
        }
    }
}