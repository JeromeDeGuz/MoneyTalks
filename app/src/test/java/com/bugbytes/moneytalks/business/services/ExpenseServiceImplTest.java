package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.persistence.fake.FakeExpenseRepository;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

    private ExpenseServiceImpl service;

    @Mock
    private ExpenseRepository repo;

    @Mock
    private ExpenseValidator validator;

    @BeforeEach
    public void setUp() {
        service = new ExpenseServiceImpl(repo, validator);
    }

    // ---------------- Constructor ----------------

    @Test
    public void testConstructor_NullRepository_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new ExpenseServiceImpl(null, validator));
    }

    @Test
    public void testConstructor_NullValidator_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new ExpenseServiceImpl(repo, null));
    }

    // ---------------- getAllExpenses ----------------

    @Test
    public void testGetAllExpenses_RepoReturnsNull_ReturnsEmptyList() {
        when(repo.getAllExpenses()).thenReturn(null);
        
        List<Expense> result = service.getAllExpenses();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repo).getAllExpenses();
    }

    @Test
    public void testGetAllExpenses_Success() {
        List<Expense> mockList = Arrays.asList(
            new Expense(1, "Item", BigDecimal.TEN, "Food", LocalDate.now(), "")
        );
        when(repo.getAllExpenses()).thenReturn(mockList);

        List<Expense> result = service.getAllExpenses();
        
        assertEquals(1, result.size());
        assertEquals("Item", result.get(0).getName());
    }

    // ---------------- Add Expense ----------------

    @Test
    public void testAddExpense_Valid_Success() {
        Expense e = new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), "");
        
        service.addExpense(e);

        verify(validator).validate(e);
        verify(repo).addExpense(e);
    }

    @Test
    public void testAddExpense_Invalid_ThrowsException() {
        Expense e = new Expense(0, "A", BigDecimal.TEN, "Food", LocalDate.now(), "");
        doThrow(new ValidationException("Too short")).when(validator).validate(e);

        assertThrows(ValidationException.class, () -> service.addExpense(e));
        verify(repo, never()).addExpense(any());
    }

    // ---------------- Update Expense ----------------

    @Test
    public void testUpdateExpense_Success() {
        Expense e = new Expense(1, "Dinner", BigDecimal.TEN, "Food", LocalDate.now(), "");
        when(repo.updateExpense(e)).thenReturn(true);

        assertTrue(service.updateExpense(e));
        
        verify(validator).validate(e);
        verify(repo).updateExpense(e);
    }

    @Test
    public void testUpdateExpense_Null_ReturnsFalse() {
        assertFalse(service.updateExpense(null));
        verifyNoInteractions(repo, validator);
    }

    @Test
    public void testUpdateExpense_NotFound_ReturnsFalse() {
        Expense e = new Expense(99, "Fake", BigDecimal.TEN, "Food", LocalDate.now(), "");
        when(repo.updateExpense(e)).thenReturn(false);

        assertFalse(service.updateExpense(e));
    }

    // ---------------- Delete Expense ----------------

    @Test
    public void testDeleteExpense_Success() {
        Expense e = new Expense(1, "Coffee", BigDecimal.ONE, "Food", LocalDate.now(), "");
        when(repo.deleteExpense(e)).thenReturn(true);

        assertTrue(service.deleteExpense(e));
        verify(repo).deleteExpense(e);
    }

    @Test
    public void testDeleteExpense_Null_ReturnsFalse() {
        assertFalse(service.deleteExpense(null));
        verifyNoInteractions(repo);
    }

    // ---------------- Sorting & Filtering ----------------

    @Test
    public void testSorting_NewestFirst() {
        Expense old = new Expense(1, "Old", BigDecimal.TEN, "Food", LocalDate.now().minusDays(5), "");
        Expense mid = new Expense(2, "Mid", BigDecimal.TEN, "Food", LocalDate.now().minusDays(2), "");
        Expense now = new Expense(3, "Now", BigDecimal.TEN, "Food", LocalDate.now(), "");
        
        when(repo.getAllExpenses()).thenReturn(Arrays.asList(old, mid, now));

        List<Expense> result = service.getExpensesSortedByDate(true);
        
        assertEquals("Now", result.get(0).getName());
        assertEquals("Mid", result.get(1).getName());
        assertEquals("Old", result.get(2).getName());
    }

    @Test
    public void testFiltering_ByCategory() {
        Expense food = new Expense(1, "Pizza", BigDecimal.TEN, "Food", LocalDate.now(), "");
        Expense taxi = new Expense(2, "Uber", BigDecimal.TEN, "Transport", LocalDate.now(), "");
        
        when(repo.getAllExpenses()).thenReturn(Arrays.asList(food, taxi));

        List<Expense> result = service.getExpensesByCategorySortedByDate("Food", true);
        
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
    }

    @Test
    public void testFiltering_SpecialCategories() {
        List<Expense> mockList = Arrays.asList(new Expense(1, "X", BigDecimal.ONE, "Cat", LocalDate.now(), ""));
        when(repo.getAllExpenses()).thenReturn(mockList);

        // Branch: "All"
        assertEquals(1, service.getExpensesByCategorySortedByDate("All", true).size());
        // Branch: null
        assertEquals(1, service.getExpensesByCategorySortedByDate(null, true).size());
    }

    @Test
    public void testFiltering_LambdaDefensiveBranches() {
        // newList.removeIf(e -> e == null || e.getCategory() == null || !categoryName.equals(e.getCategory()));
        Expense eNull = null;
        Expense noCat = new Expense(1, "NoCat", BigDecimal.ONE, null, LocalDate.now(), "");
        Expense wrongCat = new Expense(2, "Wrong", BigDecimal.ONE, "Other", LocalDate.now(), "");
        Expense rightCat = new Expense(3, "Right", BigDecimal.ONE, "Target", LocalDate.now(), "");

        List<Expense> dirtyList = new ArrayList<>(Arrays.asList(eNull, noCat, wrongCat, rightCat));
        when(repo.getAllExpenses()).thenReturn(dirtyList);

        List<Expense> result = service.getExpensesByCategorySortedByDate("Target", true);
        
        assertEquals(1, result.size());
        assertEquals("Right", result.get(0).getName());
    }

    // ---------------- getExpenseById ----------------

    @Test
    public void testGetExpenseById() {
        Expense e = new Expense(1, "Target", BigDecimal.TEN, "Shopping", LocalDate.now(), "");
        when(repo.getExpenseById(1)).thenReturn(e);

        assertNotNull(service.getExpenseById(1));
        assertNull(service.getExpenseById(99));
    }
}
