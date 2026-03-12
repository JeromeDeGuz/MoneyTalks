package com.bugbytes.moneytalks.integration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(AndroidJUnit4.class)
public class CategoryIntegrationTest
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
    public void addCategory_fromDialog_persistsAndCanBeUsedToSaveExpense()
    {
        final long timestamp = System.currentTimeMillis();

        final String newCategoryName = "Category Test " + timestamp;
        final String expenseName = "Expense With New Category " + timestamp;
        final String amount = "16.40";
        final String date = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        final String note = "Created through category integration test";

        try (ActivityScenario<ExpenseListActivity> scenario =
                     ActivityScenario.launch(ExpenseListActivity.class))
        {
            // Open the add expense screen from the main list.
            onView(withId(R.id.btnAddExpense)).perform(click());

            // Open the add category dialog.
            onView(withId(R.id.btnAddCategory)).perform(click());

            // Enter a new category name inside the dialog.
            onView(isAssignableFrom(EditText.class))
                    .inRoot(isDialog())
                    .perform(replaceText(newCategoryName), closeSoftKeyboard());

            // Confirm the new category creation.
            onView(withText("Add")).inRoot(isDialog()).perform(click());

            // Verify the category field is updated with the newly created category.
            onView(withId(R.id.autoCompleteCategory))
                    .check(matches(withText(newCategoryName)));

            // Fill the remaining expense fields.
            onView(withId(R.id.etExpenseName))
                    .perform(replaceText(expenseName), closeSoftKeyboard());

            onView(withId(R.id.etAmount))
                    .perform(replaceText(amount), closeSoftKeyboard());

            onView(withId(R.id.etDate))
                    .perform(replaceText(date), closeSoftKeyboard());

            onView(withId(R.id.etNotes))
                    .perform(replaceText(note), closeSoftKeyboard());

            // Save the expense using the newly added category.
            onView(withId(R.id.btnSave)).perform(click());

            // Verify the saved expense appears on the list screen.
            onView(withText(expenseName)).check(matches(isDisplayed()));
        }

        // Verify the category was persisted through the real category service path.
        Category savedCategory = app.getCategoryService().getCategory(newCategoryName);
        assertNotNull(savedCategory);
        assertEquals(newCategoryName, savedCategory.getName());

        // Verify the expense was persisted with the new category.
        Expense savedExpense = findExpenseByName(expenseName);
        assertNotNull(savedExpense);
        assertEquals(newCategoryName, savedExpense.getCategory());
        assertEquals(amount, savedExpense.getAmount().toPlainString());
    }

    @Test
    public void editCategory_existingCategory_persistsUpdatedNameAndUpdatesRelatedExpenses()
    {
        final long timestamp = System.currentTimeMillis();

        final String originalCategoryName = "Category Edit Old " + timestamp;
        final String updatedCategoryName = "Category Edit New " + timestamp;
        final String expenseName = "Expense Linked To Category " + timestamp;

        // Create a real category and read it back to get the real database id.
        app.getCategoryService().addCategory(new Category(originalCategoryName));

        Category savedCategory = app.getCategoryService().getCategory(originalCategoryName);
        assertNotNull(savedCategory);

        // Create a real expense that uses the original category.
        app.getExpenseService().addExpense(new Expense(
                0,
                expenseName,
                new BigDecimal("21.35"),
                originalCategoryName,
                LocalDate.now().minusDays(1),
                "Expense used to verify category update"
        ));

        // Update the category through the real category service using the saved category with id.
        app.getCategoryService().updateCategory(
                savedCategory,
                new Category(updatedCategoryName)
        );

        // Verify the old category name no longer exists and the new one does.
        assertNull(app.getCategoryService().getCategory(originalCategoryName));

        Category updatedCategory = app.getCategoryService().getCategory(updatedCategoryName);
        assertNotNull(updatedCategory);
        assertEquals(updatedCategoryName, updatedCategory.getName());

        // Verify related expenses were also updated through the integration path.
        Expense updatedExpense = findExpenseByName(expenseName);
        assertNotNull(updatedExpense);
        assertEquals(updatedCategoryName, updatedExpense.getCategory());
    }

    @Test
    public void deleteCategory_unusedCategory_removesCategoryFromPersistence()
    {
        final long timestamp = System.currentTimeMillis();
        final String categoryName = "Category Delete Test " + timestamp;

        // Create a real category that is not used by any expense.
        app.getCategoryService().addCategory(new Category(categoryName));

        Category savedCategory = app.getCategoryService().getCategory(categoryName);
        assertNotNull(savedCategory);

        // Delete the category through the real category service.
        app.getCategoryService().deleteCategory(savedCategory);

        // Verify the category was removed from persistence.
        assertNull(app.getCategoryService().getCategory(categoryName));
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
}