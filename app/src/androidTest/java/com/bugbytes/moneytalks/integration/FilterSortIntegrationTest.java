package com.bugbytes.moneytalks.integration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

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
public class FilterSortIntegrationTest
{
    private MoneyTalksApp app;

    @Before
    public void setup()
    {
        app = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void filterExpenses_byCategory_showsOnlyMatchingExpenses()
    {
        final long timestamp = System.currentTimeMillis();

        final String filterCategory = "FilterCat" + timestamp;
        final String otherCategory = "OtherCat" + timestamp;

        ensureCategoryExists(filterCategory);
        ensureCategoryExists(otherCategory);

        final String matchingExpenseName = "Filter Match " + timestamp;
        final String nonMatchingExpenseName = "Filter Other " + timestamp;

        // Create one expense in the selected category and one in a different category.
        app.getExpenseService().addExpense(new Expense(
                0,
                matchingExpenseName,
                new BigDecimal("11.25"),
                filterCategory,
                LocalDate.now().minusDays(2),
                "Should remain visible after filtering"
        ));

        app.getExpenseService().addExpense(new Expense(
                0,
                nonMatchingExpenseName,
                new BigDecimal("30.50"),
                otherCategory,
                LocalDate.now().minusDays(1),
                "Should disappear after filtering"
        ));

        try (ActivityScenario<ExpenseListActivity> scenario =
                     ActivityScenario.launch(ExpenseListActivity.class))
        {
            // Open the filter menu and select the target category.
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withText(filterCategory)).perform(click());

            // Verify the filter label updates to the selected category.
            onView(withId(R.id.btnFilter))
                    .check(matches(withText("Filtering by Category (" + filterCategory + ")")));

            // Verify only the matching expense is shown.
            onView(withText(matchingExpenseName)).check(matches(withText(matchingExpenseName)));
            onView(withText(nonMatchingExpenseName)).check(doesNotExist());
        }
    }

    @Test
    public void sortExpenses_byDate_oldestToNewest_updatesRecyclerViewOrder()
    {
        final long timestamp = System.currentTimeMillis();

        final String sortCategory = "SortCat" + timestamp;
        ensureCategoryExists(sortCategory);

        final String oldestExpenseName = "Old Expense " + timestamp;
        final String newestExpenseName = "New Expense " + timestamp;

        // Create two expenses in the same category with different dates.
        app.getExpenseService().addExpense(new Expense(
                0,
                oldestExpenseName,
                new BigDecimal("9.99"),
                sortCategory,
                LocalDate.now().minusDays(10),
                "Older record for sorting"
        ));

        app.getExpenseService().addExpense(new Expense(
                0,
                newestExpenseName,
                new BigDecimal("19.99"),
                sortCategory,
                LocalDate.now().minusDays(1),
                "Newer record for sorting"
        ));

        try (ActivityScenario<ExpenseListActivity> scenario =
                     ActivityScenario.launch(ExpenseListActivity.class))
        {
            // Filter to a unique category so the list only contains the test records.
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withText(sortCategory)).perform(click());

            // Change the sort order to oldest first.
            onView(withId(R.id.btnSort)).perform(click());
            onView(withText("Oldest to Newest")).perform(click());

            // Verify the recycler view order changed correctly.
            onView(withRecyclerView(R.id.rvExpenses).atPositionOnView(0, R.id.tvTitle))
                    .check(matches(withText(oldestExpenseName)));

            onView(withRecyclerView(R.id.rvExpenses).atPositionOnView(1, R.id.tvTitle))
                    .check(matches(withText(newestExpenseName)));
        }
    }

    private void ensureCategoryExists(String categoryName)
    {
        // Ensure the test category exists before running the UI flow.
        boolean exists = false;

        for (Category category : app.getCategoryService().getAllCategories())
        {
            if (category != null && categoryName.equals(category.getName()))
            {
                exists = true;
                break;
            }
        }

        if (!exists)
        {
            try
            {
                app.getCategoryService().addCategory(new Category(categoryName));
            }
            catch (ValidationException ignored)
            {
                // Ignore setup-time validation issues if the category becomes available.
            }
        }
    }

    private RecyclerViewMatcher withRecyclerView(int recyclerViewId)
    {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static class RecyclerViewMatcher
    {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId)
        {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPositionOnView(int position, int targetViewId)
        {
            return new TypeSafeMatcher<View>()
            {
                @Override
                public void describeTo(Description description)
                {
                    description.appendText("Matches view at position " + position +
                            " in recycler view " + recyclerViewId);
                }

                @Override
                protected boolean matchesSafely(View view)
                {
                    View recyclerView = view.getRootView().findViewById(recyclerViewId);
                    if (!(recyclerView instanceof androidx.recyclerview.widget.RecyclerView))
                    {
                        return false;
                    }

                    androidx.recyclerview.widget.RecyclerView rv =
                            (androidx.recyclerview.widget.RecyclerView) recyclerView;

                    androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder =
                            rv.findViewHolderForAdapterPosition(position);

                    if (viewHolder == null)
                    {
                        return false;
                    }

                    View targetView = viewHolder.itemView.findViewById(targetViewId);
                    return view == targetView;
                }
            };
        }
    }
}