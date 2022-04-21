package com.example.sixth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.Activities.Option1MapActivity;
import com.example.sixth.Models.RouteInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.sixth.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Option1ListAdapter extends ArrayAdapter<RouteInfo> {
    Context context;

    public Option1ListAdapter(@NonNull Context context, ArrayList<RouteInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.option1_list_item_layout, null);
        TextView id = view.findViewById(R.id.id);
        TextView path = view.findViewById(R.id.path);
        TextView traceBtn = view.findViewById(R.id.traceBtn);
        TextView start_point = view.findViewById(R.id.start_point);
        TextView end_point = view.findViewById(R.id.end_point);
        int size;

        RouteInfo info = getItem(position);
        id.setText("S.N. : " + (position + 1));
        String[] nodes = info.getPath().split(",");
        size = nodes.length;

        start_point.setText("Start: " + nodes[0]);
        end_point.setText("End: " + nodes[size - 1]);

        path.setText("Path : " + info.getPath());

        traceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bringCoords(info.getPath());

            }
        });
        return view;
    }

    public void bringCoords(String parameter) {
        String url = "https://kungadi.000webhostapp.com/Api/index.php?en=routes&op=getPathCoordinates";
        Log.d("Tag", url);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject params = new JSONObject();
        try {
            params.put("path", parameter);
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

                            if (code.equals("200")) {
                                Log.d("RESPONSE", response.getJSONArray("message").toString());
                                showOption1Map(response.getJSONArray("message").toString());
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

    public void showOption1Map(String message) {
        Intent intent = new Intent(getContext(), Option1MapActivity.class);
        intent.putExtra("message", message);
        context.startActivity(intent);
    }
}
