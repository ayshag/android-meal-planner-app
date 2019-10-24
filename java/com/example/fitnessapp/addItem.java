package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class addItem extends AppCompatActivity {

    private TextView itemSelected;
    private TextView textProteinValue;
    private TextView textCarbValue;
    private TextView textFiberValue;
    private TextView textSugarValue;
    private TextView textFatValue;
    private TextView textSatFatValue;
    private TextView textUnSatFatValue;
    private TextView textOthersValue;
    private TextView textCholestrolValue;
    private TextView textPottasiumValue;
    private FirebaseAuth firebaseAuth;

    Firebase firebase;

    private Button addToDiary;
    private String selectedItem;
    private Food selectedFood;
    private HashMap<String,Food> foodHashMap = new HashMap<>();
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        firebase = new Firebase("https://fitnessapp-74eb3.firebaseio.com");
        setupUI();

        Intent intent = getIntent();
        selectedItem = intent.getStringExtra("selectedItem");
        date = intent.getStringExtra("date");
        foodHashMap = (HashMap<String, Food>) intent.getSerializableExtra("foodMap");
        if(foodHashMap!=null) {
            System.out.println("FoodMap::" + foodHashMap.size());
            selectedFood = foodHashMap.get(selectedItem);
            Log.d("PrintLog", "SelectedItem " +selectedItem);
            Log.d("PrintLog","SelectedFood " + selectedFood.getName());
        }

        /*System.out.println("SelectedItem::"+selectedItem);
        System.out.println("date::"+date);
        System.out.println("SelectedFood::"+selectedFood);
*/
        itemSelected.setText(selectedItem);
        loadUI(selectedFood);

        addToDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDiary();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        TabLayout tablayout = (TabLayout)findViewById(R.id.usertabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = addItem.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                }

                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(addItem.this,MainActivity.class));
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
                Context context = addItem.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                }

                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(addItem.this,MainActivity.class));
                }

                context.startActivity(intent);
            }
        });
/*
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("food");


        ref.orderByChild("name").equalTo(selectedItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    selectedFood = ds.getValue(Food.class);
                    //String food =  ds.getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textCarbValue.setText(selectedFood.getCarbohydrates());
        textFatValue.setText(selectedFood.getFat());
        double satfatval = Double.parseDouble(selectedFood.getFat())/2;
        textSatFatValue.setText(String.valueOf(satfatval));

        textUnSatFatValue.setText(String.valueOf(satfatval));*/
    }


    private void setupUI(){
        itemSelected = (TextView)findViewById(R.id.itemSelected);
        textCarbValue = (TextView)findViewById(R.id.textCarbValue);
        textProteinValue = (TextView)findViewById(R.id.textProteinValue);
        textFiberValue = (TextView)findViewById(R.id.textFiberValue);
        textSugarValue = (TextView)findViewById(R.id.textSugarValue);
        textFatValue = (TextView)findViewById(R.id.textFatValue);
        textSatFatValue = (TextView)findViewById(R.id.textSatFatValue);
        textUnSatFatValue = (TextView)findViewById(R.id.textUnSatFatValue);
        textOthersValue = (TextView)findViewById(R.id.textOthersValue);
        textCholestrolValue = (TextView)findViewById(R.id.textCholestrolValue);
        textPottasiumValue = (TextView)findViewById(R.id.textPottasiumValue);

        addToDiary = findViewById(R.id.buttonDiary);
    }

    private void loadUI(Food food)
    {
        textCarbValue.setText(food.getCarbohydrates());
        textProteinValue.setText(food.getProtein());
        textFiberValue.setText(food.getFiber());
        textSugarValue.setText(food.getSugar());
        textFatValue.setText(food.getFat());
        textSatFatValue.setText(food.getSatFat());
        textUnSatFatValue.setText(food.getUnSatFat());
        textCholestrolValue.setText(food.getOtherCholestrol());
        textPottasiumValue.setText(food.getOtherPottasium());
    }

    private void addToDiary(){

        Intent intent = getIntent();
        String userIntent = intent.getStringExtra("userIntent");
        String email = intent.getStringExtra("email");
        selectedFood.setCategory(userIntent);
        addFoodItems(selectedFood,email,date);

        Intent menuIntent = new Intent(addItem.this,menu.class);

        if(userIntent!=null)
        {
            switch (userIntent)
            {
                case "breakfast":
                    menuIntent.putExtra("BreakfastCaloriesLeft","123");
                    break;
                case "lunch":
                    menuIntent.putExtra("LunchCaloriesLeft","123");
                    break;
                case "dinner":
                    menuIntent.putExtra("DinnerCaloriesLeft","123");
                    break;
                case "snack":
                    menuIntent.putExtra("SnackCaloriesLeft","123");
                    break;
                default:
                    break;
            }
        }
        addItem.this.startActivity(menuIntent);
    }

    private void addFoodItems(Food food, String email,String date)
    {


        Firebase myChild = firebase.child("foodItem");

        FoodItem foodItem = new FoodItem(email, food, date);
        foodItem.setEmail_food_date(email+"_"+food.getName()+"_"+date);
        myChild.child(firebase.push().getKey()).setValue(foodItem);
        Toast.makeText(this,"FoodItem Added",Toast.LENGTH_LONG).show();
    }

}
