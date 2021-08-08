package com.joan.makanikapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {
    public CardView callMechanic,viewProfile,signAsMechanic,logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        callMechanic = findViewById(R.id.callMechanicCard);
        viewProfile = findViewById(R.id.viewProfileCard);
        signAsMechanic = findViewById(R.id.signAsMechanicCard);
        logout = findViewById(R.id.logoutCard);

        callMechanic.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        signAsMechanic.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.callMechanicCard:
                i = new Intent(this,NavigationDrawerActivity.class);
                startActivity(i);
                break;

            case R.id.viewProfileCard:
                i = new Intent(this,ViewProfileActivity.class);
                startActivity(i);
                break;

            case R.id.signAsMechanicCard:
                i = new Intent(this,MechanicRegisterActivity.class);
                startActivity(i);
                break;

            case R.id.logoutCard:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                i = new Intent(this,LoginActivity.class);
                startActivity(i);
                break;
        }

    }
}