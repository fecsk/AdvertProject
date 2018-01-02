package com.example.asus.advertproject.advertfeed;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus.advertproject.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;

/**
 * Created by Asus on 2017. 12. 23..
 */

public class ViewPagerAdapter extends PagerAdapter {
    Fragment activity;
    String[] images;
    LayoutInflater inflater;

    public ViewPagerAdapter(Fragment activity, String[] images)
    {
        this.activity=activity;
        this.images=images;
    }
    @Override
    public int getCount()
    {
        return images.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container,int position)
    {
        inflater=(LayoutInflater) activity.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.viewpager_item,container,false);
        ImageView image;
        image=(ImageView) itemView.findViewById(R.id.imageView2);
        DisplayMetrics dis=new DisplayMetrics();
        activity.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height=dis.heightPixels;
        int width=dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);
        try {
            Glide.with(activity.getActivity().getApplicationContext())
                    .load(images[position])
                    .into(image);
        }
        catch (Exception e)
        {
        }

        container.addView(itemView);
        return itemView;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}
