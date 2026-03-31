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

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.presentation.SettingsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ManageCategoriesUiTest
{
    //addCategoryFromUiShowsNewCategoryInRecyclerView: It opens category management from settings, adds a category, and verifies that it appears in the list.
    @Test
    public void addCategoryFromUiShowsNewCategoryInRecyclerView()
    {
        String categoryName = "UiCategory" + System.currentTimeMillis();

        try (ActivityScenario<SettingsActivity> ignored = ActivityScenario.launch(SettingsActivity.class))
        {
            onView(withId(R.id.btnCategorySettings)).perform(click());

            onView(withId(R.id.rvCategories)).perform(
                    RecyclerViewActions.actionOnItem(
                            hasDescendant(withText("+ Add Category")),
                            click()
                    )
            );

            onView(isAssignableFrom(EditText.class))
                    .perform(replaceText(categoryName), closeSoftKeyboard());

            onView(withText("Save")).perform(click());

            onView(withText(categoryName)).check(matches(isDisplayed()));
        }
    }
}