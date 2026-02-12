package com.bugbytes.moneytalks.Presentation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bugbytes.moneytalks.Application.MoneyTalksApp;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.Calendar;

//screen that allows the user to add new expense.
public class AddExpense extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //connect to xml layout, built on laurens sample
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        View mainView = findViewById(R.id.layout_add_expense);
        if (mainView != null)
        {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) ->
            {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        //get references to UI elements
        EditText etExpenseName = findViewById(R.id.etExpenseName);
        EditText etAmount = findViewById(R.id.etAmount);
        EditText etDate = findViewById(R.id.etDate);
        EditText etNotes = findViewById(R.id.etNotes);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        //set up category dropdown (spinner)
        //do we still want to keep it in i1 or move it to i2 with more details to it that we discussed?
        String[] categories = {"Food", "Transport", "Shopping", "Bills", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        //date picker logic
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

        //cancel button logic
        btnCancel.setOnClickListener(v -> finish());

        //save button logic
        btnSave.setOnClickListener(v ->
        {
            //take in user input
            String name = etExpenseName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String date = etDate.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            //should be in business layer? but this is not logic validation just empty thing. Discuss with Ta.
            if (name.isEmpty() && amountStr.isEmpty() && date.isEmpty())
            {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try
            {
                // 2. Prepare amount (defaults to 0 if empty so the Business Layer can catch it)
                double amount = 0;
                if (!amountStr.isEmpty()) {
                    amount = Double.parseDouble(amountStr);
                }

                //create new exp object (Note: id will be handled by repo)
                Expense newExpense = new Expense(name, amount, category, date, notes);

                //accessing business Layer via application class
                MoneyTalksApp app = (MoneyTalksApp) getApplication();
                app.getExpenseService().addExpense(newExpense);

                //confirmation msg
                Toast.makeText(this, "Saved: " + newExpense.getName(), Toast.LENGTH_SHORT).show();

                //close activity and return to list
                finish();

            }
            catch (NumberFormatException e)
            {
                // Handles invalid number strings (like text in amount field)
                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            }
            catch (IllegalArgumentException e)
            {
                // THIS prints your specific rules from ExpenseServiceImpl
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}