package com.example.fitnessapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText FullName,email,pass;
    private Button btnSignUp;
    private TextView tvLogin;
   private FirebaseAuth firebaseAuth;
   private CheckBox chkAdmin;



  //  DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        validate();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fuName = FullName.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPass = pass.getText().toString().trim();
                Boolean valid=false;
                if(fuName.isEmpty() || userEmail.isEmpty() || userPass.isEmpty()){
                    Toast.makeText(SignUp.this, "Fill Up all details.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if(chkAdmin.isChecked()) {
                                    UserProfileChangeRequest adminprofileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName("admin").build();


                                    user.updateProfile(adminprofileUpdates);
                                }else {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName("notadmin").build();

                                    user.updateProfile(profileUpdates);
                                }


                                String retval = sendUserData();
                                Toast.makeText(SignUp.this, "Registration Successful" + retval, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, com.example.fitnessapp.MainActivity.class));
                            }else{
                                Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }





            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUp.this, com.example.fitnessapp.MainActivity.class));
            }
        });



    }
    private void validate(){
        FullName = (EditText)findViewById(R.id.etFullProfileName);
        email = (EditText)findViewById(R.id.etUserEmail);
        pass = (EditText)findViewById(R.id.etPass);
        btnSignUp = (Button)findViewById(R.id.btnUpdate);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        chkAdmin = (CheckBox)findViewById(R.id.chkAdmin);
    }
    private String sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String retval = firebaseAuth.getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference(retval);
        User user = new User(email.getText().toString().trim(),pass.getText().toString().trim(),FullName.getText().toString().trim());
        if(databaseReference.setValue(user).isSuccessful()){
            firebaseAuth.signOut();
            retval = "success";
            Toast.makeText(this, "Uploading User Data Successful" + firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();
        }
        else {
            retval = "fail";
        }
        return retval;
    }
}
