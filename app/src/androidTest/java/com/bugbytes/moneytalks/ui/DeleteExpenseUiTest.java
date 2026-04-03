package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class DeleteExpenseUiTest
{
    private String categoryName;
    private String expenseName;

    //setup: Prepares a test category and expense in the application context before UI interaction. Returns nothing.
    @Before
    public void setup() throws ValidationException
    {
        long stamp = System.currentTimeMillis();
        categoryName = "UiDeleteCategory" + stamp;
        expenseName = "UiDeleteExpense" + stamp;

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("500.00")));
        app.getExpenseService().addExpense(new Expense(
                0,
                expenseName,
                new BigDecimal("30.00"),
                categoryName,
                LocalDate.of(2026, 3, 10),
                "delete me"
        ));
    }

    //deleteExpenseFromUiRemovesRow: Verifies that deleting an expense via the UI correctly removes the row from the list. Returns nothing.
    @Test
    public void deleteExpenseFromUiRemovesRow()
    {
        try (ActivityScenario<ExpenseListActivity> ignored = ActivityScenario.launch(ExpenseListActivity.class))
        {
            // Find the expense row and click its delete button
            onView(withId(R.id.rvExpenses)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(expenseName)),
                            clickChildViewWithId(R.id.deleteButton)
                    )
            );

            // Assert that the expense row no longer exists
            onView(withText(expenseName)).check(doesNotExist());
        }
    }

    //clickChildViewWithId: Custom ViewAction to interact with a specific button inside a RecyclerView row. Returns @return ViewAction.
    private static ViewAction clickChildViewWithId(int viewId)
    {
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
                return "Click child view with id " + viewId;
            }

            @Override
            public void perform(UiController uiController, View view)
            {
                View child = view.findViewById(viewId);
                if (child != null)
                {
                    child.performClick();
                }
            }
        };
    }
}