package com.joan.makanikapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutUsActivity extends AppCompatActivity {
Button backToSettingsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        backToSettingsBtn = findViewById(R.id.backToSettingsButton);


    }

    public void backToSettings(View view) {

        Intent backToSettingsIntent = new Intent(this,SettingsActivity.class);
        startActivity(backToSettingsIntent);
    }
}