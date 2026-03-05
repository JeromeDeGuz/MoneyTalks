package com.bugbytes.moneytalks.Business.Validation;

import com.bugbytes.moneytalks.Models.Expense;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

        if (expense.getAmount() <= 0)
        {
            throw new ExpenseValidationException("Expense amount must be greater than zero.");
        }

        if (expense.getDate() == null || expense.getDate().trim().isEmpty())
        {
            throw new ExpenseValidationException("Expense date is required.");
        }

        validateDateFormat(expense.getDate());
    }

    private void validateDateFormat(String date)
    {
        // Validates standard ISO format YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setLenient(false);
        try
        {
            sdf.parse(date);
        }
        catch (ParseException e)
        {
            throw new ExpenseValidationException("Invalid date format. Expected YYYY-MM-DD.");
        }
    }
}
