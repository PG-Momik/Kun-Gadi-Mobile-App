package com.example.sixth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

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

public class Option2MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList<LatLng> locationArrayList;
    ArrayList<LatLng> path_p1;
    ArrayList<LatLng> path_p2;
    ArrayList<String> nameList;
    String message;
    JSONArray array;
    LatLng temp;
    PolylineOptions polyOptions1;
    PolylineOptions polyOptions2;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polyOptions1 = new PolylineOptions();
        polyOptions2 = new PolylineOptions();

        locationArrayList = new ArrayList<>();
        path_p1 = new ArrayList<>();
        path_p2 = new ArrayList<>();
        nameList = new ArrayList<>();

        Double temp_long;
        Double temp_lat;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] nodes = extras.getStringArray("nodes");
            try {
                array = new JSONArray(extras.getString("coordinates"));
                index = extras.getInt("index");
                for(int i=0; i<nodes.length; i++) {
                    JSONObject obj;
                    obj = array.getJSONObject(i);
                    temp_long = Double.valueOf(obj.get("longitude").toString());
                    temp_lat = Double.valueOf(obj.get("latitude").toString());
                    temp = new LatLng(temp_long, temp_lat);
                    if(i<index){
                        path_p1.add(temp);
                    }else{
                        path_p2.add(temp);
                    }
                    locationArrayList.add(temp);
                    nameList.add(nodes[i]);
                }
                path_p1.add(path_p2.get(0));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for(int i=0; i<=index; i++){
            mMap.addMarker(new MarkerOptions().
                    position(path_p1.get(i)).
                    title(nameList.get(i))).showInfoWindow();
        }
        for(int i=index, j=0; i<array.length(); i++, j++){
            mMap.addMarker(new MarkerOptions().
                    position(path_p2.get(j)).
                    title(nameList.get(i))).showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get((locationArrayList.size()-1)/2), 13));
        polyOptions1.addAll(path_p1).color(Color.GREEN).width(15);
        polyOptions2.addAll(path_p2).color(Color.BLUE).width(15);
        mMap.addPolyline(polyOptions1);
        mMap.addPolyline(polyOptions2);

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