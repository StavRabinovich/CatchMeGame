package com.example.catchmegame.acts;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.catchmegame.Direction;
import com.example.catchmegame.R;
import com.example.catchmegame.GameManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textview.MaterialTextView;


public class MainActivity extends AppCompatActivity {
    GameManager gm;
    private ImageView[] game_IMG_hearts;
    private ImageView[] game_IMG_board;
    private ImageButton[] game_BTN_arrows;
    private MaterialTextView game_LBL_score;
    private LinearLayout game_LAY_arrows;

    // Timer
    private CountDownTimer timer;
    private boolean isTimerRunning = false;

    // Location
    private LocationManager locationManager;
    private Location location;
    private float LOCATION_REFRESH_DISTANCE = 100;
    private long LOCATION_REFRESH_TIME = 15000;
    private double latitude;
    private double longitude;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    // Sensor
    private SensorManager sensorManager;
    private Sensor accSensor;
    private SensorEventListener sensorListener;
    private String gameType;

    // Sound
    MediaPlayer soundCoinCatch; // ItemCatch
    MediaPlayer soundCatCall; // catchCat

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAll();
//        buttonPressing();
//        scoreTimer();
    }

    private void initAll(){
        findViews();
        // Sound Init
        soundCoinCatch = MediaPlayer.create(MainActivity.this, R.raw.coin_collected);
        soundCatCall = MediaPlayer.create(MainActivity.this, R.raw.catcall);

        // Game type init
        gameType = getIntent().getExtras().getString("type");
        if (gameType.equals("buttons")) {
            buttonPressing();
        } else if (gameType.equals("sensor")) {
            initSensors();
        }

        // Timer
        timer = new CountDownTimer(1000000, 1000) {
            @Override
            public void onTick(long l) {
                updateUI();
            }
            @Override
            public void onFinish() {
            }
        };
        timer.start();
        isTimerRunning = true;
    }

    /*  final Handler handler = new Handler();
    final int DELAY = 1000; // 1sec
    Runnable r = new Runnable() {
        public void run() {
            if (gm.getLife() > 0){
                handler.postDelayed(this, DELAY);
            }
            else{
                finishGame();
            }
            updateUI();
        }
    };*/



    // Inits
    private void findViews(){
        gm = new GameManager();
        game_LAY_arrows = findViewById(R.id.game_LAY_arrows);
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_IMG_hearts = new ImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };
        game_IMG_board = new ImageView[]{
                findViewById(R.id.game_IMG_cell0),
                findViewById(R.id.game_IMG_cell1),
                findViewById(R.id.game_IMG_cell2),
                findViewById(R.id.game_IMG_cell3),
                findViewById(R.id.game_IMG_cell4),
                findViewById(R.id.game_IMG_cell5),
                findViewById(R.id.game_IMG_cell6),
                findViewById(R.id.game_IMG_cell7),
                findViewById(R.id.game_IMG_cell8),
                findViewById(R.id.game_IMG_cell9),
                findViewById(R.id.game_IMG_cell10),
                findViewById(R.id.game_IMG_cell11),
                findViewById(R.id.game_IMG_cell12),
                findViewById(R.id.game_IMG_cell13),
                findViewById(R.id.game_IMG_cell14),
                findViewById(R.id.game_IMG_cell15),
                findViewById(R.id.game_IMG_cell16),
                findViewById(R.id.game_IMG_cell17),
                findViewById(R.id.game_IMG_cell18),
                findViewById(R.id.game_IMG_cell19),
                findViewById(R.id.game_IMG_cell20),
                findViewById(R.id.game_IMG_cell21),
                findViewById(R.id.game_IMG_cell22),
                findViewById(R.id.game_IMG_cell23),
                findViewById(R.id.game_IMG_cell24),
                findViewById(R.id.game_IMG_cell25),
                findViewById(R.id.game_IMG_cell26),
                findViewById(R.id.game_IMG_cell27),
                findViewById(R.id.game_IMG_cell28),
                findViewById(R.id.game_IMG_cell29),
                findViewById(R.id.game_IMG_cell30),
                findViewById(R.id.game_IMG_cell31),
                findViewById(R.id.game_IMG_cell32),
                findViewById(R.id.game_IMG_cell33),
                findViewById(R.id.game_IMG_cell34),
        };
        game_BTN_arrows = new ImageButton[]{
                findViewById(R.id.game_BTN_arrowUp),
                findViewById(R.id.game_BTN_arrowDown),
                findViewById(R.id.game_BTN_arrowLeft),
                findViewById(R.id.game_BTN_arrowRight),
        };

        // First Placement
        game_IMG_board[gm.getCurrentMousePlacement()].setImageResource(R.drawable.ic_mouse);
        game_IMG_board[gm.getCurrentCatPlacement()].setImageResource(R.drawable.ic_cat);

    }
    private void buttonPressing(){
        game_BTN_arrows[0].setOnClickListener(view -> moveMouse(Direction.UP));
        game_BTN_arrows[1].setOnClickListener(view -> moveMouse(Direction.DOWN));
        game_BTN_arrows[2].setOnClickListener(view -> moveMouse(Direction.LEFT));
        game_BTN_arrows[3].setOnClickListener(view -> moveMouse(Direction.RIGHT));
    }
    private void initSensors() {
        game_LAY_arrows.setVisibility(View.GONE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                int temp; // Holds placement
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                //x>2 left ; x<-2 right ; y>9 up ; y<7 down
                Log.d("ccc", "X= " + x);
                Log.d("ccc", "Y= " + y);
                if (x > 2) {
                    temp = gm.figureMovement(Direction.LEFT, gm.getCurrentMousePlacement());
                    gm.setCurrentMousePlacement(temp);
                } else if (x < -2) {
                    temp = gm.figureMovement(Direction.RIGHT, gm.getCurrentMousePlacement());
                    gm.setCurrentMousePlacement(temp);
                } else if (y > 9) {
                    temp = gm.figureMovement(Direction.UP, gm.getCurrentMousePlacement());
                    gm.setCurrentMousePlacement(temp);
                } else if (y < 7) {
                    temp = gm.figureMovement(Direction.DOWN, gm.getCurrentMousePlacement());
                    gm.setCurrentMousePlacement(temp);                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    // Update UI
    private void updateUI(){
        for (int i = 0 ; i < game_IMG_hearts.length ; i++)
            game_IMG_hearts[i].setVisibility(gm.getLife() > i ? View.VISIBLE : View.INVISIBLE);
        gm.updateScore(1);
        game_LBL_score.setText(""+ (gm.getScore()));
        updateMovement();
    }

    // Movement on Board
    private void moveCat(){
        Direction direction = Direction.values()[(int) (Math.random() *  Direction.values().length)];
        int newPlacement;
        do {
            newPlacement = gm.figureMovement(direction, gm.getCurrentCatPlacement());
        } while (newPlacement < 0);
        // The move itself
        game_IMG_board[gm.getCurrentCatPlacement()].setImageResource(0);
        game_IMG_board[newPlacement].setImageResource(R.drawable.ic_cat);
        gm.setCurrentCatPlacement(newPlacement);
    }
    private void moveMouse(Direction direction){
        int newPlacement = gm.figureMovement(direction, gm.getCurrentMousePlacement());
        game_IMG_board[gm.getCurrentMousePlacement()].setImageResource(0);
        game_IMG_board[newPlacement].setImageResource(R.drawable.ic_mouse);
        gm.setCurrentMousePlacement(newPlacement);

    }
    private void updateMovement(){
        moveCat();
        if (!gm.getCheeseVisability()){ // Create cheese on board
            gm.setCheesePlacement();
            game_IMG_board[gm.getCheesePlacement()].setImageResource(R.drawable.ic_cheese);
        }
        if (gm.getCurrentMousePlacement() == gm.getCurrentCatPlacement()) // -1 Life
        {
            gm.reduceLife();
            if (gm.isDead()){
                Intent intent = new Intent(MainActivity.this, WinsChartActivity.class);
                intent.putExtra("score", gm.getScore());
                intent.putExtra("type", gameType);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
                finish();
            }
            soundCatCall.start();
            game_IMG_board[gm.getCurrentMousePlacement()].setImageResource(0);
            game_IMG_board[gm.getCurrentCatPlacement()].setImageResource(0);
            gm.startPlacement();
        }
        if (gm.getCheesePlacement() == gm.getCurrentMousePlacement()){
            soundCoinCatch.start();
            gm.updateScore(10);
            game_IMG_board[gm.getCheesePlacement()].setImageResource(0);
            gm.setCheeseVisability();
        }
    }
    // Timer and Scores
//    private void scoreTimer(){
//        if (gm.isDead()) {
//            stopScoring();
//        } else {
//            startScoring();
//        }
//    }
//    private void startScoring(){
//        handler.postDelayed(r, DELAY);
//    }
//    private void stopScoring(){
//        handler.removeCallbacks(r);
//    }

    // Location
    private void setLocationSettings() {
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationCriteria.setAltitudeRequired(false);
        locationCriteria.setBearingRequired(false);
        locationCriteria.setCostAllowed(true);
        locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                // Then update all the time and at every meters change.
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                        0, mLocationListener);
                                String providerName = locationManager.getBestProvider(locationCriteria, true);
                                location = locationManager.getLastKnownLocation(providerName);
                                if (location == null) {
                                    latitude = defaultLocation.latitude;
                                    longitude = defaultLocation.longitude;
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                                LatLng curLoc = new LatLng(latitude, longitude);
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                // Then update every limited time and at every limited meters change.
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                                        LOCATION_REFRESH_DISTANCE, mLocationListener);
                                String providerName = locationManager.getBestProvider(locationCriteria, true);
                                location = locationManager.getLastKnownLocation(providerName);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                LatLng curLoc = new LatLng(latitude, longitude);
                            } else { /* No location access granted. Then we can't use the location track and the 1st score location functionality is disabled.
                                That's why we have set the default location to Ness Ziona(could be anywhere else just need default). */ }
                        }
                );
        // check permissions and the app needs to show a permission rationale dialog.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    // Finish Game
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ccc", "destroy");
        timer.cancel();
        isTimerRunning = false;
        gm.resetScore();
    }

    @Override
    protected void onPause() {
        Log.d("ccc", "pause");
        super.onPause();
        timer.cancel();
        isTimerRunning = false;
        if (gameType.equals("sensor")) {
            sensorManager.unregisterListener(sensorListener);
        }
    }

    @Override
    protected void onResume() {
        Log.d("ccc", "resume");
        super.onResume();
        if (!isTimerRunning) {
            timer.start();
        }
        if (gameType.equals("sensor")) {
            sensorManager.registerListener(sensorListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    private void finishGame(){
//        scoreTimer();
        // Will present the final score
        Toast.makeText(getApplicationContext(), "Result is : "+gm.getScore(), Toast.LENGTH_LONG).show();
        finish();
    }
}