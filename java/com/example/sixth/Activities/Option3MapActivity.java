package com.example.sixth.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class Option3MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> locationArrayList;
    ArrayList<String> nameList;
    JSONArray coordinates;
    LatLng temp;
    PolylineOptions polyOptions;
    String[] nodes;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent extras = getIntent();
        Double temp_long;
        Double temp_lat;

        nodes = extras.getStringArrayExtra("nodes");
        str = extras.getStringExtra("coordinates");

        polyOptions = new PolylineOptions();
        locationArrayList = new ArrayList<>();
        nameList = new ArrayList<>();

        try {
            coordinates = new JSONArray(str);
            for(int i=0; i<nodes.length; i++) {
                JSONObject obj;
                obj = coordinates.getJSONObject(i);
                Log.d("node:", nodes[i]);
                Log.d("object", obj.toString());
                temp_long = Double.valueOf(obj.get("lng").toString());
                temp_lat = Double.valueOf(obj.get("lat").toString());
                temp = new LatLng(temp_lat, temp_long);
                locationArrayList.add(temp);
                nameList.add(nodes[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
