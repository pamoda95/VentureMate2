package com.example.venturemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocationDetailAdder extends AppCompatActivity {

    ImageView imageView ;
    Spinner spin_cat,spin_cit;
    Button detail_adding_cancel_button,submit_button;
    EditText special_notice_text,routeText,difficultiesText,locationDescriptionText,placeNameText;
    ImageButton camera_button,gallery_button;

    DatabaseReference myRef;
    Uri selectedImage;
    String id;
    static String imageUrl = "not set";
    double lat,lon;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail_adder);

        Bundle bundle = getIntent().getExtras();
        lat=bundle.getDouble("latitude");
        lon=bundle.getDouble("longitude");

        String[] categories = {  "Camping", "Cycling","Hiking","Rafting","Rock Climbing","Safari","Other" };
        String[] cities = {
                "Ambalangoda","Ampara","Anuradhapura","Badulla","Battaramulla South","Batticaloa","Bentota","Beruwala","Chilaw","Colombo","Dambulla","Dehiwala-Mount Lavinia","Devinuwara","Eravur Town","Galle","Gampaha","Gampola","Hanwella Ihala","Hatton","Hendala","Homagama","Horana South","Horawala Junction","Ja Ela","Jaffna","Kalmunai","Kalutara","Kandana","Kandy","Kataragama","Katunayaka","Kegalle","Kelaniya","Kilinochchi","Kolonnawa","Kotikawatta","Kuliyapitiya","Kurunegala","Maharagama","Matale","Matara","Minuwangoda","Monaragala","Moratuwa","Mulleriyawa","Negombo","Nuwara Eliya","Panadura","Peliyagoda","Pita Kotte","Point Pedro","Polonnaruwa","Puttalam","Ratnapura","Sri Jayewardenepura Kotte","Tangalle","Trincomalee","Vakarai","Valvedditturai","Vavuniya","Wattala","Wattegama","Weligama","Welisara","Wellawaya"
        };

        imageView = findViewById(R.id.placeImage);
        detail_adding_cancel_button = findViewById(R.id.detail_adding_cancel_button);
        submit_button = findViewById(R.id.submit_button);
        special_notice_text = findViewById(R.id.special_notice_text);
        routeText = findViewById(R.id.routeText);
        difficultiesText = findViewById(R.id.difficultiesText);
        locationDescriptionText = findViewById(R.id.locationDescriptionText);
        placeNameText = findViewById(R.id.placeNameText);

        myRef = FirebaseDatabase.getInstance().getReference("places");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("places");


        spin_cat = (Spinner) findViewById(R.id.category_list);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_cat.setAdapter(categoriesAdapter);

        spin_cit = (Spinner) findViewById(R.id.nearestTownList);
        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_cit.setAdapter(citiesAdapter);

        camera_button = (ImageButton) findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        gallery_button = (ImageButton) findViewById(R.id.gallery_button);
        gallery_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSubmitButtonClicked();
            }
        });

        detail_adding_cancel_button = (Button) findViewById(R.id.detail_adding_cancel_button);
        detail_adding_cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(LocationDetailAdder.this, CategorySelection.class);
                LocationDetailAdder.this.startActivity(myIntent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(),imageBitmap,"title",null);
                   selectedImage= Uri.parse(path);

                }
                break;
            }
            case 1: {
                    if (resultCode == RESULT_OK) {
                        selectedImage = data.getData();
                        imageView.setImageURI(selectedImage);
                    }
                    break;
                }
            }
        }


        public void onSubmitButtonClicked (){
            uploadData();
        }

    private void uploadData() {

        if(selectedImage != null && !placeNameText.getText().toString().equals( "")
                && !locationDescriptionText.getText().toString().equals( "")
                && !routeText.getText().toString().equals( "")
                && !special_notice_text.getText().toString().equals( "")
                && !difficultiesText.getText().toString().equals( "")
        )
        {
            id = UUID.randomUUID().toString();

           final Map<String, Object> place = new HashMap<String, Object>();
            place.put("placeId", id);
            place.put("placeName", placeNameText.getText().toString());
            place.put("category", spin_cat.getSelectedItem().toString());
            place.put("description", locationDescriptionText.getText().toString());
            place.put("nearestTown", spin_cit.getSelectedItem().toString());
            place.put("route", routeText.getText().toString());
            place.put("specialNotice", special_notice_text.getText().toString());
            place.put("difficultes", difficultiesText.getText().toString());
            place.put("pLikes", "0");
            place.put("pComments", "0");
            place.put("latitude",lat);
            place.put("longitude",lon);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");

            final StorageReference ref = storageReference.child(id);
            UploadTask  uploadTask = ref.putFile(selectedImage);


            progressDialog.show();

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        imageUrl = ref.getDownloadUrl().toString();

                        throw task.getException();

                    }
                    imageUrl = ref.getDownloadUrl().toString();
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        place.put("image",downloadUri.toString());
                        myRef.child(id).setValue(place);

//                        DatabaseReference loc_ref = myRef;
//                        GeoFire geoFire = new GeoFire(loc_ref);
//                        geoFire.setLocation(id, new GeoLocation(lat, lon));

                        DatabaseReference dummy = FirebaseDatabase.getInstance().getReference("DummyPlaces");
                        GeoFire dGeoFire = new GeoFire(dummy);
                        dGeoFire.setLocation(id, new GeoLocation(lat, lon));
                        dummy.child(id).child("place_name").setValue(placeNameText.getText().toString());


                        progressDialog.dismiss();
                        Toast.makeText(LocationDetailAdder.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(LocationDetailAdder.this, CategorySelection.class);
                        LocationDetailAdder.this.startActivity(myIntent);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(LocationDetailAdder.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(LocationDetailAdder.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();

        }

    }




}
