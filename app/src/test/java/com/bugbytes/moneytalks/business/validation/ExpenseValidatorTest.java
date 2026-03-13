package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests all branches in ExpenseValidator.
 * No persistence dependencies — pure business logic only.
 */
public class ExpenseValidatorTest {

    private final ExpenseValidator validator = new ExpenseValidator();

    // ── null expense ─────────────────────────────────────────────────────────

    @Test
    public void validate_NullExpense_ThrowsException() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    // ── name checks ──────────────────────────────────────────────────────────

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
        String longName = "A".repeat(51);
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, longName, BigDecimal.TEN, "Food", LocalDate.now(), "")));
    }

    // ── amount checks ─────────────────────────────────────────────────────────

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

    // ── category checks ───────────────────────────────────────────────────────

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

    // ── date checks ───────────────────────────────────────────────────────────

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

    // ── note checks ───────────────────────────────────────────────────────────

    @Test
    public void validate_NoteTooLong_ThrowsException() {
        String longNote = "N".repeat(501);
        assertThrows(ValidationException.class, () ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), longNote)));
    }

    @Test
    public void validate_NullNote_DoesNotThrow() {
        // null note is explicitly allowed (the note check only fires if note != null)
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), null)));
    }

    // ── fully valid ───────────────────────────────────────────────────────────

    @Test
    public void validate_ValidExpense_DoesNotThrow() {
        assertDoesNotThrow(() ->
                validator.validate(new Expense(0, "Lunch", new BigDecimal("12.50"), "Food", LocalDate.now(), "Optional note")));
    }
}