package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bugbytes.moneytalks.R;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout btnCategorySettings = findViewById(R.id.btnCategorySettings);
        Button btnBackSettings = findViewById(R.id.btnBackSettings);

        btnCategorySettings.setOnClickListener(v ->
        {
            Intent intent = new Intent(SettingsActivity.this, ManageCategoriesActivity.class);
            startActivity(intent);
        });

        btnBackSettings.setOnClickListener(v -> finish());
    }
}