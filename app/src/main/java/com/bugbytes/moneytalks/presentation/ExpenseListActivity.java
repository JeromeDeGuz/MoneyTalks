package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.bugbytes.moneytalks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//Main screen that displays all expenses in a list
public class ExpenseListActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseEventListener
{
    private RecyclerView recyclerView;
    private ExpenseService expenseService;
    private ExpenseAdapter adapter; // Instance kept for reuse (Suggestion #14)

    // Dropdown for sorting functionality
    private Spinner sortSpinner;
    private static final String[] SORT_OPTIONS = {"Newest to Oldest", "Oldest to Newest"};

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

        // Setup the sorting spinner to allow immediate list updates
        setupSortSpinner();

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
    }

    // Configures the dropdown and its listener for "foran" (instant) updates
    private void setupSortSpinner()
    {
        sortSpinner = findViewById(R.id.sortSpinner);
        if (sortSpinner != null)
        {
            ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, SORT_OPTIONS);
            sortSpinner.setAdapter(sortAdapter);

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    // Logic: Position 0 is Newest First, Position 1 is Oldest First
                    loadExpenses();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
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
            loadExpenses(); // Refresh list after deletion
        }
        else
        {
            Toast.makeText(this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExpenses()
    {
        // Fetch data based on the selected sort option from the Spinner
        boolean newestFirst = (sortSpinner != null && sortSpinner.getSelectedItemPosition() == 0);
        final List<Expense> data = expenseService.getExpensesSortedByDate(newestFirst);

        // Update existing adapter instead of creating a new one (Suggestion #14)
        if (adapter != null)
        {
            adapter.setExpenses(data);
        }
    }
}