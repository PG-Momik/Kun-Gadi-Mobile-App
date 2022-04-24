package com.example.sixth.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sixth.Activities.ViewMyNodeContribution;
import com.example.sixth.Models.ConRouteInfo;
import com.example.sixth.R;

import java.util.ArrayList;


public class RouteContributionAdapter extends ArrayAdapter<ConRouteInfo> {
        Context context;
        String state;

public RouteContributionAdapter(@NonNull Context context, ArrayList<ConRouteInfo> list) {
        super(context,0,list);
        this.context = context;
        }

@NonNull
@Override
public View getView(int position,
@Nullable View convertView,
@NonNull ViewGroup parent){

        View view= LayoutInflater.from(getContext()).inflate(R.layout.single_list_route_contribution,null);
        LinearLayout l1 = view.findViewById(R.id.listBox);

        TextView created = view.findViewById(R.id.con_date);
        TextView start = view.findViewById(R.id.con_start);
        TextView end = view.findViewById(R.id.con_end);
        ConRouteInfo info = getItem(position);
        Log.d( "inside adapter: ", String.valueOf(position));
        created.setText("Submitted On: "+info.getCreated());
        start.setText("Start: "+info.getStart());
        end.setText("End: "+info.getEnd());

        state = info.getState_id();
        if(state.equals("1")){
        l1.setBackgroundResource(R.drawable.shape_green);
        }else if(state.equals("2")){
        l1.setBackgroundResource(R.drawable.shape_blue);
        }else{
        l1.setBackgroundResource(R.drawable.shape_white);
        }
        return view;
        }
        }
