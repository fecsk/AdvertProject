package com.example.asus.advertproject.advertfeed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.advertproject.R;

/**
 * Created by Asus on 2017. 11. 08..
 */

public class DetailedAdvertFragment  extends Fragment{
    private View view;
    public DetailedAdvertFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.advertfeed_fragment, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }


        return view;
    }
}
