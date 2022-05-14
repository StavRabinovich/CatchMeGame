package com.example.catchmegame.winning;

import com.example.catchmegame.MSP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WinnersChart {
    Winn wins[];
    String json="";
    TypeToken token;
    private List<Integer> winsChart;

    public WinnersChart() {
        token = new TypeToken<Winn[]>() {};
        loadArray();
        winsChart = new ArrayList<>();
        for(Winn wn : wins){
            winsChart.add(wn.getResult());
        }
        Collections.sort(winsChart,Collections.reverseOrder());
    }

    public boolean isTopTen(int result, double latitude, double longitude) {
        if (result > winsChart.get(9)) {
            for(Winn wn : wins){
                if(wn.getResult() == winsChart.get(9)){
                    wn.setResult(result);
                    wn.setLatitude(latitude);
                    wn.setLongitude(longitude);
                    break;
                }
            }
            winsChart.remove(9);
            winsChart.add(result);
            Collections.sort(winsChart,Collections.reverseOrder());
            return true;
        }
        return false;
    }

    public void saveArray() {
        json = new Gson().toJson(wins);
        MSP.getMe().putString("SCORES",json);
    }

    private void loadArray() {
        json = MSP.getMe().getString("SCORES","");
        try {
            wins = new Gson().fromJson(json,token.getType());
        }catch (Exception ex){}

        if(wins == null){
            wins = new Winn[10];
            for(int i = 0; i< wins.length; i++){
                wins[i] = new Winn();
            }
        }
    }

    public ArrayList<Winn> getArray() {
        ArrayList<Winn> sorted = new ArrayList<>();
        Collections.addAll(sorted, wins);
        Collections.sort(sorted, new Comparator<Winn>() {
            @Override
            public int compare(Winn win, Winn t1) {
                return t1.getResult() - win.getResult();
            }
        });
        return sorted;
    }
}
