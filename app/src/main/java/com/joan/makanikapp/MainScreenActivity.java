package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {
    Button editProfile;

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public CardView callMechanic,viewProfile,settings,logout;
    private TextView welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        callMechanic = findViewById(R.id.callMechanicCard);
        viewProfile = findViewById(R.id.viewProfileCard);
        settings = findViewById(R.id.settingsCard);
        logout = findViewById(R.id.logoutCard);

        callMechanic.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        settings.setOnClickListener(this);
        logout.setOnClickListener(this);

        editProfile = findViewById(R.id.button_edit_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

         TextView welcome = findViewById(R.id.welcome);



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile != null) {
                    String firstname = userProfile.fname;

                    welcome.setText("Welcome, "+firstname);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainScreenActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.callMechanicCard:
                i = new Intent(this,UserIncidentPageActivity.class);
                startActivity(i);
                break;

            case R.id.viewProfileCard:
                i = new Intent(this,ViewProfileActivity.class);
                startActivity(i);
                break;

            case R.id.settingsCard:
                i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.logoutCard:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                i = new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }

    }
}