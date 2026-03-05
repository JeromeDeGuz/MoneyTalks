package com.bugbytes.moneytalks.Business.Validation;

import com.bugbytes.moneytalks.Models.Expense;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseValidatorTest
{
    private final ExpenseValidator validator = new ExpenseValidator();

    //Null expense should trigger validation exception
    @Test
    public void validateNullShouldThrow()
    {
        final Expense expense = null;
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Expense with null name should be rejected
    @Test
    public void validateNullNameShouldThrow()
    {
        final Expense expense = new Expense(null, 10.0, "Food", "20/5/2024", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Blank name (only spaces) should be invalid
    @Test
    public void validateBlankNameShouldThrow()
    {
        final Expense expense = new Expense("   ", 10.0, "Food", "20/5/2024", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Name containing only digits should not be allowed
    @Test
    public void validateOnlyNumbersNameShouldThrow()
    {
        final Expense expense = new Expense("12345", 10.0, "Food", "20/5/2024", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Name with letters and numbers should be valid
    @Test
    public void validateNameWithNumberShouldNotThrow()
    {
        final Expense expense = new Expense("Lunch 2", 10.0, "Food", "20/5/2024", "");
        assertDoesNotThrow(() -> validator.validate(expense));
    }

    //Zero amount should be rejected
    @Test
    public void validateAmountZeroShouldThrow()
    {
        final Expense expense = new Expense("Lunch", 0.0, "Food", "20/5/2024", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Negative amount should not be accepted
    @Test
    public void validateAmountNegativeShouldThrow()
    {
        final Expense expense = new Expense("Lunch", -1.0, "Food", "20/5/2024", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Null date should fail validation
    @Test
    public void validateNullDateShouldThrow()
    {
        final Expense expense = new Expense("Lunch", 10.0, "Food", null, "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Blank date should be treated as invalid input
    @Test
    public void validateBlankDateShouldThrow()
    {
        final Expense expense = new Expense("Lunch", 10.0, "Food", "  ", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Invalid date format should fail validation
    @Test
    public void validateInvalidDateFormatShouldThrow()
    {
        final Expense expense = new Expense("Lunch", 10.0, "Food", "2024-05-20", "");
        assertThrows(ExpenseValidationException.class, () -> validator.validate(expense));
    }

    //Fully valid expense should pass validation
    @Test
    public void validateValidInputShouldNotThrow()
    {
        final Expense expense = new Expense("Lunch", 12.5, "Food", "20/5/2024", "");
        assertDoesNotThrow(() -> validator.validate(expense));
    }
}