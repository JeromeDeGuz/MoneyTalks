package com.bugbytes.moneytalks.Persistence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.Application.MoneyTalksApp;
import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

//main screen that displays all expenses in a list
public class ExpenseListActivity extends AppCompatActivity
{


    private RecyclerView rv;
    private ExpenseService expenseService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);

        //get business service from application class first
        MoneyTalksApp app = (MoneyTalksApp) getApplication();
        expenseService = app.getExpenseService();

        View main = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setup RecyclerView
        rv = findViewById(R.id.rvExpenses);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //floating + button that takes to addExpense screen
        FloatingActionButton btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseListActivity.this, AddExpense.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //refresh data from persistence layer every time we return to this screen
        loadExpenses();
    }

    //loads expenses from business layer and updates recyclerView.
    private void loadExpenses()
    {
        ExpenseAdapter adapter;
        List<Expense> data = expenseService.getAllExpenses();
        // The ExpenseService is now passed to the adapter
        adapter = new ExpenseAdapter(data, expenseService);
        rv.setAdapter(adapter);
    }
}