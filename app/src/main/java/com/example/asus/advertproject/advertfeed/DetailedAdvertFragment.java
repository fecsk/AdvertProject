package com.example.asus.advertproject.advertfeed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.advertproject.R;
import com.example.asus.advertproject.model.Advert;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Asus on 2017. 11. 08..
 */

public class DetailedAdvertFragment  extends Fragment{
    private View view;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
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
            view = inflater.inflate(R.layout.detailedfeed_fragment, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }



        Advert advert = (Advert) getArguments().get("advert");

        TextView title=(TextView) view.findViewById(R.id.titleTV);
        TextView descript=(TextView) view.findViewById(R.id.descriptionTV);
        title.setText(advert.getTitle());
        descript.setText(advert.getDescription());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(advert.getPhotoURL());

        viewPager=(ViewPager) view.findViewById(R.id.viewPager);
        String[] photos=advert.photos.toArray(new String[advert.photos.size()]);
        viewPagerAdapter=new ViewPagerAdapter(this,photos);
        viewPager.setAdapter(viewPagerAdapter);


        return view;
    }
}
