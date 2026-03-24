package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//Tests all branches in ExpenseValidator. No persistence dependencies, pure business logic only.
public class ExpenseValidatorTest
{
    private final ExpenseValidator validator = new ExpenseValidator();

    //validateNullExpenseThrowsException: It ensures that passing a null expense object triggers a validation exception. Takes in nothing.
    @Test
    public void validateNullExpenseThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    //validateNullNameThrowsException: It verifies that an expense with a null name is rejected. Takes in nothing.
    @Test
    public void validateNullNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, null, BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateEmptyNameThrowsException: It confirms that empty string names are invalid for expenses. Takes in nothing.
    @Test
    public void validateEmptyNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateWhitespaceNameThrowsException: It ensures that names consisting only of spaces are treated as invalid. Takes in nothing.
    @Test
    public void validateWhitespaceNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "   ", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNumericOnlyNameThrowsException: It prevents expense names from being strictly numeric. Takes in nothing.
    @Test
    public void validateNumericOnlyNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "12345", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNameTooShortThrowsException: It verifies that names below the minimum length requirement are rejected. Takes in nothing.
    @Test
    public void validateNameTooShortThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "A", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNameTooLongThrowsException: It ensures that expense names cannot exceed the maximum character limit. Takes in nothing.
    @Test
    public void validateNameTooLongThrowsException()
    {
        String longName = "A".repeat(51);
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, longName, BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNullAmountThrowsException: It checks that an expense must have an associated amount. Takes in nothing.
    @Test
    public void validateNullAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", null, "Food", LocalDate.now(), "")));
    }

    //validateZeroAmountThrowsException: It confirms that zero-value expenses are not allowed. Takes in nothing.
    @Test
    public void validateZeroAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.ZERO, "Food", LocalDate.now(), "")));
    }

    //validateNegativeAmountThrowsException: It prevents negative amounts from being saved as expenses. Takes in nothing.
    @Test
    public void validateNegativeAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("-1"), "Food", LocalDate.now(), "")));
    }

    //validateNullCategoryThrowsException: It verifies that every expense must belong to a category. Takes in nothing.
    @Test
    public void validateNullCategoryThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, null, LocalDate.now(), "")));
    }

    //validateEmptyCategoryThrowsException: It ensures that empty category strings are rejected. Takes in nothing.
    @Test
    public void validateEmptyCategoryThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "", LocalDate.now(), "")));
    }

    //validateNullDateThrowsException: It checks that an expense must have a valid date. Takes in nothing.
    @Test
    public void validateNullDateThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", null, "")));
    }

    //validateFutureDateThrowsException: It prevents expenses from being recorded with a future date. Takes in nothing.
    @Test
    public void validateFutureDateThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now().plusDays(1), "")));
    }

    //validateNoteTooLongThrowsException: It ensures that optional notes do not exceed the character limit. Takes in nothing.
    @Test
    public void validateNoteTooLongThrowsException()
    {
        String longNote = "N".repeat(501);
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), longNote)));
    }

    //validateNullNoteDoesNotThrow: It confirms that a null note is acceptable for an expense. Takes in nothing.
    @Test
    public void validateNullNoteDoesNotThrow()
    {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), null)));
    }

    //validateValidExpenseDoesNotThrow: It verifies that a fully compliant expense object passes all validation checks. Takes in nothing.
    @Test
    public void validateValidExpenseDoesNotThrow()
    {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("12.50"), "Food", LocalDate.now(), "Optional note")));
    }
}