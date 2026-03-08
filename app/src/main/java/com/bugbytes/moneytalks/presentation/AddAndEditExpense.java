package com.bugbytes.moneytalks.presentation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ExpenseValidationException;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.R;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddAndEditExpense extends AppCompatActivity
{
    public static final String EXTRA_EXPENSE = "extra_expense";

    private EditText etExpenseName;
    private EditText etAmount;
    private EditText etDate;
    private EditText etNotes;
    private AutoCompleteTextView autoCompleteCategory;
    private Button btnSave;
    private Button btnCancel;

    private static final String[] CATEGORIES = {"Food", "Transport", "Shopping", "Bills", "Other"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private boolean isEditMode = false;
    private Expense expenseToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etNotes = findViewById(R.id.etNotes);
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        autoCompleteCategory.setAdapter(adapter);

        etDate.setOnClickListener(v -> showDatePicker());
        btnCancel.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_EXPENSE))
        {
            Object obj = intent.getSerializableExtra(EXTRA_EXPENSE);
            if (obj instanceof Expense)
            {
                isEditMode = true;
                expenseToEdit = (Expense) obj;

                setTitle("Edit Expense");
                btnSave.setText("Update");

                fillFields(expenseToEdit);
            }
        }
        else
        {
            setTitle("Add Expense");
            btnSave.setText("Save");
        }

        btnSave.setOnClickListener(v -> saveOrUpdateExpense());
    }

    private void fillFields(Expense e)
    {
        etExpenseName.setText(e.getName());
        etAmount.setText(e.getAmount().toPlainString());
        etDate.setText(e.getDate().format(DATE_FORMATTER));
        etNotes.setText(e.getNote());

        autoCompleteCategory.setText(e.getCategory(), false);
    }

    private void showDatePicker()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) ->
        {
            LocalDate selectedDate = LocalDate.of(y, m + 1, d);
            etDate.setText(selectedDate.format(DATE_FORMATTER));
        }, year, month, day);

        datePicker.show();
    }

    private void saveOrUpdateExpense()
    {
        final String name = etExpenseName.getText().toString().trim();
        final String amountStr = etAmount.getText().toString().trim();
        final String category = autoCompleteCategory.getText().toString().trim();
        final String dateStr = etDate.getText().toString().trim();
        final String notes = etNotes.getText().toString().trim();

        try
        {
            if (amountStr.isEmpty() || dateStr.isEmpty())
            {
                throw new ExpenseValidationException("Amount and Date are required.");
            }

            final BigDecimal amount = new BigDecimal(amountStr);
            final LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            MoneyTalksApp app = (MoneyTalksApp) getApplication();

            if (!isEditMode)
            {
                Expense newExpense = new Expense(0, name, amount, category, date, notes);
                app.getExpenseService().addExpense(newExpense);

                Toast.makeText(this, "Saved: " + newExpense.getName(), Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Expense updated = new Expense(expenseToEdit.getId(), name, amount, category, date, notes);
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
        } catch (NumberFormatException e)
        {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
        } catch (ExpenseValidationException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e)
        {
            Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}