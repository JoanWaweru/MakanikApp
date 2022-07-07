package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public CardView quickInformation,aboutUs,breakdownHistory,contactUs, shareApp,payment;
    //private TextView welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        quickInformation = findViewById(R.id.quickInformationCard);
        aboutUs = findViewById(R.id.aboutUsCard);
        breakdownHistory = findViewById(R.id.viewBreakdownHistoryCard);
        contactUs = findViewById(R.id.contactUsCard);
        shareApp = findViewById(R.id.shareAppCard);
        payment = findViewById(R.id.paymentCard);

        quickInformation.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        breakdownHistory.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        payment.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

        //TextView welcome = findViewById(R.id.welcome);



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

//                if (userProfile != null) {
//                    String firstname = userProfile.fname;
//
//                    welcome.setText("Welcome, "+firstname);
//
//                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingsActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;
        String appUrl = "https://www.wikihow.com/Check-a-Car-Before-Driving";
        switch (v.getId()){
            case R.id.quickInformationCard:
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(appUrl));
                startActivity(i);
                break;

            case R.id.aboutUsCard:
                i = new Intent(this,AboutUsActivity.class);
                startActivity(i);
                break;

            case R.id.viewBreakdownHistoryCard:
                i = new Intent(this,BreakdownMainActivity.class);
                startActivity(i);
                break;

            case R.id.contactUsCard:
                i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:0740182052"));
                startActivity(i);
                break;

            case R.id.shareAppCard:
                i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "Take a look at this awesome app! ");

                Intent chooser = Intent.createChooser(i, "Share via");
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                break;

            case R.id.paymentCard:
                i = new Intent(this,PaymentActivity.class);
                startActivity(i);
                break;
        }

    }
}