package com.bugbytes.moneytalks.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddTodoItemE2ETest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void clearDb() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(AppDbHelper.DB_NAME);
    }

    @Test
    public void addTodoItem_showsInList() {
        onView(withId(R.id.titleInput)).perform(replaceText("Laundry"));
        onView(withId(R.id.descInput)).perform(replaceText("wash + dry"));

        onView(withId(R.id.descInput))
                .perform(closeSoftKeyboard(), pressImeActionButton());

        onView(withId(R.id.addButton)).perform(click());

    }

}
