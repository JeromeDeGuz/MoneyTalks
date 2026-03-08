package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        final List<Expense> data = expenseService.getAllExpenses();
        // Update existing adapter instead of creating a new one (Suggestion #14)
        if (adapter != null)
        {
            adapter.setExpenses(data);
        }
    }
}