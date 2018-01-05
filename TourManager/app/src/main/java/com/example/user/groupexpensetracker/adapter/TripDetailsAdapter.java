package com.example.user.groupexpensetracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.activity.TripDetailsActivity;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by user on 12/27/2016.
 */

public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;
    StorageReference storageReference;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtNumber, textAdmin;
        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            textAdmin = (TextView) itemView.findViewById(R.id.textAdmin);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public TripDetailsAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_trip_member_details, parent, false);
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(parent.getContext());

            storageReference = storage.getReferenceFromUrl("gs://groupexpensetracker-142017.appspot.com");
        } catch (Exception e) {

        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        User user = userList.get(position);
        holder.txtName.setText(user.getName());
        holder.txtNumber.setText(user.getNumber());

        Glide.with(context).load(user.getImage()).asBitmap().centerCrop().placeholder(R.drawable.user).into(new BitmapImageViewTarget(holder.image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.image.setImageDrawable(circularBitmapDrawable);
            }
        });


        if (user.getNumber().equals(TripDetailsActivity.number)) {
            holder.textAdmin.setVisibility(View.VISIBLE);
            holder.textAdmin.setText("ADMIN");

        } else {
            holder.textAdmin.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
