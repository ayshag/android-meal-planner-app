package com.example.fitnessapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PreviewFood extends AppCompatActivity {
    private ArrayList<String> foodarray = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final ListView listview = (ListView)findViewById(R.id.food_list);
        final CustomAdapter adapter = new CustomAdapter(foodarray,PreviewFood.this);

        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = PreviewFood.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, DietitianHome.class);
                }
                else if(tab.getText().toString().equals("Add")){
                    intent = new Intent(context, AddFood.class);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                  //  startActivity(new Intent(PreviewFood.this,MainActivity.class));
                    intent = new Intent(PreviewFood.this,MainActivity.class);
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
                Context context = PreviewFood.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, DietitianHome.class);
                }
                else if(tab.getText().toString().equals("Add")){
                    intent = new Intent(context, AddFood.class);
                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    //  startActivity(new Intent(PreviewFood.this,MainActivity.class));
                    intent = new Intent(PreviewFood.this,MainActivity.class);
                }

                context.startActivity(intent);
            }
        });


        /*PreviewFood.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listview.setAdapter(new CustomAdapter(foodarray, PreviewFood.this));
            }});
*/
      //  final DatabaseHelper db = new DatabaseHelper(PreviewFood.this);

      //  final Cursor res = db.retrieve(db.getWritableDatabase(),"username");
        StringBuffer buffer = new StringBuffer();
/*
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference(firebaseAuth.getUid());

        database.orderByChild("food").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("PrintLog", "Database#onChildAdded: " + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("PrintLog", "Database#onCancelled: " + databaseError.getMessage(), databaseError.toException());
            }
        });*/
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("food");
      //  Log.i("PrintLog", ref.child("userUid").;
        Log.i("PrintLog PF CurrentUser",firebaseAuth.getCurrentUser().getEmail());

        ref.orderByChild("username").equalTo(firebaseAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Food food = ds.getValue(Food.class);
                    //String food =  ds.getValue().toString();

                    PreviewFood.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Log.i("PrintLog",food.getname());
                            adapter.addItem(food.getName());
                            adapter.notifyDataSetChanged();

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listview.setAdapter(adapter);
                                                                                                           }
      /*  ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("PrintLog", "Inside DataChanged");
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Food food = ds.getValue(Food.class);
                    //String food =  ds.getValue().toString();

                    PreviewFood.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("PrintLog",food.getname());
                            adapter.addItem(food.getname());
                            adapter.notifyDataSetChanged();

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventListener);*/

       /* ref.orderByChild("food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("PrintLog", "Retreving Value" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                   // Food food = postSnapshot.getValue(Food.class);
                    Log.i("Get Data", postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Food> foodlist = new ArrayList<Food>();
                        // Result will be holded Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                      //      foodlist.add((Food)dsp.getValue()); //add result into array list
                       Log.i("RetrievedItem", dsp.getValue().toString());
                           // adapter.addItem(dsp.getValue().toString());
                           // adapter.notifyDataSetChanged();
                        //    adapter.addItem(((Food) dsp.getValue()).getname());
                         //   adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });*/
        /*while (res.moveToNext()) {

            PreviewFood.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    adapter.addItem(res.getString(0));
                    adapter.notifyDataSetChanged();

                }
            });
        }*/

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
            Context context = PreviewFood.this;
            Intent intent = new Intent(context,MainActivity.class);

            context.startActivity(intent);

        }

        if(id==R.id.action_add_food)
        {
            Context context = PreviewFood.this;
            Intent intent = new Intent(context,AddFood.class);

            context.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}

