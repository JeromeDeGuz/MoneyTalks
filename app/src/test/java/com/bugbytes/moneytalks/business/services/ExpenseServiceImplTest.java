package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ExpenseValidator;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.persistence.ExpenseRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeRepository;
import com.bugbytes.moneytalks.models.Expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseServiceImplTest {

    private ExpenseServiceImpl service;
    private ExpenseRepository repo;
    private ExpenseValidator validator;

    @BeforeEach
    public void setUp() {
        repo = new FakeRepository();
        validator = new ExpenseValidator();
        service = new ExpenseServiceImpl(repo, validator);

        // Clear repo for test isolation
        for (Expense e : repo.getAllExpenses()) {
            repo.deleteExpense(e);
        }
    }

    // ---------------- Constructor ----------------

    @Test
    public void constructor_nullArgs_throwsException() {
        assertThrows(NullPointerException.class, () -> new ExpenseServiceImpl(null, validator));
        assertThrows(NullPointerException.class, () -> new ExpenseServiceImpl(repo, null));
    }

    // ---------------- getAllExpenses ----------------

    @Test
    public void getAllExpenses_repoReturnsNull_returnsEmptyList() {
        ExpenseRepository nullRepo = new FakeRepository() {
            @Override
            public List<Expense> getAllExpenses() { return null; }
        };
        ExpenseServiceImpl serviceWithNullRepo = new ExpenseServiceImpl(nullRepo, validator);
        
        List<Expense> result = serviceWithNullRepo.getAllExpenses();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllExpenses_success() {
        service.addExpense(new Expense(0, "Item", BigDecimal.TEN, "Food", LocalDate.now(), ""));
        assertEquals(1, service.getAllExpenses().size());
    }

    // ---------------- Add Expense & Validator Branches ----------------

    @Test
    public void addExpense_valid_passes() {
        Expense e = new Expense(0, "Lunch", new BigDecimal("10.0"), "Food", LocalDate.now(), "Test note");
        service.addExpense(e);
        assertEquals(1, service.getAllExpenses().size());
    }

    @Test
    public void addExpense_validationBranches() {
        // Null name
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, null, BigDecimal.TEN, "Food", LocalDate.now(), "")));
        
        // Blank name
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "  ", BigDecimal.TEN, "Food", LocalDate.now(), "")));

        // Name too short
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "A", BigDecimal.TEN, "Food", LocalDate.now(), "")));

        // Name too long
        String longName = "A".repeat(51);
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, longName, BigDecimal.TEN, "Food", LocalDate.now(), "")));

        // Numeric name
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "12345", BigDecimal.TEN, "Food", LocalDate.now(), "")));

        // Amount <= 0
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "Valid", BigDecimal.ZERO, "Food", LocalDate.now(), "")));

        // Category null
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "Valid", BigDecimal.TEN, null, LocalDate.now(), "")));

        // Date null
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "Valid", BigDecimal.TEN, "Food", null, "")));

        // Future Date
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "Valid", BigDecimal.TEN, "Food", LocalDate.now().plusDays(1), "")));

        // Long Note
        String longNote = "N".repeat(501);
        assertThrows(ValidationException.class, () -> 
            service.addExpense(new Expense(0, "Valid", BigDecimal.TEN, "Food", LocalDate.now(), longNote)));
    }

    // ---------------- Update Expense ----------------

    @Test
    public void updateExpense_branches() {
        assertFalse(service.updateExpense(null));

        Expense e = new Expense(0, "Dinner", BigDecimal.TEN, "Food", LocalDate.now(), "");
        service.addExpense(e);
        Expense saved = service.getAllExpenses().get(0);
        saved.setName("Updated");
        assertTrue(service.updateExpense(saved));

        Expense missing = new Expense(9999, "Missing", BigDecimal.TEN, "Food", LocalDate.now(), "");
        assertFalse(service.updateExpense(missing));
    }

    // ---------------- Delete Expense ----------------

    @Test
    public void deleteExpense_branches() {
        assertFalse(service.deleteExpense(null));

        Expense e = new Expense(0, "Coffee", BigDecimal.ONE, "Food", LocalDate.now(), "");
        service.addExpense(e);
        Expense saved = service.getAllExpenses().get(0);
        assertTrue(service.deleteExpense(saved));

        assertFalse(service.deleteExpense(saved));
    }

    // ---------------- Sorting & Filtering ----------------

    @Test
    public void sortingAndFiltering_branches() {
        service.addExpense(new Expense(0, "A_Old", BigDecimal.TEN, "Food", LocalDate.now().minusDays(2), ""));
        service.addExpense(new Expense(0, "B_New", BigDecimal.TEN, "Food", LocalDate.now(), ""));
        service.addExpense(new Expense(0, "C_Other", BigDecimal.TEN, "Other", LocalDate.now().minusDays(1), ""));

        assertEquals("B_New", service.getExpensesSortedByDate(true).get(0).getName());
        assertEquals("A_Old", service.getExpensesSortedByDate(false).get(0).getName());

        assertEquals(3, service.getExpensesByCategorySortedByDate("All", true).size());
        assertEquals(3, service.getExpensesByCategorySortedByDate(null, true).size());

        List<Expense> food = service.getExpensesByCategorySortedByDate("Food", true);
        assertEquals(2, food.size());
    }

    @Test
    public void filtering_removeIf_innerBranches() {
        ExpenseRepository dirtyRepo = new FakeRepository() {
            @Override
            public List<Expense> getAllExpenses() {
                List<Expense> list = new ArrayList<>();
                list.add(null); 
                list.add(new Expense(1, "NoCat", BigDecimal.TEN, null, LocalDate.now(), "")); 
                list.add(new Expense(2, "WrongCat", BigDecimal.TEN, "Other", LocalDate.now(), "")); 
                list.add(new Expense(3, "RightCat", BigDecimal.TEN, "Food", LocalDate.now(), ""));
                return list;
            }
        };
        ExpenseServiceImpl dirtyService = new ExpenseServiceImpl(dirtyRepo, validator);

        List<Expense> result = dirtyService.getExpensesByCategorySortedByDate("Food", true);
        assertEquals(1, result.size());
        assertEquals("RightCat", result.get(0).getName());
    }

    // ---------------- getExpenseById ----------------

    @Test
    public void getExpenseById_branches() {
        Expense e = new Expense(0, "Target", BigDecimal.TEN, "Shopping", LocalDate.now(), "");
        service.addExpense(e);
        long id = service.getAllExpenses().get(0).getId();

        assertNotNull(service.getExpenseById(id));
        assertNull(service.getExpenseById(-99));
    }
}
