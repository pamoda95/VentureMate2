package com.example.venturemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.zip.Inflater;

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


        loadPostInfo(this);
        loadUserInfo(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }

    private void postComment() {
        final String comment = comment_text.getText().toString().trim();

        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this,"Comment is empty...",Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference ref = FirebaseDatabase.getInstance("placeReviews").getReference().child(postID).child("comments");

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("cId", timeStamp);
        hashMap.put("comment",comment);
        hashMap.put("uid", myUid);
        hashMap.put("uDp", myDP);
        hashMap.put("uName", myName);

        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ReviewActivity.this, "Comment Added...", Toast.LENGTH_SHORT).show();
                comment_text.setText("");
                updateCommentCount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean mProcessComment = false;
    private void updateCommentCount() {
        mProcessComment = true;
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("placeReviews").child(postID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessComment) {
                    String comments = ""+dataSnapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments)+1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    mProcessComment = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo(final Context context) {
        Query myRef = FirebaseDatabase.getInstance().getReference("users");
        myRef.orderByChild("uid").equalTo(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    myName = ""+ds.child("name").getValue();
                    myDP = ""+ds.child("image").getValue();

                    try {
                        Picasso.with(context).load(myDP).into(pImage);
                    }
                    catch (Exception e) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostInfo(final Context context) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("places");

        Query query = ref.orderByChild("placeId").equalTo(postID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String pTitle_t = ""+ds.child("pName").getValue();
                    String pLikes_t = ""+ds.child("pLikes").getValue();
                    String pImage_t = ""+ds.child("pImage").getValue();

                    pTitle.setText(pTitle_t);
                    pLikes.setText(pLikes_t+"Likes");

                    if (pImage_t.equals("noImage")) {
                        pImage.setVisibility(View.GONE);
                    }
                    else {
                        pImage.setVisibility(View.VISIBLE);

                        try {
                            Picasso.with(context).load(pImage_t).into(pImage);
                        }
                        catch (Exception e) {

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
