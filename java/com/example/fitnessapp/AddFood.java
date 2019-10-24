package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFood extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("PrintLog", "AddFood activity onCreate");

        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = AddFood.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("View")) {
                    intent = new Intent(context, PreviewFood.class);
                }
                else if(tab.getText().toString().equals("Home")){
                    intent = new Intent(context, DietitianHome.class);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(AddFood.this,MainActivity.class);
                  //  startActivity(new Intent(AddFood.this,MainActivity.class));
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
                Context context = AddFood.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("View")) {
                    intent = new Intent(context, PreviewFood.class);
                }
                else if(tab.getText().toString().equals("Home")){
                    intent = new Intent(context, DietitianHome.class);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(AddFood.this,MainActivity.class);
                    //  startActivity(new Intent(AddFood.this,MainActivity.class));
                }

                context.startActivity(intent);
            }
        });




        //final DatabaseHelper db = new DatabaseHelper(AddFood.this);
        final EditText foodEditText = (EditText) findViewById(R.id.addEditText);
        final  EditText caloriesEditText = (EditText) findViewById(R.id.addCaloriesEditText);
        final  EditText carbohydratesEditText = (EditText) findViewById(R.id.addCarbohydratesEditText);
        final  EditText proteinEditText = (EditText) findViewById(R.id.addProteinEditText);
        final  EditText categoryEditText = (EditText) findViewById(R.id.addCategoryText);
        final  EditText fatEditText = (EditText) findViewById(R.id.addFatEditText);
        final  EditText fiberEditText = (EditText) findViewById(R.id.addFiberEditText);
        final  EditText sugarEditText = (EditText) findViewById(R.id.addSugarEditText);
        final  EditText satFatEditText = (EditText) findViewById(R.id.addSatFatEditText);
        final  EditText unSatFatEditText = (EditText) findViewById(R.id.addUnSatFatEditText);
        final  EditText cholestrolEditText = (EditText) findViewById(R.id.addOtherEditText);
        final  EditText pottasiumEditText = (EditText) findViewById(R.id.addOtherPottasiumEditText);
        Button addFoodButton = (Button) findViewById(R.id.addFoodButton);

        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.i("PrintLog", "AddFoodActivity created, with firebaseAuth: " + firebaseAuth.toString() + ", with user: " + firebaseAuth.getCurrentUser());

//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.i("PrintLog", "Database#onDataChange: " + dataSnapshot.getValue(Food.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.i("PrintLog", "Database#onCancelled: " + databaseError.getMessage(), databaseError.toException());
//            }
//        });

        addFoodButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // final Cursor res = db.retrieve(db.getWritableDatabase(),"username");
               Log.i("PrintLog", "addFoodButton#onClick. uid - " + firebaseAuth.getCurrentUser().getUid());

                Food food = new Food();
                Log.i("PrintLog", "database.child(food): " + database.child("food"));
                DatabaseReference dbreference = database.child("food").push();
                //Log.i("PrintLog", "database.child(food) pushed! Received id: " + id);
                food.setName(foodEditText.getText().toString());
                food.setCalories(caloriesEditText.getText().toString());
                food.setCarbohydrates(carbohydratesEditText.getText().toString());
                food.setProtein(proteinEditText.getText().toString());
                food.setFat(fatEditText.getText().toString());
                food.setCategory(categoryEditText.getText().toString());
                food.setFiber(fiberEditText.getText().toString());
                food.setSugar(sugarEditText.getText().toString());
                food.setSatFat(satFatEditText.getText().toString());
                food.setUnSatFat(unSatFatEditText.getText().toString());
                food.setOtherCholestrol(cholestrolEditText.getText().toString());
                food.setOtherPottasium(pottasiumEditText.getText().toString());
                food.setUserUid(firebaseAuth.getUid());
                food.setUsername_food(firebaseAuth.getCurrentUser().getEmail() + "_" + foodEditText.getText().toString());
                food.setUsername(firebaseAuth.getCurrentUser().getEmail());
                Log.i("PrintLog", "Food set: " + food.getUsername_food());

           /*database.child("food").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            data.getRef().removeValue();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                dbreference.setValue(food, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.e("PrintLog", "Database onComplete, error: " + databaseError.getMessage(), databaseError.toException());
                        }
                        Log.i("PrintLog", "Database onComplete, success: " + databaseReference.toString());

                    }
                });

                Log.i("PrintLog", "database set value called");
                //database.child("food").child(id).setValue(food);
                Toast.makeText(AddFood.this, "Food " + food.getName() + " Added", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddFood.this,PreviewFood.class);
                startActivity(intent);
               // finish();
/*
                String inputName = foodEditText.getText().toString();
                String inputCalories = caloriesEditText.getText().toString();
                String inputCarbohydrates = carbohydratesEditText.getText().toString();
                String inputProtein = proteinEditText.getText().toString();
                String inputFat = fatEditText.getText().toString();

                db.insert(db.getWritableDatabase(),inputName,inputCalories,inputCarbohydrates,inputProtein,inputFat,"username");
                Toast.makeText(AddFood.this, "Food Option Added For Users", Toast.LENGTH_LONG).show();

*/
            }
        });


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
        if(id == R.id.action_home)
        {
            Context context = AddFood.this;
            Intent intent = new Intent(context,MainActivity.class);

            context.startActivity(intent);

        }

        if(id==R.id.action_preview_food)
        {
            Context context = AddFood.this;
            Intent intent = new Intent(context,PreviewFood.class);

            context.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
