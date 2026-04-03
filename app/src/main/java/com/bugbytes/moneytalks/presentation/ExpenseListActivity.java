package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

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

public class ExpenseListActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseEventListener
{
    private RecyclerView recyclerView;
    private ExpenseService expenseService;
    private ExpenseAdapter adapter;
    private Button btnSort;
    private Button btnFilter;
    private String selectedCategory = "All";
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

        setupSortButton();
        setupFilterButton();
        setupBottomNavigation();

        recyclerView = findViewById(R.id.rvExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExpenseAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        final FloatingActionButton btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v ->
        {
            Intent intent = new Intent(ExpenseListActivity.this, AddAndEditExpense.class);
            startActivity(intent);
        });
    }

    //setupBottomNavigation: Configures the custom bottom nav bar.
    private void setupBottomNavigation()
    {
        findViewById(R.id.nav_home).setOnClickListener(v ->
        {
            // already home, do nothing
        });

        findViewById(R.id.nav_budget).setOnClickListener(v ->
                startActivity(new Intent(this, BudgetActivity.class)));

        findViewById(R.id.nav_settings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    //setupSortButton: It configures the sorting button and its popup menu for date-based ordering.
    private void setupSortButton()
    {
        btnSort = findViewById(R.id.btnSort);
        if (btnSort == null) return;

        btnSort.setOnClickListener(v ->
        {
            PopupMenu popup = new PopupMenu(ExpenseListActivity.this, btnSort);
            Menu menu = popup.getMenu();

            final int GROUP_SORT = 1;
            final int ID_NEWEST = 101;
            final int ID_OLDEST = 102;

            MenuItem newestItem = menu.add(GROUP_SORT, ID_NEWEST, 0, "Newest to Oldest");
            MenuItem oldestItem = menu.add(GROUP_SORT, ID_OLDEST, 1, "Oldest to Newest");

            newestItem.setChecked(isNewestFirst);
            oldestItem.setChecked(!isNewestFirst);
            newestItem.setCheckable(true);
            oldestItem.setCheckable(true);
            menu.setGroupCheckable(GROUP_SORT, true, true);

            popup.setOnMenuItemClickListener(item ->
            {
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
                loadExpenses();
                return true;
            });

            popup.show();
        });
    }

    //setupFilterButton: It configures the category filter button and dynamically builds its popup menu.
    private void setupFilterButton()
    {
        btnFilter = findViewById(R.id.btnFilter);
        if (btnFilter == null) return;

        btnFilter.setText("Filtering by Category (All)");

        btnFilter.setOnClickListener(v ->
        {
            PopupMenu popup = new PopupMenu(ExpenseListActivity.this, btnFilter);
            Menu menu = popup.getMenu();

            final int GROUP_FILTER = 2;
            final MoneyTalksApp app = (MoneyTalksApp) getApplication();
            List<Category> dbCategories = app.getCategoryService().getAllCategories();

            List<String> categories = new ArrayList<>();
            categories.add("All");
            for (Category c : dbCategories) categories.add(c.getName());

            for (int i = 0; i < categories.size(); i++)
            {
                String c = categories.get(i);
                MenuItem mi = menu.add(GROUP_FILTER, 200 + i, i, c);
                mi.setCheckable(true);
                if (c.equals(selectedCategory)) mi.setChecked(true);
            }

            menu.setGroupCheckable(GROUP_FILTER, true, true);

            popup.setOnMenuItemClickListener(item ->
            {
                selectedCategory = item.getTitle().toString();
                item.setChecked(true);
                btnFilter.setText("Filtering by Category (" + selectedCategory + ")");
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

    @Override
    public void onDeleteClick(Expense expense, int position)
    {
        try
        {
            expenseService.deleteExpense(expense);
            Toast.makeText(this, "Deleted: " + expense.getName(), Toast.LENGTH_SHORT).show();
            loadExpenses();
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExpenses()
    {
        final List<Expense> data = expenseService.getExpensesByCategorySortedByDate(selectedCategory, isNewestFirst);
        if (adapter != null) adapter.setExpenses(data);
    }
}