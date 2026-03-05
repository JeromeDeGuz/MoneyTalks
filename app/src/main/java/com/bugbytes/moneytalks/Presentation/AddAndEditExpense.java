package com.bugbytes.moneytalks.Presentation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bugbytes.moneytalks.Application.MoneyTalksApp;
import com.bugbytes.moneytalks.Business.Validation.ExpenseValidationException;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.Calendar;

// One screen for BOTH adding and editing an expense.
public class AddAndEditExpense extends AppCompatActivity
{
    // Key used to pass an Expense for editing via Intent.
    public static final String EXTRA_EXPENSE = "extra_expense";

    // UI references
    private EditText etExpenseName;
    private EditText etAmount;
    private EditText etDate;
    private EditText etNotes;
    private Spinner spinnerCategory;
    private Button btnSave;
    private Button btnCancel;

    // Categories for spinner
    private static final String[] CATEGORIES = {"Food", "Transport", "Shopping", "Bills", "Other"};

    // Edit mode state
    private boolean isEditMode = false;
    private Expense expenseToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense); // Reuse the same layout

        // Initialize UI elements
        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etNotes = findViewById(R.id.etNotes);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Setup category spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CATEGORIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Detect edit mode: if Intent contains an Expense, we are editing
        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra(EXTRA_EXPENSE);
        if (obj instanceof Expense)
        {
            isEditMode = true;
            expenseToEdit = (Expense) obj;

            setTitle("Edit Expense");
            btnSave.setText("Update");

            // Fill fields with existing data
            fillFields(expenseToEdit);
        }
        else
        {
            setTitle("Add Expense");
            btnSave.setText("Save");
        }

        // Save/Update button
        btnSave.setOnClickListener(v -> saveOrUpdateExpense());
    }

    // Populate UI with an existing expense (Edit mode)
    private void fillFields(Expense e)
    {
        etExpenseName.setText(e.getName());
        etAmount.setText(String.valueOf(e.getAmount()));
        etDate.setText(e.getDate());
        etNotes.setText(e.getNote());

        // Need e.getCategory() to exist in Expense model
        int pos = getCategoryPosition(e.getCategory());
        if (pos >= 0)
        {
            spinnerCategory.setSelection(pos);
        }
    }

    private int getCategoryPosition(String category)
    {
        for (int i = 0; i < CATEGORIES.length; i++)
        {
            if (CATEGORIES[i].equals(category))
            {
                return i;
            }
        }
        return -1;
    }

    // Shows a date picker dialog
    private void showDatePicker()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) ->
                etDate.setText(String.format("%d/%d/%d", d, m + 1, y)), year, month, day);

        datePicker.show();
    }

    // Save in Add mode, Update in Edit mode
    private void saveOrUpdateExpense()
    {
        final String name = etExpenseName.getText().toString().trim();
        final String amountStr = etAmount.getText().toString().trim();
        final String category = spinnerCategory.getSelectedItem().toString();
        final String date = etDate.getText().toString().trim();
        final String notes = etNotes.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty() || date.isEmpty())
        {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            final double amount = Double.parseDouble(amountStr);

            MoneyTalksApp app = (MoneyTalksApp) getApplication();

            if (!isEditMode)
            {
                // ADD mode
                Expense newExpense = new Expense(name, amount, category, date, notes);
                app.getExpenseService().addExpense(newExpense);

                Toast.makeText(this, "Saved: " + newExpense.getName(), Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                // EDIT mode: create updated object, keep the same id
                Expense updated = new Expense(name, amount, category, date, notes);
                updated.setId(expenseToEdit.getId());

                // Requires ExpenseService.updateExpense(...) to exist
                boolean ok = app.getExpenseService().updateExpense(updated);

                if (ok)
                {
                    Toast.makeText(this, "Updated: " + updated.getName(), Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Update failed (expense not found)", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
        }
        catch (ExpenseValidationException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}