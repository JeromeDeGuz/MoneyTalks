package com.bugbytes.moneytalks.Persistence.Fake;

import com.bugbytes.moneytalks.Models.Expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FakeRepositoryTest {

    private FakeRepository repo;

    @BeforeEach
    void setup() {
        repo = new FakeRepository();

        // static list: clear between tests
        repo.getAllExpenses().clear();
    }

    @Test
    void getAllExpenses_initiallyEmpty() {
        assertTrue(repo.getAllExpenses().isEmpty());
    }

    @Test
    void addExpense_shouldAddToList() {
        Expense e = new Expense("Dinner", 20.0, "Food", "2026-02-06", "Pizza");

        repo.addExpense(e);

        List<Expense> all = repo.getAllExpenses();
        assertEquals(1, all.size());
        assertSame(e, all.get(0));
    }

    @Test
    void getAllExpenses_returnsSameBackingList_staticPersists() {
        Expense e1 = new Expense("Coffee", 3.5, "Food", "2026-02-06", "");
        repo.addExpense(e1);

        // New instance should still see the same static list
        FakeRepository repo2 = new FakeRepository();
        List<Expense> all2 = repo2.getAllExpenses();
        System.out.println("PRINTING: " + all2);

        assertEquals(1, all2.size());
        assertSame(e1, all2.get(0));
    }

    @Test
    void deleteExpense_existingExpense_shouldRemoveFromList() {
        Expense e1 = new Expense("Book", 15.0, "Shopping", "2024-01-01", "Sci-fi book");
        Expense e2 = new Expense("Movie", 12.0, "Entertainment", "2024-01-02", "Action movie");
        repo.addExpense(e1);
        repo.addExpense(e2);

        repo.deleteExpense(e1);

        List<Expense> remaining = repo.getAllExpenses();
        assertEquals(1, remaining.size());
        assertSame(e2, remaining.get(0));
        assertFalse(remaining.contains(e1));
    }

    @Test
    void deleteExpense_nonExistingExpense_shouldNotChangeList() {
        Expense e1 = new Expense("Lunch", 10.0, "Food", "2024-01-03", "Sandwich");
        Expense e2_nonExisting = new Expense("Snack", 5.0, "Food", "2024-01-04", "Chips");
        repo.addExpense(e1);

        repo.deleteExpense(e2_nonExisting);

        assertEquals(1, repo.getAllExpenses().size());
        assertSame(e1, repo.getAllExpenses().get(0));
    }

    @Test
    void deleteExpense_nullExpense_shouldNotCrashAndNotChangeList() {
        Expense e1 = new Expense("Gas", 40.0, "Transport", "2024-01-05", "Fill up");
        repo.addExpense(e1);

        // This test also ensures the implementation is robust and doesn't crash on null.
        repo.deleteExpense(null);

        assertEquals(1, repo.getAllExpenses().size());
    }
}