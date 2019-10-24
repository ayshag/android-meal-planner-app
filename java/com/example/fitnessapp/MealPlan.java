package com.example.fitnessapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealPlan extends AppCompatActivity {

    ArrayList<String> itemList = new ArrayList<>();
    Firebase firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private static final String TAG = "MealPlan";

    private Button mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView recommendation;
    private DonutProgress donutProgress;
    TextView calConsumed;
    private Integer cals = new Integer(0);
    private Integer totalCalRecommendation = new Integer(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        recommendation = (TextView) findViewById(R.id.mealCalRec);

        Intent intent = getIntent();
        final String mealPlan = intent.getStringExtra("mealPlan");
        //itemList = intent.getStringArrayListExtra("itemList");

        switch (mealPlan){
            case "breakfast":
                recommendation.setText("Breakfast Calorie Recommendation:  300-400 CALS");
                totalCalRecommendation = 400;
                break;
            case "lunch":
                recommendation.setText("Lunch Calorie Recommendation: 500-700 CALS");
                totalCalRecommendation = 700;
                break;
            case "dinner":
                recommendation.setText("Dinner Calorie Recommendation: 500-700 CALS");
                totalCalRecommendation = 700;
                break;
            case "snack":
                recommendation.setText("Snack Calorie Recommendation: 200 CALS");
                totalCalRecommendation =200;
                break;
        }

        final ListView listView = (ListView)findViewById(R.id.mealPlan);
        CustomMealPlanAdapter adapter = new CustomMealPlanAdapter(itemList,MealPlan.this);

        Firebase.setAndroidContext(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebase = new Firebase("https://fitnessapp-74eb3.firebaseio.com/");
        firebaseUser = firebaseAuth.getCurrentUser();

        mDisplayDate = (Button) findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MealPlan.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        final List<String> dateList = new ArrayList<String>();
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                dateList.add(date);
                mDisplayDate.setText(date);
               // final CustomAdapter adapter = new CustomAdapter(new ArrayList<String>(),MealPlan.this);
                final CustomMealPlanAdapter adapter = new CustomMealPlanAdapter(itemList,MealPlan.this);
                adapter.setSelectedDate(date);
                listView.setAdapter(adapter);
                displayCurrentPlan(mealPlan,adapter,listView,date);
            }
        };



        TabLayout tablayout = (TabLayout)findViewById(R.id.usertabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = MealPlan.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                    context.startActivity(intent);
                }
                else if(tab.getText().toString().equals("Share")) {
                    //Log.d("PrintLog", adapter.getItem(0).toString());
                    String message = "My " + mealPlan + " meal plan for " + dateList.get(0)+"\n";
                    System.out.println("ItemList Size::"+itemList.size());
                    for(int i = 0 ; i< itemList.size(); i++)
                    {
                        message += "\n" + itemList.get(i).toString();
                    }


                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, message);

                    startActivity(Intent.createChooser(intent, "Title of the dialog the system will open"));
                }
                else  if(tab.getText().toString().equals("Alert")) {

                    LinearLayout layout = new LinearLayout(context);
                  /*  final TextView notification = new TextView(context);
                    notification.setTextSize(20);
                    notification.setText("Please Set Your Custom Reminder");
                    layout.addView(notification);

                    final EditText notificationEditText = new EditText(context);
                    layout.addView(notificationEditText);

*/
  /*                  final TextView time = new TextView(context);
                    time.setTextSize(20);
                    time.setText("Please Enter Time");

                    layout.addView(time);
                    final EditText hourEditText = new EditText(context);
                    layout.addView(hourEditText);
                    final TextView seperator = new TextView(context);
                    seperator.setText(":");
                    seperator.setTextSize(20);
                    layout.addView(seperator);
                    final EditText minuteEditText = new EditText(context);
                    layout.addView(minuteEditText);*/
                    final TimePicker timePicker = new TimePicker(context);
                    layout.addView(timePicker);



                    new AlertDialog.Builder(MealPlan.this)
                            .setTitle("Set Notification")
                            .setView(layout)
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Log.d("PrintLog","Test Notification 2");
                                    //  setAlarm(Integer.parseInt(hourEditText.getText().toString()),Integer.parseInt(minuteEditText.getText().toString()));
                                    setAlarm(timePicker.getHour(),timePicker.getMinute());

                                    Toast.makeText(MealPlan.this, "Notification Set", Toast.LENGTH_LONG).show();


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MealPlan.this, "Canceled", Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();



                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(MealPlan.this,MainActivity.class);
                    context.startActivity(intent);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = MealPlan.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                    context.startActivity(intent);
                }
                else if(tab.getText().toString().equals("Share")) {
                    String message = "My Meal Plan for " + dateList.get(0) + "\n";
                    for (int i = 0; i < itemList.size(); i++) {
                        message += "\n" + itemList.get(i).toString();
                    }


                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, message);

                    startActivity(Intent.createChooser(intent, "Title of the dialog the system will open"));
                }
                else  if(tab.getText().toString().equals("Alert")) {

                    LinearLayout layout = new LinearLayout(context);
                 /*   final TextView notification = new TextView(context);
                    notification.setTextSize(20);
                    notification.setText("Please Set Your Custom Reminder");
                    layout.addView(notification);

                    final EditText notificationEditText = new EditText(context);
                    layout.addView(notificationEditText);

*/
             /*      final TextView time = new TextView(context);
                    time.setTextSize(20);
                    time.setText("Please Enter Time");

                    layout.addView(time);
                    final EditText hourEditText = new EditText(context);
                    layout.addView(hourEditText);
                    final TextView seperator = new TextView(context);
                    seperator.setText(":");
                    seperator.setTextSize(20);
                    layout.addView(seperator);
                    final EditText minuteEditText = new EditText(context);
                    layout.addView(minuteEditText);*/
                    final TimePicker timePicker = new TimePicker(context);
                    layout.addView(timePicker);


                    new AlertDialog.Builder(MealPlan.this)
                            .setTitle("Set Notification")
                            .setView(layout)
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("PrintLog","Test Notification 1");
                                    //setAlarm(Integer.parseInt(hourEditText.getText().toString()),Integer.parseInt(minuteEditText.getText().toString()));
                                    setAlarm(timePicker.getHour(),timePicker.getMinute());

                                    Toast.makeText(MealPlan.this, "Notification Set", Toast.LENGTH_LONG).show();


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MealPlan.this, "Canceled", Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();



                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(MealPlan.this,MainActivity.class);
                    context.startActivity(intent);
                }


            }
        });

        //itemList.add("TEst");


    }


    public void displayCurrentPlan(final String mealPlan,final CustomMealPlanAdapter adapter,final ListView listView, final String date)
    {
        firebase.child("foodItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.reset();
                cals = new Integer(0);
                for (com.firebase.client.DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    final FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    System.out.println("=====Value Found======="+foodItem.getFood().getName());
                    System.out.println("=====Meal Plan======="+foodItem.getFood().getCategory());
                    //itemList.add(foodItem.getName());
                    MealPlan.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(foodItem.getEmail()!=null) {
                                if (mealPlan.toLowerCase().equalsIgnoreCase(foodItem.getFood().getCategory()) &&
                                        firebaseUser.getEmail().toLowerCase().equalsIgnoreCase(foodItem.getEmail().toLowerCase()) &&
                                        date.toLowerCase().equalsIgnoreCase(foodItem.getDate())) {
                                    Log.i("PrintLog", foodItem.getFood().getName());
                                    cals+=Integer.parseInt(foodItem.getFood().getCalories());
                                    setCalories(cals);
                                    System.out.println("Cals Consumned::"+String.valueOf(cals));
                                    System.out.println("=====Value Found After Condition=======" + foodItem.getFood().getName());
                                    itemList.add(foodItem.getFood().getName() + "     " +foodItem.getFood().getCalories()+" CALS");
                                    adapter.addItem(foodItem.getFood().getName() + "     " +foodItem.getFood().getCalories()+" CALS");
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView.setAdapter(adapter);
    }
    public void setAlarm(int hour, int minute){
        Calendar calendar =    Calendar.getInstance();
        // calendar.add(Calendar.MINUTE,2);
        Log.d("PrintLog", "Setting Alarm");
        //  if(hour == 0)
        //    hour = 12;

        Log.d("PrintLog","Hour :" + hour + " Minute : " + minute);


        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),hour,minute,0
        );

        if(hour>=0 && hour<=12)
            calendar.set(Calendar.AM_PM,0);
        else
            calendar.set(Calendar.AM_PM,1);
        //calendar.add(Calendar.SECOND,2);
        //setAlarm(calendar.getTimeInMillis());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    public void setCalories(Integer cals){
        calConsumed = (TextView) findViewById(R.id.mealCalCons);
        donutProgress = (DonutProgress)findViewById(R.id.donut_progress);
        if (cals>totalCalRecommendation) {
            calConsumed.setText("You are " + String.valueOf( cals - totalCalRecommendation) + " CALS ahead of Recommendaion");
            donutProgress.setProgress((100));

        }
        else {
            calConsumed.setText("You have consumed " + String.valueOf(cals) + " CALS");
            donutProgress.setProgress((cals*100)/totalCalRecommendation);
        }

    }
}


/*
package com.example.fitnessapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealPlan extends AppCompatActivity {

    ArrayList<String> itemList = new ArrayList<>();
    Firebase firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private static final String TAG = "MealPlan";

    private Button mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        Intent intent = getIntent();
        final String mealPlan = intent.getStringExtra("mealPlan");
        //itemList = intent.getStringArrayListExtra("itemList");

        final ListView listView = (ListView)findViewById(R.id.mealPlan);
        final CustomAdapter adapter = new CustomAdapter(itemList,MealPlan.this);

        Firebase.setAndroidContext(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebase = new Firebase("https://healthapp-de69e.firebaseio.com/");
        firebaseUser = firebaseAuth.getCurrentUser();

        mDisplayDate = (Button) findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MealPlan.this,
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
                final CustomAdapter adapter = new CustomAdapter(new ArrayList<String>(),MealPlan.this);
                listView.setAdapter(adapter);
                displayCurrentPlan(mealPlan,adapter,listView,date);
            }
        };



        TabLayout tablayout = (TabLayout)findViewById(R.id.usertabs);
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = MealPlan.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                    context.startActivity(intent);
                }
                else if(tab.getText().toString().equals("Share")) {
                    //Log.d("PrintLog", adapter.getItem(0).toString());
                    String message = "My " + mealPlan + " meal plan: ";
                    for(int i = 0 ; i< adapter.getCount(); i++)
                    {
                        message += "\n" + adapter.getItem(i).toString();
                    }


                    Intent mealIntent = new Intent(Intent.ACTION_SEND);
                    mealIntent.setType("text/plain");
                    mealIntent.putExtra("Meal Plan", message);

                    intent = Intent.createChooser(mealIntent, "Open using one of the following apps: ");
                    context.startActivity(intent);
                 }
                else  if(tab.getText().toString().equals("Alert")) {

                    LinearLayout layout = new LinearLayout(context);
                  */
/*  final TextView notification = new TextView(context);
                    notification.setTextSize(20);
                    notification.setText("Please Set Your Custom Reminder");
                    layout.addView(notification);

                    final EditText notificationEditText = new EditText(context);
                    layout.addView(notificationEditText);

*//*

  */
/*                  final TextView time = new TextView(context);
                    time.setTextSize(20);
                    time.setText("Please Enter Time");

                    layout.addView(time);
                    final EditText hourEditText = new EditText(context);
                    layout.addView(hourEditText);
                    final TextView seperator = new TextView(context);
                    seperator.setText(":");
                    seperator.setTextSize(20);
                    layout.addView(seperator);
                    final EditText minuteEditText = new EditText(context);
                    layout.addView(minuteEditText);*//*

                   final TimePicker timePicker = new TimePicker(context);
                   layout.addView(timePicker);



                    new AlertDialog.Builder(MealPlan.this)
                            .setTitle("Set Notification")
                            .setView(layout)
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Log.d("PrintLog","Test Notification 2");
                                  //  setAlarm(Integer.parseInt(hourEditText.getText().toString()),Integer.parseInt(minuteEditText.getText().toString()));
                                    setAlarm(timePicker.getHour(),timePicker.getMinute());

                                    Toast.makeText(MealPlan.this, "Notification Set", Toast.LENGTH_LONG).show();


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MealPlan.this, "Canceled", Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();



                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(MealPlan.this,MainActivity.class);
                    context.startActivity(intent);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PrintLog", tab.getText().toString());
                Context context = MealPlan.this;
                Intent intent  = null;
                if(tab.getText().toString().equals("Home")) {
                    intent = new Intent(context, menu.class);
                    context.startActivity(intent);
                }
                else if(tab.getText().toString().equals("Share")) {
                    String message = "My Meal Plan: ";
                    for (int i = 0; i < adapter.getCount(); i++) {
                        message += "\n" + adapter.getItem(i).toString();
                    }

                    Intent mealIntent = new Intent(Intent.ACTION_SEND);
                    mealIntent.setType("text/plain");
                    mealIntent.putExtra("Meal Plan", message);

                    intent = Intent.createChooser(mealIntent, "Open using one of the following apps:");
                    context.startActivity(intent);
                }
               else  if(tab.getText().toString().equals("Alert")) {

                    LinearLayout layout = new LinearLayout(context);
                 */
/*   final TextView notification = new TextView(context);
                    notification.setTextSize(20);
                    notification.setText("Please Set Your Custom Reminder");
                    layout.addView(notification);

                    final EditText notificationEditText = new EditText(context);
                    layout.addView(notificationEditText);

*//*

             */
/*      final TextView time = new TextView(context);
                    time.setTextSize(20);
                    time.setText("Please Enter Time");

                    layout.addView(time);
                    final EditText hourEditText = new EditText(context);
                    layout.addView(hourEditText);
                    final TextView seperator = new TextView(context);
                    seperator.setText(":");
                    seperator.setTextSize(20);
                    layout.addView(seperator);
                    final EditText minuteEditText = new EditText(context);
                    layout.addView(minuteEditText);*//*

                    final TimePicker timePicker = new TimePicker(context);
                    layout.addView(timePicker);


                    new AlertDialog.Builder(MealPlan.this)
                            .setTitle("Set Notification")
                            .setView(layout)
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("PrintLog","Test Notification 1");
                                   //setAlarm(Integer.parseInt(hourEditText.getText().toString()),Integer.parseInt(minuteEditText.getText().toString()));
                                    setAlarm(timePicker.getHour(),timePicker.getMinute());

                                    Toast.makeText(MealPlan.this, "Notification Set", Toast.LENGTH_LONG).show();


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MealPlan.this, "Canceled", Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();



                }
                else if(tab.getText().toString().equals("Logout"))
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    finish();
                    intent = new Intent(MealPlan.this,MainActivity.class);
                    context.startActivity(intent);
                }


            }
        });

        //itemList.add("TEst");


    }


    public void displayCurrentPlan(final String mealPlan,final CustomAdapter adapter,final ListView listView, final String date)
    {
        firebase.child("foodItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (com.firebase.client.DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    final FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    System.out.println("=====Value Found======="+foodItem.getFood().getName());
                    System.out.println("=====Meal Plan======="+foodItem.getFood().getCategory());
                    //itemList.add(foodItem.getName());
                    MealPlan.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mealPlan.toLowerCase().equalsIgnoreCase(foodItem.getFood().getCategory()) &&
                                    firebaseUser.getEmail().toLowerCase().equalsIgnoreCase(foodItem.getEmail().toLowerCase()) &&
                                    date.toLowerCase().equalsIgnoreCase(foodItem.getDate()))
                            {
                                Log.i("PrintLog", foodItem.getFood().getName());
                                System.out.println("=====Value Found After Condition======="+foodItem.getFood().getName());
                                adapter.addItem(foodItem.getFood().getName());
                                adapter.notifyDataSetChanged();
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView.setAdapter(adapter);
    }
    public void setAlarm(int hour, int minute){
        Calendar calendar =    Calendar.getInstance();
        // calendar.add(Calendar.MINUTE,2);
        Log.d("PrintLog", "Setting Alarm");
      //  if(hour == 0)
        //    hour = 12;

        Log.d("PrintLog","Hour :" + hour + " Minute : " + minute);


        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),hour,minute,0
        );

        if(hour>=0 && hour<=12)
             calendar.set(Calendar.AM_PM,0);
        else
            calendar.set(Calendar.AM_PM,1);
        //calendar.add(Calendar.SECOND,2);
        //setAlarm(calendar.getTimeInMillis());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }
}
*/
