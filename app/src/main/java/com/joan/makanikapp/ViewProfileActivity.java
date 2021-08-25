package com.joan.makanikapp;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class ViewProfileActivity extends AppCompatActivity {

    TextView firstname,lastname,email,phone_number;
    Button editProfile;

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        //create hooks
        firstname = findViewById(R.id.profile_displayfirstname);
        lastname = findViewById(R.id.profile_displaylastname);
        email = findViewById(R.id.profile_displayemail);
        phone_number = findViewById(R.id.profile_displayphonenumber);
        editProfile = findViewById(R.id.editProfile);



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

        final TextView firstnameTextView = findViewById(R.id.profile_displayfirstname);
        final TextView lastnameTextView = findViewById(R.id.profile_displaylastname);
        final TextView emailTextView = findViewById(R.id.profile_displayemail);
        final TextView phonenumberTextView = findViewById(R.id.profile_displayphonenumber);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile != null) {
                    String firstname = userProfile.fname;
                    String lastname = userProfile.lname;
                    String email = userProfile.email;
                    String phonenumber = userProfile.phoneno;


                    firstnameTextView.setText(firstname);
                    lastnameTextView.setText(lastname);
                    emailTextView.setText(email);
                    phonenumberTextView.setText(phonenumber);


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProfileActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });

    }
    public void EditProfile(View view) {
        switch (view.getId()){
            case R.id.editProfile:

                startActivity(new Intent(this,UserIncidentPageActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

                break;
        }
    }
}