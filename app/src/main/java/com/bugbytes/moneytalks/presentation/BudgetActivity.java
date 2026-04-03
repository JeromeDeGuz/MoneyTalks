package com.bugbytes.moneytalks.presentation;

import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.services.BudgetService;
import com.bugbytes.moneytalks.models.BudgetSummary;

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

    //onCreate: Sets up the activity layout, initializes the budget service, and prepares the RecyclerView and controls. @param savedInstanceState Stores the previous activity state if available.
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

    //onResume: Reloads the budget summaries whenever the activity becomes visible again.
    @Override
    protected void onResume()
    {
        super.onResume();
        loadBudgetSummaries();
    }

    //setupYearMonthSpinners: Creates and initializes the year and month spinners, then updates the selected values when the user changes them.
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

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(resolveThemeColor(com.google.android.material.R.attr.colorOnSurface));
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(resolveThemeColor(com.google.android.material.R.attr.colorOnSurface));
                return tv;
            }
        };
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(resolveThemeColor(com.google.android.material.R.attr.colorOnSurface));
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(resolveThemeColor(com.google.android.material.R.attr.colorOnSurface));
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

    //resolveThemeColor: Returns the color value for the given theme attribute. @param attr The theme color attribute to resolve.
    private int resolveThemeColor(int attr)
    {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    //loadBudgetSummaries: Retrieves the budget summaries for the selected year and month, then displays them in the RecyclerView.
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

    //showEditBudgetDialog: Opens a dialog for editing the selected category budget and saves the new value. @param budgetSummary The budget summary selected by the user.
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