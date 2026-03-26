package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpenseValidator implements Validator<Expense>
{
    //one big method that contains all the single validation checks
    @Override
    public void validate(Expense expense) throws ValidationException
    {
        if (expense == null)
        {
            throw new ValidationException("Expense object cannot be null.");
        }

        checkNameisNotNullorEmpty(expense);
        checkNameisNotNumbers(expense);
        checkNameisValidLength(expense);
        checkAmountisValid(expense);
        checkCategoryisNotNullorEmpty(expense);
        checkDateisNotNull(expense);
        checkDateisNotFuture(expense);
        checkNoteisValidLength(expense);
    }

    //validates null or empty name
    private void checkNameisNotNullorEmpty(Expense expense) throws ValidationException
    {
        if (expense.getName() == null || expense.getName().trim().isEmpty())
        {
            throw new ValidationException("Expense name is required and cannot be empty.");
        }
    }

    //validates name isnt all digits
    private void checkNameisNotNumbers(Expense expense) throws ValidationException
    {
        if (expense.getName().trim().matches("^\\d+$"))
        {
            throw new ValidationException("Expense name cannot be only numbers.");
        }
    }

    //validates name within bounds
    private void checkNameisValidLength(Expense expense) throws ValidationException
    {
        String name = expense.getName().trim();
        if (name.length() < 2 || name.length() > 50)
        {
            throw new ValidationException("Expense name must be between 2 and 50 characters.");
        }
    }

    //validates the amount is positive and not null
    private void checkAmountisValid(Expense expense) throws ValidationException
    {
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ValidationException("Expense amount must be greater than zero.");
        }
    }

    //validates a category is not null or empty
    private void checkCategoryisNotNullorEmpty(Expense expense) throws ValidationException
    {
        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty())
        {
            throw new ValidationException("Expense category is required.");
        }
    }

    //validates date not null
    private void checkDateisNotNull(Expense expense) throws ValidationException
    {
        if (expense.getDate() == null)
        {
            throw new ValidationException("Expense date is required.");
        }
    }

    //validates that the date is not in the future
    private void checkDateisNotFuture(Expense expense) throws ValidationException
    {
        if (expense.getDate() != null && expense.getDate().isAfter(LocalDate.now()))
        {
            throw new ValidationException("Expense date cannot be in the future.");
        }
    }

    //validates note length not out of bounds
    private void checkNoteisValidLength(Expense expense) throws ValidationException
    {
        if (expense.getNote() != null && expense.getNote().length() > 500)
        {
            throw new ValidationException("Notes cannot exceed 500 characters.");
        }
    }

    public Expense validateAndParse(String name, String amountStr, String category, String dateStr, String notes) throws ValidationException {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new ValidationException("Date is required.");
        }
        if (amountStr == null || amountStr.isEmpty()) {
            throw new ValidationException("Amount is required.");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new ValidationException("Amount is not a valid number.");
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            throw new ValidationException("Date format must be dd-MM-yyyy.");
        }

        Expense expense = new Expense(0, name, amount, category, date, notes);

        validate(expense);

        return expense;
    }
}

