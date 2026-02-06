package com.bugbytes.moneytalks.Presistence.Fake;

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
        Expense e = new Expense(1, "Dinner", 20.0, "Food", "2026-02-06", "Pizza");

        repo.addExpense(e);

        List<Expense> all = repo.getAllExpenses();
        assertEquals(1, all.size());
        assertSame(e, all.get(0));
    }

    @Test
    void getAllExpenses_returnsSameBackingList_staticPersists() {
        Expense e1 = new Expense(1, "Coffee", 3.5, "Food", "2026-02-06", "");
        repo.addExpense(e1);

        // New instance should still see the same static list
        FakeRepository repo2 = new FakeRepository();
        List<Expense> all2 = repo2.getAllExpenses();

        assertEquals(1, all2.size());
        assertSame(e1, all2.get(0));
    }
}
