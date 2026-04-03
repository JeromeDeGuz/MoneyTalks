package com.bugbytes.moneytalks.ui;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.presentation.SettingsActivity;
import com.google.android.material.card.MaterialCardView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SettingsThemeUiTest
{
    private static final String PREFS_NAME = "moneytalks_prefs";
    private static final String KEY_THEME = "theme_mode";

    @Before
    public void setup()
    {
        Context context = ApplicationProvider.getApplicationContext();

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @After
    public void tearDown()
    {
        Context context = ApplicationProvider.getApplicationContext();

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Test
    public void clickDarkMode_savesDarkTheme_andHighlightsDarkCard()
    {
        try (ActivityScenario<SettingsActivity> scenario =
                     ActivityScenario.launch(SettingsActivity.class))
        {
            scenario.onActivity(activity ->
            {
                activity.findViewById(R.id.btnDarkMode).performClick();
            });

            Context context = ApplicationProvider.getApplicationContext();
            SharedPreferences prefs =
                    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            assertEquals(
                    AppCompatDelegate.MODE_NIGHT_YES,
                    prefs.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO)
            );

            assertEquals(
                    AppCompatDelegate.MODE_NIGHT_YES,
                    AppCompatDelegate.getDefaultNightMode()
            );

            scenario.onActivity(activity ->
            {
                MaterialCardView darkCard = activity.findViewById(R.id.cardDarkMode);
                MaterialCardView lightCard = activity.findViewById(R.id.cardLightMode);

                assertEquals(4, darkCard.getStrokeWidth());
                assertEquals(0, lightCard.getStrokeWidth());
            });
        }
    }

    @Test
    public void clickLightMode_savesLightTheme_andHighlightsLightCard()
    {
        Context context = ApplicationProvider.getApplicationContext();
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_YES)
                .commit();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        try (ActivityScenario<SettingsActivity> scenario =
                     ActivityScenario.launch(SettingsActivity.class))
        {
            scenario.onActivity(activity ->
            {
                activity.findViewById(R.id.btnLightMode).performClick();
            });

            SharedPreferences prefs =
                    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            assertEquals(
                    AppCompatDelegate.MODE_NIGHT_NO,
                    prefs.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_YES)
            );

            assertEquals(
                    AppCompatDelegate.MODE_NIGHT_NO,
                    AppCompatDelegate.getDefaultNightMode()
            );

            scenario.onActivity(activity ->
            {
                MaterialCardView darkCard = activity.findViewById(R.id.cardDarkMode);
                MaterialCardView lightCard = activity.findViewById(R.id.cardLightMode);

                assertEquals(0, darkCard.getStrokeWidth());
                assertEquals(4, lightCard.getStrokeWidth());
            });
        }
    }
}