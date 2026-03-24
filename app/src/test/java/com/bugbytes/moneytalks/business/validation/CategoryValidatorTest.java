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

    //setUp: It initializes the validator with the mocked repository before each test. Takes in nothing.
    @BeforeEach
    public void setUp()
    {
        validator = new CategoryValidator(categoryRepo);
    }

    //validateNullCategoryThrowsException: It ensures that a null category object triggers a validation exception. Takes in nothing.
    @Test
    public void validateNullCategoryThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    //validateEmptyNameThrowsException: It verifies that a category with an empty string name is rejected. Takes in nothing.
    @Test
    public void validateEmptyNameThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(new Category("")));
    }

    //validateWhitespaceNameThrowsException: It confirms that names consisting only of spaces are treated as invalid. Takes in nothing.
    @Test
    public void validateWhitespaceNameThrowsException()
    {
        assertThrows(ValidationException.class, () -> validator.validate(new Category("   ")));
    }

    //validateDuplicateNameExactMatchThrowsException: It prevents adding a category that matches an existing name exactly. Takes in nothing.
    @Test
    public void validateDuplicateNameExactMatchThrowsException()
    {
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertThrows(ValidationException.class, () -> validator.validate(new Category("Food")));
    }

    //validateDuplicateNameCaseInsensitiveThrowsException: It ensures that duplicate checks are case-insensitive. Takes in nothing.
    @Test
    public void validateDuplicateNameCaseInsensitiveThrowsException()
    {
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertThrows(ValidationException.class, () -> validator.validate(new Category("food")));
    }

    //validateUniqueNameWithExistingCategoriesPasses: It confirms that a new, unique name passes validation successfully. Takes in nothing.
    @Test
    public void validateUniqueNameWithExistingCategoriesPasses()
    {
        when(categoryRepo.getAllCategories())
                .thenReturn(Arrays.asList(new Category(1, "Food", BigDecimal.ZERO)));

        assertDoesNotThrow(() -> validator.validate(new Category("Shopping")));
    }

    //validateEmptyRepoPasses: It verifies that any valid category name is accepted if the repository is empty. Takes in nothing.
    @Test
    public void validateEmptyRepoPasses()
    {
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(new Category("NewCategory")));
    }

    //validateNumericOnlyNameThrowsException: It checks that category names cannot consist solely of numbers. Takes in nothing.
    @Test
    public void validateNumericOnlyNameThrowsException()
    {
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> validator.validate(new Category("12345")));
    }

    //validateAlphanumericNamePasses: It confirms that names containing both letters and numbers are valid. Takes in nothing.
    @Test
    public void validateAlphanumericNamePasses()
    {
        when(categoryRepo.getAllCategories()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(new Category("Food123")));
    }

    //validateDeleteCategoryInUseThrowsException: It ensures categories linked to expenses cannot be deleted. Takes in nothing.
    @Test
    public void validateDeleteCategoryInUseThrowsException()
    {
        Category inUse = new Category("Food");
        when(expenseRepo.categoryExists(inUse)).thenReturn(true);

        assertThrows(ValidationException.class, () -> validator.validateDelete(inUse, expenseRepo));
    }

    //validateDeleteCategoryNotInUsePasses: It confirms that unused categories can be safely deleted. Takes in nothing.
    @Test
    public void validateDeleteCategoryNotInUsePasses()
    {
        Category unused = new Category("OldCategory");
        when(expenseRepo.categoryExists(unused)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateDelete(unused, expenseRepo));
    }
}