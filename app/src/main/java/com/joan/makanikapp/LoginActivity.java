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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import coding.insight.cleanuiloginregister.R;


public class LoginActivity extends AppCompatActivity{

        TextView email,password,forgotpassword;
        Button login;
        CheckBox rememberMe;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //for changing status bar icon colors
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            setContentView(R.layout.activity_login);

            //extract variables from layout file
            email = findViewById(R.id.editloginTextEmail);
            password = findViewById(R.id.editTextloginPassword);
            login = findViewById(R.id.button_login);
            forgotpassword = findViewById(R.id.button_forgotpassword);
            rememberMe = findViewById(R.id.rememberMe);


        }

        public void onLoginClick(View View){
            startActivity(new Intent(this,RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

        }


         public void loginClick(View view) {
            switch (view.getId()){
                case R.id.button_login:
                    rootnode = FirebaseDatabase.getInstance();
                    reference = rootnode.getReference("user");
                    mAuth = FirebaseAuth.getInstance();
                    userLogIn();
                    break;

            }

    }

    private void userLogIn() {
        String userEnteredEmail = email.getText().toString().trim();
        String userEnteredPassword = password.getText().toString().trim();

        if(userEnteredEmail.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;
            
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEnteredEmail).matches()){
            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return;
        }
        if(userEnteredPassword.isEmpty()){
            email.setError("Password is Required");
            email.requestFocus();
            return;
        }

        if(rememberMe.isChecked()){

        }


        mAuth.signInWithEmailAndPassword(userEnteredEmail,userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //check if user is email verified
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){
                        //redirect to ViewProfile Activity

                        startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));

                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this,"Check Your Email To Verify Your Account",Toast.LENGTH_LONG).show();



                    }


                }
                else {
                    Toast.makeText(LoginActivity.this,"Log In Failed. Please Check Your Credentials",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void forgotpassword(View view) {

        switch (view.getId()){
            case R.id.button_forgotpassword:
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;
        }

    }


    public void logInAsMechanic(View view) {
        switch (view.getId()){
            case R.id.mechanic_login_texteview:
                startActivity(new Intent(LoginActivity.this,MechanicLoginActivity.class));

                break;
        }
    }
}

