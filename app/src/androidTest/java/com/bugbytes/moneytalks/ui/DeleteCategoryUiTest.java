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
import com.bugbytes.moneytalks.presentation.ManageCategoriesActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DeleteCategoryUiTest
{
    private String categoryName;

    @Before
    public void setup() throws ValidationException
    {
        categoryName = "DeleteCategory" + System.currentTimeMillis();

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(categoryName));
    }

    @Test
    public void deleteCategoryFromUiRemovesRow()
    {
        try (ActivityScenario<ManageCategoriesActivity> ignored = ActivityScenario.launch(ManageCategoriesActivity.class))
        {
            onView(withId(R.id.rvCategories)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(categoryName)),
                            clickChildViewWithId(R.id.btnDeleteCategory)
                    )
            );

            onView(withText("Delete")).perform(click());
            onView(withText(categoryName)).check(doesNotExist());
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
