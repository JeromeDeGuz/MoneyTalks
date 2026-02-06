package com.bugbytes.moneytalks.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private ExpenseAdapter adapter;
    private List<Expense> data;

    //Launcher to handle the result from AddExpense
    private final ActivityResultLauncher<Intent> addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Retrieve the expense object sent back
                    Expense newExpense = (Expense) result.getData().getSerializableExtra("new_expense");
                    if (newExpense != null) {
                        data.add(newExpense);
                        adapter.notifyItemInserted(data.size() - 1);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);

        View main = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize Floating Action Button
        FloatingActionButton btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseListActivity.this, AddExpense.class);
            addExpenseLauncher.launch(intent); // Use launcher instead of startActivity
        });

        //Setup RecyclerView
        RecyclerView rv = findViewById(R.id.rvExpenses);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //Initialize data and adapter
        data = new ArrayList<>();
        data.add(new Expense(1, "Coffee", 5.50, "Food", "2026-02-05", "Morning coffee"));
        data.add(new Expense(2, "Bus Fare", 3.00, "Transport", "2026-02-05", "Daily commute"));
        data.add(new Expense(3, "Snacks", 12.99, "Shopping", "2026-02-04", "Grocery run"));

        adapter = new ExpenseAdapter(data);
        rv.setAdapter(adapter);
    }
}