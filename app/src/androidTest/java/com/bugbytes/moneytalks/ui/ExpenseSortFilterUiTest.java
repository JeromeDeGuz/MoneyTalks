package com.bugbytes.moneytalks.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.presentation.ExpenseListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class ExpenseSortFilterUiTest
{
    private String foodCategory;
    private String travelCategory;
    private String newestExpense;
    private String oldestExpense;

    @Before
    public void setup() throws ValidationException
    {
        long stamp = System.currentTimeMillis();
        foodCategory = "UiFood" + stamp;
        travelCategory = "UiTravel" + stamp;
        newestExpense = "NewestExpense" + stamp;
        oldestExpense = "OldestExpense" + stamp;

        MoneyTalksApp app = ApplicationProvider.getApplicationContext();
        app.getCategoryService().addCategory(new Category(foodCategory, new BigDecimal("500.00")));
        app.getCategoryService().addCategory(new Category(travelCategory, new BigDecimal("500.00")));

        app.getExpenseService().addExpense(new Expense(
                0, oldestExpense, new BigDecimal("10.00"), foodCategory,
                LocalDate.of(2026, 3, 1), "old item"
        ));
        app.getExpenseService().addExpense(new Expense(
                0, newestExpense, new BigDecimal("20.00"), foodCategory,
                LocalDate.of(2026, 3, 20), "new item"
        ));
        app.getExpenseService().addExpense(new Expense(
                0, "TravelExpense" + stamp, new BigDecimal("15.00"), travelCategory,
                LocalDate.of(2026, 3, 15), "travel item"
        ));
    }

    @Test
    public void filterAndSortWorkFromUi()
    {
        try (ActivityScenario<ExpenseListActivity> ignored = ActivityScenario.launch(ExpenseListActivity.class))
        {
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withText(foodCategory)).perform(click());

            onView(withText(newestExpense)).check(matches(isDisplayed()));
            onView(withText(oldestExpense)).check(matches(isDisplayed()));
            onView(withId(R.id.btnFilter)).check(matches(withText("Filtering by Category (" + foodCategory + ")")));

            onView(withId(R.id.rvExpenses)).check(matches(atPosition(0, hasDescendant(withText(newestExpense)))));

            onView(withId(R.id.btnSort)).perform(click());
            onView(withText("Oldest to Newest")).perform(click());

            onView(withId(R.id.rvExpenses)).check(matches(atPosition(0, hasDescendant(withText(oldestExpense)))));
        }
    }

    private static Matcher<View> atPosition(int position, Matcher<View> itemMatcher)
    {
        return new TypeSafeMatcher<>()
        {
            @Override
            protected boolean matchesSafely(View view)
            {
                if (!(view instanceof androidx.recyclerview.widget.RecyclerView))
                {
                    return false;
                }

                androidx.recyclerview.widget.RecyclerView recyclerView =
                        (androidx.recyclerview.widget.RecyclerView) view;
                androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(position);

                if (viewHolder == null)
                {
                    return false;
                }

                return itemMatcher.matches(viewHolder.itemView);
            }

            @Override
            public void describeTo(@NonNull Description description)
            {
                description.appendText("Item at position " + position);
            }
        };
    }
}
