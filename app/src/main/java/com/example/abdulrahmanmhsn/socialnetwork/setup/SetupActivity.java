package com.example.abdulrahmanmhsn.socialnetwork.setup;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abdulrahmanmhsn.socialnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity implements Interface {

    //weidget
    private EditText mUserName , mFullName ,mCountry;
    private Button mSave;
    private CircleImageView mImageProfile;
    private ProgressDialog loadingBar;

    //vars
    private String userName , fullName , country;
    private String currentUserId;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        initView();

    }


    @Override
    public void initView() {
        // init views
        mUserName = findViewById(R.id.setup_edtxt_username);
        mFullName = findViewById(R.id.setup_edtxt_fullname);
        mCountry = findViewById(R.id.setup_edtxt_country);
        mSave = findViewById(R.id.setup_bttn_save);
        mImageProfile = findViewById(R.id.setup_image_profile);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void addEventToView() {
        // add listener button save
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountSetupInformation();
            }
        });

    }

    @Override
    public void saveAccountSetupInformation() {
        //get text from editText
        userName = Objects.requireNonNull(mUserName).getText().toString().trim();
        fullName = Objects.requireNonNull(mFullName).getText().toString().trim();
        country = Objects.requireNonNull(mCountry).getText().toString().trim();

        //validation
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(country)) {
            Toast.makeText(this, "Please write your country...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", userName);
            userMap.put("fullname", fullName);
            userMap.put("country", country);
            userMap.put("status", "Hey there, i am using Poster Social Network, developed by Coding Cafe.");
            userMap.put("gender", "none");
            userMap.put("dob", "none");
            userMap.put("relationshipstatus", "none");

        }
    }
}
