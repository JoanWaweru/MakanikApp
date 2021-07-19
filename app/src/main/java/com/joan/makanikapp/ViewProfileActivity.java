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

public class ViewProfileActivity extends AppCompatActivity {

    TextView firstname,lastname,email,phone_number,welcome;
    Button logout;
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
        welcome = findViewById(R.id.text_welcome);
        logout = findViewById(R.id.button_logout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

        final TextView firstnameTextView = findViewById(R.id.profile_displayfirstname);
        final TextView lastnameTextView = findViewById(R.id.profile_displaylastname);
        final TextView emailTextView = findViewById(R.id.profile_displayemail);
        final TextView phonenumberTextView = findViewById(R.id.profile_displayphonenumber);
        final TextView welcomeTextView = findViewById(R.id.text_welcome);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if(userProfile!=null){
                    String firstname = userProfile.fname;
                    String lastname = userProfile.lname;
                    String email = userProfile.email;
                    String phonenumber = userProfile.phoneno;

                    welcomeTextView.setText("Welcome,  "+firstname+" ");
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

        //show all data
        showAllUserData();

    }

    private void showAllUserData() {
//        Intent intent = getIntent();
//        String user_firstname = intent.getStringExtra("fname");
//        String user_lastname = intent.getStringExtra("lname");
//        String user_email = intent.getStringExtra("email");
//        String user_phoneno = intent.getStringExtra("phoneno");
//
//        firstname.setText(user_firstname);
//        lastname.setText(user_lastname);
//        email.setText(user_email);
//        phone_number.setText(user_phoneno);


    }

    public void logOut(View view) {
        switch (view.getId()){
            case R.id.button_logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(ViewProfileActivity.this,LoginActivity.class));
                break;
        }

    }
}