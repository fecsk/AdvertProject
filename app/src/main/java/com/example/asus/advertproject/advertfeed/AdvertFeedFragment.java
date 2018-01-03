package com.example.asus.advertproject.advertfeed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.main.NavigationManager;
import com.example.asus.advertproject.model.Advert;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 2017. 11. 08..
 */
//this fragment shows all adverts
public class AdvertFeedFragment extends Fragment implements AdvertClickListener{

    private NavigationManager manager;
    private View view;
    private List<Advert> advertList;
    private DatabaseReference mDatabase;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;






    public AdvertFeedFragment() {
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

        initViews();
        loadData();
        return view;
    }

    private void initViews() {

        getActivity().setTitle("Advert Feed");
        advertList = new ArrayList<>();
        manager = new NavigationManager(getFragmentManager());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerViewAdapter(advertList, getContext(), this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    private void loadData() {
        //download data from firebase

        mDatabase.child("adverts").orderByChild("hidden").equalTo("no")

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()) {}
                        advertList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            Advert a= data.getValue(Advert.class);
                            advertList.add(a);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }






    @Override
    public void onItemClick(Advert advert) {
        DetailedAdvertFragment advertDetailFragment = new DetailedAdvertFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("advert",advert);
        advertDetailFragment.setArguments(bundle);
        manager.switchToFragment(advertDetailFragment);
    }


}
