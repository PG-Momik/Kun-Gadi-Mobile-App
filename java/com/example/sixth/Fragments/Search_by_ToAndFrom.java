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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sixth.Adapter.Option3ListAdapter;
import com.example.sixth.Models.RouteInfo;
import com.example.sixth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search_by_ToAndFrom extends Fragment {
    EditText e1;
    EditText e2;
    ImageButton searchBtn;
    String url ="https://kungadi.000webhostapp.com/Api/index.php?en=routes&op=getByFromAndTo";
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
        View view = inflater.inflate(R.layout.fragment_search_by__to_and_from, container, false);
        e1 =(EditText)view.findViewById(R.id.param_start);
        e2 =(EditText)view.findViewById(R.id.param_end);
        searchBtn = (ImageButton)view.findViewById(R.id.search_btn);
        listView = getActivity().findViewById(R.id.listview);
        hidden_batta = getActivity().findViewById(R.id.hidden_container);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String param1 = e1.getText().toString();
                String param2 = e2.getText().toString();
                searchBy(param1, param2);
            }
        });
        return view;
    }
    public void searchBy(String param1, String param2){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JSONObject params = new JSONObject();
        try{
            params.put("to", param2);
        }catch (JSONException e){
            e.printStackTrace();
        }
        try {
            params.put("from", param1);
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
                                Log.d("herum ta?", "lalalaa");
                                parseData(response.getJSONArray("message"));
                                Log.d("onResponse: ", String.valueOf(response.getJSONArray("message")));
                                hidden_batta.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.d( "eta xa ki?", "sasdfadfad");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Errorrrrrr: " + error.getMessage());
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

    public void parseData(JSONArray data) {
        list = new ArrayList<RouteInfo>();
        JSONArray coords =  new JSONArray();
        String path="";
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                JSONObject lat_lng = new JSONObject();
                path = path+", "+obj.getString("name");
                lat_lng.put("lat", obj.getString("lat"));
                lat_lng.put("lng", obj.getString("lng"));
                coords.put(lat_lng);
            }
            int length = path.length();
            path = path.substring(2, length);
            RouteInfo info = new RouteInfo();
            info.setPath(path);
            info.setCoordinates(coords);
            list.add(info);
            listView.setAdapter(new Option3ListAdapter(getContext(), list));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
        }
    }
}