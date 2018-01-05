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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by user on 12/16/2016.
 */

public class CustomTripAdapter extends RecyclerView.Adapter<CustomTripAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;
    StorageReference storageReference;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageTrip;



        public MyViewHolder(View itemView) {
            super(itemView);

            imageTrip = (ImageView) itemView.findViewById(R.id.imageTrip);

        }

    }

    public CustomTripAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public CustomTripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_trip_memories, parent, false);
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(parent.getContext());

            storageReference = storage.getReferenceFromUrl(Config.FILEUPLOAD_URL);
        } catch (Exception e) {

        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final User user = userList.get(position);

        Glide.with(context).load(user.getImage()).asBitmap().centerCrop().placeholder(R.drawable.user_placeholder).into(new BitmapImageViewTarget(holder.imageTrip) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imageTrip.setImageDrawable(circularBitmapDrawable);
            }
        });



    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
