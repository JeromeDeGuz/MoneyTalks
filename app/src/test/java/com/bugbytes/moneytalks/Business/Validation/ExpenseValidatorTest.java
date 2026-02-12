package com.bugbytes.moneytalks.Business.Validation;

import com.bugbytes.moneytalks.Models.Expense;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseValidatorTest
{

    private final ExpenseValidator validator = new ExpenseValidator();

    @Test
    void validate_nullName_shouldThrow()
    {
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(null));
    }

    @Test
    void validate_nullName_shouldThrow() {
        Expense expense = new Expense(1, null, 10.0, "Food", "2024-05-20", "");
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(expense));
    }

    @Test
    void validate_blankName_shouldThrow() {
        Expense expense = new Expense(1, "   ", 10.0, "Food", "2024-05-20", "");
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(expense));
    }

    @Test
    void validate_amountZero_shouldThrow() {
        Expense expense = new Expense(1, "Lunch", 0.0, "Food", "2024-05-20", "");
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(expense));
    }

    @Test
    void validate_amountNegative_shouldThrow() {
        Expense expense = new Expense(1, "Lunch", -1.0, "Food", "2024-05-20", "");
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(expense));
    }

    @Test
    void validate_nullDate_shouldThrow() {
        Expense expense = new Expense(1, "Lunch", 10.0, "Food", null, "");
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(expense));
    }

    @Test
    void validate_blankDate_shouldThrow() {
        Expense expense = new Expense(1, "Lunch", 10.0, "Food", "  ", "");
        assertThrows(ExpenseValidationException.class,
                () -> validator.validate(expense));
    }

    @Test
    void validate_validInput_shouldNotThrow() {
        Expense expense = new Expense(1, "Lunch", 12.5, "Food", "2024-05-20", "");
        assertDoesNotThrow(() -> validator.validate(expense));
    }
}