package com.example.venturemate;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String place_name;
    double place_latitude,place_longitude;

    ArrayList markerPoints= new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        place_name = getIntent().getStringExtra("Place_name");
        Bundle bundle = getIntent().getExtras();
        place_latitude=bundle.getDouble("latitude");
        place_longitude=bundle.getDouble("longitude");
        LatLng place = new LatLng(place_latitude, place_longitude);
        mMap.addMarker(new MarkerOptions().position(place).title(place_name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
    }



}
