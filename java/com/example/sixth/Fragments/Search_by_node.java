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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.sixth.Models.NodeInfo;
import com.example.sixth.Models.RouteInfo;
import com.example.sixth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.sixth.Adapter.Option2ListAdapter;

public class Search_by_node extends Fragment {
    EditText e1;
    ImageButton searchBtn;
    ProgressBar pb;
    String parameter;
    String url ="https://kungadi.000webhostapp.com/Api/index.php?en=routes&op=getToNode";
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
        pb = getActivity().findViewById(R.id.progressBar);
        View view = inflater.inflate(R.layout.fragment_search_by_node, container, false);
        e1 = view.findViewById(R.id.parameter);
        searchBtn = view.findViewById(R.id.search_btn);
        listView = getActivity().findViewById(R.id.listview);
        hidden_batta = getActivity().findViewById(R.id.hidden_container);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) hidden_batta.getLayoutParams();
        params.height = 1000;
        hidden_batta.setLayoutParams(params);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                parameter = e1.getText().toString();
                searchByNode(parameter);
            }
        });
        return view ;
    }
    public void searchByNode(String parameter){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JSONObject params = new JSONObject();
        try {
            params.put("node", parameter);
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
                                makeSingleJson(response.getJSONObject("message"));
                            }
                        } catch (JSONException e) {
                            Log.d( "Am i here",  "eta");
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

    public void makeSingleJson(JSONObject message) throws JSONException {
        ArrayList<RouteInfo> list = new ArrayList<>();

        ArrayList<String> keys = new ArrayList<>();
        Iterator<String> iterator = message.keys();

        JSONArray routes = new JSONArray();
        while (iterator.hasNext()) {
            keys.add(iterator.next());
        }
        for(int i=0; i<keys.size(); i++){
            RouteInfo info = new RouteInfo();
            JSONArray coordinates = new JSONArray();

            //get route number from key
            String key = keys.get(i);
            String subString = String.format("repeated%s", i);
            String route_no = new String();
            JSONObject route = new JSONObject();
            JSONArray array = message.getJSONArray(keys.get(i));
            if(key.contains(subString)){
                key=key.replace(subString, "");
            }

            //take out name, long and lat from all objects in array
            //concat names to make path
            String path = null;
            for(int j= 0; j<array.length(); j++){
                JSONObject obj = array.getJSONObject(j);
                JSONObject coord =  new JSONObject();
                String name = obj.getString("name");
                coord.put("longitude", obj.getString("longitude"));
                coord.put("latitude", obj.getString("latitude"));
                path = path+", "+ name;
                coordinates.put(coord);
            }

            route_no = key;
            path = path.substring(6);
            info.setSn(String.valueOf(i+1));
            info.setPath(path);
            info.setNode(parameter);
            info.setRoute_no(route_no);
            info.setCoordinates(coordinates);

            list.add(info);
            pb.setVisibility(View.GONE);
        }
        listView.setAdapter(new Option2ListAdapter(getContext(), list));


    }

}


