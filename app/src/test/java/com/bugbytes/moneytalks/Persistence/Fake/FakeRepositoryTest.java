package com.bugbytes.moneytalks.Persistence.Fake;

import com.bugbytes.moneytalks.Models.Expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FakeRepositoryTest
{
    private FakeRepository repo;

    @BeforeEach
    public void setUp()
    {
        //Initialize repository instance for each test
        repo = new FakeRepository();

        //Clear static list to prevent shared state between tests
        repo.getAllExpenses().clear();
    }

    //Repository should start empty after clearing static list
    @Test
    public void getAllExpensesInitiallyEmpty()
    {
        assertTrue(repo.getAllExpenses().isEmpty());
    }

    //Verify that adding an expense stores it in the static list
    @Test
    public void addExpenseShouldAddToList()
    {
        final Expense e = new Expense("Dinner", 20.0, "Food", "2026/02/06", "Pizza");

        repo.addExpense(e);

        final List<Expense> all = repo.getAllExpenses();
        assertEquals(1, all.size());
        assertSame(e, all.get(0));
    }

    //Add expense and verify static list persists across instances
    @Test
    public void getAllExpensesReturnsSameBackingListStaticPersists()
    {

        final Expense e1 = new Expense("Coffee", 3.5, "Food", "2026/02/06", "");
        repo.addExpense(e1);

        //New repository instance should reference same static list
        final FakeRepository repo2 = new FakeRepository();
        final List<Expense> all2 = repo2.getAllExpenses();

        //Static list includes hard-coded defaults plus added item
        assertEquals(4, all2.size());
        assertSame(e1, all2.get(0));
    }

    //Verify deleting an existing expense removes it from list
    @Test
    public void deleteExpenseExistingExpenseShouldRemoveFromList()
    {
        final Expense e1 = new Expense("Book", 15.0, "Shopping", "2024/01/01", "Sci/fi book");
        final Expense e2 = new Expense("Movie", 12.0, "Entertainment", "2024/01/02", "Action movie");

        repo.addExpense(e1);
        repo.addExpense(e2);

        repo.deleteExpense(e1);

        final List<Expense> remaining = repo.getAllExpenses();
        assertEquals(1, remaining.size());
        assertSame(e2, remaining.get(0));
        assertFalse(remaining.contains(e1));
    }

    //Verify deleting non-existing expense does not modify list
    @Test
    public void deleteExpenseNonExistingExpenseShouldNotChangeList()
    {
        final Expense e1 = new Expense("Lunch", 10.0, "Food", "2024/01/03", "Sandwich");
        final Expense e2NonExisting = new Expense("Snack", 5.0, "Food", "2024/01/04", "Chips");

        repo.addExpense(e1);
        repo.deleteExpense(e2NonExisting);

        assertEquals(1, repo.getAllExpenses().size());
        assertSame(e1, repo.getAllExpenses().get(0));
    }
}
