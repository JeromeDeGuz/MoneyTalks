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
public class CategoryServiceImplTest {

    private CategoryServiceImpl service;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private ExpenseRepository expenseRepo;

    @Mock
    private CategoryValidator validator; // This matches the type in the implementation

    @BeforeEach
    public void setUp() {
        service = new CategoryServiceImpl(categoryRepo, validator, expenseRepo);
    }

    @Test
    public void testConstructor_NullRepository_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(null, validator, expenseRepo));
    }

    @Test
    public void testConstructor_NullValidator_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, null, expenseRepo));
    }

    @Test
    public void testConstructor_NullExpenseRepository_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(categoryRepo, validator, null));
    }

    @Test
    public void testAddCategory_Valid_Success() {
        Category category = new Category("Bills");

        // No stubbing needed for void methods that should succeed
        service.addCategory(category);

        verify(validator).validate(category);
        verify(categoryRepo).addCategory(category);
    }

    @Test
    public void testAddCategory_Invalid_ThrowsException() {
        Category category = new Category("");
        // Stub the void method to throw an exception
        doThrow(new ValidationException("Category name cannot be empty")).when(validator).validate(category);

        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    @Test
    public void testAddCategory_Duplicate_ThrowsException() {
        Category category = new Category("Food");
        doThrow(new ValidationException("Category name already exists")).when(validator).validate(category);

        assertThrows(ValidationException.class, () -> service.addCategory(category));
        verify(categoryRepo, never()).addCategory(any());
    }

    @Test
    public void testGetAllCategories_Success() {
        List<Category> mockList = Arrays.asList(new Category("Food"), new Category("Bills"));
        when(categoryRepo.getAllCategories()).thenReturn(mockList);

        List<Category> result = service.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepo).getAllCategories();
    }

    @Test
    public void testUpdateCategory_Success() {
        Category category = new Category(1, "Health", java.math.BigDecimal.ZERO);

        service.updateCategory(category);

        verify(categoryRepo).updateCategory(category);
    }

    @Test
    public void testDeleteCategory_Success() {
        Category category = new Category(1, "Shopping", java.math.BigDecimal.ZERO);

        service.deleteCategory(category);

        verify(validator).validateDelete(category, expenseRepo);
        verify(categoryRepo).deleteCategory(category);
    }

    @Test
    public void testDeleteCategory_UsedInExpense_ThrowsException() {
        Category category = new Category(1, "Gas", java.math.BigDecimal.ZERO);
        doThrow(new ValidationException("Cannot delete category that exists within an expense"))
                .when(validator).validateDelete(category, expenseRepo);

        assertThrows(ValidationException.class, () -> service.deleteCategory(category));
        verify(categoryRepo, never()).deleteCategory(any());
    }

    @Test
    public void testGetCategory_Found() {
        Category mockCat = new Category(1, "Travel", java.math.BigDecimal.ZERO);
        when(categoryRepo.getCategoryByName("Travel")).thenReturn(mockCat);

        Category result = service.getCategory("Travel");

        assertNotNull(result);
        assertEquals("Travel", result.getName());
    }

    @Test
    public void testGetCategory_NotFound_ReturnsNull() {
        when(categoryRepo.getCategoryByName("Unknown")).thenReturn(null);
        assertNull(service.getCategory("Unknown"));
    }
}