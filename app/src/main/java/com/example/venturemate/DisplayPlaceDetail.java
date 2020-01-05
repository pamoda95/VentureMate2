package com.example.venturemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DisplayPlaceDetail extends AppCompatActivity {

    TextView textView,category_text, like_text,comment_text,description,nTownText, routeText, difficultiesText,special_notice_text;

    ImageView image,like_image,comment_image;
    Button review_button, map_button;
    LinearLayout layout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_place_detail);

        final Place place = (Place) getIntent().getParcelableExtra("PLACE");

        image = findViewById(R.id.image);
        layout3 = findViewById(R.id.layout3);

        textView = findViewById(R.id.textView);
        category_text = findViewById(R.id.category_text);
        like_text = findViewById(R.id.like_text);
        comment_text = findViewById(R.id.comment_text);
        description = findViewById(R.id.description);
        nTownText = findViewById(R.id.nTownText);
        routeText = findViewById(R.id.routeText);
        difficultiesText = findViewById(R.id.difficultiesText);
        special_notice_text = findViewById(R.id.special_notice_text);
        review_button = findViewById(R.id.review_button);
        map_button = findViewById(R.id.map_button);

        textView.setText(place.getPlaceName());
        category_text.setText(place.getCategory());
        like_text.setText(place.getLikes());
        comment_text.setText(place.getComments());
        description.setText(place.getDescription());
        nTownText.setText(place.getNearestTown());
        routeText.setText(place.getRoute());
        difficultiesText.setText(place.getDifficultes());
        special_notice_text.setText(place.getSpecialNotice());

        String imagePath = place.getImage();
        Glide.with(DisplayPlaceDetail.this).load(imagePath).into(image);

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(DisplayPlaceDetail.this, CategorySelection.class);
                DisplayPlaceDetail.this.startActivity(myIntent);
            }
        });

        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReview(place);

            }
        });


        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReview(place);

            }
        });


    }

    public void goToReview(Place place){
        Intent myIntent = new Intent(DisplayPlaceDetail.this, ReviewActivity.class);
        myIntent.putExtra("placeId",place.getPlaceId());
        DisplayPlaceDetail.this.startActivity(myIntent);
    }


}
