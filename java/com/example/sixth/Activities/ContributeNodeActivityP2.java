package com.example.sixth.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContributeNodeActivityP2 extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    LatLng selected_LatLng;
    String selected_id,
    selected_node,
    selected_longitude,
    selected_latitude;
    String coordinates;
    String url = "https://kungadi.000webhostapp.com/Api/index.php?en=routes&op=getToNode";
    String url2 = "https://kungadi.000webhostapp.com/Api/index.php?en=contribute_node&op=addContribution";
    SupportMapFragment mapFragment;
    TextView oldCoordinates;
    EditText newCoordinates;
    Double newLong, newLat;
    Button submit_btn;
    SharedPreferences pref;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_node_p2);
        oldCoordinates = findViewById(R.id.old_coordinates);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        newCoordinates = findViewById(R.id.new_coordinates);
        if(getIntent()!=null) {
            selected_id = getIntent().getStringExtra("id");
            selected_node = getIntent().getStringExtra("name");
            selected_longitude = getIntent().getStringExtra("longitude");
            selected_latitude = getIntent().getStringExtra("latitude");
        }
        String oldCoords = selected_longitude+", "+selected_latitude;
        oldCoordinates.setText(oldCoords);

        submit_btn = findViewById(R.id.submit_btn);
        mapFragment.getMapAsync(this);
        String id = getIntent().getStringExtra("id");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coordinates = newCoordinates.getText().toString();
                pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                user_id = pref.getString("user_id", "");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("user_id", user_id);
                    obj.put("coordinate_id", selected_id);
                    obj.put("longitude", newLong.toString());
                    obj.put("latitude", newLat.toString());
                    Log.d("obj ", obj.getString("user_id"));
                    Log.d("obj ", obj.getString("coordinate_id"));
                    Log.d("obj ", obj.getString("longitude"));
                    Log.d("obj ", obj.getString("latitude"));
                    addContribution(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

//    Map Related
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Marker m;
        selected_LatLng = new LatLng(Double.valueOf(selected_longitude), Double.valueOf(selected_latitude));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.getUiSettings().setZoomControlsEnabled(true);
        showOldMarker();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                showOldMarker();
                showNewMarker(latLng);
                newLong = latLng.longitude;
                newLat = latLng.latitude;
                coordinates = newLat + ", " + newLong;
                newCoordinates.setText(coordinates);
            }
        });
    }
    private void showOldMarker() {
        mMap.addMarker(new MarkerOptions().
                position(selected_LatLng).
                title(selected_node).
                icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_RED))).
                showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selected_LatLng, 19));
    }
    private void showNewMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().
                position(latLng).title("New Position").
                icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).
                showInfoWindow();
    }

    //Api related
    private void addContribution(JSONObject params) {
        RequestQueue queue = Volley.newRequestQueue(ContributeNodeActivityP2.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url2, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if(code.equals("200")){
                                Toast.makeText(ContributeNodeActivityP2.this, "Thank you for your effort.", Toast.LENGTH_SHORT).show();
                                Intent gotoMain = new Intent(ContributeNodeActivityP2.this, MainActivity.class);
                                gotoMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                gotoMain.putExtra("goto", "MenuActivity");
                                startActivity(gotoMain);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);
    }

}