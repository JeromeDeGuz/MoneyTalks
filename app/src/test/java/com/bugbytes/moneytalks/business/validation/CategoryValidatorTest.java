package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Category;
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

@ExtendWith(MockitoExtension.class)
public class CategoryValidatorTest
{
    private CategoryValidator validator;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private ExpenseRepository expenseRepo;

    //setUp: Initializes the validator with the mocked repository before each test. Returns nothing.
    @BeforeEach
    public void setUp()
    {
        validator = new CategoryValidator(categoryRepo);
    }

    //validateNullCategoryThrowsException: Ensures that a null category object triggers a validation exception. Returns nothing.
    @Test
    public void validateNullCategoryThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    //validateEmptyNameThrowsException: Verifies that a category with an empty string name is rejected. Returns nothing.
    @Test
    public void validateEmptyNameThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(new Category("")));
    }

    //validateWhitespaceNameThrowsException: Confirms that names consisting only of spaces are treated as invalid. Returns nothing.
    @Test
    public void validateWhitespaceNameThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(new Category("   ")));
    }

    //validateDuplicateNameExactMatchThrowsException: Prevents adding a category that matches an existing name exactly. Returns nothing.
    @Test
    public void validateDuplicateNameExactMatchThrowsException()
    {
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertThrows(ValidationException.class, () -> validator.validate(new Category("Food")));
    }

    //validateDuplicateNameCaseInsensitiveThrowsException: Ensures that duplicate checks are case-insensitive. Returns nothing.
    @Test
    public void validateDuplicateNameCaseInsensitiveThrowsException()
    {
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertThrows(ValidationException.class, () -> validator.validate(new Category("food")));
    }

    //validateUniqueNameWithExistingCategoriesPasses: Confirms that a new, unique name passes validation successfully. Returns nothing.
    @Test
    public void validateUniqueNameWithExistingCategoriesPasses()
    {
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertDoesNotThrow(() -> validator.validate(new Category("Shopping")));
    }

    //validateEmptyRepoPasses: Verifies that any valid category name is accepted if the repository is empty. Returns nothing.
    @Test
    public void validateEmptyRepoPasses()
    {
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(new Category("NewCategory")));
    }

    //validateNumericOnlyNameThrowsException: Checks that category names cannot consist solely of numbers. Returns nothing.
    @Test
    public void validateNumericOnlyNameThrowsException()
    {
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> validator.validate(new Category("12345")));
    }

    //validateAlphanumericNamePasses: Confirms that names containing both letters and numbers are valid. Returns nothing.
    @Test
    public void validateAlphanumericNamePasses()
    {
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(new Category("Food123")));
    }

    //validateDeleteCategoryInUseThrowsException: Ensures categories linked to expenses cannot be deleted. Returns nothing.
    @Test
    public void validateDeleteCategoryInUseThrowsException()
    {
        Category inUse = new Category("Food");
        when(expenseRepo.categoryExists(inUse)).thenReturn(true);

        assertThrows(ValidationException.class, () -> validator.validateDelete(inUse, expenseRepo));
    }

    //validateDeleteCategoryNotInUsePasses: Confirms that unused categories can be safely deleted. Returns nothing.
    @Test
    public void validateDeleteCategoryNotInUsePasses()
    {
        Category unused = new Category("OldCategory");
        when(expenseRepo.categoryExists(unused)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateDelete(unused, expenseRepo));
    }
}