package com.example.sixth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sixth.Activities.Option2MapActivity;
import com.example.sixth.Activities.Option3MapActivity;
import com.example.sixth.Models.RouteInfo;
import com.example.sixth.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class Option3ListAdapter extends ArrayAdapter<RouteInfo> {
    Context context;

    public Option3ListAdapter(@NonNull Context context, ArrayList<RouteInfo> list) {
        super(context,0,list);
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.option3_list_item_layout,null);
        TextView path= view.findViewById(R.id.path);
        TextView traceBtn = view.findViewById(R.id.traceBtn);

        RouteInfo info = getItem(position);
        path.setText(info.getPath());

        String[] nodes = info.getPath().split(", ");

        traceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOption3Map(nodes, info.getCoordinates());
            }
        });
        return view;
    }

    private void showOption3Map(String[] nodes, JSONArray coordinates) {
        Intent intent = new Intent(getContext(), Option3MapActivity.class);
        intent.putExtra("nodes", nodes);
        intent.putExtra("coordinates", coordinates.toString());
        getContext().startActivity(intent);
    }
}
