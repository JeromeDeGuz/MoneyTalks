package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.CategoryValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.persistence.CategoryRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest
{
    private CategoryServiceImpl service;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private ExpenseRepository expenseRepo;

    @Mock
    private CategoryValidator validator;

    //setUp: It initializes the service and mocks before each test. Takes in nothing.
    @BeforeEach
    public void setUp()
    {
        service = new CategoryServiceImpl(categoryRepo, validator, expenseRepo);
    }

    //testConstructorNullRepositoryThrowsException: It verifies that passing a null repository triggers an exception. Takes in nothing.
    @Test
    public void testConstructorNullRepositoryThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(null, validator, expenseRepo));
    }

    //testConstructorNullValidatorThrowsException: It ensures the service fails if the validator is missing. Takes in nothing.
    @Test
    public void testConstructorNullValidatorThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, null, expenseRepo));
    }

    //testConstructorNullExpenseRepositoryThrowsException: It checks for null safety with the expense repository. Takes in nothing.
    @Test
    public void testConstructorNullExpenseRepositoryThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, validator, null));
    }

    //testAddCategoryValidSuccess: It confirms that a valid category is validated and added. Takes in nothing.
    @Test
    public void testAddCategoryValidSuccess() throws ValidationException {

        Category category = new Category("Bills");
        service.addCategory(category);
        verify(validator).validate(category);
        verify(categoryRepo).addCategory(category);
    }

    //testAddCategoryInvalidThrowsException: It ensures that validation errors prevent a category from being saved. Takes in nothing.
    @Test
    public void testAddCategoryInvalidThrowsException() throws ValidationException {
        Category category = new Category("");
        doThrow(new ValidationException("Category name cannot be empty")).when(validator).validate(category);
        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    //testAddCategoryDuplicateThrowsException: It verifies that the service handles duplicate category validation. Takes in nothing.
    @Test
    public void testAddCategoryDuplicateThrowsException() throws ValidationException {
        Category category = new Category("Food");
        doThrow(new ValidationException("Category name already exists")).when(validator).validate(category);
        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    //testGetAllCategoriesSuccess: It checks if all categories are correctly retrieved from the repository. Takes in nothing.
    @Test
    public void testGetAllCategoriesSuccess()
    {
        List<Category> mockList = Arrays.asList(new Category("Food"), new Category("Bills"));
        when(categoryRepo.getAllCategories()).thenReturn(mockList);
        List<Category> result = service.getAllCategories();
        assertEquals(2, result.size());
        verify(categoryRepo).getAllCategories();
    }

    //testUpdateCategorySuccess: It verifies that updating a category also triggers an update in the expense repository. Takes in nothing.
    @Test
    public void testUpdateCategorySuccess() throws ValidationException
    {
        Category oldCategory = new Category(1, "Health", java.math.BigDecimal.ZERO);
        Category newCategory = new Category(1, "Wellness", java.math.BigDecimal.ZERO);

        service.updateCategory(oldCategory, newCategory);

        verify(expenseRepo).updateExpenseCategory(oldCategory, newCategory);
        verify(categoryRepo).updateCategory(oldCategory, newCategory);
    }

    //testDeleteCategorySuccess: It ensures a category is deleted when it is not in use by any expenses. Takes in nothing.
    @Test
    public void testDeleteCategorySuccess() throws ValidationException {
        Category category = new Category(1, "Shopping", java.math.BigDecimal.ZERO);
        when(expenseRepo.categoryExists(category)).thenReturn(false);

        service.deleteCategory(category);

        verify(categoryRepo).deleteCategory(category);
    }

    //testDeleteCategoryUsedInExpenseThrowsException: It prevents deletion when the category is linked to existing expenses. Takes in nothing.
    @Test
    public void testDeleteCategoryUsedInExpenseThrowsException()
    {
        Category category = new Category(1, "Gas", java.math.BigDecimal.ZERO);
        when(expenseRepo.categoryExists(category)).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.deleteCategory(category));
        verify(categoryRepo, never()).deleteCategory(any());
    }

    //testGetCategoryFound: It checks if a category can be retrieved by its name successfully. Takes in nothing.
    @Test
    public void testGetCategoryFound()
    {
        Category mockCat = new Category(1, "Travel", java.math.BigDecimal.ZERO);
        when(categoryRepo.getCategoryByName("Travel")).thenReturn(mockCat);
        Category result = service.getCategory("Travel");
        assertNotNull(result);
        assertEquals("Travel", result.getName());
    }

    //testGetCategoryNotFoundReturnsNull: It ensures that searching for a non-existent category returns null. Takes in nothing.
    @Test
    public void testGetCategoryNotFoundReturnsNull()
    {
        when(categoryRepo.getCategoryByName("Unknown")).thenReturn(null);
        assertNull(service.getCategory("Unknown"));
    }
}