package com.example.sixth.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sixth.Fragments.Search_by_ToAndFrom;
import com.example.sixth.Fragments.Search_by_node;
import com.example.sixth.Fragments.Search_by_route;
import com.example.sixth.R;

public class MainActivity extends AppCompatActivity {
    TextView nav_menu, nav_home;
    TextView option1, option2, option3;
    ProgressBar pb;
    LinearLayout l1;
    String user_id;
    String black="#272727";
    String white ="#ffffff";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", "");
        redirectIfNeeded();
        initializeStuff();
        readyNav();
        readyOptions();
        loadFragment(new Search_by_route(), 0);
        resetOptionColor();
        option1.setBackgroundColor(Color.parseColor(black));
        option1.setTextColor(Color.parseColor(white));
    }

    private void redirectIfNeeded() {
        if(getIntent().getStringExtra("goto")!=null){
            String activity = getIntent().getStringExtra("goto");
            switch (activity){
                case "MenuActivity":
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    i.putExtra("user_id", user_id);
                    startActivity(i);
                    break;
                default:
                    break;
            }
        }
    }

    public void initializeStuff(){
        nav_home = findViewById(R.id.nav_home);
        nav_menu = findViewById(R.id.nav_menu);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        l1 = findViewById(R.id.map_container);
        pb = findViewById(R.id.progressBar);
    }
    public void readyNav(){
        nav_home.setBackgroundColor(Color.parseColor(white));
        nav_home.setTextColor(Color.parseColor(black));
        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Already inside HOME.", Toast.LENGTH_SHORT).show();
            }
        });

        nav_menu.setBackgroundColor(Color.parseColor(black));
        nav_menu.setTextColor(Color.parseColor(white));
        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
    }
    public void readyOptions(){
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Search_by_route(), 0);
                resetOptionColor();
                option1.setBackgroundColor(Color.parseColor(black));
                option1.setTextColor(Color.parseColor(white));

            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Search_by_node(), 1);
                resetOptionColor();
                option2.setBackgroundColor(Color.parseColor(black));
                option2.setTextColor(Color.parseColor(white));

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Search_by_ToAndFrom(), 1);
                resetOptionColor();
                option3.setBackgroundColor(Color.parseColor(black));
                option3.setTextColor(Color.parseColor(white));
            }
        });

    }
    public void resetNavColor() {
        nav_home.setBackgroundColor(Color.parseColor(black));
        nav_menu.setBackgroundColor(Color.parseColor(black));
    }
    public void resetOptionColor() {
        option1.setBackgroundColor(Color.parseColor(white));
        option1.setTextColor(Color.parseColor(black));
        option2.setBackgroundColor(Color.parseColor(white));
        option2.setTextColor(Color.parseColor(black));
        option3.setBackgroundColor(Color.parseColor(white));
        option3.setTextColor(Color.parseColor(black));

    }
    //Fragment load garne function
    public void loadFragment(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag == 0){
            ft.add(R.id.container, fragment);
            clearBackStack(fm);
        }else{
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
        }
        ft.commit();
    }
    //This is for tyo multiple back nahosh fragrment ma vanera
    private void clearBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void makeColor(String color){
        l1.setBackgroundColor(Color.parseColor(color));
    }

}
