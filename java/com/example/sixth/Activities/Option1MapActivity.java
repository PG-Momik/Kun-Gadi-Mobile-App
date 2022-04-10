package com.example.sixth.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.sixth.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Option1MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> locationArrayList;
    ArrayList<String> nameList;
    String message;
    JSONArray array;
    LatLng temp;
    PolylineOptions polyOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();

        polyOptions = new PolylineOptions();
        locationArrayList = new ArrayList<>();
        Double temp_long;
        Double temp_lat;
        nameList = new ArrayList<>();
        message =  intent.getStringExtra("message");
        try {
            array = new JSONArray(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0;i<array.length();i++){
            try {
                JSONObject jsonNode = array.getJSONObject(i);
                temp_long = Double.valueOf(jsonNode.get("lat").toString());
                temp_lat = Double.valueOf(jsonNode.get("lng").toString());
                temp = new LatLng(temp_long, temp_lat);
                locationArrayList.add(temp);
                nameList.add(jsonNode.get("name").toString());
               } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int j = 0; j < locationArrayList.size(); j++) {
            mMap.addMarker(new MarkerOptions().
                    position(locationArrayList.get(j)).
                    title(nameList.get(j))).showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get((locationArrayList.size()-1)/2), 13));
        polyOptions.addAll(locationArrayList).width(15);
        mMap.addPolyline(polyOptions);
    }
    public void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        enableMyLocation();
    }
}
