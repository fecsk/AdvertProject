package com.example.asus.advertproject.advertfeed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.advertproject.R;

import java.io.File;
import java.util.List;

/**
 * Created by Asus on 2017. 12. 30..
 */

public class MyListViewAdapter extends ArrayAdapter<Bitmap> {
    private List<Bitmap> objects;
    private final Context context;
    public MyListViewAdapter(Context context, List<Bitmap> objects) {
        super(context, R.layout.listviewitem, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.listviewitem, parent,
                false);
        ImageView imgbtn=(ImageView)  rowView.findViewById(R.id.deleteIV);
        final int pz=position;
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                                MyListViewAdapter.this.remove(getItem(pz));
                                MyListViewAdapter.this.notifyDataSetChanged();

            }});




        ImageView tvimg=(ImageView) rowView.findViewById(R.id.imgV);
        tvimg.setImageBitmap(objects.get(position));

        return rowView;

    }
    public List<Bitmap> getItems() {
        return objects;
    }


}