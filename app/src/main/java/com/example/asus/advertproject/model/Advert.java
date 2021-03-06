package com.example.asus.advertproject.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Asus on 2017. 11. 02..
 */

public class Advert implements Parcelable {
    public String advertID;
    public String title;
    public String description;
    public String creatorid;
    public String timestamp;
    public String photoURL;
    public String creator_photo_URL;
    public ArrayList<String> photos;
    public double latitude;
    public double longitude;
    public String  hidden;

    public Advert()
    {};

    public Advert(String advertID,String title, String description, String creatorid,  String timestamp, double latitude, double longitude, String photoURL, String creator_photo_URL, ArrayList<String> photos,String hidden) {
        this.advertID=advertID;
        this.title = title;
        this.description = description;
        this.creatorid = creatorid;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoURL = photoURL;
        this.creator_photo_URL=creator_photo_URL;
        this.photos=photos;
        this.hidden=hidden;
        }


    protected Advert(Parcel in) {
        this.advertID=in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.creatorid = in.readString();
        this.timestamp = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.photoURL =in.readString();
        this.creator_photo_URL=in.readString();
        this.photos = (ArrayList<String>) in.readSerializable();
        this.hidden=in.readString();
    }

    public static final Creator<Advert> CREATOR = new Creator<Advert>() {
        @Override
        public Advert createFromParcel(Parcel in) {
            return new Advert(in);
        }

        @Override
        public Advert[] newArray(int size) {
            return new Advert[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(advertID);
        parcel.writeString(title );
        parcel.writeString( description);
        parcel.writeString(creatorid );
        parcel.writeString( timestamp);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(photoURL);
        parcel.writeString(creator_photo_URL);
        parcel.writeSerializable(photos);
        parcel.writeString(hidden);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getCreator_photo_URL() {
        return creator_photo_URL;
    }
}
