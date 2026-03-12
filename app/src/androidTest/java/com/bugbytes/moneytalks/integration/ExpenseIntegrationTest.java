package com.bugbytes.moneytalks.integration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.presentation.AddAndEditExpense;
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExpenseIntegrationTest
{
    private MoneyTalksApp app;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Before
    public void setup()
    {
        app = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void addExpense_validInput_savesToDatabaseAndDisplaysInList()
    {
        final String category = "Food";
        ensureCategoryExists(category);

        // Use a unique name so the test does not conflict with existing records.
        final String uniqueName = "Integration Add Test " + System.currentTimeMillis();
        final String amount = "12.50";
        final String date = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        final String note = "Created by integration test";

        // Record the number of persisted expenses before the UI flow begins.
        final int countBefore = app.getExpenseService().getAllExpenses().size();

        try (ActivityScenario<ExpenseListActivity> scenario =
                     ActivityScenario.launch(ExpenseListActivity.class))
        {
            // Open the add expense screen from the main list.
            onView(withId(R.id.btnAddExpense)).perform(click());

            // Fill the form with valid input.
            onView(withId(R.id.etExpenseName))
                    .perform(replaceText(uniqueName), closeSoftKeyboard());

            onView(withId(R.id.etAmount))
                    .perform(replaceText(amount), closeSoftKeyboard());

            onView(withId(R.id.autoCompleteCategory))
                    .perform(replaceText(category), closeSoftKeyboard());

            onView(withId(R.id.etDate))
                    .perform(replaceText(date), closeSoftKeyboard());

            onView(withId(R.id.etNotes))
                    .perform(replaceText(note), closeSoftKeyboard());

            // Save the new expense.
            onView(withId(R.id.btnSave)).perform(click());

            // Verify the new expense is shown after returning to the list screen.
            onView(withText(uniqueName)).check(matches(isDisplayed()));
        }

        // Verify the expense was persisted through the real service and repository path.
        List<Expense> expensesAfter = app.getExpenseService().getAllExpenses();
        assertEquals(countBefore + 1, expensesAfter.size());

        Expense savedExpense = findExpenseByName(uniqueName);
        assertNotNull(savedExpense);
        assertEquals(amount, savedExpense.getAmount().toPlainString());
        assertEquals(category, savedExpense.getCategory());
        assertEquals(note, savedExpense.getNote());
    }

    @Test
    public void editExpense_validInput_updatesDatabase()
    {
        final String category = "Food";
        ensureCategoryExists(category);

        // Create a persisted expense first so the edit screen has real data to update.
        final String originalName = "Edit Test Original " + System.currentTimeMillis();
        final Expense originalExpense = new Expense(
                0,
                originalName,
                new BigDecimal("10.00"),
                category,
                LocalDate.now().minusDays(2),
                "Original note"
        );

        app.getExpenseService().addExpense(originalExpense);

        // Read back the saved expense so the test uses the real generated database id.
        Expense savedExpense = findExpenseByName(originalName);
        assertNotNull(savedExpense);

        final String updatedName = "Edit Test Updated " + System.currentTimeMillis();
        final String updatedAmount = "25.75";
        final String updatedDate = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        final String updatedNote = "Updated by integration test";

        // Launch the add/edit screen directly in edit mode.
        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                AddAndEditExpense.class
        );
        intent.putExtra(AddAndEditExpense.EXTRA_EXPENSE, savedExpense);

        try (ActivityScenario<AddAndEditExpense> scenario = ActivityScenario.launch(intent))
        {
            // Replace the existing values with new valid data.
            onView(withId(R.id.etExpenseName))
                    .perform(replaceText(updatedName), closeSoftKeyboard());

            onView(withId(R.id.etAmount))
                    .perform(replaceText(updatedAmount), closeSoftKeyboard());

            onView(withId(R.id.autoCompleteCategory))
                    .perform(replaceText(category), closeSoftKeyboard());

            onView(withId(R.id.etDate))
                    .perform(replaceText(updatedDate), closeSoftKeyboard());

            onView(withId(R.id.etNotes))
                    .perform(replaceText(updatedNote), closeSoftKeyboard());

            // Submit the update.
            onView(withId(R.id.btnSave)).perform(click());
        }

        // Verify the original version is gone and the updated version exists.
        Expense oldVersion = findExpenseByName(originalName);
        assertNull(oldVersion);

        Expense updatedExpense = findExpenseByName(updatedName);
        assertNotNull(updatedExpense);
        assertEquals(savedExpense.getId(), updatedExpense.getId());
        assertEquals(updatedAmount, updatedExpense.getAmount().toPlainString());
        assertEquals(category, updatedExpense.getCategory());
        assertEquals(updatedNote, updatedExpense.getNote());
    }

    @Test
    public void deleteExpense_existingExpense_removesFromUiAndDatabase()
    {
        final String category = "Food";
        ensureCategoryExists(category);

        // Create a unique persisted expense so the test can target one exact row.
        final String uniqueName = "Delete Test " + System.currentTimeMillis();
        final Expense expenseToDelete = new Expense(
                0,
                uniqueName,
                new BigDecimal("18.75"),
                category,
                LocalDate.now().minusDays(1),
                "Created for delete integration test"
        );

        app.getExpenseService().addExpense(expenseToDelete);

        // Confirm the expense exists before the UI flow starts.
        Expense savedExpense = findExpenseByName(uniqueName);
        assertNotNull(savedExpense);

        final int countBefore = app.getExpenseService().getAllExpenses().size();

        try (ActivityScenario<ExpenseListActivity> scenario =
                     ActivityScenario.launch(ExpenseListActivity.class))
        {
            // Click the delete button inside the row that contains the target expense title.
            onView(withId(R.id.rvExpenses)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(uniqueName)),
                            clickChildViewWithId(R.id.deleteButton)
                    )
            );

            // Verify the deleted expense is no longer visible in the list UI.
            onView(withText(uniqueName)).check(doesNotExist());
        }

        // Verify the record was removed from persisted data as well.
        final int countAfter = app.getExpenseService().getAllExpenses().size();
        assertEquals(countBefore - 1, countAfter);
        assertNull(findExpenseByName(uniqueName));
    }

    private void ensureCategoryExists(String categoryName)
    {
        // Ensure the required category exists before running a UI flow.
        boolean exists = false;

        for (Category category : app.getCategoryService().getAllCategories())
        {
            if (category != null && categoryName.equals(category.getName()))
            {
                exists = true;
                break;
            }
        }

        if (!exists)
        {
            try
            {
                app.getCategoryService().addCategory(new Category(categoryName));
            }
            catch (ValidationException ignored)
            {
                // Ignore setup-time validation issues if the category becomes available.
            }
        }
    }

    private Expense findExpenseByName(String expenseName)
    {
        // Find a persisted expense by its name.
        for (Expense expense : app.getExpenseService().getAllExpenses())
        {
            if (expense != null && expenseName.equals(expense.getName()))
            {
                return expense;
            }
        }
        return null;
    }

    private ViewAction clickChildViewWithId(int viewId)
    {
        // Click a child view inside a RecyclerView item, such as the delete button.
        return new ViewAction()
        {
            @Override
            public Matcher<View> getConstraints()
            {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription()
            {
                return "Click a child view with a specific id.";
            }

            @Override
            public void perform(UiController uiController, View view)
            {
                View childView = view.findViewById(viewId);
                if (childView != null)
                {
                    childView.performClick();
                    uiController.loopMainThreadUntilIdle();
                }
            }
        };
    }
}