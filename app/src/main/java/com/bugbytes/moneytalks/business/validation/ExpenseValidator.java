package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseValidator implements Validator<Expense>
{
    //validate: It ensures all required fields are present and logically sound. Takes in @param expense.
    @Override
    public void validate(Expense expense)
    {
        if (expense == null)
        {
            throw new ValidationException("Expense object cannot be null.");
        }

        if (expense.getName() == null || expense.getName().trim().isEmpty())
        {
            throw new ValidationException("Expense name is required and cannot be empty.");
        }

        //Logic check: Expense names should not be purely numeric
        if (expense.getName().trim().matches("^\\d+$"))
        {
            throw new ValidationException("Expense name cannot be only numbers.");
        }

        //Name Length Check
        if (expense.getName().trim().length() < 2 || expense.getName().trim().length() > 50)
        {
            throw new ValidationException("Expense name must be between 2 and 50 characters.");
        }

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ValidationException("Expense amount must be greater than zero.");
        }

        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty())
        {
            throw new ValidationException("Expense category is required.");
        }

        //Strict Date check to ensure data integrity
        if (expense.getDate() == null)
        {
            throw new ValidationException("Expense date is required.");
        }

        //Future Date Check
        if (expense.getDate().isAfter(LocalDate.now()))
        {
            throw new ValidationException("Expense date cannot be in the future.");
        }

        //Note Length Check
        if (expense.getNote() != null && expense.getNote().length() > 500)
        {
            throw new ValidationException("Notes cannot exceed 500 characters.");
        }
    }
}