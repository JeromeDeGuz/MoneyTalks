package com.bugbytes.moneytalks.Presentation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View; // ADDED: Required to fix the "cannot find symbol class View" error
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Edge-to-edge padding
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize views
        EditText etExpenseName = findViewById(R.id.etExpenseName);
        EditText etAmount = findViewById(R.id.etAmount);
        EditText etDate = findViewById(R.id.etDate);
        EditText etNotes = findViewById(R.id.etNotes);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnSave = findViewById(R.id.btnSave);

        // Spinner setup
        String[] categories = {"Food", "Transport", "Shopping", "Bills", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Date picker
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) -> {
                etDate.setText(String.format("%d/%d/%d", d, m + 1, y));
            }, year, month, day);
            datePicker.show();
        });

        // Save button logic
        btnSave.setOnClickListener(v -> {
            String name = etExpenseName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString(); // This gets "Food", "Transport", etc.
            String date = etDate.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (name.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Expense newExpense = new Expense(0, name, amount, category, date, notes);

                // --- THE FIX STARTS HERE ---
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_expense", newExpense); // Wrap the object
                setResult(RESULT_OK, resultIntent);              // Hand it to the previous screen
                // --- THE FIX ENDS HERE ---

                Toast.makeText(this, "Saved: " + newExpense.getName(), Toast.LENGTH_SHORT).show();
                finish();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });
    }
}