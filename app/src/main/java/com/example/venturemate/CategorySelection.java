package com.example.venturemate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.venturemate.ImageAdapter;
import com.example.venturemate.R;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CategorySelection extends AppCompatActivity implements LocationListener {
    private  static String TAG = "geofireTAG";
    GridView gridView;
    Button addNewLocationButton;
    LocationManager locationManager;


    static final String[] CATEGORIES = new String[] {
            "Hiking", "Cycling","Rafting","Rock Climbing","Camping","Safari","Other" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        Log.d(TAG, "oncreateGeao");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Active_users");
        Log.d(TAG, "geoaaaaaaaaaaaa");
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        GeoFire geoFire = new GeoFire(ref);

        geoFire.setLocation("X4v0Hmgxz1cr4LKNC86pjH6IpkG3", new GeoLocation(6.798813, 79.902468));



        addNewLocationButton = findViewById(R.id.add_button);
        addNewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(CategorySelection.this, AddLocation.class);
                CategorySelection.this.startActivity(myIntent);
            }
        });


        gridView = (GridView) findViewById(R.id.gridView1);

        gridView.setAdapter(new ImageAdapter(this, CATEGORIES));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById(R.id.grid_item_label))
                                .getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }

    }



    @Override
    public void onLocationChanged(Location location) {

//        Log.d(TAG, "onLocationChange");
//
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/7lqitaKDlQaxeQiYDtwAyuOvkEP2");
//        Log.d(TAG, "geoaaaaaaaaaaaa");
////        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//        GeoFire geoFire = new GeoFire(ref);
//
//        geoFire.setLocation("location", new GeoLocation(location.getLatitude(), -location.getLongitude()), new GeoFire.CompletionListener(){
//
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                Log.d(TAG, "geoError"+ error);
//
//            }
//        });

        Log.d(TAG, "geo");
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(37.7832, -122.4056), 0.6);
//        Log.d(TAG, "geo "+ geoQuery);
         }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
