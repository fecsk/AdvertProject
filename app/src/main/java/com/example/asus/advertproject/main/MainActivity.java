package com.example.asus.advertproject.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.advertfeed.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private NavigationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_main);

        initViews();
        manager.switchToMainFragment(new AdvertFeedFragment());
    }

    private void initViews() {
        manager = new NavigationManager(getSupportFragmentManager());
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}

