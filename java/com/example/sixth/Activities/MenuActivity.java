package com.example.sixth.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sixth.R;

public class MenuActivity extends AppCompatActivity {
    TextView nav_menu, nav_home;
    String black="#272727";
    String white ="#ffffff";
    String user_id;
    LinearLayout menu1, menu2, menu3, menu4, menu5, menu6 ;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", "");
        nav_home = findViewById(R.id.nav_home);
        nav_menu = findViewById(R.id.nav_menu);
        menu1= findViewById(R.id.menu_1);
        menu2= findViewById(R.id.menu_2);
        menu3= findViewById(R.id.menu_3);
        menu4= findViewById(R.id.menu_4);
        menu5= findViewById(R.id.menu_5);
        menu6= findViewById(R.id.menu_6);

        nav_home.setBackgroundColor(Color.parseColor(black));
        nav_home.setTextColor(Color.parseColor(white));
        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMain = new Intent(MenuActivity.this, MainActivity.class);
                gotoMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gotoMain);
                finish();
            }
        });

        nav_menu.setBackgroundColor(Color.parseColor(white));
        nav_menu.setTextColor(Color.parseColor(black));
        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Already inside MENU.", Toast.LENGTH_SHORT).show();
            }
        });

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuActivity.this, ContributeNodeActivityP1.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
                finish();
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuActivity.this, ContributeRouteActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
                startActivity(i);
                finish();
            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuActivity.this,ProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
                finish();
            }
        });
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuActivity.this,AboutUsActivity.class);
                startActivity(i);
                finish();
            }
        });
        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuActivity.this,FAQsActivity.class);
                startActivity(i);
                finish();
            }
        });
        menu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref =   getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("login");
                editor.clear();
                editor.commit();
                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }
}