package com.example.catchmegame.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.catchmegame.R;
import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    private MaterialButton menu_BTN_playArrows;
    private MaterialButton menu_BTN_playSensor;
    private MaterialButton menu_BTN_winsChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        menu_BTN_playArrows.setOnClickListener(view -> buttonsGame());
        menu_BTN_playSensor.setOnClickListener(view -> sensorGame());
        menu_BTN_winsChart.setOnClickListener(view -> winsChart());
    }

    private void findViews() {
        menu_BTN_playArrows = findViewById(R.id.menu_BTN_playArrows);
        menu_BTN_playSensor = findViewById(R.id.menu_BTN_playSensor);
        menu_BTN_winsChart = findViewById(R.id.menu_BTN_winsChart);
    }

    private void winsChart() {
        Intent game = new Intent(MenuActivity.this, WinsChartActivity.class);
        startActivity(game);
        finish();
    }

    private void sensorGame() {
        Intent game = new Intent(MenuActivity.this, MainActivity.class);
        game.putExtra("type","sensor");
        startActivity(game);
        finish();
    }
    private void buttonsGame() {
        Intent game = new Intent(MenuActivity.this, MainActivity.class);
        game.putExtra("type", "buttons");
        startActivity(game);
        finish();
    }
}