package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseValidatorTest
{
    private final ExpenseValidator validator = new ExpenseValidator();

    @Test
    public void validateNullExpenseShouldThrow()
    {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    public void validateNullNameShouldThrow()
    {
        final Expense expense = new Expense(0, null, new BigDecimal("10.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateEmptyNameShouldThrow()
    {
        final Expense expense = new Expense(0, "", new BigDecimal("10.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateWhitespaceNameShouldThrow()
    {
        final Expense expense = new Expense(0, "   ", new BigDecimal("10.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateNameTooShortShouldThrow()
    {
        final Expense expense = new Expense(0, "A", new BigDecimal("10.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateNameTooLongShouldThrow()
    {
        String longName = "A".repeat(51);
        final Expense expense = new Expense(0, longName, new BigDecimal("10.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateOnlyNumbersNameShouldThrow()
    {
        final Expense expense = new Expense(0, "12345", new BigDecimal("10.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateNullAmountShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", null, "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateZeroAmountShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", BigDecimal.ZERO, "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateNegativeAmountShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("-5.0"), "Food", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateNullCategoryShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), null, LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateEmptyCategoryShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "", LocalDate.now(), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateNullDateShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", null, "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateFutureDateShouldThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", LocalDate.now().plusDays(1), "");
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateLongNoteShouldThrow()
    {
        String longNote = "N".repeat(501);
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", LocalDate.now(), longNote);
        assertThrows(ValidationException.class, () -> validator.validate(expense));
    }

    @Test
    public void validateValidInputShouldNotThrow()
    {
        final Expense expense = new Expense(0, "Lunch", new BigDecimal("12.5"), "Food", LocalDate.now(), "Optional note");
        assertDoesNotThrow(() -> validator.validate(expense));
    }
}
