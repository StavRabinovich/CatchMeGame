package com.example.catchmegame.frags;

import com.example.catchmegame.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.catchmegame.winning.Winn;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment {

    GoogleMap map;
    ArrayList<Winn> wins = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initMap();
        return view;
    }
    private void initMap() {
        //init map
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                for(Winn wn : wins){
                    if(!(wn.getLatitude() == 0 && wn.getLongitude() ==0)) {
                        LatLng latLng = new LatLng(wn.getLatitude(), wn.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .title("Location of " + wn.getResult()));
                    }
                }
            }
        });
    }

    public void gotoLocation(double latitude,double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.moveCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void setList(ArrayList<Winn> list){
        wins = list;
    }
}