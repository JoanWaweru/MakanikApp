package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainScreenActivity extends AppCompatActivity {

    private Button callmechanic;
    private TextView welcome;
    private Button viewprofile;
    private Button logout;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        callmechanic = findViewById(R.id.call_mechanic_textview);
        viewprofile = findViewById(R.id.view_profile_textview);
        logout = findViewById(R.id.logout_textview);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

         TextView welcomeTextView = findViewById(R.id.text_welcome);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile != null) {
                    String firstname = userProfile.fname;
                    welcomeTextView.setText("Welcome,  " + firstname + " ");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainScreenActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });
    }

    public void call_mechanic(View view) {
        switch (view.getId()){
            case R.id.call_mechanic_textview:

                startActivity(new Intent(this,UserIncidentPageActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

                break;
        }
    }

    public void view_profile(View view) {


        switch (view.getId()){
            case R.id.view_profile_textview:

                startActivity(new Intent(this,ViewProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

                break;
        }


    }
    public void logOut(View view) {
//        switch (view.getId()){
//            case R.id.logout_textview:
//                mAuth = FirebaseAuth.getInstance();
//                mAuth.signOut();
//                startActivity(new Intent(this,LoginActivity.class));
//                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
//
//                break;
//        }

    }
}