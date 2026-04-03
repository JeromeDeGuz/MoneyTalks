package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bugbytes.moneytalks.R;

public class SettingsActivity extends AppCompatActivity
{
    private static final String PREFS_NAME = "moneytalks_prefs";
    private static final String KEY_THEME = "theme_mode";

    //onCreate: It initializes the settings layout and sets up navigation listeners. Takes in @param savedInstanceState.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout btnCategorySettings = findViewById(R.id.btnCategorySettings);
        LinearLayout btnBudgetSettings = findViewById(R.id.btnBudgetSettings);
        LinearLayout btnLightMode = findViewById(R.id.btnLightMode);
        LinearLayout btnDarkMode = findViewById(R.id.btnDarkMode);
        Button btnBackSettings = findViewById(R.id.btnBackSettings);

        com.google.android.material.card.MaterialCardView cardLightMode = findViewById(R.id.cardLightMode);
        com.google.android.material.card.MaterialCardView cardDarkMode = findViewById(R.id.cardDarkMode);

        btnCategorySettings.setOnClickListener(v ->
        {
            Intent intent = new Intent(SettingsActivity.this, ManageCategoriesActivity.class);
            startActivity(intent);
        });

        btnBudgetSettings.setOnClickListener(v ->
        {
            Intent intent = new Intent(SettingsActivity.this, BudgetActivity.class);
            startActivity(intent);
        });

        btnLightMode.setOnClickListener(v ->
        {
            saveTheme(AppCompatDelegate.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            updateActiveIndicator(cardLightMode, cardDarkMode, AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnDarkMode.setOnClickListener(v ->
        {
            saveTheme(AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            updateActiveIndicator(cardLightMode, cardDarkMode, AppCompatDelegate.MODE_NIGHT_YES);
        });

        // Show which mode is currently saved
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedMode = prefs.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        updateActiveIndicator(cardLightMode, cardDarkMode, savedMode);

        btnBackSettings.setOnClickListener(v ->
        {
            finish();
        });
    }

    //saveTheme: Saves the theme preference to SharedPreferences. Takes in @param mode. Returns nothing.
    private void saveTheme(int mode)
    {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putInt(KEY_THEME, mode)
                .apply();
    }

    //updateActiveIndicator: Highlights the active card with a border based on current mode. Takes in @param cardLight, cardDark, and currentMode.
    private void updateActiveIndicator(
            com.google.android.material.card.MaterialCardView cardLight,
            com.google.android.material.card.MaterialCardView cardDark,
            int currentMode)
    {
        cardLight.setStrokeWidth(currentMode == AppCompatDelegate.MODE_NIGHT_NO ? 4 : 0);
        cardDark.setStrokeWidth(currentMode == AppCompatDelegate.MODE_NIGHT_YES ? 4 : 0);
    }
}