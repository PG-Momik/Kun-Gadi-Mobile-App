package com.example.sixth.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.Models.RouteInfo;
import com.example.sixth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.sixth.Adapter.Option1ListAdapter;

public class Search_by_route extends Fragment {
    EditText e1;
    ProgressBar pb;
    ImageButton searchBtn;
    String url ="https://kungadi.000webhostapp.com/Api/index.php?en=routes&op=getByRouteNum";
    ArrayList<RouteInfo> list;
    ListView listView;
    LinearLayout hidden_batta;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_by_route, container, false);
        e1 =(EditText)view.findViewById(R.id.parameter);
        pb = getActivity().findViewById(R.id.progressBar);
        searchBtn = (ImageButton)view.findViewById(R.id.search_btn);
        listView = getActivity().findViewById(R.id.listview);
        hidden_batta = getActivity().findViewById(R.id.hidden_container);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                String parameter = e1.getText().toString();
                searchByRouteNo(parameter);
            }
        });
        return view;
    }


    public void searchByRouteNo(String parameter){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JSONObject params = new JSONObject();
        try {
            params.put("route_no", parameter);
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
                                parseData(response.getJSONArray("message").toString(), parameter);
                                hidden_batta.setVisibility(View.VISIBLE);
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

    public void parseData(String data, String parameter) {
        list = new ArrayList<RouteInfo>();
        Log.d("parameter 3", parameter);
        try {
            JSONArray msg = new JSONArray(data);
//            Log.d("parseData: ", msg.toString());
            for (int i = 0; i < msg.length(); i++) {
                JSONObject s_obj = msg.getJSONObject(i);
                RouteInfo info = new RouteInfo();
                info.setId(s_obj.getString("id"));
                info.setPath(s_obj.getString("path"));
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
        }
        pb.setVisibility(View.GONE);
        listView.setAdapter(new Option1ListAdapter(getContext(), list));
    }

}