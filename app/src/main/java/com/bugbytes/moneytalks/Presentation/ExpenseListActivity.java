package com.bugbytes.moneytalks.Presentation;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);

        // Insets (requires a view with id "main" in activity_expense_list.xml)
        View main = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView (requires RecyclerView with id "rvExpenses" in activity_expense_list.xml)
        RecyclerView rv = findViewById(R.id.rvExpenses);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Fake data for testing (matches your Expense constructor)
        List<Expense> data = new ArrayList<>();
        data.add(new Expense(1, 5.50, "Food", "2026-02-05", "Coffee"));
        data.add(new Expense(2, 3.00, "Transport", "2026-02-05", "Bus"));
        data.add(new Expense(3, 12.99, "Shopping", "2026-02-04", "Snacks"));

        // Adapter
        rv.setAdapter(new ExpenseAdapter(data));
    }
}
