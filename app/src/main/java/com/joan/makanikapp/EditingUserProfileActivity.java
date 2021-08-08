package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditingUserProfileActivity extends AppCompatActivity {
    EditText email,phone_number;
    Button editingProfile;

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_user_profile);

        email = findViewById(R.id.profile_displayingemail);
        phone_number = findViewById(R.id.profile_displayingphonenumber);
        editingProfile = findViewById(R.id.editingProfile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();


        EditText emailTextView = findViewById(R.id.profile_displayingemail);
        EditText phonenumberTextView = findViewById(R.id.profile_displayingphonenumber);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile != null) {

                    String email = userProfile.email;
                    String phonenumber = userProfile.phoneno;



                    emailTextView.setText(email);
                    phonenumberTextView.setText(phonenumber);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditingUserProfileActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });
    }

    public void EditingProfile(View view) {
    }

    public void Back(View view) {
    }
}