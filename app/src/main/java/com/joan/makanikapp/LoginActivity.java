package com.joan.makanikapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import coding.insight.cleanuiloginregister.R;


public class LoginActivity extends AppCompatActivity {

        TextView email,password;
        Button login;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //for changing status bar icon colors
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            setContentView(R.layout.activity_login);

            //extract variables from layout file
            email = findViewById(R.id.editloginTextEmail);
            password = findViewById(R.id.editTextloginPassword);
            login = findViewById(R.id.button_login);

        }

        public void onLoginClick(View View){
            startActivity(new Intent(this,RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

        }
        private Boolean vallidateemail(){
            String val =email.getText().toString();
            String noWhiteSpace = "A\\w{4,20}\\z";

            if(val.isEmpty()){
                email.setError("Field Cannot be Empty");
                return false;
            }

            else {
                email.setError(null);
                email.setEnabled(false);
                return true;

            }

        }
        private Boolean validatepassword(){
            String val = password.getText().toString();

            if(val.isEmpty()){
                password.setError("Field Cannot be Empty");
                return false;

            }
            else{
                password.setError(null);

                return true;
            }

        }

         public void loginClick(View view) {
            if(!vallidateemail()||!validatepassword()){


            }
            else{
                isUser();
            }

    }

    private void isUser() {
            String userEnteredEmail = email.getText().toString().trim();
            String userEnteredPassword = password.getText().toString().trim();

        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("user");


        Query checkUser = reference.orderByChild("email").equalTo(userEnteredEmail);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    email.setError(null);

                    String passwordFromDB = snapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword)){
                        String firstnameFromDB = snapshot.child(userEnteredEmail).child("fname").getValue(String.class);
                        String lastnameFromDB = snapshot.child(userEnteredEmail).child("lname").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredEmail).child("email").getValue(String.class);
                        String phonenumberFromDB = snapshot.child(userEnteredEmail).child("phoneno").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), ViewProfileActivity.class);
                        intent.putExtra("fname",firstnameFromDB);
                        intent.putExtra("lname",lastnameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("phoneno",phonenumberFromDB);
                        intent.putExtra("password",passwordFromDB);

                        startActivity(intent);

                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus();

                    }

                }
                else {
                    email.setError("Email not Recognized");
                    email.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

