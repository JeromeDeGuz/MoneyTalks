package com.bugbytes.moneytalks.presentation;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.models.BudgetSummary;
import com.bugbytes.moneytalks.business.services.BudgetService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity
{
    private BudgetService budgetService;
    private BudgetAdapter adapter;

    private Spinner spinnerYear;
    private Spinner spinnerMonth;

    private int selectedYear;
    private int selectedMonth;

    //onCreate: It sets up the activity layout, initializes the budget service, and prepares the RecyclerView and controls. Takes in @param savedInstanceState.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        final MoneyTalksApp app = (MoneyTalksApp) getApplication();
        budgetService = app.getBudgetService();

        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerMonth = findViewById(R.id.spinnerMonth);

        RecyclerView rvBudgetList = findViewById(R.id.rvBudgetList);
        rvBudgetList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BudgetAdapter(new ArrayList<>(), this::showEditBudgetDialog);
        rvBudgetList.setAdapter(adapter);

        Button btnBackBudget = findViewById(R.id.btnBackBudget);
        btnBackBudget.setOnClickListener(v -> finish());

        setupYearMonthSpinners();
    }

    //onResume: It refreshes the monthly budget data whenever the activity returns to the foreground. Takes in nothing.
    @Override
    protected void onResume()
    {
        super.onResume();
        loadBudgetSummaries();
    }

    //setupYearMonthSpinners: It sets the default selected year and month to the current date and reloads data when the selection changes. Takes in nothing.
    private void setupYearMonthSpinners()
    {
        LocalDate today = LocalDate.now();
        selectedYear = today.getYear();
        selectedMonth = today.getMonthValue();

        List<Integer> years = new ArrayList<>();
        for (int year = selectedYear - 5; year <= selectedYear + 5; year++)
        {
            years.add(year);
        }

        List<String> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++)
        {
            months.add(String.valueOf(month));
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.widget.TextView tv = (android.widget.TextView) super.getView(position, convertView, parent);
                int[] attrs = { android.R.attr.textColorPrimary };
                android.content.res.TypedArray ta = getContext().obtainStyledAttributes(attrs);
                tv.setTextColor(ta.getColor(0, android.graphics.Color.BLACK));
                ta.recycle();
                return tv;
            }
        };
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.widget.TextView tv = (android.widget.TextView) super.getView(position, convertView, parent);
                int[] attrs = { android.R.attr.textColorPrimary };
                android.content.res.TypedArray ta = getContext().obtainStyledAttributes(attrs);
                tv.setTextColor(ta.getColor(0, android.graphics.Color.BLACK));
                ta.recycle();
                return tv;
            }
        };
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        spinnerYear.setSelection(years.indexOf(selectedYear));
        spinnerMonth.setSelection(selectedMonth - 1);

        spinnerYear.setOnItemSelectedListener(new SimpleItemSelectedListener(position ->
        {
            selectedYear = years.get(position);
            loadBudgetSummaries();
        }));

        spinnerMonth.setOnItemSelectedListener(new SimpleItemSelectedListener(position ->
        {
            selectedMonth = position + 1;
            loadBudgetSummaries();
        }));
    }

    //loadBudgetSummaries: It retrieves the budget summaries for the selected year and month and updates the adapter. Takes in nothing.
    private void loadBudgetSummaries()
    {
        try
        {
            List<BudgetSummary> summaries = budgetService.getMonthlyBudgetSummary(selectedYear, selectedMonth);
            adapter.setBudgetSummaries(summaries);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load budget data", Toast.LENGTH_SHORT).show();
        }
    }

    //showEditBudgetDialog: It shows a dialog that lets the user update only the budget value for one category. Takes in @param budgetSummary.
    private void showEditBudgetDialog(BudgetSummary budgetSummary)
    {
        final EditText budgetInput = new EditText(this);
        budgetInput.setText(budgetSummary.getBudget().toPlainString());
        budgetInput.setSelection(budgetInput.getText().length());
        budgetInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("Edit Budget")
                .setView(budgetInput)
                .setPositiveButton("Save", (dialog, which) ->
                {
                    String budgetText = budgetInput.getText().toString().trim();

                    if (budgetText.isEmpty())
                    {
                        Toast.makeText(this, "Budget cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try
                    {
                        BigDecimal newBudget = new BigDecimal(budgetText);
                        budgetService.updateCategoryBudget(budgetSummary.getCategoryName(), newBudget);
                        Toast.makeText(this, "Budget updated", Toast.LENGTH_SHORT).show();
                        loadBudgetSummaries();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Failed to update budget", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}