package com.example.fitnessapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText email,pass;
    private Button btLogin;
    private TextView tvSignUp;
    private TextView tvForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
   // DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("PrintLog", "MainActivity#onCreate");
        FirebaseApp.initializeApp(MainActivity.this);
        validate();

        firebaseAuth = FirebaseAuth.getInstance();
        Log.i("PrintLog", "firebaseAuth: " + firebaseAuth);
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
       /* if(user !=null) {
            Log.i("PrintLog", "user not null");
            finish();
            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            if(user.getDisplayName().equals("admin")) {
                Intent dataIntent = new Intent(MainActivity.this, DietitianHome.class);
                dataIntent.putExtra("email", email.getText().toString().trim());
                startActivity(dataIntent);
            }
            //Start user activity
            else
            {
                Intent dataIntent = new Intent(MainActivity.this, menu.class);
                if(user.getEmail()!=null)
                    dataIntent.putExtra("email", user.getEmail().toString().trim());
                else
                    System.out.println("============Email Address is Null!!!=============");
                startActivity(dataIntent);
            }

        }*/
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignIn", "Button Clicked");
               // startActivity(new Intent(MainActivity.this,DietitianHome.class));
                validateUser(email.getText().toString().trim(),pass.getText().toString().trim());



            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PasswordReset.class);
                if(!email.getText().toString().trim().isEmpty()) {
                    intent.putExtra("email", email.getText().toString().trim());
                }
                startActivity(intent);
            }
        });
        /*btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   // sendOnChanel();
//                Calendar calendar =    Calendar.getInstance();
//                calendar.set(
//                        calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH),9,40,0
//                );
//                setAlarm(calendar.getTimeInMillis());
            }
        });*/
      // setAlarm(12,07);
        setAlarm(2,01);

    }
    public void setAlarm(int hour, int minute){
        Calendar calendar =    Calendar.getInstance();
       // calendar.add(Calendar.MINUTE,2);



        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),hour,minute,0
        );
        calendar.set(Calendar.AM_PM,1);
        //calendar.add(Calendar.SECOND,2);
        //setAlarm(calendar.getTimeInMillis());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }
//    public void sendOnChanel(){
//
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1,nb.build());
//    }
    private void validate(){
        email = (EditText)findViewById(R.id.etEmail);
        pass = (EditText)findViewById(R.id.etPass);
        btLogin = (Button)findViewById(R.id.btnLogin);
        tvSignUp = (TextView)findViewById(R.id.tvSignUP);
        tvForgotPassword =(TextView)findViewById(R.id.tvForgotPassword);
    }

    private void validateUser(final String email, String password){
        Log.d("SignIn", "Validating");
        progressDialog.setMessage("You are about to enter the world of fitness.");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                    if(user.getDisplayName()!=null && user.getDisplayName().equals("admin")) {
                        Intent dataIntent = new Intent(MainActivity.this, DietitianHome.class);
                        dataIntent.putExtra("email", email);
                        startActivity(dataIntent);
                    }
                    //Start user activity
                    else
                    {
                        Intent dataIntent = new Intent(MainActivity.this, menu.class);
                        dataIntent.putExtra("email", email);
                        startActivity(dataIntent);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

}
