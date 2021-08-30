package com.joan.makanikapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserIncidentPageActivity2 extends AppCompatActivity {

    private Button submit_incident;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_incident_page2);

        submit_incident = findViewById(R.id.button_Find_Mechanic);

        submit_incident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIncidentIntent = new Intent(UserIncidentPageActivity2.this, UserMapsActivity.class);
                startActivity(submitIncidentIntent);
            }
        });

    }



}