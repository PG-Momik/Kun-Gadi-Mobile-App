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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

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

public class ContributeNodeActivityP1 extends FragmentActivity implements AdapterView.OnItemSelectedListener {
    String url ="https://kungadi.000webhostapp.com/Api/index.php?en=node&op=getAllNode";
    ArrayList<NodeInfo> info;
    LinearLayout buttonContainer;
    Button cancel_btn, next_btn;
    Spinner spin;
    String[] id_array, node_array, lon_array, lat_array;
    int selected_index;
    SharedPreferences pref;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_node_p1);
        spin = findViewById(R.id.node_spinner);
        buttonContainer = findViewById(R.id.buttonContainer);
        cancel_btn =  findViewById(R.id.cancel_btn);
        next_btn = findViewById(R.id.next_btn);
        info =  new ArrayList<>();

        bringSpinnerData();

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_index==0){
                    Toast.makeText(ContributeNodeActivityP1.this, "Select a node", Toast.LENGTH_SHORT).show();
                }else{
                    //Can go to other activity
                    pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    user_id = pref.getString("user_id", "");
                    Intent i = new Intent(ContributeNodeActivityP1.this, ContributeNodeActivityP2.class);
                    i.putExtra("id", id_array[selected_index-1]);
                    i.putExtra("user_id",user_id);
                    i.putExtra("name", node_array[selected_index]);
                    i.putExtra("longitude", lon_array[selected_index-1]);
                    i.putExtra("latitude", lat_array[selected_index-1]);
                    startActivity(i);
                }
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        selected_index = position;
        if(position==0){
            buttonContainer.setVisibility(View.INVISIBLE);
        }else{
            buttonContainer.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    //Bring Spinner Data
    private void bringSpinnerData() {
        RequestQueue queue = Volley.newRequestQueue(ContributeNodeActivityP1.this);
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
                            }else{
                                Toast.makeText(ContributeNodeActivityP1.this, "Err. Cannot fetch data. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ContributeNodeActivityP1.this, "Err. Something went wrong.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContributeNodeActivityP1.this, "Connection Error. Check your internet connection.", Toast.LENGTH_SHORT).show();
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
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,node_array);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

    }

}