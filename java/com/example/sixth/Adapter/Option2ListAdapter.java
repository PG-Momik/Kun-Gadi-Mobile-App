package com.example.sixth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sixth.Activities.Option2MapActivity;
import com.example.sixth.Models.RouteInfo;
import com.example.sixth.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class Option2ListAdapter extends ArrayAdapter<RouteInfo> {
    Context context;

    public Option2ListAdapter(@NonNull Context context, ArrayList<RouteInfo> list) {
        super(context,0,list);
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.option2_list_item_layout,null);
        TextView id = view.findViewById(R.id.id);
        TextView route_no = view.findViewById(R.id.route_no);
        TextView path= view.findViewById(R.id.path);
        LinearLayout split_texts = view.findViewById(R.id.split_texts);
        TextView split_path_1 = view.findViewById(R.id.split_p1);
        TextView split_path_2 = view.findViewById(R.id.split_p2);

        TextView splitBtn = view.findViewById(R.id.splitBtn);
        TextView traceBtn = view.findViewById(R.id.traceBtn);

        RouteInfo info = getItem(position);
        id.setText("S.N.: "+info.getSn());
        route_no.setText("Route No.: "+info.getRoute_no());
        path.setText("Path: "+info.getPath());

        String[] nodes = info.getPath().split(", ");

        int index = 0;
        int size = nodes.length;
        ArrayList<String> first_half = new ArrayList<>();
        ArrayList<String> second_half = new ArrayList<>();
        String first_path = "";
        String second_path= "";

        for(int x = 0; x<size; x++){
            if(nodes[x].toUpperCase().equals(info.getNode().toUpperCase())){
                index=x;
            }
        }
        for (int i = 0; i < index; i++) {
            first_half.add(nodes[i]);
        }
        for (int i = 0; i < (size-index); i++) {
            if((i+index)<size){
                second_half.add(nodes[i+index]);
            }
        }

        first_half.add(second_half.get(0));
        for (int i = 0; i < first_half.size(); i++) {
            first_path = first_path+", "+ first_half.get(i);
        }
        for (int i = 0; i < second_half.size(); i++) {
            second_path = second_path+", "+ second_half.get(i);
        }

        String finalFirst_path = first_path.substring(2);
        String finalSecond_path = second_path.substring(2);

        splitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                split_texts.setVisibility(View.VISIBLE);
                split_path_1.setText(finalFirst_path);
                split_path_2.setText(finalSecond_path);
            }
        });

        int finalIndex = index;
        traceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOption2Map(nodes, info.getCoordinates(), finalIndex);
            }
        });
        return view;
    }

    private void showOption2Map(String[] nodes, JSONArray coordinates, int finalIndex) {
        Intent intent = new Intent(getContext(), Option2MapActivity.class);
        intent.putExtra("nodes", nodes);
        intent.putExtra("coordinates", coordinates.toString());
        intent.putExtra("index", finalIndex);
        getContext().startActivity(intent);
    }


    public void splitPath(){

    }



//    public void bringCoords(String parameter) {
//        String url = "https://kungadi.000webhostapp.com/index.php?en=routes&op=getPathCoords";
//        Log.d("Tag", url);
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        JSONObject params = new JSONObject();
//        try {
//            params.put("path", parameter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                url, params,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String code = response.getString("code");
//
//                            if(code.equals("200")){
//                                showOption2Map(response.getJSONArray("message").toString());
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error: " + error.getMessage());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        queue.add(jsonObjReq);
//    }

}
