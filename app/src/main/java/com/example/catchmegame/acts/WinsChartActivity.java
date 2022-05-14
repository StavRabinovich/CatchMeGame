package com.example.catchmegame.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.example.catchmegame.CallBack_MapFocus;
import com.example.catchmegame.R;
import com.example.catchmegame.frags.ListFragment;
import com.example.catchmegame.frags.MapFragment;
import com.example.catchmegame.winning.WinnersChart;
import com.google.android.material.button.MaterialButton;

public class WinsChartActivity extends AppCompatActivity {
    private MaterialButton wins_BTN_playAgain;
    private MaterialButton wins_BTN_backToStart;
    private ListFragment fragment_list;
    private MapFragment fragment_map;

    private double latitude;
    private double longitude;
    private WinnersChart winnersChart = new WinnersChart();
    private int newScore;
    private String gameType;
    private MediaPlayer highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wins_chart);

        findViews();
        //get last score and location
        highScore = MediaPlayer.create(WinsChartActivity.this,R.raw.btnSound);
        newScore = 0;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newScore = 0;
                latitude = 0;
                longitude = 0;
                gameType = "";
                wins_BTN_playAgain.setText("Back To Menu");
            } else {
                newScore = extras.getInt("score");
                latitude = extras.getDouble("latitude");
                longitude = extras.getDouble("longitude");
                gameType = extras.getString("type");
            }
        } else {
            newScore = (int) savedInstanceState.getSerializable("score");
            latitude = (double) savedInstanceState.getSerializable("latitude");
            longitude = (double) savedInstanceState.getSerializable("longitude");
            gameType = (String) savedInstanceState.getSerializable("type");
        }
        Log.d("ccc", "new score" + String.valueOf(newScore));
        //check score
        if(winnersChart.isTopTen(newScore, latitude, longitude)){
            highScore.start();
        }

        // Map
        fragment_map = new MapFragment();
        fragment_map.setList(winnersChart.getArray());
        getSupportFragmentManager().beginTransaction().add(R.id.wins_FRM_map, fragment_map).commit();

        // List
        fragment_list = new ListFragment();
        fragment_list.setList(winnersChart.getArray());
        fragment_list.setCallBack_mapFocus(callBack_mapFocus);
        getSupportFragmentManager().beginTransaction().add(R.id.wins_FRM_list, fragment_list).commit();
        ListFragment fragment_list = new ListFragment();

        // Buttons
        wins_BTN_playAgain.setOnClickListener(view -> playAgainButtons());
        wins_BTN_backToStart.setOnClickListener(view -> gotoStartButton());
    }

    private CallBack_MapFocus callBack_mapFocus = new CallBack_MapFocus() {
        @Override
        public void focusMap(double latitude, double longitude) {
            fragment_map.gotoLocation(latitude, longitude);
        }
    };

    private void playAgainButtons() {
        Intent intent;
        if (gameType.equals("")) {
            intent = new Intent(WinsChartActivity.this, MenuActivity.class);
        } else {
            intent = new Intent(WinsChartActivity.this, MainActivity.class);
            intent.putExtra("type", gameType);
        }
        startActivity(intent);
        finish();
    }

    private void gotoStartButton(){
        Intent intent;
        if (gameType.equals("")) {
            intent = new Intent(WinsChartActivity.this, MenuActivity.class);
        } else {
            intent = new Intent(WinsChartActivity.this, MainActivity.class);
            intent.putExtra("type", gameType);
        }
        startActivity(intent);
        finish();
    }

    private void findViews() {
        wins_BTN_playAgain = findViewById(R.id.wins_BTN_playAgain);
        wins_BTN_backToStart = findViewById(R.id.wins_BTN_backToStart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ctch", "start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        winnersChart.saveArray();
    }



}