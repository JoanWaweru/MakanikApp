package com.joan.makanikapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import coding.insight.cleanuiloginregister.R;


public class MechanicRegisterActivity extends AppCompatActivity {


    TextView firstname,lastname,email,phone_number,password;
//    mechanicCertNo;
    Button register_button;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_register);
        changeStatusBarColor();
//extract values into variables from the layout file
        firstname= findViewById(R.id.editTextFirstName);
        lastname= findViewById(R.id.editTextLastName);
        email= findViewById(R.id.editTextEmail);
        password= findViewById(R.id.editTextPassword);
        phone_number= findViewById(R.id.editTextMobile);
//        mechanicCertNo=findViewById(R.id.textInputMechanicCertNumber);
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
        startActivity(new Intent(this,MechanicLoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }


    //save data into firebase on clicking the sign up button
    public void signUpUser(View view) {


//        rootnode = FirebaseDatabase.getInstance();
//        reference = rootnode.getReference("user");

        //get all values
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String emailaddress = email.getText().toString();
        String phoneno = phone_number.getText().toString();
        String userpassword = password.getText().toString();
        String status = "offline";
        String approved = "no";
        mAuth = FirebaseAuth.getInstance();

        if(fname.isEmpty()){
            firstname.setError("Field is Required");
            firstname.requestFocus();
            return;

        }
        if(lname.isEmpty()){
            lastname.setError("Field is Required");
            lastname.requestFocus();
            return;

        }
        if(emailaddress.isEmpty()){
            email.setError("Field is Required");
            email.requestFocus();
            return;

        }
        if(phoneno.isEmpty()){
            phone_number.setError("Field is Required");
            phone_number.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()){
            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return;
        }
        if(userpassword.isEmpty()){
            password.setError("Password is Required");
            password.requestFocus();
            return;

        }
        if(userpassword.length()<8){
            password.setError("Password Should Have a Minimum of 8 Characters");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailaddress,userpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            MechanicHelperClass helperClass = new MechanicHelperClass(fname,lname,emailaddress,phoneno,userpassword,status,approved);
                            FirebaseDatabase.getInstance().getReference("mechanic")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MechanicRegisterActivity.this,"Sign Up Successful. Check Your Email To Verify Your Account. ",Toast.LENGTH_LONG).show();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        startActivity(new Intent(MechanicRegisterActivity.this,MechanicLoginActivity.class));


                                    }
                                    else {
                                        Toast.makeText(MechanicRegisterActivity.this,"Sign Up Was Unsuccessful. Try Again",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }
                        else {
                            Toast.makeText(MechanicRegisterActivity.this,"Sign Up Unsuccessful. Try Again",Toast.LENGTH_SHORT).show();

                        }

                    }
                });










    }
}
