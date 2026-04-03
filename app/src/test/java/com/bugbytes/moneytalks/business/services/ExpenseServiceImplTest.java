package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
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
public class ExpenseServiceImplTest
{
    private ExpenseServiceImpl service;

    @Mock
    private ExpenseRepository repo;

    @Mock
    private ExpenseValidator validator;

    //setUp: Initializes the service and mocks before each test execution. Returns nothing.
    @BeforeEach
    public void setUp()
    {
        service = new ExpenseServiceImpl(repo, validator);
    }

    //testConstructorNullRepositoryThrowsException: Verifies that a null repository triggers a NullPointerException. Returns nothing.
    @Test
    public void testConstructorNullRepositoryThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new ExpenseServiceImpl(null, validator));
    }

    //testConstructorNullValidatorThrowsException: Ensures the service fails if the validator is missing. Returns nothing.
    @Test
    public void testConstructorNullValidatorThrowsException()
    {
        assertThrows(NullPointerException.class, () -> new ExpenseServiceImpl(repo, null));
    }

    //testGetAllExpensesRepoReturnsNullReturnsEmptyList: Checks if the service returns an empty list when the repo returns null. Returns nothing.
    @Test
    public void testGetAllExpensesRepoReturnsNullReturnsEmptyList()
    {
        when(repo.getAllExpenses()).thenReturn(null);

        List<Expense> result = service.getAllExpenses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repo).getAllExpenses();
    }

    //testGetAllExpensesSuccess: Verifies that all expenses are correctly retrieved from the repository. Returns nothing.
    @Test
    public void testGetAllExpensesSuccess()
    {
        List<Expense> mockList = Arrays.asList(
                new Expense(1, "Item", BigDecimal.TEN, "Food", LocalDate.now(), "")
        );
        when(repo.getAllExpenses()).thenReturn(mockList);

        List<Expense> result = service.getAllExpenses();

        assertEquals(1, result.size());
        assertEquals("Item", result.get(0).getName());
    }

    //testAddExpenseValidSuccess: Confirms that a valid expense is validated and added to the repository. Returns nothing.
    @Test
    public void testAddExpenseValidSuccess() throws ValidationException
    {
        Expense e = new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), "");

        service.addExpense(e);

        verify(validator).validate(e);
        verify(repo).addExpense(e);
    }

    //testAddExpenseInvalidThrowsException: Ensures that validation errors prevent the expense from being saved. Returns nothing.
    @Test
    public void testAddExpenseInvalidThrowsException() throws ValidationException
    {
        Expense e = new Expense(0, "A", BigDecimal.TEN, "Food", LocalDate.now(), "");
        doThrow(new ValidationException("Too short")).when(validator).validate(e);

        assertThrows(ValidationException.class, () -> service.addExpense(e));
        verify(repo, never()).addExpense(any());
    }

    //testUpdateExpenseSuccess: Verifies that updating an expense validates it and returns true on success. Returns nothing.
    @Test
    public void testUpdateExpenseSuccess() throws ValidationException
    {
        Expense e = new Expense(1, "Dinner", BigDecimal.TEN, "Food", LocalDate.now(), "");
        when(repo.updateExpense(e)).thenReturn(true);

        assertTrue(service.updateExpense(e));

        verify(validator).validate(e);
        verify(repo).updateExpense(e);
    }

    //testUpdateExpenseNullReturnsFalse: Ensures updating a null expense returns false without interactions. Returns nothing.
    @Test
    public void testUpdateExpenseNullReturnsFalse() throws ValidationException
    {
        assertFalse(service.updateExpense(null));
        verifyNoInteractions(repo, validator);
    }

    //testUpdateExpenseNotFoundReturnsFalse: Checks if updating a non-existent expense returns false. Returns nothing.
    @Test
    public void testUpdateExpenseNotFoundReturnsFalse() throws ValidationException
    {
        Expense e = new Expense(99, "Fake", BigDecimal.TEN, "Food", LocalDate.now(), "");
        when(repo.updateExpense(e)).thenReturn(false);

        assertFalse(service.updateExpense(e));
    }

    //testDeleteExpenseSuccess: Verifies that a valid expense is deleted successfully from the repository. Returns nothing.
    @Test
    public void testDeleteExpenseSuccess()
    {
        Expense e = new Expense(1, "Coffee", BigDecimal.ONE, "Food", LocalDate.now(), "");
        when(repo.deleteExpense(e)).thenReturn(true);

        assertTrue(service.deleteExpense(e));
        verify(repo).deleteExpense(e);
    }

    //testDeleteExpenseNullReturnsFalse: Ensures that deleting a null expense returns false without repository interaction. Returns nothing.
    @Test
    public void testDeleteExpenseNullReturnsFalse()
    {
        assertFalse(service.deleteExpense(null));
        verifyNoInteractions(repo);
    }

    //testSortingNewestFirst: Confirms the logic for sorting expenses from newest to oldest. Returns nothing.
    @Test
    public void testSortingNewestFirst()
    {
        Expense old = new Expense(1, "Old", BigDecimal.TEN, "Food", LocalDate.now().minusDays(5), "");
        Expense mid = new Expense(2, "Mid", BigDecimal.TEN, "Food", LocalDate.now().minusDays(2), "");
        Expense now = new Expense(3, "Now", BigDecimal.TEN, "Food", LocalDate.now(), "");

        when(repo.getAllExpenses()).thenReturn(Arrays.asList(old, mid, now));

        List<Expense> result = service.getExpensesSortedByDate(true);

        assertEquals("Now", result.get(0).getName());
        assertEquals("Mid", result.get(1).getName());
        assertEquals("Old", result.get(2).getName());
    }

    //testFilteringByCategory: Verifies that the list is correctly filtered based on a specific category name. Returns nothing.
    @Test
    public void testFilteringByCategory()
    {
        Expense food = new Expense(1, "Pizza", BigDecimal.TEN, "Food", LocalDate.now(), "");
        Expense taxi = new Expense(2, "Uber", BigDecimal.TEN, "Transport", LocalDate.now(), "");

        when(repo.getAllExpenses()).thenReturn(Arrays.asList(food, taxi));

        List<Expense> result = service.getExpensesByCategorySortedByDate("Food", true);

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
    }

    //testFilteringSpecialCategories: Checks if "All" or null category inputs return the full list. Returns nothing.
    @Test
    public void testFilteringSpecialCategories()
    {
        List<Expense> mockList = Arrays.asList(new Expense(1, "X", BigDecimal.ONE, "Cat", LocalDate.now(), ""));
        when(repo.getAllExpenses()).thenReturn(mockList);

        assertEquals(1, service.getExpensesByCategorySortedByDate("All", true).size());
        assertEquals(1, service.getExpensesByCategorySortedByDate(null, true).size());
    }

    //testFilteringLambdaDefensiveBranches: Verifies that the filter logic safely handles null expenses or null categories. Returns nothing.
    @Test
    public void testFilteringLambdaDefensiveBranches()
    {
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

    //testGetExpenseById: Confirms that a specific expense can be retrieved by its ID. Returns nothing.
    @Test
    public void testGetExpenseById()
    {
        Expense e = new Expense(1, "Target", BigDecimal.TEN, "Shopping", LocalDate.now(), "");
        when(repo.getExpenseById(1)).thenReturn(e);

        assertNotNull(service.getExpenseById(1));
        assertNull(service.getExpenseById(99));
    }

    //testAddExpenseStringBasedValidSuccess: Verifies that string inputs are correctly parsed and saved as an expense. Returns nothing.
    @Test
    public void testAddExpenseStringBasedValidSuccess() throws ValidationException
    {
        Expense parsed = new Expense(0, "Lunch", BigDecimal.TEN, "Food", LocalDate.now(), "");
        when(validator.validateAndParse("Lunch", "10.00", "Food", "10-03-2026", "")).thenReturn(parsed);

        service.addExpense("Lunch", "10.00", "Food", "10-03-2026", "");

        verify(repo).addExpense(parsed);
    }

    //testUpdateExpenseStringBasedValidSuccess: Verifies that string-based updates are correctly parsed and saved. Returns nothing.
    @Test
    public void testUpdateExpenseStringBasedValidSuccess() throws ValidationException
    {
        Expense parsed = new Expense(0, "Dinner", BigDecimal.TEN, "Food", LocalDate.now(), "");
        when(validator.validateAndParse("Dinner", "10.00", "Food", "10-03-2026", "")).thenReturn(parsed);
        when(repo.updateExpense(any(Expense.class))).thenReturn(true);

        assertTrue(service.updateExpense(1L, "Dinner", "10.00", "Food", "10-03-2026", ""));
    }
}