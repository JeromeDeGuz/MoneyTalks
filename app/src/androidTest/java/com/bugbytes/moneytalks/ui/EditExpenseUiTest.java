package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class EditExpenseUiTest
{
    private String categoryName;
    private String originalName;
    private String updatedName;

    //setup: It inserts one unique expense before the UI test so the edit target row is deterministic.
    @Before
    public void setup() throws ValidationException
    {
        long stamp = System.currentTimeMillis();
        categoryName = "UiEditCategory" + stamp;
        originalName = "UiOriginalExpense" + stamp;
        updatedName = "UiUpdatedExpense" + stamp;

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("500.00")));

        app.getExpenseService().addExpense(new Expense(
                0,
                originalName,
                new BigDecimal("20.00"),
                categoryName,
                LocalDate.of(2026, 3, 10),
                "before edit"
        ));
    }

    //editExpenseFromUiUpdatesRowInList: It edits an existing expense through the UI and verifies that the list shows the updated values.
    @Test
    public void editExpenseFromUiUpdatesRowInList()
    {
        try (ActivityScenario<ExpenseListActivity> ignored = ActivityScenario.launch(ExpenseListActivity.class))
        {
            onView(withId(R.id.rvExpenses)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(originalName)),
                            click()
                    )
            );

            onView(withId(R.id.etExpenseName))
                    .perform(replaceText(updatedName), closeSoftKeyboard());

            onView(withId(R.id.etAmount))
                    .perform(replaceText("88.90"), closeSoftKeyboard());

            onView(withId(R.id.etNotes))
                    .perform(replaceText("after edit"), closeSoftKeyboard());

            onView(withId(R.id.btnSave)).perform(click());

            onView(withId(R.id.rvExpenses)).perform(
                    RecyclerViewActions.scrollTo(
                            hasDescendant(withText(updatedName))
                    )
            );

            onView(withText(updatedName)).check(matches(isDisplayed()));
            onView(withText(originalName)).check(doesNotExist());
        }
    }
}