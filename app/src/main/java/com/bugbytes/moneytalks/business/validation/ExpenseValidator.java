package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpenseValidator implements Validator<Expense>
{
    //validate: One big method that runs all individual business rule checks for an expense. Takes in @param expense. @throws ValidationException if any rule is violated.
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

    //checkNameisNotNullorEmpty: Validates that the expense name is provided and not just whitespace. Takes in @param expense. @throws ValidationException if name is empty.
    private void checkNameisNotNullorEmpty(Expense expense) throws ValidationException
    {
        if (expense.getName() == null || expense.getName().trim().isEmpty())
        {
            throw new ValidationException("Expense name is required and cannot be empty.");
        }
    }

    //checkNameisNotNumbers: Validates that the expense name is not composed entirely of digits. Takes in @param expense. @throws ValidationException if name is only numbers.
    private void checkNameisNotNumbers(Expense expense) throws ValidationException
    {
        if (expense.getName().trim().matches("^\\d+$"))
        {
            throw new ValidationException("Expense name cannot be only numbers.");
        }
    }

    //checkNameisValidLength: Validates that the name length is within the allowed bounds (2-50 characters). Takes in @param expense. @throws ValidationException if length is invalid.
    private void checkNameisValidLength(Expense expense) throws ValidationException
    {
        String name = expense.getName().trim();
        if (name.length() < 2 || name.length() > 50)
        {
            throw new ValidationException("Expense name must be between 2 and 50 characters.");
        }
    }

    //checkAmountisValid: Validates that the amount is non-null and greater than zero. Takes in @param expense. @throws ValidationException if amount is invalid.
    private void checkAmountisValid(Expense expense) throws ValidationException
    {
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ValidationException("Expense amount must be greater than zero.");
        }
    }

    //checkCategoryisNotNullorEmpty: Validates that a category has been selected for the expense. Takes in @param expense. @throws ValidationException if category is missing.
    private void checkCategoryisNotNullorEmpty(Expense expense) throws ValidationException
    {
        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty())
        {
            throw new ValidationException("Expense category is required.");
        }
    }

    //checkDateisNotNull: Validates that a date is associated with the expense. Takes in @param expense. @throws ValidationException if date is null.
    private void checkDateisNotNull(Expense expense) throws ValidationException
    {
        if (expense.getDate() == null)
        {
            throw new ValidationException("Expense date is required.");
        }
    }

    //checkDateisNotFuture: Validates that the expense date is not set in the future. Takes in @param expense. @throws ValidationException if date is after current day.
    private void checkDateisNotFuture(Expense expense) throws ValidationException
    {
        if (expense.getDate() != null && expense.getDate().isAfter(LocalDate.now()))
        {
            throw new ValidationException("Expense date cannot be in the future.");
        }
    }

    //checkNoteisValidLength: Validates that the notes do not exceed the 500 character limit. Takes in @param expense. @throws ValidationException if note is too long.
    private void checkNoteisValidLength(Expense expense) throws ValidationException
    {
        if (expense.getNote() != null && expense.getNote().length() > 500)
        {
            throw new ValidationException("Notes cannot exceed 500 characters.");
        }
    }

    //validateAndParse: Helper to convert UI strings into a validated Expense object. Takes in @param name, amountStr, category, dateStr, and notes. Returns @return Expense. @throws ValidationException if parsing or logic fails.
    public Expense validateAndParse(String name, String amountStr, String category, String dateStr, String notes) throws ValidationException
    {
        if (dateStr == null || dateStr.isEmpty())
        {
            throw new ValidationException("Date is required.");
        }
        if (amountStr == null || amountStr.isEmpty())
        {
            throw new ValidationException("Amount is required.");
        }

        BigDecimal amount;
        try
        {
            amount = new BigDecimal(amountStr);
        }
        catch (NumberFormatException e)
        {
            throw new ValidationException("Amount is not a valid number.");
        }

        LocalDate date;
        try
        {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        catch (DateTimeParseException e)
        {
            throw new ValidationException("Date format must be dd-MM-yyyy.");
        }

        Expense expense = new Expense(0, name, amount, category, date, notes);

        validate(expense);

        return expense;
    }
}