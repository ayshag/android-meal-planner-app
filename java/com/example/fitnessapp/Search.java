package com.example.fitnessapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity {

    MaterialSearchView materialSearchView;
    private FirebaseAuth firebaseAuth;
    ListView lstView;
    List<String> lstFound = new ArrayList<>();
    private ArrayList<String> foodarray = new ArrayList<>();
    List<String> lstSource = new ArrayList<>();
    HashMap<String,Food> foodMap = new HashMap<>();
    int lowCalorieLimit = 100, lowCarbLimit = 7, highProteinLimit = 7, lowFatLimit = 4;
    /* String[] lstSource = {
            "Brown Bread",
            "Wheat Bread",
            "Milk Bread",
            "Italian Bread",
            "Multi grain Bread",
            "Greek yogurt",
            "Full Fat yogurt"
    };*/
    private Button mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "SearchActivity";
    private ArrayList<String> history = new ArrayList<>();

    private ArrayList<String> recent = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

       android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Items");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        final CheckBox lowCalorieCheck = (CheckBox)findViewById(R.id.lowCalorieCheck);
        final CheckBox lowCarbsCheck = (CheckBox)findViewById(R.id.lowCarbsCheck);
        final CheckBox highProteinCheck = (CheckBox)findViewById(R.id.highProteinCheck);
        final CheckBox lowFatCheck = (CheckBox)findViewById(R.id.lowFatCheck);
        Button filterButton = (Button) findViewById(R.id.filterButton);
        loadUI();

        //Date Picker
        mDisplayDate = (Button) findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Search.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                    mDisplayDate.setText(date);
            }
        };



        Intent intent = getIntent();
        final String userIntent = intent.getStringExtra("userIntent");
        final CustomAdapter ca = new CustomAdapter(foodarray,Search.this);
        firebaseAuth = FirebaseAuth.getInstance();
        final String email = firebaseAuth.getCurrentUser().getEmail();
        final ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstSource);


        Firebase firebase  = new Firebase("https://fitnessapp-74eb3.firebaseio.com/");
        firebase.child("foodItem").addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                for (com.firebase.client.DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Log.d("PrintLog", "Size - "+ history.size());
                    final FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    System.out.println("=====Value Found======="+foodItem.getFood().getName());
                    System.out.println("=====Meal Plan======="+foodItem.getFood().getCategory());
                    //itemList.add(foodItem.getName());

                    if(firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase(foodItem.getEmail()) &&  foodItem.getFood().getCategory().equalsIgnoreCase(userIntent.toString())/*&&!history.contains(foodItem.getFood().getName())*/) {
                        Log.d("PrintLog","Adding Item " + foodItem.getFood().getName());
                        history.add(foodItem.getFood().getName());
                    }

                }
                Log.d("PrintLog", "Size - "+ history.size());

                for(int count = 1, i = history.size()-1; count<=3;i--)
                {

                    if(i<0)
                        break;
                    Log.d("PrintLog", "History - "+ history.get(i));
                    if(!recent.contains(history.get(i))) {
                        recent.add(history.get(i));
                      //  i--;
                        count++;
                    }
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("food");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        System.out.println("====Search======");
                        // Result will be holded Here

                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Log.i("PrintLog", "Food item " + dsp.getValue());
                            final Food food =   dsp.getValue(Food.class);
                            //boolean calorie = true, fat= true, protein = true, carbs = true;


                            //if( /*&& food.getCategory()!=null && food.getCategory().toLowerCase().equalsIgnoreCase(userIntent.toString().toLowerCase())*/)
                            if(recent.contains(food.getName()))
                            {
                                Log.d("PrintLog", food.getName()+ "          Recently Added");
                                lstSource.add(food.getName()+ "          Recently Added"); //add result into array list
                            }
                            else
                                lstSource.add(food.getName());
                            foodMap.put(food.getName(),food);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        filterButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstSource = new ArrayList<>();
                adapter.notifyDataSetChanged();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("food");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        System.out.println("====Search======");
                        // Result will be holded Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Log.i("PrintLog", "Food item " + dsp.getValue());
                            final Food food =   dsp.getValue(Food.class);
                            //boolean calorie = true, fat= true, protein = true, carbs = true;
                            boolean add = true;
                       //     Log.i("PrintLog", "lowCalorieCheck" +lowCalorieCheck.isChecked());
                            Log.i("PrintLog", "lowCalorieCheck" +food.getCalories());
                            Log.i("PrintLog", "lowCalorieCheck" +lowCalorieLimit);
                            if(lowCalorieCheck.isChecked() /*&& food.getCalories()!=null && !food.getCalories().equals("") */&& Integer.parseInt(food.getCalories())>lowCalorieLimit) {

                                //  calorie = false;
                                Log.i("PrintLog", "lowCalorieCheckLoop" +food.getCalories());
                                Log.i("PrintLog", "lowCalorieCheckLoop" +lowCalorieLimit);
                                add = false;
                            }
                            if(lowFatCheck.isChecked()&& !food.getFat().equals(""))
                            {
                                Log.i("PrintLog", "lowFatCheck" +food.getFat());
                                Log.i("PrintLog", "lowFatCheck" +lowFatLimit);
                                if( food.getFat().contains("g")) {
                                    if (Double.parseDouble(food.getFat().split("g")[0]) > lowFatLimit)
                                        add = false;
                                }
                                else if (Double.parseDouble(food.getFat()) > lowFatLimit)
                                    add = false;
                            }
                            if(lowCarbsCheck.isChecked() && !food.getCarbohydrates().equals("")&& Double.parseDouble(food.getCarbohydrates())>lowCarbLimit) {
                                Log.i("PrintLog", "lowCarbsCheck" +food.getCarbohydrates());
                                Log.i("PrintLog", "lowCarbsCheck" +lowCarbLimit);
                                //   carbs = false;
                                add = false;
                            }
                            if(highProteinCheck.isChecked() && !food.getProtein().equals(""))
                            {
                                Log.i("PrintLog", "highProteinCheck" +food.getProtein());
                                Log.i("PrintLog", "highProteinCheck" +highProteinLimit);
                                if(food.getProtein().contains("g")) {
                                    if (Double.parseDouble(food.getProtein().split("g")[0]) < highProteinLimit)
                                        add = false;
                                }
                                else if (Double.parseDouble(food.getProtein())<highProteinLimit)
                                    add = false;
                            }
                            Log.i("PrintLog", "add" +add);
                            //if(calorie && fat && carbs && protein)

                            if(add /*&& food.getCategory()!=null && food.getCategory().toLowerCase().equalsIgnoreCase(userIntent.toString().toLowerCase())*/) {
                              //  lstSource.add(food.getName()); //add result into array list
                                if(recent.contains(food.getName()))
                                {
                                    Log.d("PrintLog", food.getName()+ "          Recently Added");
                                    lstSource.add(food.getName()+ "          Recently Added"); //add result into array list
                                }
                                else
                                    lstSource.add(food.getName());
                                foodMap.put(food.getName(), food);
                            }

                        }
                        ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstSource);

                        lstView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            });

       /* TabLayout tablayout = (TabLayout)findViewById(R.id.usertabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){*/

           /* @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = Search.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                }

                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Search.this,MainActivity.class));
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
                Context context = Search.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                }

                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Search.this,MainActivity.class));
                }

                context.startActivity(intent);
            }
        });*/


        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                System.out.println("====SearchViewShown======"+lstSource.size() );

                lstView.setAdapter(adapter);
            }

            @Override
            public void onSearchViewClosed() {
                System.out.println("====SearchViewClosed======");
               // ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstSource);
              //  ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstSource);

                lstView.setAdapter(adapter);
            }
        });

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                System.out.println("=====================submit:::=============="+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText != null && !newText.isEmpty()){
                    lstFound = new ArrayList<String>();
                    for(String item: lstSource){
                        if(item.toLowerCase().contains(newText.toLowerCase()))
                            lstFound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstFound);
                    lstView.setAdapter(adapter);
                }
                else{
                    //If search text is null
                    //return source list
                 //   ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstSource);
                  //  ArrayAdapter adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1,lstSource);

                    lstView.setAdapter(adapter);
                }
                return true;
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String foodItem = "";
                if(lstFound!=null && lstFound.size()>0) {
                    System.out.println("===========Search from Filtered List::===============" + lstFound.get(position));
                    if(lstFound.get(position).contains("Recent"))
                        foodItem = lstFound.get(position).split("          Recently Added")[0];
                    else
                      foodItem = lstFound.get(position);
                }
                else if(lstSource!=null && lstSource.size()>0){
                    System.out.println("===========Search from Original List::===============" + lstSource.get(position));
                    if(lstSource.get(position).contains("Recent"))
                        foodItem = lstSource.get(position).split("          Recently Added")[0];
                   else
                        foodItem = lstSource.get(position);
                }


                Intent addItem = new Intent(Search.this, com.example.fitnessapp.addItem.class);
                addItem.putExtra("selectedItem",foodItem);
                addItem.putExtra("userIntent",userIntent);
                addItem.putExtra("email",email);
                addItem.putExtra("foodMap",foodMap);
                addItem.putExtra("date",mDisplayDate.getText().toString());
                Search.this.startActivity(addItem);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }


    private void loadUI()
    {
        materialSearchView = (MaterialSearchView)findViewById(R.id.search_view);
        lstView = (ListView)findViewById(R.id.lstView);
    }
}
