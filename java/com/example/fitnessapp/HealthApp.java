package com.example.fitnessapp;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

public class HealthApp extends Application {
    Firebase firebase;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(HealthApp.this);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://healthapp-de69e.firebaseio.com");

    }
}
