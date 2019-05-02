package com.example.abdulrahmanmhsn.socialnetwork.signUp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.abdulrahmanmhsn.socialnetwork.R;
import com.example.abdulrahmanmhsn.socialnetwork.login.Login;
import com.example.abdulrahmanmhsn.socialnetwork.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUp extends AppCompatActivity implements Interface {

    // Views
    private CircleImageView mImageUser;
    private EditText mFName, mLName, mUserName, mEmail, mPassword, mConPassword, mMobile;
    private Button mCreate;
    private ProgressDialog loadingBar;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUsers;
    private StorageReference mUserProfileImgRef;

    //vars
    private String firstName, lastName, userName, email, password, conPassword, mobile;
    private String currentUserID;
    private final static int PERMISSION_CODE = 1;
    private final static int PERMISSION_REQUEST_CODE = 2;

    //URi
    private Uri pickedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // init FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
 //       currentUserID = mAuth.getCurrentUser().getUid();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mUserProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        // call method initView
        initView();

        //Event method
        addEventToView();


    }

    @Override
    public void initView() {
        //imageView
        mImageUser = findViewById(R.id.register_image_user);
        //EditText
        mFName = findViewById(R.id.register_edtxt_fname);
        mLName = findViewById(R.id.register_edtxt_lname);
        mUserName = findViewById(R.id.register_edtxt_username);
        mEmail = findViewById(R.id.register_edtxt_email);
        mPassword = findViewById(R.id.register_edtxt_password);
        mConPassword = findViewById(R.id.register_edtxt_conpassword);
        mMobile = findViewById(R.id.register_edtxt_mobile);
        //Button
        mCreate = findViewById(R.id.register_bttn_create);
        // Dialog
        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void addEventToView() {
        //add listener to Button (Create)
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

        //add listener to editText (password)
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = Objects.requireNonNull(mPassword).getText().toString();
                String password_confirm = Objects.requireNonNull(mConPassword).getText().toString();
                checkPassword(password, password_confirm);

            }
        });

        //add listener to editText (confirm password)
        mConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = Objects.requireNonNull(mPassword).getText().toString();
                String password_confirm = Objects.requireNonNull(mConPassword).getText().toString();
                checkPassword(password, password_confirm);
            }
        });

        //add listener to imageView (image user)
        mImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 22) {
//                    checkAndRequestForPermission();
//                } else {
                    openGallery();
              //  }
            }
        });

    }

    @Override
    public void registerNewUser() {

        firstName = Objects.requireNonNull(mFName).getText().toString().trim();
        lastName = Objects.requireNonNull(mLName).getText().toString().trim();
        userName = Objects.requireNonNull(mUserName).getText().toString().trim();
        email = Objects.requireNonNull(mEmail).getText().toString().trim();
        password = Objects.requireNonNull(mPassword).getText().toString().trim();
        conPassword = Objects.requireNonNull(mConPassword).getText().toString().trim();
        mobile = Objects.requireNonNull(mMobile).getText().toString().trim();

        //check validation
        if (firstName.isEmpty()) {
            mFName.setError(getString(R.string.first_name_is_required));
            mFName.requestFocus();
        } else if (lastName.isEmpty()) {
            mLName.setError(getString(R.string.last_name_is_required));
            mLName.requestFocus();
        } else if (userName.isEmpty()) {
            mUserName.setError(getString(R.string.user_name_is_required));
            mUserName.requestFocus();
        } else if (email.isEmpty()) {
            mEmail.setError(getString(R.string.email_is_required));
            mEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError(getString(R.string.please_enter_a_valid_email));
            mEmail.requestFocus();
        } else if (password.isEmpty()) {
            mPassword.setError(getString(R.string.password_is_required));
            mPassword.requestFocus();
        } else if (conPassword.isEmpty()) {
            mConPassword.setError(getString(R.string.conPassword_is_required));
            mConPassword.requestFocus();
        } else if (password.length() < 6) {
            mPassword.setError(getString(R.string.Minimum_lenght_of_password_should_be_6));
            mPassword.requestFocus();
        } else if (mobile.isEmpty()) {
            mPassword.setError(getString(R.string.phone_is_required));
            mPassword.requestFocus();
        } else if (mobile.length() != 11) {
            mMobile.setError(getString(R.string.enter_real_phone));
            mPassword.requestFocus();
        } else {

            createUserWithEmail(email, password);

        }


    }

    @Override
    public void createUserWithEmail(final String email, final String password) {
        loadingBar.setTitle("Login");
        loadingBar.setMessage("Please wait, while we are allowing you to login into your Account...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        //register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //save user to db
                Model userData = new Model();
                userData.setFirstName(firstName);
                userData.setUserName(userName);
                userData.setLastName(lastName);
                userData.setUserName(userName);
                userData.setEmail(email);
                userData.setPassword(password);
                userData.setConfirmPassword(conPassword);
                userData.setMobil(mobile);

                //user mail to key
                mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                Toast.makeText(getApplicationContext(), "Sucess Rgister", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failer Rgister", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        })
                ;
            }
        });
    }

    @Override
    public void checkPassword(String password, String conPassword) {
        if (password.equals(conPassword)) {
            if (!password.isEmpty()) {
                Objects.requireNonNull(mConPassword).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_mark, 0);
                Objects.requireNonNull(mPassword).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_mark, 0);
            }
        }
    }

    @Override
    public void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessage("please accept for required permission");
            } else {
                ActivityCompat.requestPermissions(SignUp.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }

        } else {
            openGallery();
        }
    }

    @Override
    public void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PERMISSION_REQUEST_CODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (requestCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                StorageReference filePath = mUserProfileImgRef.child(mAuth.getCurrentUser().getUid() + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            showMessage("Profile Image stored successfully to Firebase storage...");

                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                            mUsers.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(SignUp.this, SignUp.class);
                                                startActivity(selfIntent);
                                                showMessage("Profile Image stored to Firebase Database Successfully...");
                                            } else {
                                                String message = task.getException().getMessage();
                                                showMessage("Error Occured: " + message);
                                            }
                                        }
                                    });
                        }

                    }
                });
            } else {
                showMessage("Error Occured: Image can not be cropped. Try Again.");
            }

        }
    }
}

