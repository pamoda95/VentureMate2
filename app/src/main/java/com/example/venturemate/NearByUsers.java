package com.example.venturemate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private boolean naerByUserFound = false;
    private int counter =0;
    double lat,lon;

    Marker mCurrLocationMarker;

    private GoogleMap mMap;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private boolean mLocationPermissionGranted;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);

        Intent intent = getIntent();

        context=this;
        Log.d(TAG , "user  latitude  "+ UserDetails.latitude);

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



    }





    private void getNearbyUsers(LatLng latLng) {
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


//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(6.798113, 79.902269), 1.5);
        Log.d(TAG ,"ON  getNearbyUsers(latlag) "+latLng.latitude +"   "+ latLng.longitude);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 1.5);
        Log.d(TAG ,"chschj  "+ geoQuery.toString()+"   "+ geoQuery);
        geoQuery.removeAllListeners();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                naerByUserFound = true;
                final String[] userName={} ;
                Log.d(TAG , "found userssssssssssssss    "+key +" "+ location.toString());

                Marker mCurrMarker = null;

//

                Log.d(TAG,nearbyUsers.toString());




                if (!Objects.equals(key, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                if (!Objects.equals(key, "wgrui23t893rph3uihnhge")) {
                    nearbyUsers.add(key);

                }



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
                markNearbyUsers();

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
//        Toast.makeText(getApplicationContext(), "USER MARKER",
//                Toast.LENGTH_LONG).show();

        Log.d(TAG,marker.getTitle());
        String title =marker.getTitle();

        if(title.equals("Current Position")){
            return false;

        }

        final String name = marker.getTitle();
        String options[] = {"Chat with "+name};


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0) {
                    UserDetails.chatWith = name;
                    Intent intent = new Intent(context, Chat.class);
                    context.startActivity(intent);
                }
            }
        });

        builder.create().show();



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
        if(!naerByUserFound){
            getNearbyUsers(latLng);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
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




    int i;

    //Adding markers to nearby users
    private void markNearbyUsers() {


        Log.d(TAG, "markNearbyUsers:  nearbyUsers size" + nearbyUsers.size());
        for (i = 0; i < nearbyUsers.size(); i++) {
            Log.i("markNearbyUsers: ", "Value of i is: " + i);
            final ArrayList<String> userNmae = new ArrayList<String>();

            DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().
                    child("Active_users").child(String.valueOf(nearbyUsers.get(i))).child("username");
            userNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG,"NAME   SNAPSHOT  " +dataSnapshot.toString());
                    userNmae.add((String) dataSnapshot.getValue());
//                    userNmae[0] = (String) dataSnapshot.getValue();
                    Log.d(TAG,"NAME      **************  " +userNmae);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference userLocationRef = FirebaseDatabase.getInstance().getReference().
                    child("Active_users").child(String.valueOf(nearbyUsers.get(i))).child("l");
            Log.d(TAG, "userLocationRef   ####  " + userLocationRef);
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


                        Marker m = mMap.addMarker(new MarkerOptions().
                            position(otherUserLocation).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.newmarkeruser2)).
                                title(userNmae.get(0)));
//                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).

                        m.setTag("set TAg");
                        m.showInfoWindow();
                        nearbyMarkers.add(m);


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

//        getNearbyUsers();


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