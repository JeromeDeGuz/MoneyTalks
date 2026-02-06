package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
import com.bugbytes.moneytalks.Presistence.Fake.FakeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseServiceImplTest {

    private ExpenseRepository repo;
    private ExpenseService service;

    @BeforeEach
    void setup() {
        repo = new FakeRepository();

        // FakeRepository uses a static list, so clear it between tests
        repo.getAllExpenses().clear();

        service = new ExpenseServiceImpl(repo);
    }

    @Test
    void getAllExpenses_initiallyEmpty() {
        assertTrue(service.getAllExpenses().isEmpty());
    }

    @Test
    void addExpense_shouldStoreExpense() {
        Expense e = new Expense(
                1,
                "Lunch",
                12.50,
                "Food",
                "2026-02-06",
                "Sandwich"
        );

        service.addExpense(e);

        List<Expense> all = service.getAllExpenses();
        assertEquals(1, all.size());
        assertSame(e, all.get(0));
    }

    @Test
    void addExpense_multipleExpenses_shouldStoreAll() {
        Expense e1 = new Expense(1, "Coffee", 3.50, "Food", "2026-02-06", "");
        Expense e2 = new Expense(2, "Bus", 2.75, "Transport", "2026-02-06", "to campus");

        service.addExpense(e1);
        service.addExpense(e2);

        List<Expense> all = service.getAllExpenses();
        assertEquals(2, all.size());
        assertSame(e1, all.get(0));
        assertSame(e2, all.get(1));
    }
}
