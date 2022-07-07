package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class MechanicLandingActivity extends AppCompatActivity implements View.OnClickListener {
    //Button editProfile;

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String mechanicID;
    FirebaseAuth mAuth;
    FirebaseUser mechanic;

    public CardView viewRequest,viewProfile,signAsMechanic,logout;
    private TextView welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_landing);

        viewRequest = findViewById(R.id.callViewRequestsCard);
        viewProfile = findViewById(R.id.viewMechanicProfileCard);
        signAsMechanic = findViewById(R.id.signAsMechanicCard);
        logout = findViewById(R.id.logoutMechanicCard);

        viewRequest.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        signAsMechanic.setOnClickListener(this);
        logout.setOnClickListener(this);

        //editProfile = findViewById(R.id.button_edit_profile);

        mechanic = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("mechanic");
        mechanicID = mechanic.getUid();

        TextView welcome = findViewById(R.id.welcome);



        reference.child(mechanicID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile != null) {
                    String firstname = userProfile.fname;

                    welcome.setText("Welcome, Mechanic "+firstname);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MechanicLandingActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.callViewRequestsCard:
                i = new Intent(this,RequestMainActivity.class);
                startActivity(i);
                break;

            case R.id.viewMechanicProfileCard:
                i = new Intent(this,MechanicViewProfileActivity.class);
                startActivity(i);
                break;

            case R.id.signAsMechanicCard:
                i = new Intent(this,MechanicRegisterActivity.class);
                startActivity(i);
                break;

            case R.id.logoutMechanicCard:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                i = new Intent(this,MechanicLoginActivity.class);
                startActivity(i);
                finish();
                break;
        }

    }
}