package com.example.sixth.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    String userDetailsUls = "https://kungadi.000webhostapp.com/Api/index.php?en=user&op=getById";

    ImageView myNodes, editProfileBtn, myRoutes;
    TextView full_name;
    TextInputEditText name, email, phone, role, created;
    ProgressBar pb ;
    LinearLayout prof_cont;
    Button updateBtn;
    SharedPreferences pref;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myNodes = findViewById(R.id.myEventsBtn);
        myRoutes = findViewById(R.id.rate_btn);
        full_name = findViewById(R.id.full_name);
        editProfileBtn = findViewById(R.id.editProfile_btn);
        pb = findViewById(R.id.profile_load);
        prof_cont = findViewById(R.id.profile_container);
        updateBtn = findViewById(R.id.upload_btn);

        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_phone);
        role = findViewById(R.id.profile_role);
        created = findViewById(R.id.profile_created);

        pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", "");
        getUserDetails(user_id);

        myNodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(ProfileActivity.this,MyNodeContributionsActivity.class);
                i.putExtra("user_id", "15");
                startActivity(i);
                finish();
            }
        });
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                name.setClickable(true);
                name.setCursorVisible(true);

                email.setEnabled(true);
                email.setClickable(true);
                email.setCursorVisible(true);

                phone.setEnabled(true);
                phone.setClickable(true);
                phone.setCursorVisible(true);

                updateBtn.setVisibility(View.VISIBLE);
            }
        });
        myRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(ProfileActivity.this,MyRouteContributionsActivity.class);
                i.putExtra("user_id", "15");
                startActivity(i);
                finish();
            }
        });
    }

    private void getUserDetails(String user_id) {
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                userDetailsUls, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                parseData(response.getJSONObject("message"));
                                Log.d( "onResponse: ", "eta mah");
                            }
                        } catch (JSONException e) {
                            Log.d("Message: ", "inside catch");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "onErrorResponse: ", "jesus");
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

    private void parseData(JSONObject message) throws JSONException {
        full_name.setText(message.getString("name"));
        role.setText(message.getString("role"));
        name.setText(message.getString("name"));
        email.setText(message.getString("email"));
        phone.setText(message.getString("phone"));
        created.setText(message.getString("created"));
        editProfileBtn.setClickable(true);
        pb.setVisibility(View.INVISIBLE);
        prof_cont.setVisibility(View.VISIBLE);
    }


}