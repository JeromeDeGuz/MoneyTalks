package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseValidatorTest {

    private final ExpenseValidator validator = new ExpenseValidator();

    // ---------------- validate() ----------------

    @Test
    public void validate_NullExpense_ThrowsException() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    public void validate_NullName_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, null, BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_EmptyName_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_WhitespaceName_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "   ", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_NumericOnlyName_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "12345", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_NameTooShort_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "A", BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_NameTooLong_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "A".repeat(51), BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_NullAmount_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", null, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_ZeroAmount_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.ZERO, "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_NegativeAmount_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("-1"), "Food", LocalDate.now(), "")));
    }

    @Test
    public void validate_NullCategory_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, null, LocalDate.now(), "")));
    }

    @Test
    public void validate_EmptyCategory_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "", LocalDate.now(), "")));
    }

    @Test
    public void validate_NullDate_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", null, "")));
    }

    @Test
    public void validate_FutureDate_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now().plusDays(1), "")));
    }

    @Test
    public void validate_NoteTooLong_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), "N".repeat(501))));
    }

    @Test
    public void validate_NullNote_DoesNotThrow() {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), null)));
    }

    @Test
    public void validate_ValidExpense_DoesNotThrow() {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("12.50"), "Food", LocalDate.now(), "note")));
    }

    // ---------------- validateAndParse() ----------------

    @Test
    public void validateAndParse_NullDate_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "10.00", "Food", null, ""));
    }

    @Test
    public void validateAndParse_EmptyDate_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "10.00", "Food", "", ""));
    }

    @Test
    public void validateAndParse_NullAmount_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", null, "Food", "10-03-2026", ""));
    }

    @Test
    public void validateAndParse_EmptyAmount_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "", "Food", "10-03-2026", ""));
    }

    @Test
    public void validateAndParse_InvalidAmountFormat_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "abc", "Food", "10-03-2026", ""));
    }

    @Test
    public void validateAndParse_InvalidDateFormat_ThrowsException() {
        assertThrows(ValidationException.class, () ->
                validator.validateAndParse("Lunch", "10.00", "Food", "2026-03-10", ""));
    }

    @Test
    public void validateAndParse_ValidInputs_ReturnsExpense() throws ValidationException {
        Expense result = validator.validateAndParse("Lunch", "10.00", "Food", "10-03-2026", "note");
        assertNotNull(result);
        assertEquals("Lunch", result.getName());
        assertEquals(new BigDecimal("10.00"), result.getAmount());
        assertEquals("Food", result.getCategory());
        assertEquals("note", result.getNote());
    }
}