package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.EditText;

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

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(AndroidJUnit4.class)
public class BudgetUiTest
{
    private String categoryName;

    //setup: It inserts one unique category before the budget UI test so the edit target row is deterministic.
    @Before
    public void setup() throws ValidationException
    {
        categoryName = "UiBudgetCategory" + System.currentTimeMillis();

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("10.00")));
    }

    //editBudgetFromUiUpdatesBudgetAmountInList: It opens the budget screen, edits a category budget, and verifies that the new amount is shown.
    @Test
    public void editBudgetFromUiUpdatesBudgetAmountInList()
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
                    .perform(replaceText("123.45"), closeSoftKeyboard());

            onView(withText("Save")).perform(click());

            onView(withId(R.id.rvBudgetList)).perform(
                    RecyclerViewActions.scrollTo(
                            hasDescendant(withText(categoryName))
                    )
            );

            onView(withText("123.45")).check(matches(isDisplayed()));
        }
    }

    //clickChildViewWithId: It clicks a child view inside a RecyclerView row. Takes in @param viewId.
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
                return "Click on a child view with the given id.";
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