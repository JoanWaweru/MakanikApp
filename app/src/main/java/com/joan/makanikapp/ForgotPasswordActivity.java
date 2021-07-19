package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView email;
    Button resetpasswordbutton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.textInputForgetEmail);
        resetpasswordbutton = findViewById(R.id.button_forgettingpassword);
        auth = FirebaseAuth.getInstance();
    }

    public void resetpassword(View view) {
        resettingPassword();
    }

    private void resettingPassword() {
        String resetEmail = email.getText().toString().trim();

        if(resetEmail.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(resetEmail).matches()){
            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Check Your Email to Reset your Password",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(ForgotPasswordActivity.this,"Ensure Your Email Is Correct",Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}