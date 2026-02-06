package com.bugbytes.moneytalks.Business.Validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseValidatorTest {

    private final ExpenseValidator validator = new ExpenseValidator();

    @Test
    void validate_nullName_shouldThrow() {
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(null, 10.0));
    }

    @Test
    void validate_blankName_shouldThrow() {
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate("   ", 10.0));
    }

    @Test
    void validate_amountZero_shouldThrow() {
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate("Lunch", 0.0));
    }

    @Test
    void validate_amountNegative_shouldThrow() {
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate("Lunch", -1.0));
    }

    @Test
    void validate_validInput_shouldNotThrow() {
        assertDoesNotThrow(() -> validator.validate("Lunch", 12.5));
    }
}
