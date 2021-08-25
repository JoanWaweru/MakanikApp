package com.joan.makanikapp;

//import androidx.annotation.ColorInt;
//import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
//import android.content.res.TypedArray;
//import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
//import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.yarolegovich.slidingrootnav.SlidingRootNav;
//import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
//
//import java.lang.reflect.Array;
//import java.util.Arrays;
//import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    TextView welcome;
    EditText firstname,lastname,email,phone_number;
    Button logout,editProfileBtn;
    String _FIRSTNAME,_LASTNAME,_EMAIL,_PHONENUMBER;

    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;

//    private static final int POS_DASHBOARD = 0;
//    private static final int POS_MY_PROFILE = 1;
//    private static final int POS_ABOUT_US = 2;
//    private static final int POS_SIGN_IN_AS_MECHANIC = 3;
//    private static final int POS_LOGOUT = 5;
//
//    private String[] screenTitles;
//    private Drawable[] screenIcons;
//
//    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        //create hooks
        firstname = findViewById(R.id.profile_displayfirstname);
        lastname = findViewById(R.id.profile_displaylastname);
        email = findViewById(R.id.profile_displayemail);
        phone_number = findViewById(R.id.profile_displayphonenumber);

        editProfileBtn = findViewById(R.id.button_edit_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

        final EditText firstnameTextView = findViewById(R.id.profile_displayfirstname);
        final EditText lastnameTextView = findViewById(R.id.profile_displaylastname);
        final EditText emailTextView = findViewById(R.id.profile_displayemail);
        final EditText phonenumberTextView = findViewById(R.id.profile_displayphonenumber);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile != null) {
                    String firstname = userProfile.fname;
                    String lastname = userProfile.lname;
                    String email = userProfile.email;
                    String phonenumber = userProfile.phoneno;


                    firstnameTextView.setText(firstname);
                    lastnameTextView.setText(lastname);
                    emailTextView.setText(email);
                    phonenumberTextView.setText(phonenumber);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProfileActivity.this,"Something Went Wrong. Try Again",Toast.LENGTH_LONG).show();


            }
        });

        //show all data
        showAllUserData();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//
//        slidingRootNav = new SlidingRootNavBuilder(this)
//                .withDragDistance(180)
//                .withRootViewScale(0.75f)
//                .withRootViewElevation(25)
//                .withToolbarMenuToggle(toolbar)
//                .withMenuOpened(false)
//                .withContentClickableWhenMenuOpened(false)
//                .withSavedState(savedInstanceState)
//                .withMenuLayout(R.layout.drawer_menu)
//                .inject();
//
//        screenIcons = loadScreenIcons();
//        screenTitles = loadScreenTitles();
//
////        String a[] = new String[]{
////                "createItemFor(POS_CLOSE)",
////                "createItemFor(POS_DASHBOARD).setChecked(true)",
////                "createItemFor(POS_MY_PROFILE)",
////                "createItemFor(POS_ABOUT_US)",
////                "createItemFor(POS_SIGN_IN_AS_MECHANIC)",
////                "new SpaceItem(260)",
////                "createItemFor(POS_LOG_OUT)"
////        };
////
////        List list1 = Arrays.asList(a);
//
////        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
////                createItemFor(POS_DASHBOARD).setChecked(true),
////                createItemFor(POS_MY_PROFILE),
////                createItemFor(POS_ABOUT_US),
////                createItemFor(POS_SIGN_IN_AS_MECHANIC),
////                new SpaceItem(260),
////                createItemFor(POS_LOGOUT)));
////
////        adapter.setListener(this);
////
////        adapter.setListener((AdapterView.OnItemSelectedListener) this);
//
//        RecyclerView list = findViewById(R.id.drawer_list);
//        list.setNestedScrollingEnabled(false);
//        list.setLayoutManager(new LinearLayoutManager(this));
////        list.setAdapter(adapter);
//
////        adapter.setSelected(POS_DASHBOARD);
//    }
//
//    private DrawerItem createItemFor(int position){
//        return new SimpleItem(screenIcons[position],screenTitles[position])
//                .withIconTint(color(R.color.colorSecondary))
//                .withTextTint(color(R.color.colorOnSecondary))
//                .withSelectedIconTint(color(R.color.colorSecondary))
//                .withSelectedTextTint(color(R.color.colorSecondary));
//    }
//
//    @ColorInt
//    private int color(@ColorRes int res){
//        return ContextCompat.getColor(this,res);
//    }
//
//    private String[] loadScreenTitles() {
//        return getResources().getStringArray(R.array.id_activityScreenTitles);
//    }
//
//    private Drawable[] loadScreenIcons() {
//        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
//        Drawable[] icons = new Drawable[ta.length()];
//        for (int i = 0; i<ta.length(); i++){
//            int id = ta.getResourceId(i,0);
//            if (id != 0) {
//                continue;
//            }
//            icons[i] = ContextCompat.getDrawable(this,id);
//        }
//        ta.recycle();
//        return icons;
//    }
//
//    @Override
//    public void onBackPressed(){
//        finish();
//    }
//
//    public void onItemSelected(int position) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        if (position == POS_DASHBOARD) {
//            DashboardFragment dashboardFragment = new DashboardFragment();
//            transaction.replace(R.id.container, dashboardFragment);
//        }

//        if(position == POS_MY_PROFILE){
//            ViewMyProfileFragment viewMyProfileActivity = new DashboardFragment();
//            transaction.replace(R.id.container,dashboardFragment);
//        }

//        else if (position == POS_ABOUT_US) {
//            AboutUsFragment aboutUsFragment = new AboutUsFragment();
//            transaction.replace(R.id.container, aboutUsFragment);
//        } else if (position == POS_SIGN_IN_AS_MECHANIC) {
//            SignInAsMechanicFragment signInAsMechanicFragment = new SignInAsMechanicFragment();
//            transaction.replace(R.id.container, signInAsMechanicFragment);
//        } else if (position == POS_LOG_OUT) {
//            finish();
//        }

//        SlidingRootNav.closeMenu();
//        transaction.addToBackStack(null);
//        transaction.commit();

    }

    private void showAllUserData() {
//        Intent intent = getIntent();
//        String user_firstname = intent.getStringExtra("fname");
//        String user_lastname = intent.getStringExtra("lname");
//        String user_email = intent.getStringExtra("email");
//        String user_phoneno = intent.getStringExtra("phoneno");
//
//        firstname.setText(user_firstname);
//        lastname.setText(user_lastname);
//        email.setText(user_email);
//        phone_number.setText(user_phoneno);


    }

    public void logOut(View view) {
        switch (view.getId()){

        }
    }

//    public void updateProfile(View view) {
//        switch (view.getId()){
//            case R.id.button_edit_profile:
//                String _EMAIL = email.getEditText().getText().toString;
//                String _PHONENUMBER = phone_number.getEditText().getText().toString;
//                firebaseDatebase
//    }


    //editProfileBtn = findViewById(R.id.button_edit_profile);


}