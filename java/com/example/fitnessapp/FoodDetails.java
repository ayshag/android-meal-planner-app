package com.example.fitnessapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FoodDetails extends AppCompatActivity {
    private boolean updated = false;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = FoodDetails.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("View")) {
                    intent = new Intent(context, PreviewFood.class);
                    context.startActivity(intent);
                }
                else if(tab.getText().toString().equals("Add")){
                    intent = new Intent(context, AddFood.class);
                    context.startActivity(intent);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(FoodDetails.this,MainActivity.class);
                    context.startActivity(intent);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
            }
        });

        //final DatabaseHelper db = new DatabaseHelper(FoodDetails.this);
        final String name =  getIntent().getStringExtra("Food Name");
        //final Cursor res = db.retrieveDetails(db.getWritableDatabase(),name,"username");

        final TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        final TextView caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        final TextView carbohydratesTextView = (TextView) findViewById(R.id.carbohydratesTextView);
        final TextView proteinTextView = (TextView) findViewById(R.id.proteinTextView);
        final TextView fatTextView = (TextView) findViewById(R.id.fatTextView);
        final TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        final TextView fiberTextView = (TextView) findViewById(R.id.fiberTextView);
        final TextView sugarTextView = (TextView) findViewById(R.id.sugarTextView);
        final TextView satFatTextView = (TextView) findViewById(R.id.satFatTextView);
        final TextView unSatFatTextView = (TextView) findViewById(R.id.unSatFatTextView);
        final TextView cholestrolTextView = (TextView) findViewById(R.id.cholestrolTextView);

        final TextView pottasiumTextView = (TextView) findViewById(R.id.pottasiumTextView);


        nameTextView.setText("Name: " + name);

        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("food");
        Log.i("PrintLog", "FD Created");

        database.orderByChild("username_food").equalTo(firebaseAuth.getCurrentUser().getEmail() + "_" + name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    final Food food =   data.getValue(Food.class);
                    caloriesTextView.setText("Calories: " + food.getCalories());
                    carbohydratesTextView.setText("Carbohydrates: " + food.getCarbohydrates());
                    proteinTextView.setText("Protein: " + food.getFat());
                    fatTextView.setText("Fat: "+ food.getFat());
                    categoryTextView.setText("Category: "+ food.getCategory());
                    fiberTextView.setText("Fiber: "+ food.getFiber());
                    sugarTextView.setText("Sugars: "+ food.getSugar());
                    satFatTextView.setText("Saturated Fat: "+ food.getSatFat());
                    unSatFatTextView.setText("Unsaturated Fat: "+ food.getUnSatFat());
                    cholestrolTextView.setText("Cholestrol: "+ food.getOtherCholestrol());
                    pottasiumTextView.setText("Pottasium: "+ food.getOtherPottasium());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*
        while (res.moveToNext()) {

            FoodDetails.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    caloriesTextView.setText(caloriesTextView.getText().toString() + res.getString(0));
                    carbohydratesTextView.setText(carbohydratesTextView.getText().toString() + res.getString(1));
                    proteinTextView.setText(proteinTextView.getText().toString() + res.getString(2));
                    fatTextView.setText(fatTextView.getText().toString() + res.getString(3));

                }
            });
        }*/

        Button editButton = (Button) findViewById(R.id.editButton);

        editButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Context context = FoodDetails.this;
                ScrollView scrollView = new ScrollView(context);

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                scrollView.removeAllViews();
                layout.removeAllViews();

                final TextView dialogName = new TextView(context);
                dialogName.setText("Name");
                layout.addView(dialogName);
                final EditText nameEditText = new EditText(context);
                layout.addView(nameEditText);

                final TextView dialogCalories = new TextView(context);
                dialogCalories.setText("Calories");
                layout.addView(dialogCalories);
                final EditText caloriesEditText = new EditText(context);
                layout.addView(caloriesEditText);

                final TextView dialogCarbohydrates = new TextView(context);
                dialogCarbohydrates.setText("Carbohydrates");
                layout.addView(dialogCarbohydrates);
                final EditText carbohydratesEditText = new EditText(context);
                layout.addView(carbohydratesEditText);

                final TextView dialogProtein = new TextView(context);
                dialogProtein.setText("Protein");
                layout.addView(dialogProtein);
                final EditText proteinEditText = new EditText(context);
                layout.addView(proteinEditText);

                final TextView dialogFat = new TextView(context);
                dialogFat.setText("Fat");
                layout.addView(dialogFat);
                final EditText fatEditText = new EditText(context);
                layout.addView(fatEditText);

                final TextView dialogCategory = new TextView(context);
                dialogCategory.setText("Recommended Meal");
                layout.addView(dialogCategory);
                final EditText categoryEditText = new EditText(context);
                layout.addView(categoryEditText);

                final TextView dialogFiber = new TextView(context);
                dialogFiber.setText("Fiber");
                layout.addView(dialogFiber);
                final EditText FiberEditText = new EditText(context);
                layout.addView(FiberEditText);

                final TextView dialogSugars = new TextView(context);
                dialogSugars.setText("Sugars");
                layout.addView(dialogSugars);
                final EditText SugarsEditText = new EditText(context);
                layout.addView(SugarsEditText);

                final TextView dialogSatFat = new TextView(context);
                dialogSatFat.setText("Saturated Fat");
                layout.addView(dialogSatFat);
                final EditText SatfatEditText = new EditText(context);
                layout.addView(SatfatEditText);

                final TextView dialogUnSatFat = new TextView(context);
                dialogUnSatFat.setText("Unsaturated Fat");
                layout.addView(dialogUnSatFat);
                final EditText UnSatfatEditText = new EditText(context);
                layout.addView(UnSatfatEditText);

                final TextView dialogCholestrol = new TextView(context);
                dialogCholestrol.setText("Cholestrol");
                layout.addView(dialogCholestrol);
                final EditText cholestrolEditText = new EditText(context);
                layout.addView(cholestrolEditText);

                final TextView dialogPottasium = new TextView(context);
                dialogPottasium.setText("Pottasium");
                layout.addView(dialogPottasium);
                final EditText pottasiumEditText = new EditText(context);
                layout.addView(pottasiumEditText);
                scrollView.addView(layout);
               // scrollView.addView(layout);

                // dialog.setView(layout); // Again this is a set method, not add
              /*
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editLayout.setLayoutParams(lp);
*/
                new AlertDialog.Builder(FoodDetails.this)
                        .setTitle("Edit Food")
                        .setView(scrollView)
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                       // final DatabaseHelper db = new DatabaseHelper(FoodDetails.this);

                                        final String foodName = getIntent().getStringExtra("Food Name");


                                        //  Button editFoodButton = (Button) findViewById(R.id.editFoodButton);


                                        //editFoodButton.setOnClickListener( new View.OnClickListener() {

                                        //  @Override
                                        // public void onClick(View v) {
                                        // final Cursor res = db.retrieve(db.getWritableDatabase(),"username");
                                        final String inputFood = nameEditText.getText().toString();
                                        final String inputCalories = caloriesEditText.getText().toString();
                                        final String inputCarbohydrates = carbohydratesEditText.getText().toString();
                                        final String inputProtein = proteinEditText.getText().toString();
                                        final String inputFat = fatEditText.getText().toString();
                                        final String inputFiber = FiberEditText.getText().toString();
                                        final String inputSugar = SugarsEditText.getText().toString();
                                        final String inputSatFat = SatfatEditText.getText().toString();
                                        final String inputUnSatFat = UnSatfatEditText.getText().toString();
                                        final String inputCholestrol = cholestrolEditText.getText().toString();
                                        final String inputCategory = categoryEditText.getText().toString();
                                        final String inputPottasium = pottasiumEditText.getText().toString();

                                        Log.i("PrintLog", "Username_Food" + firebaseAuth.getCurrentUser().getEmail() + "_" + name);
                                        //db.update(db.getWritableDatabase(),foodName, inputFood,inputCalories,inputCarbohydrates,inputProtein,inputFat);
                                        database.orderByChild("username_food").equalTo(firebaseAuth.getCurrentUser().getEmail() + "_" + name).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Log.i("PrintLog", "Found food with name " + name);
                                                Log.d("PrintLog", dataSnapshot.getValue().toString());
                                                Food food = new Food();
                                                Food currentfood = new Food();
                                                for(DataSnapshot data: dataSnapshot.getChildren()) {
                                                      currentfood = data.getValue(Food.class);
                                                }
                                                if (!inputFood.equals("")) {
                                                    food.setName(inputFood.toString());
                                                    food.setUsername_food(firebaseAuth.getCurrentUser().getEmail() + "_" + inputFood);
                                                }
                                                else
                                                {
                                                    food.setName(currentfood.getName());
                                                    food.setUsername_food(firebaseAuth.getCurrentUser().getEmail() + "_" + currentfood.getName());
                                                }
                                                if (!inputCalories.equals(""))
                                                    food.setCalories(inputCalories.toString());
                                                else
                                                    food.setCalories(currentfood.getCalories());

                                                if(!inputCarbohydrates.equals(""))
                                                    food.setCarbohydrates(inputCarbohydrates);
                                                else
                                                    food.setCarbohydrates(currentfood.getCarbohydrates());
                                                if(!inputProtein.equals(""))
                                                    food.setProtein(inputProtein);
                                                else
                                                    food.setProtein(currentfood.getProtein());
                                                if(!inputFat.equals(""))
                                                    food.setFat(inputFat);
                                                else
                                                    food.setFat(currentfood.getFat());
                                                if(!inputCategory.equals(""))
                                                    food.setCategory(inputCategory);
                                                else
                                                    food.setCategory(currentfood.getCategory());
                                                if(!inputFiber.equals(""))
                                                    food.setFiber(inputFiber);
                                                else
                                                    food.setFiber(currentfood.getFiber());
                                                if(!inputSugar.equals(""))
                                                    food.setSugar(inputSugar);
                                                else
                                                    food.setSugar(currentfood.getSugar());
                                                if(!inputSatFat.equals(""))
                                                    food.setSatFat(inputSatFat);
                                                else
                                                    food.setSatFat(currentfood.getSatFat());
                                                if(!inputUnSatFat.equals(""))
                                                    food.setUnSatFat(inputUnSatFat);
                                                else
                                                    food.setUnSatFat(currentfood.getUnSatFat());
                                                if(!inputCholestrol.equals(""))
                                                    food.setOtherCholestrol(inputCholestrol);
                                                else
                                                    food.setOtherCholestrol(currentfood.getOtherCholestrol());
                                                if(!inputPottasium.equals(""))
                                                    food.setOtherPottasium(inputPottasium);
                                                else
                                                    food.setOtherPottasium(currentfood.getOtherPottasium());

                                                food.setUsername(firebaseAuth.getCurrentUser().getEmail());
                                                food.setUserUid(firebaseAuth.getUid());

                                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    String dsfood = ds.child("name").getValue().toString();
                                                    Log.i("PrintLog + Current name", dsfood);
                                                    ds.getRef().setValue(food);
                                                }

                                            }



                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });





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



                                Toast.makeText(FoodDetails.this, "Food Option Updated", Toast.LENGTH_LONG).show();

                                if(!inputFood.equals(""))
                                {
                                    nameTextView.setText("Name : " + inputFood);
                                }
                                if(!inputCalories.equals(""))
                                {
                                    caloriesTextView.setText("Calories : " + inputCalories);
                                }
                                if(!inputCarbohydrates.equals(""))
                                {
                                    carbohydratesTextView.setText("Carbohydrates : " + inputCarbohydrates);
                                }
                                if(!inputProtein.equals(""))
                                {
                                    proteinTextView.setText("Protein : " + inputProtein);
                                }
                                if(!inputFat.equals(""))
                                {
                                    fatTextView.setText("Fat : " + inputFat);
                                }
                                if(!inputFiber.equals(""))
                                {
                                    fiberTextView.setText("Fiber : " + inputFiber);
                                }
                                if(!inputSugar.equals(""))
                                {
                                    sugarTextView.setText("Sugar : " + inputSugar);
                                }
                                if(!inputSatFat.equals(""))
                                {
                                    satFatTextView.setText("Saturated Fat : " + inputSatFat);
                                }
                                if(!inputUnSatFat.equals(""))
                                {
                                    unSatFatTextView.setText("Unsaturated Fat : " + inputUnSatFat);
                                }
                                if(!inputCholestrol.equals(""))
                                {
                                    cholestrolTextView.setText("Cholestrol : " + inputCholestrol);
                                }
                                if(!inputPottasium.equals(""))
                                {
                                    pottasiumTextView.setText("Pottasium : " + inputPottasium);
                                }
                                if(!inputCategory.equals(""))
                                {
                                    categoryTextView.setText("Category : " + inputCategory);
                                }
                                        /*
                                Intent intent = getIntent();
                                if(!inputFood.equals(""))
                                     intent.putExtra("Food Name", inputFood);

                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                    //}
                                //});

*/
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(FoodDetails.this, "Canceled", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
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


        if(id==R.id.action_home)
        {
            Context context = FoodDetails.this;
            Intent intent = new Intent(context,MainActivity.class);

            context.startActivity(intent);
        }
        if(id == R.id.action_add_food)
        {
            Context context = FoodDetails.this;
            Intent intent = new Intent(context,AddFood.class);

            context.startActivity(intent);

        }

        if(id==R.id.action_preview_food)
        {
            Context context = FoodDetails.this;
            Intent intent = new Intent(context,PreviewFood.class);

            context.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
