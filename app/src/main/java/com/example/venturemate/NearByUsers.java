package com.example.venturemate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NearByUsers extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1 ;
    private  static String TAG = "locationTAG";
    private  static String TAG2 = "markerTAG";

    private ArrayList<String> nearbyUsers;
    private ArrayList<Marker> nearbyMarkers;
    private HashMap<String, Marker> nearbyHashMap;
    private boolean userFound = false;
    private int counter =0;

    Marker mCurrLocationMarker;

    private GoogleMap mMap;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);
//        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        currentUserID = ;
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        nearbyUsers = new ArrayList<String>();
        nearbyMarkers = new ArrayList<>();
//        getNearbyUsers();



        //Find Nearby Users Button
//        Button mLookupUsers = (Button) findViewById(R.id.LookupUsers);
//        mLookupUsers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nearbyUsers = new ArrayList<String>();
//                nearbyMarkers = new ArrayList<>();
//                getNearbyUsers();
////                markNearbyUsers();
//            }
//        });

    }


//    private void getNearbyUsers() {
//
//        if (nearbyHashMap != null) {
//            Iterator iterator = nearbyHashMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Marker> pair = (Map.Entry<String, Marker>) iterator.next();
//                pair.getValue().remove();
//            }
//
//            nearbyHashMap.clear();
//
//        } else
//            nearbyHashMap = new HashMap<>();
//
//
//        DatabaseReference userLocation = FirebaseDatabase.
//                getInstance().getReference().child("Active_users");
//
//        GeoFire geoFire = new GeoFire(userLocation);
//        Log.d(TAG , "location "+userLocation.toString());
//
//        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
////        Log.d(TAG , mLastLocation.toString());
////        GeoQuery geoQuery =
////                geoFire.queryAtLocation(
////                        new GeoLocation(
////                                mLastLocation.getLatitude(), mLastLocation.getLongitude()),radius);
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(6.798113, 79.902269), 10);
//
//
//        //Query Listener
//
//        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                userFound = true;
//                Log.d(TAG , key );
//
//                if (!Objects.equals(key, "7lqitaKDlQaxeQiYDtwAyuOvkEP2")) {
//                    nearbyUsers.add(key);
//
//
////                    Log.d(TAG, "Current user is " + FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    Log.d(TAG , "UserID found" + key + " array size is" + nearbyUsers.size());
//
//
//                    Marker m = mMap.addMarker(new MarkerOptions().
//                            position(new LatLng(location.latitude, location.longitude)).
//                            title(key));
//                    nearbyMarkers.add(m);
////                    m.setTag(key);
//
//
//                    nearbyHashMap.put(key, m);
//
//                }
//                //markNearbyUsers();
//
//
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//                if (!userFound) {
////                    radius += 0.1;
//                    getNearbyUsers();
//                }
//                markNearbyUsers();
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//
//
//    }


    private void getNearbyUsers() {
        Log.d(TAG, "getNearbyUsers");


//        if (nearbyHashMap != null) {
//            Iterator iterator = nearbyHashMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Marker> pair = (Map.Entry<String, Marker>) iterator.next();
//                pair.getValue().remove();
//            }
//
//            nearbyHashMap.clear();
//
//        } else
//            nearbyHashMap = new HashMap<>();


        DatabaseReference userLocationRef = FirebaseDatabase.getInstance().getReference().
                child("Active_users").child("ULLPMiPHGCd3zYqv5OjeFuKkWOE2").child("l");




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Active_users");

        GeoFire geoFire = new GeoFire(ref);
//        Log.d(TAG , mLastLocation.toString());
//        GeoQuery geoQuery =
//                geoFire.queryAtLocation(
//                        new GeoLocation(
//                                mLastLocation.getLatitude(), mLastLocation.getLongitude()),10000);


        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(6.798113, 79.902269), 1000000);
        Log.d(TAG ,"chschj  "+ geoQuery.toString()+"   "+ geoQuery);
        geoQuery.removeAllListeners();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                userFound = true;
                Log.d(TAG , "found userssssssssssssss    "+key +" "+ location.toString());

                Marker mCurrMarker = null;

//                Log.d(TAG , "dghj");
//                Log.d(TAG, key);
//                Log.d(TAG, location.toString());
//                nearbyUsers.add(key+"  "+location);

                Log.d(TAG,nearbyUsers.toString());


//                if (!Objects.equals(key, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                if (!Objects.equals(key, "wgrui23t893rph3uihnhge")) {
                    nearbyUsers.add(key);

//                    Log.i("currentUser", "Current user is " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.i("onKeyEntered: ", "UserID found" + key + " array size is" + nearbyUsers.size());

                    Log.d(TAG,"beforeMarkertag");
                    Marker m = mMap.addMarker(new MarkerOptions().
                            position(new LatLng(location.latitude, location.longitude)).
                            title(key));
                    m.setTag(key);
                    nearbyMarkers.add(m);

//

//                    nearbyMarkers.add(m);
//                    nearbyHashMap.put(key, m);


                }
//                nearbyUsers.add(nearbyHashMap);
//                markNearbyUsers();


            }

            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
//                if (counter != 5) {
////                    radius += 0.1;
//
//                    getNearbyUsers();
//                }

                Log.d(TAG , "33333333333"+ nearbyUsers);
//                markNearbyUsers();

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d(TAG , error.toString());

            }


        });

    }

    public void  addMarkers( ArrayList nearbyMarkers){

    }







    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(Location location) {


        Log.d(TAG2, "on locaion change ");

        mLastLocation = location;
        Log.d(TAG2, "on locaion change "  + location.toString());
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        Log.d(TAG2, "after add marker" + nearbyMarkers.toString() );

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }

    }
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }


    int i;

    //Adding markers to nearby users
    private void markNearbyUsers() {

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(6, -79))
                .title("Hello world"));

        Log.d(TAG, "markNearbyUsers:  nearbyUsers size" + nearbyUsers.size());
        for (i = 0; i < nearbyUsers.size(); i++) {
            Log.i("markNearbyUsers: ", "Value of i is: " + i);

            DatabaseReference userLocationRef = FirebaseDatabase.getInstance().getReference().
                    child("Active_users").child(String.valueOf(nearbyUsers.get(i))).child("l");
            Log.d(TAG, "userLocationRef     " + userLocationRef);
            userLocationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<Object> map = (List<Object>) dataSnapshot.getValue();
                        double otherUserLat = 0;
                        double otherUserLng = 0;

                        if (map.get(0) != null && map.get(1) != null) {
                            otherUserLat = Double.parseDouble(map.get(0).toString());
                            otherUserLng = Double.parseDouble(map.get(1).toString());
                        }

                        LatLng otherUserLocation = new LatLng(otherUserLat, otherUserLng);
                        Log.d(TAG , "add marker method  otherUserLocation: "+ otherUserLocation);

                        if (nearbyMarkers.size() > i) {
                            for (int j = 0; j < nearbyMarkers.size(); j++) {
                                nearbyMarkers.get(j).remove();
                            }
                            nearbyMarkers.clear();
                            Log.d(TAG ,"markNearbyUsers: " +"Cleared, Value of i is: " + i);

                        }

//                        Marker mkr = mMap.addMarker(new MarkerOptions().
//                                        position(otherUserLocation).title("Hello Classes.User"));
//                        mkr.setVisible(true);


//
                        nearbyMarkers.add(mMap.addMarker(new MarkerOptions().
                                position(otherUserLocation).title("Hello Classes.User")));

                        Log.d(TAG, "nearbyMarkers<>  "+nearbyMarkers.toString());


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        getNearbyUsers();
//        markNearbyUsers();
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(6, -70))
//                .title("Hello world"));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
    }
}