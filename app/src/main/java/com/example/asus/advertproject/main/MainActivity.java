package com.example.asus.advertproject.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.advertfeed.*;
import com.example.asus.advertproject.login.LoginActivity;
import com.example.asus.advertproject.model.Advert;
import com.example.asus.advertproject.profile.EditProfileActivity;
import com.example.asus.advertproject.profile.ViewProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private NavigationManager manager;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAction));
        }


        initViews();
       /* for(int i=0;i<10;i++) {
            sendPostToFirebase();
        }
        */

        manager.switchToMainFragment(new AdvertFeedFragment());
    }

    private void initViews() {
        manager = new NavigationManager(getSupportFragmentManager());
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bottomNavigationView = findViewById(R.id.main_navigation);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.recentlist:
                        manager.switchToMainFragment(new AdvertFeedFragment());
                        return true;
                    case R.id.myadverts:
                        if(user != null) {
                            manager.switchToMainFragment(new AdvertFeedFragment());
                            return true;

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please log in first!",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }



                }
                return false;
            }

        };
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    public void setSelectedMenuItem(int resId){
        bottomNavigationView.setSelectedItemId(resId);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        user = auth.getCurrentUser();
        //noinspection SimplifiableIfStatement

        if (id == R.id.Addbutton) {


            if(user != null) {
                Intent i=new Intent(MainActivity.this,AddActivity.class);
                startActivity(i);
                return true;

            }
            else{
                Toast.makeText(MainActivity.this, "Please log in first!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (id == R.id.Loginbutton) {
            if(user == null) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                return true;
            }
            else{
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();

                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        if (id == R.id.viewProfileButton) {
            if(user != null) {
                Intent i = new Intent(MainActivity.this, ViewProfileActivity.class);
                startActivity(i);
                return true;
            }
            else{
                Toast.makeText(MainActivity.this, "Please log in first!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (id == R.id.editProfileButton) {
            if(user != null) {
                Intent i = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(i);
                return true;
            }
            else{
                Toast.makeText(MainActivity.this, "Please log in first!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return super.onOptionsItemSelected(item);
    }


}

