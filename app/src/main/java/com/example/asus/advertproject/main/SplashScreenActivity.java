package com.example.asus.advertproject.main;

/**
 * Created by Asus on 2017. 11. 19..
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.asus.advertproject.R;



public class SplashScreenActivity extends AppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    Log.d("hova", "main");
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
