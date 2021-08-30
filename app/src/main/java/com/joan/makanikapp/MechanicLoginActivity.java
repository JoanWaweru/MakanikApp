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

public class MechanicLoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_mechanic_login);

        //extract variables from layout file
        email = findViewById(R.id.editloginTextEmail_mechanic);
        password = findViewById(R.id.editTextloginPassword_mechanic);
        login = findViewById(R.id.button_login_mechanic);
        forgotpassword = findViewById(R.id.button_forgotpassword_mechanic);
        rememberMe = findViewById(R.id.rememberMe);
    }
    public void onLoginClick1(View View){
        startActivity(new Intent(this,MechanicRegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }


    public void onLoginClick2(View view) {
        switch (view.getId()){
            case R.id.button_login_mechanic:
                rootnode = FirebaseDatabase.getInstance();
                reference = rootnode.getReference("mechanic");
                mAuth = FirebaseAuth.getInstance();
                userLogIn1();
                break;

        }

    }

    private void userLogIn1() {
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

                        startActivity(new Intent(MechanicLoginActivity.this, MechanicLandingActivity.class));

                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MechanicLoginActivity.this,"Check Your Email To Verify Your Account",Toast.LENGTH_LONG).show();



                    }


                }
                else {
                    Toast.makeText(MechanicLoginActivity.this,"Log In Failed. Please Check Your Credentials",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void forgotpassword1(View view) {

        switch (view.getId()){
            case R.id.button_forgotpassword_mechanic:
                startActivity(new Intent(MechanicLoginActivity.this,ForgotPasswordActivity.class));
                break;
        }

    }


    public void logInAsUser(View view) {
        switch (view.getId()) {
            case R.id.user_login_textview:


                startActivity(new Intent(MechanicLoginActivity.this,LoginActivity.class));

                break;
        }
    }
}


