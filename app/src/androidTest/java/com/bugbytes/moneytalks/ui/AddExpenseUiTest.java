package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(AndroidJUnit4.class)
public class AddExpenseUiTest
{
    private String categoryName;
    private String expenseName;

    //setup: Prepares a test category in the application context before UI interaction. Returns nothing.
    @Before
    public void setup() throws ValidationException
    {
        long stamp = System.currentTimeMillis();
        categoryName = "UiAddCategory" + stamp;
        expenseName = "UiAddedExpense" + stamp;

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("500.00")));
    }

    //addExpenseFromUiShowsSavedExpenseInList: Verifies that adding an expense via UI correctly displays it in the RecyclerView. Returns nothing.
    @Test
    public void addExpenseFromUiShowsSavedExpenseInList()
    {
        try (ActivityScenario<ExpenseListActivity> ignored = ActivityScenario.launch(ExpenseListActivity.class))
        {
            //Open the add expense screen
            onView(withId(R.id.btnAddExpense)).perform(click());

            //Fill in the expense details
            onView(withId(R.id.etExpenseName)).perform(replaceText(expenseName), closeSoftKeyboard());
            onView(withId(R.id.etAmount)).perform(replaceText("12.34"), closeSoftKeyboard());
            onView(withId(R.id.autoCompleteCategory)).perform(click(), replaceText(categoryName), closeSoftKeyboard());
            onView(withId(R.id.etDate)).perform(replaceText("10-03-2026"), closeSoftKeyboard());
            onView(withId(R.id.etNotes)).perform(replaceText("UI add test note"), closeSoftKeyboard());

            //Save the expense
            onView(withId(R.id.btnSave)).perform(click());

            //Scroll to find the newly added expense
            onView(withId(R.id.rvExpenses)).perform(
                    RecyclerViewActions.scrollTo(hasDescendant(withText(expenseName)))
            );

            //Assert that the expense is displayed on screen
            onView(withText(expenseName)).check(matches(isDisplayed()));
        }
    }
}