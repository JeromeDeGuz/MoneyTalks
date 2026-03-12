package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseValidatorTest
{
    private final ExpenseValidator validator = new ExpenseValidator();

    //Null expense should trigger validation exception
    @Test
    public void validateNullShouldThrow()
    {
        final Expense expense = null;
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Expense with null name should be rejected
    @Test
    public void validateNullNameShouldThrow()
    {
        // Added ID, BigDecimal, and LocalDate
        final Expense expense = new Expense(0, null, new BigDecimal("10.0"), "Food", LocalDate.of(2024, 5, 20), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Blank name (only spaces) should be invalid
    @Test
    public void validateBlankNameShouldThrow()
    {
        final Expense expense = new Expense(0, "   ", new BigDecimal("10.0"), "Food", LocalDate.of(2024, 5, 20), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Name containing only digits should not be allowed
    @Test
    public void validateOnlyNumbersNameShouldThrow()
    {
        final Expense expense = new Expense(0, "12345", new BigDecimal("10.0"), "Food", LocalDate.of(2024, 5, 20), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Name with letters and numbers should be valid
    @Test
    public void validateNameWithNumberShouldNotThrow()
    {
        final Expense expense = new Expense(0, "Lunch 2", new BigDecimal("10.0"), "Food", LocalDate.of(2024, 5, 20), "");
        assertDoesNotThrow(() -> validator.validate(expense));
    }

    //Zero amount should be rejected
    @Test
    public void validateAmountZeroShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", BigDecimal.ZERO, "Food", LocalDate.of(2024, 5, 20), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Negative amount should not be accepted
    @Test
    public void validateAmountNegativeShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("-1.0"), "Food", LocalDate.of(2024, 5, 20), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Null date should fail validation
    @Test
    public void validateNullDateShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", null, "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Blank date should be treated as invalid input
    @Test
    public void validateBlankDateShouldThrow()
    {
        // Note: Since Expense model now uses LocalDate, a "blank" date is handled at the parsing level (AddAndEditExpense)
        // This test now checks for null to represent missing date input
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", null, "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Invalid date format should fail validation
    @Test
    public void validateInvalidDateFormatShouldThrow()
    {
        // LocalDate prevents invalid formats by design
        // Testing null to ensure the validator still catches missing dates
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", null, "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    //Fully valid expense should pass validation
    @Test
    public void validateValidInputShouldNotThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("12.5"), "Food", LocalDate.of(2024, 5, 20), "");
        assertDoesNotThrow(() -> validator.validate(expense));
    }
}