package com.example.catchmegame;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        MSP.init(this);
    }
}
