package com.example.venturemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class ReviewActivity extends AppCompatActivity {
    String myUid, myEmail, myName, myDP, postID, postTitle;

    ImageView pImage,profile_Image;
    TextView pTitle, pLikes, pComments;
    ImageButton likeBtn, shareBtn,commentBtn;
    LinearLayout profileLayout;

    RecyclerView recyclerView;

    List<ModelComment> commentList;
    AdapterComments adapterComments;

    EditText comment_text;
    ImageButton sendBtn;

    int noOfLikes;

    boolean mProcessComment = false;
    boolean mProcessLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ActionBar ac = getSupportActionBar();
        ac.setTitle("Place Review");
        ac.setDisplayShowHomeEnabled(true);
        ac.setDisplayHomeAsUpEnabled(true);

        /*ImageView imageView = new ImageView(ac.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.adventure_travel);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        ac.setCustomView(imageView);*/

        Intent intent = getIntent();
        postID = intent.getStringExtra("placeId");
        //DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("places");
        //postID = "a950dd1d-46e3-496b-bc72-1acacc7c5482";

        pImage = findViewById(R.id.pImage);
        profile_Image = findViewById(R.id.profile_Image);
        pTitle = findViewById(R.id.pTitle);
        pLikes = findViewById(R.id.pLikes);
        pComments = findViewById(R.id.pComments);

        likeBtn = findViewById(R.id.like);
        commentBtn = findViewById(R.id.comment);
        shareBtn = findViewById(R.id.share);

        comment_text = findViewById(R.id.comment_txt);
        sendBtn = findViewById(R.id.send_btn);

        recyclerView = findViewById(R.id.recyclerView);


        loadPostInfo(this);
        //loadUserInfo(this,ac);
        //createLikeDatabase();


        myName=UserDetails.username;
        myUid=UserDetails.uid;
        myDP=UserDetails.userImage;

        try {
            Picasso.with(this).load(myDP).into(profile_Image);
        }
        catch (Exception e) {

        }
        
        loadComments();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likePost();
            }
        });
    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        commentList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("placeReviews").child(postID).child("comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelComment modelComment = ds.getValue(ModelComment.class);

                    commentList.add(modelComment);

                    adapterComments = new AdapterComments(getApplicationContext(), commentList,myUid,myName);
                    recyclerView.setAdapter(adapterComments);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadLike() {

        final DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("likes");
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postID).hasChild(userID)) {
                    likeBtn.setImageResource(R.drawable.ic_liked);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void likePost() {
        mProcessLike = true;

        final DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("likes");
        final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("places");
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessLike) {
                    if (dataSnapshot.child(postID).hasChild(userID)) {
                        postsRef.child(postID).child("pLikes").setValue(""+(noOfLikes-1));
                        likesRef.child(postID).child(userID).removeValue();
                        mProcessLike = false;
                        likeBtn.setImageResource(R.drawable.ic_like);
                    } else {
                        postsRef.child(postID).child("pLikes").setValue(""+(noOfLikes+1));
                        likesRef.child(postID).child(userID).setValue("Liked");
                        mProcessLike = false;
                        likeBtn.setImageResource(R.drawable.ic_liked);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("placeReviews").child(postID).child("comments");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("cId", timeStamp);
        hashMap.put("comment",comment);
        hashMap.put("uid", userID);
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


    private void updateCommentCount() {
        mProcessComment = true;
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("places").child(postID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessComment) {
                    String comments = ""+dataSnapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments)+1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    pComments.setText(newCommentVal+" Comments");
                    mProcessComment = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo(final Context context, final ActionBar ac) {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myRef = FirebaseDatabase.getInstance().getReference("users").orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        System.out.println("bbbb user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("bbbb user");
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    myName = ""+ds.child("name").getValue();
                    myDP = ""+ds.child("image").getValue();
                    System.out.println("bbbb"+myName);
                    UserDetails.username=myName;
                    UserDetails.userImage=myDP;

                    try {
                        Picasso.with(context).load(myDP).into(profile_Image);
                    }
                    catch (Exception e) {

                    }
                    ac.setSubtitle("Signed in as: "+ myName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadPostInfo(final Context context) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("places");

        Query query = ref.orderByKey().equalTo(postID);
        System.out.println("ssss"+postID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("aaaawwwwa");
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String pTitle_t = ""+ds.child("placeName").getValue();
                    String pLikes_t = ""+ds.child("pLikes").getValue();
                    String pImage_t = ""+ds.child("image").getValue();
                    String pComment_t = ""+ds.child("pComments").getValue();

                    pTitle.setText(pTitle_t);
                    pLikes.setText(pLikes_t+" Likes");
                    pComments.setText(pComment_t+" Comments");
                    noOfLikes = Integer.parseInt(pLikes_t);

                    System.out.println("aaaa"+pTitle_t);
                    System.out.println("bbbb"+pLikes_t);

                    if (pImage_t.equals("noImage")) {
                        pImage.setVisibility(View.GONE);
                        System.out.println("bbbbmmm");
                    }
                    else {
                        pImage.setVisibility(View.VISIBLE);
                        System.out.println("bbbbnnnn");
                        try {
                            Picasso.with(context).load(pImage_t).into(pImage);

                        }
                        catch (Exception e) {

                        }
                    }

                }

                loadLike();
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
