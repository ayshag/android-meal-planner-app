package com.example.fitnessapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {
    private EditText etEmailReset;
    private Button btnResetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        etEmailReset = (EditText)findViewById(R.id.etEmailReset);
        String userEmail =getIntent().getStringExtra("email");
        if(userEmail!=null && !userEmail.isEmpty()){
            etEmailReset.setText(userEmail);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        btnResetPassword = (Button)findViewById(R.id.btnPasswordReset);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailReset.getText().toString().trim();
                
                if(email.isEmpty()){
                    Toast.makeText(PasswordReset.this, "Please enter your registered email id ", Toast.LENGTH_LONG).show();

                }else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordReset.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordReset.this,MainActivity.class));
                            }else{
                                Toast.makeText(PasswordReset.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
