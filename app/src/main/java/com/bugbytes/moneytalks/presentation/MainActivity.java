package com.bugbytes.moneytalks.presentation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.services.ItemService;
import com.bugbytes.moneytalks.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ItemService itemService;

    private EditText titleInput;
    private EditText descInput;
    private EditText dateInput;
    private EditText priceInput;


    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemService = ((MoneyTalksApp) getApplication()).getItemService();

        titleInput = findViewById(R.id.titleInput);
        descInput = findViewById(R.id.descInput);
        dateInput = findViewById(R.id.dateInput);
        priceInput = findViewById(R.id.editTextNumberDecimal4);

        setupDatePicker();

        Button addButton = findViewById(R.id.addButton);

        RecyclerView list = findViewById(R.id.todoList);
        adapter = new TodoAdapter(new ArrayList<>());
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            try {
                itemService.addItem(
                        titleInput.getText().toString(),
                        descInput.getText().toString()
                );

                clearInputs();
                refreshList();
            } catch (com.bugbytes.moneytalks.business.validation.ValidationException ex) {
                // simplest: show error on the title field (or a Toast)
                titleInput.setError(ex.getMessage());
                titleInput.requestFocus();
            }
        });

        refreshList();
    }


    private void setupDatePicker() {
        dateInput.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, y, m, d) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(y, m, d);

                        SimpleDateFormat sdf =
                                new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

                        dateInput.setText(sdf.format(selected.getTime()));
                    },
                    year, month, day
            );

            dialog.show();
        });
    }

    private void refreshList() {
        List<Expense> items = itemService.getAllItems();
        adapter.setItems(items);
    }

    private void clearInputs() {
        titleInput.setText("");
        descInput.setText("");
        dateInput.setText("");
        priceInput.setText("");
    }

}
