package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.presentation.AddAndEditExpense;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(AndroidJUnit4.class)
public class BudgetWarningUiTest
{
    private String categoryName;
    private String expenseName;

    //setup: Prepares a category with a low budget to trigger the warning during tests. Returns nothing.
    @Before
    public void setup() throws ValidationException
    {
        long stamp = System.currentTimeMillis();
        categoryName = "WarningCategory" + stamp;
        expenseName = "WarningExpense" + stamp;

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("10.00")));
    }

    //saveExpenseOverBudgetShowsWarningDialog: Verifies that saving an expense exceeding the budget triggers a warning dialog. Returns nothing.
    @Test
    public void saveExpenseOverBudgetShowsWarningDialog()
    {
        try (ActivityScenario<AddAndEditExpense> scenario =
                     ActivityScenario.launch(AddAndEditExpense.class))
        {
            //Input expense details that exceed the $10.00 budget
            onView(withId(R.id.etExpenseName))
                    .perform(replaceText(expenseName), closeSoftKeyboard());

            onView(withId(R.id.etAmount))
                    .perform(replaceText("20.00"), closeSoftKeyboard());

            onView(withId(R.id.autoCompleteCategory))
                    .perform(click(), replaceText(categoryName), closeSoftKeyboard());

            onView(withId(R.id.etDate))
                    .perform(replaceText("10-03-2026"), closeSoftKeyboard());

            onView(withId(R.id.etNotes))
                    .perform(replaceText("budget warning test"), closeSoftKeyboard());

            //Click save and check for the "Over Budget" dialog
            onView(withId(R.id.btnSave)).perform(click());

            onView(withText("Over Budget"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()));

            //Verify specific dialog content
            onView(withText(containsString("Category: " + categoryName)))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()));

            onView(withText(containsString("Budget: $10.00")))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()));

            onView(withText(containsString("Spent This Month: $20.00")))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()));

            onView(withText(containsString("Over By: $10.00")))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()));

            //Dismiss the dialog
            onView(withText("OK"))
                    .inRoot(isDialog())
                    .perform(click());
        }
    }
}