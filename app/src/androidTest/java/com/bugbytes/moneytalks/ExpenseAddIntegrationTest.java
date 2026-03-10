package com.bugbytes.moneytalks;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExpenseAddIntegrationTest
{
    private MoneyTalksApp app;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Before
    public void setup()
    {
        app = (MoneyTalksApp) ApplicationProvider.getApplicationContext();
    }

    @Test
    public void addExpense_validInput_savesToDatabaseAndDisplaysInList()
    {
        // Use a unique name so the test is less likely to conflict with existing data.
        final String uniqueName = "Integration Add Test " + System.currentTimeMillis();
        final String amount = "12.50";
        final String category = "Food";
        final String date = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        final String note = "Created by integration test";

        // Read the current number of expenses before the UI flow starts.
        final int countBefore = app.getExpenseService().getAllExpenses().size();

        // Launch the main expense list screen.
        ActivityScenario.launch(ExpenseListActivity.class);

        // Open the add expense screen.
        onView(withId(R.id.btnAddExpense)).perform(click());

        // Fill in all required fields with valid data.
        onView(withId(R.id.etExpenseName)).perform(replaceText(uniqueName), closeSoftKeyboard());
        onView(withId(R.id.etAmount)).perform(replaceText(amount), closeSoftKeyboard());
        onView(withId(R.id.autoCompleteCategory)).perform(replaceText(category), closeSoftKeyboard());
        onView(withId(R.id.etDate)).perform(replaceText(date), closeSoftKeyboard());
        onView(withId(R.id.etNotes)).perform(replaceText(note), closeSoftKeyboard());

        // Save the new expense.
        onView(withId(R.id.btnSave)).perform(click());

        // Verify the new expense is displayed after returning to the list screen.
        onView(withText(uniqueName)).check(matches(isDisplayed()));

        // Verify the new expense was actually persisted through the service/repository path.
        List<Expense> expensesAfter = app.getExpenseService().getAllExpenses();

        assertEquals(countBefore + 1, expensesAfter.size());
        assertTrue(
                expensesAfter.stream().anyMatch(expense ->
                        expense != null
                                && uniqueName.equals(expense.getName())
                                && expense.getAmount() != null
                                && expense.getAmount().toPlainString().equals(amount)
                                && category.equals(expense.getCategory())
                )
        );
    }
}