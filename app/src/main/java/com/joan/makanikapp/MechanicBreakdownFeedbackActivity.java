package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MechanicBreakdownFeedbackActivity extends AppCompatActivity {
    Button submitMechanicdescription;
    EditText mechanicDescription, mechanicprice;
    private String customerid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_breakdown_feedback);
        submitMechanicdescription = findViewById(R.id.button_submit_mechanic_description);
        mechanicDescription = findViewById(R.id.edittext_mechanic_description);
        mechanicprice = findViewById(R.id.edittext_price);


    }

    private DatabaseReference ridestuff;
    private ValueEventListener ridestufflistener;

    public void submitMechanicDescription(View view) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String description = String.valueOf(mechanicDescription);
        String price = String.valueOf(mechanicprice);
        ridestuff = FirebaseDatabase.getInstance().getReference().child("mechanic").child(userId).child("rides");
        ridestufflistener = ridestuff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists() ){
                    snapshot.getRef().removeValue();


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        DatabaseReference customerref = FirebaseDatabase.getInstance().getReference().child("mechanic").child(userId).child("customerRideId");
        customerref.addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 customerid = snapshot.getValue().toString();

                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("mechanic_comments");
        String requestId = historyRef.push().getKey();



        HashMap < String, Object > map = new HashMap<>();
        map.put("mechanic", userId);
        map.put("customer", customerid);
        map.put("description", description);
        map.put("price", price);

        assert requestId != null;


        historyRef.child(requestId).updateChildren(map).addOnSuccessListener(suc-> {
            Toast.makeText(MechanicBreakdownFeedbackActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MechanicBreakdownFeedbackActivity.this, MechanicLandingActivity.class));
        }).addOnFailureListener(er->{
                    Toast.makeText(MechanicBreakdownFeedbackActivity.this,"Something Went Wrong. Try Again"+er.getMessage(),Toast.LENGTH_LONG).show();
                }

        );



    }
}