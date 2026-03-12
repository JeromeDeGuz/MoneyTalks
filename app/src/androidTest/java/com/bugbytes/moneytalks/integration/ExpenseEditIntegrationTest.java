package com.bugbytes.moneytalks;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.presentation.AddAndEditExpense;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(AndroidJUnit4.class)
public class ExpenseEditIntegrationTest
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
    public void editExpense_validInput_updatesDatabase()
    {
        final String category = "Food";
        ensureCategoryExists(category);

        // Create a real expense first so the edit screen has persisted data to update.
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

        // Launch the screen directly in edit mode with the saved expense.
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

        // Verify the original name no longer exists after the update.
        Expense oldVersion = findExpenseByName(originalName);
        assertNull(oldVersion);

        // Verify the updated record exists and keeps the same database id.
        Expense updatedExpense = findExpenseByName(updatedName);
        assertNotNull(updatedExpense);
        assertEquals(savedExpense.getId(), updatedExpense.getId());
        assertEquals("25.75", updatedExpense.getAmount().toPlainString());
        assertEquals(category, updatedExpense.getCategory());
        assertEquals(updatedNote, updatedExpense.getNote());
    }

    private void ensureCategoryExists(String categoryName)
    {
        // Ensure the required category exists before running the UI flow.
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
                // Ignore setup-time validation issues if the category already becomes available.
            }
        }
    }

    private Expense findExpenseByName(String expenseName)
    {
        // Find an expense by name from persisted data.
        for (Expense expense : app.getExpenseService().getAllExpenses())
        {
            if (expense != null && expenseName.equals(expense.getName()))
            {
                return expense;
            }
        }
        return null;
    }
}