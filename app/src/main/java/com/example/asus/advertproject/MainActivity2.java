package com.example.asus.advertproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus.advertproject.advertfeed.Advert;
import com.example.asus.advertproject.advertfeed.AdvertClickListener;
import com.example.asus.advertproject.advertfeed.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements AdvertClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private DatabaseReference mDatabase;
     List<Advert> advertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertfeed_fragment);
        advertList=new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapter(advertList,this.getApplicationContext(),this);
        mRecyclerView.setAdapter(mAdapter);
        loadData();



    }
    @Override
    public void onItemClick(Advert advert) {

    }

    private void sendPostToFirebase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String timestamp = Long.toString(System.currentTimeMillis());
        Advert advert=new Advert("Elado banan!!","nagyon finom erett banan 10 lej/kg vagy cserelem kukoricara 1:10 aranyban","abc",
                "42w4234234","433543543","gs://advertproject-10f39.appspot.com/advertpics/banana.jpg","gs://advertproject-10f39.appspot.com/profilepics/harambe.jpg");
        FirebaseDatabase.getInstance().getReference("adverts").child(timestamp).setValue(advert);
    advertList.add(advert);
    }
    private void loadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("adverts").orderByKey()

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()) {}
                        advertList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            Advert a= data.getValue(Advert.class);
                            advertList.add(a);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
