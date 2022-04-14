package com.example.sixth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


public class RegisterActivity extends AppCompatActivity {
    String url = "https://kungadi.000webhostapp.com/Api/index.php?en=user&op=registerUser";
    EditText e1, e2, e3, e4, e5;
    Button b1;
    TextView t1;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e1 = findViewById(R.id.username);
        e2 = findViewById(R.id.phone);
        e3 = findViewById(R.id.email);
        e4 = findViewById(R.id.password);
        e5 = findViewById(R.id.confirm_password);
        t1 = findViewById(R.id.login_redirect);
        b1 = findViewById(R.id.register_btn);
        pb = findViewById(R.id.progressBar);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                String username = e1.getText().toString();
                String phone = e2.getText().toString();
                String email = e3.getText().toString();
                String password = e4.getText().toString();
                String con_password = e5.getText().toString();
                if(username.isEmpty()||phone.isEmpty()||email.isEmpty()||con_password.isEmpty()||con_password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(username, phone, email, password, con_password);
                }

            }
        });

    }


    public void registerUser(String username, String phone, String email, String password, String confirm_password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        //JSON object banako so that we can send to server
        JSONObject params = new JSONObject();
        try {
            params.put("name", username);
            params.put("phone", phone);
            params.put("email", email);
            params.put("password", password);
            params.put("con_password", confirm_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            String code = response.getString("code");
                            //check if error code is 200, 200 vayo vane sab thik xa .
                            //Thik vaye ma k garne, tyo chai if block vitra lekhne
                            if(code.equals("200")){
                                Toast.makeText(RegisterActivity.this, code, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                            else{
                                String error_msg = response.getString("message");
                                TextView error_card = (TextView)findViewById(R.id.error_card);
                                error_card.setVisibility(View.VISIBLE);
                                error_card.setText(error_msg);
                                pb.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, "Err. Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Connection Error. Please check your internet connection.", Toast.LENGTH_SHORT).show();
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

}





