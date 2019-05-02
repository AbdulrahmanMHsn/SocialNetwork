package com.example.abdulrahmanmhsn.socialnetwork.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdulrahmanmhsn.socialnetwork.R;
import com.example.abdulrahmanmhsn.socialnetwork.setup.SetupActivity;
import com.example.abdulrahmanmhsn.socialnetwork.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements Interface {

    //views
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView; //menu
    private RecyclerView mPostList;  //list of posts
    private ImageView mMenu;
    private Button mLogOut;
    private CircleImageView mNavProfileImage;
    private TextView mNavProfileUserName;


    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;

    //vars
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        //call method initView()
        initView();
        //call method addEventToView
        addEventToView();
//        getDataFromServer();

    }

    @Override
    public void initView() {
        // init view
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mPostList = findViewById(R.id.all_users_post_list);
        mMenu = findViewById(R.id.menu_image);
        mLogOut = findViewById(R.id.main_btn_logout);
    }

    @Override
    public void addEventToView() {
        //add listener to image view (Menu)
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });

        //add listener to NavigationView
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });

        //add listener to logout
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });
    }

    @Override
    public void userMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_post:
                Toast.makeText(this, "New Add Post", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_friends:
                Toast.makeText(this, "Friends", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_find_friends:
                Toast.makeText(this, "Find Friends", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_messages:
                Toast.makeText(this, "Messages", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_setting:
                Toast.makeText(this, "Setting", Toast.LENGTH_LONG).show();
                break;

//            case R.id.nav_logout:
//                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
//                mAuth.signOut();
//                break;
        }
    }

    @Override
    public void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void sendUserToSetupActivity() {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    @Override
    public void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    sendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getDataFromServer() {
        mNavProfileUserName = findViewById(R.id.nav_username);

        UsersRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("userName"))
                    {
                        String userName = dataSnapshot.child("userName").getValue().toString();
                        mNavProfileUserName.setText(userName);
                    }
//                    if(dataSnapshot.hasChild("profileimage"))
//                    {
//                        String image = dataSnapshot.child("profileimage").getValue().toString();
//                        Picasso.get().load(image).placeholder(R.mipmap.profile).into(mNavProfileImage);
//                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendUserToLoginActivity();
        } else {
            CheckUserExistence();
        }

    }


}


