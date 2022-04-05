package com.example.sixth.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.sixth.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String url = "https://kungadi.000webhostapp.com/Api/index.php?en=user&op=loginUser";
    String url2 = "https://kungadi.000webhostapp.com/Api/index.php?en=user&op=getIdFromPhone";
    EditText p1, e2;
    Button b1;
    TextView t1;
    CheckBox rememberme;
    ProgressBar pb;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_login);
        if(getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE).getBoolean("isUserLogin", Boolean.parseBoolean(""))){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }else {
            p1 = findViewById(R.id.phone);
            e2 = findViewById(R.id.password);
            t1 = findViewById(R.id.register_redirect);
            b1 = findViewById(R.id.login_btn);
            rememberme = findViewById(R.id.rememberme);
            pb = findViewById(R.id.progressBar);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });
            pref = getSharedPreferences("login", Context.MODE_PRIVATE);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pb.setVisibility(View.VISIBLE);
                    String phone = p1.getText().toString();
                    String password = e2.getText().toString();
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
                    } else {
                        loginUser(phone, password);
                    }

//                if (rememberme.isChecked()) {
//                    sharedPreferences.edit().putBoolean("rememberme", true).commit();
//                }
//                finish();
                }

            });
        }
}

    private void loginUser(String phone, String password) {
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            JSONObject params = new JSONObject();
            try {
                params.put("phone", phone);
                params.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String code = response.getString("code");
                                if(code.equals("200")){
                                    loadHome(phone);
                                }else{
                                    Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "Err. Something went wrong.", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Connection Error. Check your internet connection.", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.INVISIBLE);
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

    private void loadHome(String phone) {
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("phone", phone);
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
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", response.getString("message"));
                                editor.putBoolean("isUserLogin", true);
                                editor.commit();
                                pb.setVisibility(View.INVISIBLE);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Err. Something went wrong.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Connection Error. Check your internet connection.", Toast.LENGTH_SHORT).show();
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

