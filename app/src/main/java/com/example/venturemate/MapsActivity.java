package com.example.venturemate;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;

import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    String place_name;
    static double place_latitude,place_longitude;
    static double current_latitude,current_longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        place_name = getIntent().getStringExtra("Place_name");
        Bundle bundle = getIntent().getExtras();
        place_latitude=bundle.getDouble("latitude");
        place_longitude=bundle.getDouble("longitude");

        String id = UserDetails.uid;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Active_users").child(id);
        GeoFire geoFire = new GeoFire(ref);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        LatLng place = new LatLng(place_latitude, place_longitude);
        mMap.addMarker(new MarkerOptions().position(place).title(place_name));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera(zoom);

        /*LatLng origin = new LatLng(current_latitude, current_longitude);
        LatLng destination = new LatLng(place_latitude, place_longitude);
        DrawRouteMaps.getInstance(this)
                .draw(origin, destination, mMap);
        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.ic_place_foreground, "Current Location");
        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.ic_destination_foreground, place_name);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));*/


    }

    @Override
    public void onLocationChanged(Location location) {
        current_latitude = location.getLatitude();
        current_longitude = location.getLongitude();
    }
}
