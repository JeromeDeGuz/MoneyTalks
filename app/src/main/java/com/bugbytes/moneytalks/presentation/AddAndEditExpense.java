package com.bugbytes.moneytalks.presentation;

import androidx.appcompat.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Category;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.R;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private ImageButton btnAddCategory;

    private List<String> categoryNames;
    private ArrayAdapter<String> categoryAdapter;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private boolean isEditMode = false;
    private Expense expenseToEdit = null;

    //onCreate: It initializes the UI components and sets up the expense mode. Takes in @param savedInstanceState.
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
        btnAddCategory = findViewById(R.id.btnAddCategory);

        MoneyTalksApp app = (MoneyTalksApp) getApplication();
        loadCategories(app);

        etDate.setOnClickListener(v -> showDatePicker());
        btnCancel.setOnClickListener(v -> finish());
        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog(app));

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

    //loadCategories: It retrieves category data and populates the adapter. Takes in @param app.
    private void loadCategories(MoneyTalksApp app)
    {
        List<Category> categories = app.getCategoryService().getAllCategories();
        categoryNames = new ArrayList<>();
        for (Category c : categories)
        {
            categoryNames.add(c.getName());
        }

        categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categoryNames);
        autoCompleteCategory.setAdapter(categoryAdapter);
    }

    //showAddCategoryDialog: It manages the dialog for on-the-fly category creation. Takes in @param app.
    private void showAddCategoryDialog(MoneyTalksApp app)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Category");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v ->
        {
            String name = input.getText().toString().trim();
            try
            {
                app.getCategoryService().addCategory(new Category(name));
                loadCategories(app);
                autoCompleteCategory.setText(name, false);
                dialog.dismiss();
            }
            catch (ValidationException | IllegalArgumentException e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //fillFields: It populates form fields with existing data for editing. Takes in @param e.
    private void fillFields(Expense e)
    {
        etExpenseName.setText(e.getName());
        etAmount.setText(e.getAmount().toPlainString());
        etDate.setText(e.getDate().format(DATE_FORMATTER));
        etNotes.setText(e.getNote());

        autoCompleteCategory.setText(e.getCategory(), false);
    }

    //showDatePicker: It displays a calendar dialog to select an expense date. Takes in nothing.
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

    //saveOrUpdateExpense: It gathers input and delegates saving logic to the service layer. Takes in nothing.
    private void saveOrUpdateExpense() {
        try {
            if (!isEditMode) {
                saveExpense();
            } else {
                updateExpense();
            }
            finish();
        } catch (ValidationException | IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveExpense() throws ValidationException {
        MoneyTalksApp app = (MoneyTalksApp) getApplication();
        app.getExpenseService().addExpense(
                etExpenseName.getText().toString().trim(),
                etAmount.getText().toString().trim(),
                autoCompleteCategory.getText().toString().trim(),
                etDate.getText().toString().trim(),
                etNotes.getText().toString().trim()
        );
        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateExpense() throws ValidationException {
        MoneyTalksApp app = (MoneyTalksApp) getApplication();
        app.getExpenseService().updateExpense(
                expenseToEdit.getId(),
                etExpenseName.getText().toString().trim(),
                etAmount.getText().toString().trim(),
                autoCompleteCategory.getText().toString().trim(),
                etDate.getText().toString().trim(),
                etNotes.getText().toString().trim()
        );
        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
    }
}