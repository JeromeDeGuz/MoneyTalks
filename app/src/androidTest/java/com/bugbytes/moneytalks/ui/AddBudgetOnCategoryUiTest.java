package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.bugbytes.moneytalks.presentation.BudgetActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(AndroidJUnit4.class)
public class AddBudgetOnCategoryUiTest
{
    private String categoryName;

    //setup: Prepares the test category in the application context before UI interaction. Returns nothing.
    @Before
    public void setup() throws ValidationException
    {
        categoryName = "BudgetCategory" + System.currentTimeMillis();

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("0.00")));
    }

    //addBudgetOnCategoryUpdatesBudgetValue: Verifies that editing a budget in the UI correctly updates the value. Returns nothing.
    @Test
    public void addBudgetOnCategoryUpdatesBudgetValue()
    {
        try (ActivityScenario<BudgetActivity> ignored = ActivityScenario.launch(BudgetActivity.class))
        {
            onView(withId(R.id.rvBudgetList)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(categoryName)),
                            clickChildViewWithId(R.id.btnEditBudget)
                    )
            );

            onView(isAssignableFrom(EditText.class))
                    .perform(replaceText("250.00"), closeSoftKeyboard());

            onView(withText("Save")).perform(click());

            onView(withId(R.id.rvBudgetList)).perform(
                    RecyclerViewActions.scrollTo(
                            withBudgetRow(categoryName, "250.00")
                    )
            );

            onView(withId(R.id.rvBudgetList))
                    .check(matches(hasDescendant(withBudgetRow(categoryName, "250.00"))));
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
                    uiController.loopMainThreadUntilIdle();
                }
            }
        };
    }

    //withBudgetRow: Custom Matcher to verify that a row contains both the correct category and budget amount. Returns @return Matcher.
    private static Matcher<View> withBudgetRow(String categoryName, String budgetAmount)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText(
                        "Budget row with category name \"" + categoryName +
                                "\" and budget amount \"" + budgetAmount + "\""
                );
            }

            @Override
            protected boolean matchesSafely(View view)
            {
                TextView tvCategoryName = view.findViewById(R.id.tvCategoryName);
                TextView tvBudgetAmount = view.findViewById(R.id.tvBudgetAmount);

                if (tvCategoryName == null || tvBudgetAmount == null)
                {
                    return false;
                }

                String actualCategoryName = tvCategoryName.getText().toString();
                String actualBudgetAmount = tvBudgetAmount.getText().toString();

                return categoryName.equals(actualCategoryName)
                        && budgetAmount.equals(actualBudgetAmount);
            }
        };
    }
}