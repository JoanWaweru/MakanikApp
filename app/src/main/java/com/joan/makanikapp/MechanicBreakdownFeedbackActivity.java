package com.joan.makanikapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MechanicBreakdownFeedbackActivity extends AppCompatActivity {
    Button submitMechanicdescription;
    EditText mechanicDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_breakdown_feedback);
        submitMechanicdescription = findViewById(R.id.button_submit_mechanic_description);
        mechanicDescription = findViewById(R.id.edittext_mechanic_description);

    }

    public void submitMechanicDescription(View view) {

    }
}