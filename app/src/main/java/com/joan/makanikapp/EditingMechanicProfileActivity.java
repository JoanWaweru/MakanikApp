package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditingMechanicProfileActivity extends AppCompatActivity {
    EditText email,phone_number;
    Button editingProfile,backBtn;

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String mechanicID;
    FirebaseAuth mAuth;
    FirebaseUser mechanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_mechanic_profile);

        email = findViewById(R.id.profile_mechanic_displayingemail);
        phone_number = findViewById(R.id.profile_mechanic_displayingphonenumber);
        editingProfile = findViewById(R.id.mechanic_editingProfile);
        backBtn = findViewById(R.id.mechanic_back_viewingprofile);

        mechanic = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("mechanic");
        mechanicID = mechanic.getUid();


        EditText emailTextView = findViewById(R.id.profile_mechanic_displayingemail);
        EditText phonenumberTextView = findViewById(R.id.profile_mechanic_displayingphonenumber);


        reference.child(mechanicID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass mechanicProfile = snapshot.getValue(UserHelperClass.class);

                if (mechanicProfile != null) {

                    String email = mechanicProfile.email;
                    String phonenumber = mechanicProfile.phoneno;

                    emailTextView.setText(email);
                    phonenumberTextView.setText(phonenumber);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditingMechanicProfileActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }

        });
    }

    public void MechanicEditingProfile(View v){

        mechanic = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("mechanic");
        mechanicID = mechanic.getUid();

        reference.child(mechanicID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                String email = userProfile.email;
                String phonenumber = userProfile.phoneno;

                EditText emailTextView = findViewById(R.id.profile_mechanic_displayingemail);
                EditText phonenumberTextView = findViewById(R.id.profile_mechanic_displayingphonenumber);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("email", emailTextView.getText().toString());
                hashMap.put("phoneno", phonenumberTextView.getText().toString());

                reference.child(mechanicID).updateChildren(hashMap).addOnSuccessListener(suc-> {
                    Toast.makeText(EditingMechanicProfileActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->{
                            Toast.makeText(EditingMechanicProfileActivity.this,"Something Went Wrong. Try Again"+er.getMessage(),Toast.LENGTH_LONG).show();
                        }

                );

//                reference.child("user").child("email").setValue(emailTextView);
//                reference.child("user").child("phoneno").setValue(phonenumberTextView);

                emailTextView.setText(email);
                phonenumberTextView.setText(phonenumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditingMechanicProfileActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();

            }

//            public void MechanicBack(View view) {
//            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditingMechanicProfileActivity.this,MechanicViewProfileActivity.class);
                startActivity(i);
            }
        });
    }
}