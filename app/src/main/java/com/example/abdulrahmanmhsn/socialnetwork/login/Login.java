package com.example.abdulrahmanmhsn.socialnetwork.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abdulrahmanmhsn.socialnetwork.main.MainActivity;
import com.example.abdulrahmanmhsn.socialnetwork.R;
import com.example.abdulrahmanmhsn.socialnetwork.signUp.SignUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Objects;

public class Login extends AppCompatActivity implements Interface {

    // Views
    private Button mLogin, mRegister;
    private EditText mEmail, mPassword;
    private ProgressDialog loadingBar;

    // Vars
    private String email, password;

    // Firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //Call method
        initUI();
        addEventView();


    }


    @Override
    public void initUI() {
        //init view
        mLogin = findViewById(R.id.login_bttn_login);
        mRegister = findViewById(R.id.login_bttn_register);
        mEmail = findViewById(R.id.login_edtxt_email);
        mPassword = findViewById(R.id.login_edtxt_password);
        loadingBar = new ProgressDialog(this);
    }


    public void addEventView() {
        //add listener to Button Register
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });


        //add listener to Button Login
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Objects.requireNonNull(mEmail).getText().toString().trim();
                password = Objects.requireNonNull(mPassword).getText().toString().trim();

                if (email.isEmpty()) {
                    mEmail.setError(getString(R.string.email_is_required));
                    mEmail.requestFocus();
                }
                else if (password.isEmpty()) {
                    mPassword.setError(getString(R.string.password_is_required));
                    mPassword.requestFocus();
                }else
                {
                    signInWithEmail(email,password);
                }


            }
        });
    }


    public void signInWithEmail(String email, String password) {

        loadingBar.setTitle("Login");
        loadingBar.setMessage("Please wait, while we are allowing you to login into your Account...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                showMessage("Done");
                Intent maiIntent = new Intent(Login.this,MainActivity.class);
                maiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(maiIntent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Failed");
                loadingBar.dismiss();
            }
        });
    }


    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public void SendUserToMainActivity() {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }
}
