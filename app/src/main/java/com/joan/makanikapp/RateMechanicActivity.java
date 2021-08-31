package com.joan.makanikapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateMechanicActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText comment;
    private DatabaseReference breakdownsHistory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_mechanic);
        ratingBar = findViewById(R.id.rating_bar);
        comment = findViewById(R.id.leave_a_comment_edittext);
        displayUserRelatedObjects();

    }

    private void displayUserRelatedObjects() {
        breakdownsHistory = FirebaseDatabase.getInstance().getReference().child("ratings");
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                breakdownsHistory.child("rating").setValue(rating);
                breakdownsHistory.child("customerid").setValue(userid);


                ratingBar.setRating(rating);



            }
        });


    }

    public void submitRating(View view) {
        startActivity(new Intent(RateMechanicActivity.this, PaymentActivity.class));



    }
}