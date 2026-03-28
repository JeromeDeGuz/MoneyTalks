package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

import com.bugbytes.moneytalks.R;

public class SettingsActivity extends AppCompatActivity
{
    //onCreate: It initializes the settings layout and sets up navigation listeners. Takes in @param savedInstanceState.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout btnCategorySettings = findViewById(R.id.btnCategorySettings);
        LinearLayout btnLightMode = findViewById(R.id.btnLightMode);
        LinearLayout btnDarkMode = findViewById(R.id.btnDarkMode);
        Button btnBackSettings = findViewById(R.id.btnBackSettings);

        // NEW: get card references for active indicator
        com.google.android.material.card.MaterialCardView cardLightMode = findViewById(R.id.cardLightMode);
        com.google.android.material.card.MaterialCardView cardDarkMode = findViewById(R.id.cardDarkMode);

        btnCategorySettings.setOnClickListener(v ->
        {
            Intent intent = new Intent(SettingsActivity.this, ManageCategoriesActivity.class);
            startActivity(intent);
        });

        btnLightMode.setOnClickListener(v ->
        {
            applyTheme(AppCompatDelegate.MODE_NIGHT_NO);
            updateActiveIndicator(cardLightMode, cardDarkMode, AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnDarkMode.setOnClickListener(v ->
        {
            applyTheme(AppCompatDelegate.MODE_NIGHT_YES);
            updateActiveIndicator(cardLightMode, cardDarkMode, AppCompatDelegate.MODE_NIGHT_YES);
        });

        // NEW: show which mode is currently active when screen opens
        SharedPreferences prefs = getSharedPreferences("moneytalks_prefs", MODE_PRIVATE);
        int currentMode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        updateActiveIndicator(cardLightMode, cardDarkMode, currentMode);

        btnBackSettings.setOnClickListener(v -> finish());
    }

    // NEW: saves the theme preference
    private void applyTheme(int mode)
    {
        AppCompatDelegate.setDefaultNightMode(mode);
        getSharedPreferences("moneytalks_prefs", MODE_PRIVATE)
                .edit()
                .putInt("theme_mode", mode)
                .apply();
    }

    // NEW: highlights the active card with a purple border
    private void updateActiveIndicator(
            com.google.android.material.card.MaterialCardView cardLight,
            com.google.android.material.card.MaterialCardView cardDark,
            int currentMode)
    {
        int activeStroke = 4;
        int inactiveStroke = 0;

        cardLight.setStrokeWidth(currentMode == AppCompatDelegate.MODE_NIGHT_NO ? activeStroke : inactiveStroke);
        cardDark.setStrokeWidth(currentMode == AppCompatDelegate.MODE_NIGHT_YES ? activeStroke : inactiveStroke);
    }
}