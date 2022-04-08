package com.example.sixth.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.Adapter.NodeContributionAdapter;
import com.example.sixth.Models.ConNodeInfo;
import com.example.sixth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyNodeContributionsActivity extends AppCompatActivity {
    String myInterestsUrl = "https://kungadi.000webhostapp.com/Api/index.php?en=contribute_node&op=readContributionsByUser";
    String user_id;
    ArrayList<ConNodeInfo> list;
    ListView listView;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contribution_node);
        Intent oldIntent = getIntent();
        listView = findViewById(R.id.interestedListView);
        pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", "");
        getMyInterests(user_id);
    }

    private void getMyInterests(String user_id) {
        RequestQueue queue = Volley.newRequestQueue(MyNodeContributionsActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                myInterestsUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                Log.d("Message", response.getJSONArray("message").toString());
                                parseData(response.getJSONArray("message"));
                            }
                        } catch (JSONException e) {
                            Log.d("Message: ", "inside catch");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyNodeContributionsActivity.this, "Error. Check Internet Connection.", Toast.LENGTH_SHORT).show();
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
    private void parseData(JSONArray message) throws JSONException {
        list = new ArrayList<ConNodeInfo>();
        int size = message.length();
        for (int i = 0; i < size; i++) {
            Log.d("parseData: ", "loop");
            try {
                JSONObject obj = message.getJSONObject(i);
                ConNodeInfo info = new ConNodeInfo();
                info.setName(obj.getString("name"));
                info.setState_id(obj.getString("state_id"));
                info.setCreated(obj.getString("created"));
                info.setO_lat(obj.getString("o_lat"));
                info.setO_lng(obj.getString("o_lng"));
                info.setN_lat(obj.getString("n_lat"));
                info.setN_lng(obj.getString("n_lng"));
                list.add(info);
            } catch (JSONException e) {
                Toast.makeText(this, "Something went wrong. Refresh.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        listView.setAdapter(new NodeContributionAdapter(MyNodeContributionsActivity.this, list));
    }

}
