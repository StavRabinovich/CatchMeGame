package com.example.catchmegame.winning;

public class Winn {
    private int result;
    private double lat; // Latitude
    private double lng; // Longitude

    public Winn(){}

    public int getResult() {
        return result;
    }
    public Winn setResult(int result) {
        this.result = result;
        return this;
    }

    public double getLatitude() {
        return lat;
    }
    public Winn setLatitude(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLongitude() {
        return lng;
    }
    public Winn setLongitude(double lng) {
        this.lng = lng;
        return this;
    }
}
