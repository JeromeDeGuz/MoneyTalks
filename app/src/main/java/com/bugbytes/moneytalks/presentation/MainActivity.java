package com.bugbytes.moneytalks.presentation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.services.ItemService;
import com.bugbytes.moneytalks.models.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ItemService itemService;

    private EditText titleInput;
    private EditText descInput;

    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemService = ((MoneyTalksApp) getApplication()).getItemService();

        titleInput = findViewById(R.id.titleInput);
        descInput = findViewById(R.id.descInput);

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

    private void refreshList() {
        List<Item> items = itemService.getAllItems();
        adapter.setItems(items);
    }

    private void clearInputs() {
        titleInput.setText("");
        descInput.setText("");
    }

}
