package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private TextView tvWelcome;
    private Button btnLogOut;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvWelcome = (TextView)findViewById(R.id.tvWelcome);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Home.this,MainActivity.class));
            }
        });
        String userEmail =getIntent().getStringExtra("email");
        tvWelcome.setText("Welcome User: " + userEmail);

    }
private void Logout(){
    firebaseAuth.signOut();
    finish();
    startActivity(new Intent(Home.this,MainActivity.class));
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutmenu:
                    Logout();
                break;
            case R.id.editProfile:
                startActivity(new Intent(Home.this,UserProfile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

