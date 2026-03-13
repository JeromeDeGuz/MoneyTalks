package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.services.ExpenseService;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.R;


import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//Main screen that displays all expenses in a list
public class ExpenseListActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseEventListener
{
    private RecyclerView recyclerView;
    private ExpenseService expenseService;
    private ExpenseAdapter adapter; // Instance kept for reuse (Suggestion #14)

    // Chotu button for sorting functionality to replace the clunky spinner
    private Button btnSort;

    private Button btnFilter;
    // default
    private String selectedCategory = "All";
    // Tracks current sort state
    private boolean isNewestFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);

        final MoneyTalksApp app = (MoneyTalksApp) getApplication();
        expenseService = app.getExpenseService();

        final View mainView = findViewById(R.id.layout_expense_list);
        if (mainView != null)
        {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) ->
            {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        else
        {
            // Logging if layout guarantees are missed (Suggestion #15)
            android.util.Log.e("ExpenseListActivity", "Main view layout_expense_list not found!");
        }

        // Setup the sorting button to allow immediate list updates via PopupMenu
        setupSortButton();

        // Setup the filtering button to allow immediate list updates via PopupMenu
        setupFilterButton();

        recyclerView = findViewById(R.id.rvExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list and 'this' as listener (Suggestion #13)
        adapter = new ExpenseAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        final FloatingActionButton btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v ->
        {
            Intent intent = new Intent(ExpenseListActivity.this, AddAndEditExpense.class);
            startActivity(intent);
        });

        final ImageButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v ->
        {
            Intent intent = new Intent(ExpenseListActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
    private void setupSortButton()
    {
        btnSort = findViewById(R.id.btnSort);
        if (btnSort == null)
        {
            return;
        }

        btnSort.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ExpenseListActivity.this, btnSort);
            Menu menu = popup.getMenu();

            // Use a menu group so items become mutually exclusive (single choice)
            final int GROUP_SORT = 1;
            final int ID_NEWEST = 101;
            final int ID_OLDEST = 102;

            MenuItem newestItem = menu.add(GROUP_SORT, ID_NEWEST, 0, "Newest to Oldest");
            MenuItem oldestItem = menu.add(GROUP_SORT, ID_OLDEST, 1, "Oldest to Newest");

            // Pre-check the currently active sort option when opening the popup
            newestItem.setChecked(isNewestFirst);
            oldestItem.setChecked(!isNewestFirst);

            // Make items checkable and enforce single selection within the group
            newestItem.setCheckable(true);
            oldestItem.setCheckable(true);
            menu.setGroupCheckable(GROUP_SORT, true, true);

            popup.setOnMenuItemClickListener(item -> {
                // Update state based on selection and mark the chosen item checked
                if (item.getItemId() == ID_NEWEST)
                {
                    isNewestFirst = true;
                    item.setChecked(true);
                }
                else if (item.getItemId() == ID_OLDEST)
                {
                    isNewestFirst = false;
                    item.setChecked(true);
                }

                // Refresh list using the current sort + filter state
                loadExpenses();
                return true;
            });

            popup.show();
        });
    }

    // Configures the Filter button popup menu (shows a checkmark on the current selection)
    private void setupFilterButton()
    {
        btnFilter = findViewById(R.id.btnFilter);
        if (btnFilter == null)
        {
            return;
        }

        // Default label
        btnFilter.setText("Filtering by Category (All)");

        btnFilter.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ExpenseListActivity.this, btnFilter);
            Menu menu = popup.getMenu();

            // Use a menu group so items become mutually exclusive (single choice)
            final int GROUP_FILTER = 2;

            // Fetch dynamic categories from the service
            final MoneyTalksApp app = (MoneyTalksApp) getApplication();
            List<Category> dbCategories = app.getCategoryService().getAllCategories();

            // Build the filter list starting with "All"
            List<String> categories = new ArrayList<>();
            categories.add("All");
            for (Category c : dbCategories)
            {
                categories.add(c.getName());
            }

            // Build checkable menu items and pre-check the current category
            for (int i = 0; i < categories.size(); i++)
            {
                String c = categories.get(i);
                int itemId = 200 + i;

                MenuItem mi = menu.add(GROUP_FILTER, itemId, i, c);
                mi.setCheckable(true);

                // Pre-check the currently active category when opening the popup
                if (c.equals(selectedCategory))
                {
                    mi.setChecked(true);
                }
            }

            // Enforce single selection within the group
            menu.setGroupCheckable(GROUP_FILTER, true, true);

            popup.setOnMenuItemClickListener(item -> {
                // Update state and mark selected item checked
                selectedCategory = item.getTitle().toString();
                item.setChecked(true);

                // Update the button label to reflect the chosen category
                btnFilter.setText("Filtering by Category (" + selectedCategory + ")");

                // Refresh list using the current sort + filter state
                loadExpenses();
                return true;
            });

            popup.show();
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadExpenses();
    }

    // Implementing the callback from Adapter (Suggestion #13)
    @Override
    public void onDeleteClick(Expense expense, int position)
    {
        boolean success = expenseService.deleteExpense(expense);
        if (success)
        {
            Toast.makeText(this, "Deleted: " + expense.getName(), Toast.LENGTH_SHORT).show();
            loadExpenses();
        }
        else
        {
            Toast.makeText(this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExpenses()
    {
        // Fetch data based on the selected sort and filter state (either from initialization or Popup)
        final List<Expense> data = expenseService.getExpensesByCategorySortedByDate(selectedCategory, isNewestFirst);

        // Update existing adapter instead of creating a new one (Suggestion #14)
        if (adapter != null)
        {
            adapter.setExpenses(data);
        }
    }
}