package com.example.venturemate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReviewActivity extends AppCompatActivity {
    String myUid, myEmail, myName, myDP, postID, postTitle;

    ImageView pImage;
    TextView pTitle, pLikes;
    ImageButton likeBtn, shareBtn;
    Button commentBtn;
    LinearLayout profileLayout;

    EditText comment_text;
    ImageButton sendBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ActionBar ac = getSupportActionBar();
        ac.setTitle("Place Review");
        ac.setDisplayShowHomeEnabled(true);
        ac.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        postID = intent.getStringExtra("placeId");

        pImage = findViewById(R.id.pImage);
        pTitle = findViewById(R.id.pTitle);
        pLikes = findViewById(R.id.pLikes);

        likeBtn = findViewById(R.id.like);
        commentBtn = findViewById(R.id.comment);
        shareBtn = findViewById(R.id.share);

        comment_text = findViewById(R.id.comment_txt);
        sendBtn = findViewById(R.id.send_btn);


        loadPostInfo();
    }

    private void loadPostInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("places");

        Query query = ref.orderByChild("placeId").equalTo(postID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
