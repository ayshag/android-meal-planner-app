package com.example.fitnessapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DietitianHome extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    int lowCalorieLimit = 100, lowCarbLimit = 7, highProteinLimit = 7, lowFatLimit = 4;
    int calorieItems = 0, proteinItems = 0, fatItems = 0, carbItems = 0;

    PieChartView pieChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietitian_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(DietitianHome.this,"Welcome", Toast.LENGTH_LONG).show();

        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = DietitianHome.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("View")) {
                    intent = new Intent(context, PreviewFood.class);
                }
                else if(tab.getText().toString().equals("Add")){
                    intent = new Intent(context, AddFood.class);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(DietitianHome.this,MainActivity.class);
                   // startActivity(new Intent(DietitianHome.this,MainActivity.class));
                }

                context.startActivity(intent);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
                Log.d("PrintLog", tab.getText().toString());
                Context context = DietitianHome.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("View")) {
                    intent = new Intent(context, PreviewFood.class);
                }
                else if(tab.getText().toString().equals("Add")){
                    intent = new Intent(context, AddFood.class);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(DietitianHome.this,MainActivity.class);
                    // startActivity(new Intent(DietitianHome.this,MainActivity.class));
                }

                context.startActivity(intent);
            }
        });

      //  firebaseAuth = FirebaseAuth.getInstance();
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_add_food)
        {
           Context context = DietitianHome.this;
            Intent intent = new Intent(context,AddFood.class);

            context.startActivity(intent);

        }

        if(id==R.id.action_preview_food)
        {
            Context context = DietitianHome.this;
            Intent intent = new Intent(context,PreviewFood.class);

            context.startActivity(intent);
        }
        if(id==R.id.logOutmenu)
        {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(DietitianHome.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
