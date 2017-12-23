package com.example.asus.advertproject.advertfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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


import java.util.List;

/**
 * Created by Asus on 2017. 11. 04..
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private List<Advert> advertList; // list for events
    private Context context;
    private AdvertClickListener listener;

    //viewholder for one event
    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPhotoIV;
        public ImageView mIVprofile;
        public TextView mTitleTV;
        public TextView mDescrTV;


        public MyViewHolder(View itemView) {
            super(itemView);
            mPhotoIV =(ImageView) itemView.findViewById(R.id.photo_iw);
            mTitleTV =(TextView)  itemView.findViewById(R.id.title_tv);
            mDescrTV=(TextView) itemView.findViewById(R.id.description_tv);
            mIVprofile =(ImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    public RecyclerViewAdapter(List<Advert> mQuestionList, Context context, AdvertClickListener listener) {
        this.advertList = mQuestionList;
        this.context=context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advert_item_holder, parent, false);

        return new MyViewHolder(itemView);
    }

    //fill viewholder with data
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Advert question = advertList.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(question.getPhotoURL());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.mPhotoIV);
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReferenceFromUrl(question.getCreator_photo_URL());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference2)
                .into(holder.mIVprofile);

        holder.mTitleTV.setText(question.getTitle());
        holder.mDescrTV.setText(question.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(question);
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }
}