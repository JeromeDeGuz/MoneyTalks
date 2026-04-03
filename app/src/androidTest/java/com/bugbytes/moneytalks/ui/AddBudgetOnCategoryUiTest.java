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
public class AddBudgetOnCategoryUiTest
{
    private String categoryName;

    @Before
    public void setup() throws ValidationException
    {
        categoryName = "BudgetCategory" + System.currentTimeMillis();

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName, new BigDecimal("0.00")));
    }

    @Test
    public void addBudget_onCategory_updatesBudgetValue()
    {
        try (ActivityScenario<BudgetActivity> ignored = ActivityScenario.launch(BudgetActivity.class))
        {
            onView(withId(R.id.rvBudgetList)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(categoryName)),
                            clickChildViewWithId(R.id.btnEditBudget)
                    )
            );

            onView(isAssignableFrom(EditText.class)).perform(replaceText("250.00"), closeSoftKeyboard());
            onView(withText("Save")).perform(click());

            onView(withId(R.id.rvBudgetList)).perform(
                    RecyclerViewActions.scrollTo(hasDescendant(withText(categoryName)))
            );

            onView(withText("250.00")).check(matches(isDisplayed()));
        }
    }

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
                return "Click child view";
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
