package com.example.asus.advertproject.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.example.asus.advertproject.R;

/**
 * Created by Asus on 2017. 11. 19..
 */

public class NavigationManager {


    private final FragmentManager manager;

    public NavigationManager(FragmentManager manager){
        this.manager = manager;
    }

    public void switchToFragment(Fragment fragment) {
        manager.beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    public void switchToMainFragment(Fragment fragment) {

        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        }
        manager.beginTransaction().replace(R.id.content, fragment).commit();

    }
}
