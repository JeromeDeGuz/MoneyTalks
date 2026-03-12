package com.bugbytes.moneytalks.business.validation;

import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeCategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryValidatorTest {

    private CategoryValidator validator;
    private CategoryRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new FakeCategoryRepository();
        validator = new CategoryValidator(repository);
        
        // Clear repo
        for (Category c : repository.getAllCategories()) {
            repository.deleteCategory(c);
        }
    }

    @Test
    public void validate_nullCategory_throwsException() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    public void validate_emptyName_throwsException() {
        assertThrows(ValidationException.class, () -> validator.validate(new Category("")));
        assertThrows(ValidationException.class, () -> validator.validate(new Category("   ")));
    }

    @Test
    public void validate_duplicateName_throwsException() {
        repository.addCategory(new Category("Food"));
        // Test case-insensitive duplicate
        assertThrows(ValidationException.class, () -> validator.validate(new Category("food")));
    }

    @Test
    public void validate_numericName_throwsException() {
        assertThrows(ValidationException.class, () -> validator.validate(new Category("12345")));
    }

    @Test
    public void validate_validName_passes() {
        assertDoesNotThrow(() -> validator.validate(new Category("Entertainment")));
    }
}
