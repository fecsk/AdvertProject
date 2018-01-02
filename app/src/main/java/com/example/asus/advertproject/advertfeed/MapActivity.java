package com.example.asus.advertproject.advertfeed;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.permissions.Permissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    public final static int RC_SUCCESS = 1;
    private final static String TAG = "MapActivity: ";

    private GoogleMap mMap;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Add Fab to confirm selection
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", latitude);
                returnIntent.putExtra("longitude", longitude);
                setResult(RC_SUCCESS, returnIntent);
                finish();
            }
        });


        LatLng marosVasarhely = new LatLng(46.55, 24.56);
        //mMap.addMarker(new MarkerOptions().position(marosVasarhely).title("Marker in Marosvasarhely"));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Permissions.permissionRequest(this, Permissions.permissions, Permissions.PERMISSION_KEY);
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marosVasarhely,13));
        mMap.setMaxZoomPreference(14.0f);
    }


}