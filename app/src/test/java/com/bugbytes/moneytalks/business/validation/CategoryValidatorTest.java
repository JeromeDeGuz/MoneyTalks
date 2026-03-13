package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests all branches in CategoryValidator.
 * Uses Mockito to mock repositories — no persistence dependencies.
 */
@ExtendWith(MockitoExtension.class)
public class CategoryValidatorTest
{

    private CategoryValidator validator;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private ExpenseRepository expenseRepo;

    @BeforeEach
    public void setUp()
    {
        validator = new CategoryValidator(categoryRepo);
    }

    // ── isNullOrEmpty ─────────────────────────────────────────────────────────

    @Test
    public void validate_NullCategory_ThrowsException()
    {
        // Branch: category == null (first condition of OR)
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    public void validate_EmptyName_ThrowsException()
    {
        // Branch: name is empty (second condition of OR)
        assertThrows(ValidationException.class, () -> validator.validate(new Category("")));
    }

    @Test
    public void validate_WhitespaceName_ThrowsException()
    {
        // Branch: name is whitespace only — trims to empty
        assertThrows(ValidationException.class, () -> validator.validate(new Category("   ")));
    }

    // ── isCaseDuplicate ───────────────────────────────────────────────────────

    @Test
    public void validate_DuplicateName_ExactMatch_ThrowsException()
    {
        // Branch: equalsIgnoreCase returns true
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertThrows(ValidationException.class, () -> validator.validate(new Category("Food")));
    }

    @Test
    public void validate_DuplicateName_CaseInsensitive_ThrowsException()
    {
        // Branch: equalsIgnoreCase returns true on different case
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertThrows(ValidationException.class, () -> validator.validate(new Category("food")));
    }

    @Test
    public void validate_UniqueName_WithExistingCategories_Passes()
    {
        // Branch: loop runs but equalsIgnoreCase never matches
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertDoesNotThrow(() -> validator.validate(new Category("Shopping")));
    }

    @Test
    public void validate_EmptyRepo_Passes()
    {
        // Branch: loop body never entered
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(new Category("NewCategory")));
    }

    // ── isNotNumbers ──────────────────────────────────────────────────────────

    @Test
    public void validate_NumericOnlyName_ThrowsException()
    {
        // Branch: regex matches "^\d+$"
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> validator.validate(new Category("12345")));
    }

    @Test
    public void validate_AlphanumericName_Passes()
    {
        // Branch: regex does not match
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(new Category("Food123")));
    }

    // ── validateDelete ────────────────────────────────────────────────────────

    @Test
    public void validateDelete_CategoryInUse_ThrowsException()
    {
        // Branch: categoryExists returns true
        Category inUse = new Category("Food");
        when(expenseRepo.categoryExists(inUse)).thenReturn(true);

        assertThrows(ValidationException.class, () -> validator.validateDelete(inUse, expenseRepo));
    }

    @Test
    public void validateDelete_CategoryNotInUse_Passes()
    {
        // Branch: categoryExists returns false
        Category unused = new Category("OldCategory");
        when(expenseRepo.categoryExists(unused)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateDelete(unused, expenseRepo));
    }
}