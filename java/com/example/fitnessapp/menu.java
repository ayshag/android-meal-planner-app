package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {

    private Button breakfast;
    private Button lunch;
    private Button dinner;
    private Button snack;

    private TextView txtBreakfastCal;
    private TextView txtLunchCal;
    private TextView txtDinnerCal;
    private TextView txtSnackCal;

    private Button currentBreakfastPlan;
    private Button currentLunchPlan;
    private Button currentDinnerPlan;
    private Button currentSnackPlan;

    private String email = "";
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setupUI();

        Intent intent = getIntent();
        /*email = intent.getStringExtra("email");*/
        firebaseAuth = FirebaseAuth.getInstance();
        email = firebaseAuth.getCurrentUser().getEmail();
        System.out.println("Email=========="+email);
        TabLayout tablayout = (TabLayout)findViewById(R.id.usertabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = menu.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                }

                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                   // startActivity(new Intent(menu.this,MainActivity.class));
                    intent = new Intent(menu.this,MainActivity.class);
                }

                context.startActivity(intent);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = menu.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                }

                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    // startActivity(new Intent(menu.this,MainActivity.class));
                    intent = new Intent(menu.this,MainActivity.class);
                }

                context.startActivity(intent);
            }
        });
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breakfastIntent = new Intent(menu.this, Search.class);
                breakfastIntent.putExtra("userIntent",getResources().getString(R.string.breakfast_menu_option));
                breakfastIntent.putExtra("email",email);
                startActivity(breakfastIntent) ;
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breakfastIntent = new Intent(menu.this, Search.class);
                breakfastIntent.putExtra("userIntent",getResources().getString(R.string.lunch_menu_option));
                breakfastIntent.putExtra("email",email);
                startActivity(breakfastIntent) ;
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breakfastIntent = new Intent(menu.this, Search.class);
                breakfastIntent.putExtra("userIntent",getResources().getString(R.string.dinner_menu_option));
                breakfastIntent.putExtra("email",email);
                startActivity(breakfastIntent) ;
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breakfastIntent = new Intent(menu.this, Search.class);
                breakfastIntent.putExtra("userIntent",getResources().getString(R.string.snack_menu_option));
                breakfastIntent.putExtra("email",email);
                startActivity(breakfastIntent) ;
            }
        });

        currentBreakfastPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMealPlan(getResources().getString(R.string.breakfast_menu_option),email);
            }
        });

        currentLunchPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMealPlan(getResources().getString(R.string.lunch_menu_option),email);
            }
        });

        currentDinnerPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMealPlan(getResources().getString(R.string.dinner_menu_option),email);
            }
        });

        currentSnackPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMealPlan(getResources().getString(R.string.snack_menu_option),email);
            }
        });

        verifyIntent();
    }

    private void setupUI(){

        breakfast = (Button)findViewById(R.id.buttonBreakfast);
        lunch = (Button)findViewById(R.id.buttonLunch);
        dinner = (Button)findViewById(R.id.buttonDinner);
        snack = (Button)findViewById(R.id.buttonSnack);

        txtBreakfastCal = (TextView) findViewById(R.id.textbrkfstCalRec);
        txtLunchCal = (TextView) findViewById(R.id.textlnchCalRec);
        txtSnackCal = (TextView) findViewById(R.id.textsnackCalRec);
        txtDinnerCal = (TextView) findViewById(R.id.textdinnerCalRec);

        currentBreakfastPlan = (Button)findViewById(R.id.buttonBreakfastPlan);
        currentLunchPlan = (Button)findViewById(R.id.buttonLunchPlan);
        currentDinnerPlan = (Button)findViewById(R.id.buttonDinnerplan);
        currentSnackPlan = (Button)findViewById(R.id.buttonSnackPlan);
    }

    private void verifyIntent()
    {
        //Verify Intent
        Intent intent = getIntent();
        String calRecBrkfst = intent.getStringExtra("BreakfastCaloriesLeft");
        String calRecLnch = intent.getStringExtra("LunchCaloriesLeft");
        String calRecDinner = intent.getStringExtra("DinnerCaloriesLeft");
        String calRecSnack = intent.getStringExtra("SnackCaloriesLeft");

        if(calRecBrkfst!=null)
            txtBreakfastCal.setText(calRecBrkfst+" CALS UNDER");
        else if(calRecLnch!=null)
            txtLunchCal.setText(calRecLnch+" CALS UNDER");
        else if(calRecDinner!=null)
            txtDinnerCal.setText(calRecDinner+" CALS UNDER");
        else if(calRecSnack!=null)
            txtSnackCal.setText(calRecSnack+" CALS UNDER");
        else {
            txtLunchCal.setText(getResources().getString(R.string.lunch_recommendation));
            txtBreakfastCal.setText(getResources().getString(R.string.breakfast_recommendation));
            txtDinnerCal.setText(getResources().getString(R.string.dinner_recommendation));
            txtSnackCal.setText(getResources().getString(R.string.snack_recommendation));
        }


    }

    private void showMealPlan(String mealPlan, String emailAddress)
    {
        Intent intent = new Intent(menu.this,MealPlan.class);
        intent.putExtra("mealPlan",mealPlan);
        intent.putExtra("email",emailAddress);
        menu.this.startActivity(intent);
    }

}
