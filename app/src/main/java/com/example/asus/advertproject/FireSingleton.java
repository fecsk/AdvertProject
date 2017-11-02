package com.example.asus.advertproject;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Asus on 2017. 10. 25..
 */

public class FireSingleton {
    private static FirebaseAuth mAuth;
    private static FireSingleton instance = null;
    private static String TAG="FireSingleton";
    private FireSingleton() {
        mAuth=FirebaseAuth.getInstance();
    }

    public static FireSingleton getInstance() {
        if(instance == null) {
            instance = new FireSingleton();
        }
        return instance;
    }
    public static FirebaseAuth.AuthStateListener getMauthListener()
    {
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        return mAuthListener;

    }


}