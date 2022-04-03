package com.example.sixth.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.Models.NodeInfo;
import com.example.sixth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContributeRouteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String url ="https://kungadi.000webhostapp.com/Api/index.php?en=node&op=getAllNode";
    String url2 = "https://kungadi.000webhostapp.com/Api/index.php?en=contribute_route&op=addContribution";
    Spinner start_spinner, end_spinner;
    EditText path_et;
    Button cancel_btn, submit_btn;
    ArrayList<NodeInfo> info;
    LinearLayout buttonContainer;
    String[] id_array, node_array, lon_array, lat_array;
    int selected_index;
    SharedPreferences pref;
    String user_id;
    String start, end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_route);
        start_spinner = findViewById(R.id.start_spinner);
        end_spinner = findViewById(R.id.end_spinner);
        path_et = findViewById(R.id.complete_path);
        cancel_btn = findViewById(R.id.cancel_btn);
        submit_btn = findViewById(R.id.submit_btn);
        pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", "");
        bringSpinnerData();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(path_et.getText().toString())) {
                    uploadContribution(user_id, path_et.getText().toString());
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        selected_index = position;
        if(position!=0){
            if(arg0.getId() == R.id.start_spinner){
                start = node_array[position-1];
            }else{
                end = node_array[position-1];
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    //Bring Spinner Data
    private void bringSpinnerData() {
        RequestQueue queue = Volley.newRequestQueue(ContributeRouteActivity.this);
        JSONObject params = new JSONObject();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if(code.equals("200")){
                                loadSpinner(response.getJSONArray("message"));
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

    //    Load Spinner
    private void loadSpinner(JSONArray message) throws JSONException {
        int size = message.length();
        node_array = new String[(size+1)];
        id_array = new String[size];
        lon_array = new String[size];
        lat_array = new String[size];
        node_array[0]= "Select a Node";
        for(int i = 0; i<message.length(); i++){
            JSONObject obj = message.getJSONObject(i);
            id_array[i] = obj.getString("id");
            node_array[i+1] = obj.getString("name");
            lon_array[i] = obj.getString("longitude");
            lat_array[i] = obj.getString("latitude");
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,node_array);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_spinner.setOnItemSelectedListener(this);
        end_spinner.setOnItemSelectedListener(this);
        start_spinner.setAdapter(aa);
        end_spinner.setAdapter(aa);

    }

    private void uploadContribution(String user_id, String path){
        RequestQueue queue = Volley.newRequestQueue(ContributeRouteActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", user_id);
            params.put("start", start);
            params.put("end", end);
            params.put("path", path);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url2, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if(code.equals("200")){
                                Toast.makeText(ContributeRouteActivity.this, "Thank you for your effort.", Toast.LENGTH_SHORT).show();
                                Intent gotoMain = new Intent(ContributeRouteActivity.this, MainActivity.class);
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