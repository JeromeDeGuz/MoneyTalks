package com.bugbytes.moneytalks.Presentation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView; // Updated Import
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bugbytes.moneytalks.Application.MoneyTalksApp;
import com.bugbytes.moneytalks.Business.Validation.ExpenseValidationException;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.Calendar;

// Screen that allows the user to add new expense.
public class AddExpense extends AppCompatActivity {
    // UI references
    private EditText etExpenseName;
    private EditText etAmount;
    private EditText etDate;
    private EditText etNotes;
    private AutoCompleteTextView autoCompleteCategory; // Fixed: Changed from Spinner
    private Button btnSave;
    private Button btnCancel;

    // Categories for dropdown
    private static final String[] CATEGORIES = {"Food", "Transport", "Shopping", "Bills", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Initialize UI elements
        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etNotes = findViewById(R.id.etNotes);
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory); // Fixed: Matches new XML ID
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Setup category dropdown (Material style)
        // Fixed: Use simple_dropdown_item_1line for AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        autoCompleteCategory.setAdapter(adapter);

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveExpense());
    }

    // Shows a date picker dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) ->
                etDate.setText(String.format("%d/%d/%d", d, m + 1, y)), year, month, day);

        datePicker.show();
    }

    // Saves a new expense
    private void saveExpense() {
        final String name = etExpenseName.getText().toString().trim();
        final String amountStr = etAmount.getText().toString().trim();

        // Fixed: For AutoCompleteTextView, get text directly instead of getSelectedItem()
        final String category = autoCompleteCategory.getText().toString().trim();

        final String date = etDate.getText().toString().trim();
        final String notes = etNotes.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty() || date.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            final double amount = Double.parseDouble(amountStr);
            Expense newExpense = new Expense(name, amount, category, date, notes);

            MoneyTalksApp app = (MoneyTalksApp) getApplication();
            app.getExpenseService().addExpense(newExpense);

            Toast.makeText(this, "Saved: " + newExpense.getName(), Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
        } catch (ExpenseValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}