package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseValidatorTest
{
    private final ExpenseValidator validator = new ExpenseValidator();

    //validateNullExpenseThrowsException: Verifies that a null expense object triggers a validation exception. Returns nothing.
    @Test
    public void validateNullExpenseThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    //validateNullNameThrowsException: Ensures that an expense with a null name is rejected. Returns nothing.
    @Test
    public void validateNullNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, null, BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateEmptyNameThrowsException: Verifies that an empty string name triggers an exception. Returns nothing.
    @Test
    public void validateEmptyNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateWhitespaceNameThrowsException: Confirms that names containing only spaces are treated as invalid. Returns nothing.
    @Test
    public void validateWhitespaceNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "   ", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNumericOnlyNameThrowsException: Checks that names cannot consist solely of numeric characters. Returns nothing.
    @Test
    public void validateNumericOnlyNameThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "12345", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNameTooShortThrowsException: Verifies that names shorter than the minimum requirement are rejected. Returns nothing.
    @Test
    public void validateNameTooShortThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "A", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNameTooLongThrowsException: Ensures that names exceeding the maximum length are rejected. Returns nothing.
    @Test
    public void validateNameTooLongThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "A".repeat(51), BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    //validateNullAmountThrowsException: Confirms that a null amount triggers a validation exception. Returns nothing.
    @Test
    public void validateNullAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", null, "Food", LocalDate.now(), "")));
    }

    //validateZeroAmountThrowsException: Verifies that an expense with zero amount is invalid. Returns nothing.
    @Test
    public void validateZeroAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.ZERO, "Food", LocalDate.now(), "")));
    }

    //validateNegativeAmountThrowsException: Ensures that negative expense amounts are not allowed. Returns nothing.
    @Test
    public void validateNegativeAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("-1"), "Food", LocalDate.now(), "")));
    }

    //validateNullCategoryThrowsException: Confirms that a missing category triggers an exception. Returns nothing.
    @Test
    public void validateNullCategoryThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, null, LocalDate.now(), "")));
    }

    //validateEmptyCategoryThrowsException: Verifies that an empty category string is treated as invalid. Returns nothing.
    @Test
    public void validateEmptyCategoryThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "", LocalDate.now(), "")));
    }

    //validateNullDateThrowsException: Confirms that a missing date triggers a validation exception. Returns nothing.
    @Test
    public void validateNullDateThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", null, "")));
    }

    //validateFutureDateThrowsException: Ensures that expenses cannot be recorded for future dates. Returns nothing.
    @Test
    public void validateFutureDateThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now().plusDays(1), "")));
    }

    //validateNoteTooLongThrowsException: Verifies that notes exceeding the character limit are rejected. Returns nothing.
    @Test
    public void validateNoteTooLongThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), "N".repeat(501))));
    }

    //validateNullNoteDoesNotThrow: Confirms that a null note is acceptable for an expense. Returns nothing.
    @Test
    public void validateNullNoteDoesNotThrow()
    {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), null)));
    }

    //validateValidExpenseDoesNotThrow: Verifies that a fully valid expense object passes validation. Returns nothing.
    @Test
    public void validateValidExpenseDoesNotThrow()
    {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("12.50"), "Food", LocalDate.now(), "note")));
    }

    //validateAndParseNullDateThrowsException: Confirms that parsing fails if the date string is null. Returns nothing.
    @Test
    public void validateAndParseNullDateThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "10.00", "Food", null, ""));
    }

    //validateAndParseEmptyDateThrowsException: Verifies that parsing fails if the date string is empty. Returns nothing.
    @Test
    public void validateAndParseEmptyDateThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "10.00", "Food", "", ""));
    }

    //validateAndParseNullAmountThrowsException: Confirms that parsing fails if the amount string is null. Returns nothing.
    @Test
    public void validateAndParseNullAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", null, "Food", "10-03-2026", ""));
    }

    //validateAndParseEmptyAmountThrowsException: Verifies that parsing fails if the amount string is empty. Returns nothing.
    @Test
    public void validateAndParseEmptyAmountThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "", "Food", "10-03-2026", ""));
    }

    //validateAndParseInvalidAmountFormatThrowsException: Ensures parsing fails for non-numeric amount formats. Returns nothing.
    @Test
    public void validateAndParseInvalidAmountFormatThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "abc", "Food", "10-03-2026", ""));
    }

    //validateAndParseInvalidDateFormatThrowsException: Verifies that parsing fails for incorrect date string formats. Returns nothing.
    @Test
    public void validateAndParseInvalidDateFormatThrowsException()
    {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "10.00", "Food", "2026-03-10", ""));
    }

    //validateAndParseValidInputsReturnsExpense: Confirms that valid string inputs are correctly parsed into an Expense object. Returns nothing.
    @Test
    public void validateAndParseValidInputsReturnsExpense() throws ValidationException
    {
        Expense result = validator.validateAndParse("Lunch", "10.00", "Food", "10-03-2026", "note");
        assertNotNull(result);
        assertEquals("Lunch", result.getName());
        assertEquals(new BigDecimal("10.00"), result.getAmount());
        assertEquals("Food", result.getCategory());
        assertEquals("note", result.getNote());
    }
}