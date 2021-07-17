package com.joan.makanikapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewProfileActivity extends AppCompatActivity {

    TextView firstname,lastname,email,phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        //create hooks
        firstname = findViewById(R.id.profile_displayfirstname);
        lastname = findViewById(R.id.profile_displaylastname);
        email = findViewById(R.id.profile_displayemail);
        phone_number = findViewById(R.id.profile_displayphonenumber);
        //show all data
        showAllUserData();

    }

    private void showAllUserData() {
        Intent intent = getIntent();
        String user_firstname = intent.getStringExtra("fname");
        String user_lastname = intent.getStringExtra("lname");
        String user_email = intent.getStringExtra("email");
        String user_phoneno = intent.getStringExtra("phoneno");

        firstname.setText(user_firstname);
        lastname.setText(user_lastname);
        email.setText(user_email);
        phone_number.setText(user_phoneno);


    }

    public void logOut(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}