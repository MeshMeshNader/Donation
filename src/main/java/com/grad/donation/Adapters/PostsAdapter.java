package com.grad.donation.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grad.donation.DataModels.PostDataModel;
import com.grad.donation.R;
import com.grad.donation.Templates.DetailsTemp;

import java.util.ArrayList;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.myPostViewHolder> {

    Context context;
    ArrayList<PostDataModel> postsList;
    boolean vertical;
    DatabaseReference mPostRef;
    boolean isAdmin;
    String userType = "";


    public PostsAdapter(Context context, ArrayList<PostDataModel> postsList, boolean vertical, boolean isAdmin) {
        this.context = context;
        this.isAdmin = isAdmin;
        this.postsList = postsList;
        this.vertical = vertical;
    }


    @NonNull
    @Override
    public myPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        View viewVertical = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_vertical, parent, false);

        myPostViewHolder viewHolder;

        if (vertical)
            viewHolder = new myPostViewHolder(viewVertical);
        else
            viewHolder = new myPostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myPostViewHolder holder, int position) {

        PostDataModel post = postsList.get(position);

        holder.postItemLabel.setText(post.getPostLabel());
        holder.postItemDesc.setText(post.getPostDescription());

        holder.postItemValue.setText(post.getPostDonationValue() + " $");

        try {
            Glide.with(holder.itemView)
                    .load(post.getPostImageURL().get(0))
                    .into(holder.postItemImage);
        } catch (Exception e) {
            Glide.with(holder.itemView)
                    .load("https://gfsstore.com/wp-content/themes/gfsstore.com/images/no_image_available.png")
                    .into(holder.postItemImage);
            Log.e("PostAdapter", "onBindViewHolder: Error while put photo" + e.getMessage());
        }

        if (isAdmin)
            userType = "admin";
        else
            userType = "user";
        holder.postItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postDetails = new Intent(context, DetailsTemp.class);
                postDetails.putExtra("postKey", post.getPostKey());
                postDetails.putExtra("userType", userType);
                context.startActivity(postDetails);
            }
        });


        if (isAdmin) {

            mPostRef = FirebaseDatabase.getInstance().getReference(context.getResources().getString(R.string.donations_ref));
            holder.postDeleteImage.setVisibility(View.VISIBLE);

            holder.postDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to delete this Item!")
                            .setCancelable(false)
                            .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        mPostRef.child(post.getPostKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (Exception e) {
                                        Log.e("RemovePost", "onClick: " + e.getMessage().toString());
                                    }
                                }
                            })
                            .setNegativeButton("No!", null)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public class myPostViewHolder extends RecyclerView.ViewHolder {

        LinearLayout postItemBtn;
        ImageView postItemImage, postDeleteImage;
        TextView postItemLabel, postItemDesc, postItemValue;


        public myPostViewHolder(@NonNull View itemView) {
            super(itemView);

            postItemBtn = itemView.findViewById(R.id.post_btn);
            postItemImage = itemView.findViewById(R.id.post_image);
            postItemLabel = itemView.findViewById(R.id.post_label);
            postItemDesc = itemView.findViewById(R.id.post_desc);
            postItemValue = itemView.findViewById(R.id.post_value);
            postDeleteImage = itemView.findViewById(R.id.delete_post);
        }
    }
}
