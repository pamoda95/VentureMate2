package com.example.venturemate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryData extends AppCompatActivity {

    private  static String TAG = "dataTAG";
    ListView listView;
    TextView error;
    final ArrayList<Place> placeList = new ArrayList<>();
    ArrayList <String> mTitle = new ArrayList<>();
    ArrayList <String> mDescription= new ArrayList<>();
    ArrayList <String> mlikes= new ArrayList<>();
    ArrayList <String> images= new ArrayList<>();
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        categoryName = getIntent().getStringExtra("CATEGORY");

        setContentView(R.layout.activity_category_data);
        TextView title = findViewById(R.id.pageTitle);
        title.setText(categoryName);
        error=findViewById(R.id.error);

        listView = findViewById(R.id.listView);


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("places");

        Query query = ref.orderByChild("category").equalTo(categoryName);
        Log.d(TAG, "query taken");
        //System.out.println("pendingC"+query);
        final Boolean[] dataAvailable = {false};
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    final long[] pendingLoadCount = { dataSnapshot.getChildrenCount()};
                    System.out.println("pendingC"+pendingLoadCount[0]);
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            double latitude = issue.child("latitude").getValue(Double.class);
                            double longitude = issue.child("longitude").getValue(Double.class);
                            String  placeName = issue.child("placeName").getValue(String.class);
                            String  placeId = issue.child("placeId").getValue(String.class);
                            String  category = issue.child("category").getValue(String.class);
                            String  description = issue.child("description").getValue(String.class);
                            String  route = issue.child("route").getValue(String.class);
                            String  specialNotice = issue.child("specialNotice").getValue(String.class);
                            String  nearestTown = issue.child("nearestTown").getValue(String.class);
                            String  difficultes = issue.child("difficultes").getValue(String.class);
                            String image = issue.child("image").getValue(String.class);
                            String likes = issue.child("pLikes").getValue(String.class);
                            String comments =issue.child("pComments").getValue(String.class);
                            Place place = new Place(placeName,placeId,category,description,latitude,longitude,route,specialNotice,nearestTown,difficultes,image,likes,comments);
                            placeList.add(place);
                            Log.d(TAG, "place object created");
                            pendingLoadCount[0] = pendingLoadCount[0] - 1;
                            if (pendingLoadCount[0] == 0) {
                                Log.d(TAG, "no pending");
                                for (Place item : placeList){
                                    mTitle.add(item.getPlaceName());
                                    mDescription.add(item.getNearestTown());
                                    mlikes.add(item.getLikes());
                                    images.add(item.getImage());
                                }
                                Log.d(TAG, "data retrieved");
                                MyAdapter adapter = new MyAdapter(CategoryData.this, mTitle, mDescription, images, mlikes);
                                listView.setAdapter(adapter);
                                progressDialog.dismiss();
                            }
                            dataAvailable[0] =true;
                        }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(CategoryData.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }

        });

        if(!dataAvailable[0]){
            progressDialog.dismiss();
            error.setText("No results available");
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(CategoryData.this, DisplayPlaceDetail.class);
                myIntent.putExtra("PLACE",placeList.get(position));
                CategoryData.this.startActivity(myIntent);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList <String> rTitle;
        ArrayList <String> rDescription;
        ArrayList <String> rImgs;
        ArrayList <String> rLikes;

        MyAdapter (Context c, ArrayList <String> title, ArrayList <String>  description, ArrayList <String> imgs, ArrayList <String> likes) {
            super(c, R.layout.activity_row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;
            this.rLikes =likes;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.activity_row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);
            TextView likes = row.findViewById(R.id.likes);
            ImageView map = row.findViewById(R.id.map);



            myTitle.setText(rTitle.get(position));
            myDescription.setText(rDescription.get(position));
            likes.setText(rLikes.get(position));
            String imageIndex = rImgs.get(position);
            Glide.with(CategoryData.this).load(imageIndex).into(images);

            return row;
        }
    }


}
