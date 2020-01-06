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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CategorySelection extends AppCompatActivity implements LocationListener {

    GridView gridView;
    ImageButton addNewLocationButton;
    LocationManager locationManager;
    LatLng myLocation;
    private  static String TAG = "AAATAG";


    static final String[] CATEGORIES = new String[] {
            "Hiking", "Cycling","Rafting","Rock Climbing","Camping","Safari","Other" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        ActionBar ac = getSupportActionBar();
        ac.setTitle("Venture Mate");
        ac.setDisplayShowHomeEnabled(true);
        ac.setDisplayHomeAsUpEnabled(true);

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
                String name = ((TextView) v.findViewById(R.id.grid_item_label)).getText().toString();
                Intent intent= new Intent(CategorySelection.this, CategoryData.class);
                intent.putExtra("CATEGORY", name);
                CategorySelection.this.startActivity(intent);

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
        checkUserStatus();


    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Log.d(TAG , "onLOcation "+latLng.toString());


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = UserDetails.username;

        Log.d(TAG,"ON LOCATION CHANGE  :" + userId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Active_users");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));

        ref.child(userId).child("username").setValue(UserDetails.username);


    }

    private void loadUserInfo() {
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myRef = FirebaseDatabase.getInstance().getReference("users").orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        System.out.println("bbbb user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("bbbb user");
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String myName = ""+ds.child("name").getValue();
                    String myDP = ""+ds.child("image").getValue();
                    System.out.println("bbbb"+myName);
                    UserDetails.username=myName;
                    UserDetails.userImage=myDP;
                    UserDetails.uid=myUid;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

    private void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // if user signed in stay here
            loadUserInfo();
            System.out.println("ssss");


        }else {
            startActivity(new Intent(CategorySelection.this,MainActivity.class));
            finish();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String username = UserDetails.username;

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Active_users");
            ref.child(userId).removeValue();

            FirebaseAuth.getInstance().signOut();
            checkUserStatus();


        }

        if (id == R.id.action_nearByUsers){
            Intent myIntent = new Intent(CategorySelection.this, NearByUsers.class);
            CategorySelection.this.startActivity(myIntent);
        }

        if(id == R.id.action_nearByPlaces){

            Intent myIntent = new Intent(CategorySelection.this, NearByPlaces.class);
            CategorySelection.this.startActivity(myIntent);


        }
        return super.onOptionsItemSelected(item);
    }

}
