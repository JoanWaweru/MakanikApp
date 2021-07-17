package com.joan.makanikapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import coding.insight.cleanuiloginregister.R;


public class RegisterActivity extends AppCompatActivity {


    TextView firstname,lastname,email,phone_number,password;
    Button register_button;
    FirebaseDatabase rootnode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
//extract values into variables from the layout file
        firstname= findViewById(R.id.editTextFirstName);
        lastname= findViewById(R.id.editTextLastName);
        email= findViewById(R.id.editTextEmail);
        password= findViewById(R.id.editTextPassword);
        phone_number= findViewById(R.id.editTextMobile);
        register_button = findViewById(R.id.button_signup);





    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }


//save data into firebase on clicking the sign up button
    public void signUpUser(View view) {
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("user");

        //get all values
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String emailaddress = email.getText().toString();
        String phoneno = phone_number.getText().toString();
        String userpassword = password.getText().toString();
        UserHelperClass helperClass = new UserHelperClass(fname,lname,emailaddress,phoneno,userpassword);

        reference.child(phoneno).setValue(helperClass);
        Toast.makeText(RegisterActivity.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }
}
