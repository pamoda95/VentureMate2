package com.example.venturemate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder> {

    Context context;
    List<ModelComment> commentList;
    String myUid,myName,postId;

    public AdapterComments(Context context, List<ModelComment> commentList,String myUid,String myName) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.myName = myName;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String uid = commentList.get(position).getUid();
        final String name = commentList.get(position).getuName();
        final String image = commentList.get(position).getuDp();
        String cid = commentList.get(position).getcId();
        String comment = commentList.get(position).getComment();
        //String timestamp = commentList.get(position).getTimestamp();

        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTimeInMillis(Long.parseLong(cid));
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
        String pTime = sdf.format(d);

        holder.nameTv.setText(name);
        holder.commentTv.setText(comment);
        holder.timeTv.setText(pTime);

        try {
            Picasso.with(context).load(image).placeholder(R.drawable.profile).into(holder.profileIv);
        } catch (Exception e) {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!myUid.equals(uid)) {
                    String options[] = {"Chat with "+name};
                    UserDetails.chatWithImage = image;

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getRootView().getContext());


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

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Chat with "+name);

                    builder.setItems(, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        ImageView profileIv;
        TextView nameTv, commentTv,timeTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv= itemView.findViewById(R.id.profile_Image_c);
            nameTv = itemView.findViewById(R.id.name_c);
            commentTv = itemView.findViewById(R.id.comment_c);
            timeTv = itemView.findViewById(R.id.time_c);
        }
    }
}
