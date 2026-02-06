package com.bugbytes.moneytalks.Presentation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bugbytes.moneytalks.R;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        // Save button
        btnSave.setOnClickListener(v -> {
            String name = etExpenseName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String date = etDate.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (name.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, String.format("Expense saved: %s - $%.2f", name, amount), Toast.LENGTH_SHORT).show();

            // Clear fields
            etExpenseName.setText("");
            etAmount.setText("");
            etDate.setText("");
            etNotes.setText("");
            spinnerCategory.setSelection(0);
        });
    }
}
