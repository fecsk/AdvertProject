package com.example.asus.advertproject.main;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.advertfeed.*;
import com.example.asus.advertproject.login.LoginActivity;
import com.example.asus.advertproject.model.Advert;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private NavigationManager manager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_main);
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
                        manager.switchToMainFragment(new AdvertFeedFragment());
                        return true;

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

        //noinspection SimplifiableIfStatement

        if (id == R.id.Addbutton) {
            Intent i=new Intent(MainActivity.this,AddActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.Loginbutton) {
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}

