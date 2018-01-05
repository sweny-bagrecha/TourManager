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
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by user on 8/22/2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private List<User> userList;
    StorageReference storageReference;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtNumber;
        public ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_member_cardview, parent, false);
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(parent.getContext());

            storageReference = storage.getReferenceFromUrl("gs://groupexpensetracker-142017.appspot.com");
        } catch (Exception e) {

        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtName.setText(user.getName());
        holder.txtNumber.setText(user.getNumber());

//        https://firebasestorage.googleapis.com/v0/b/groupexpensetracker-142017.appspot.com/o/photos%2F14302?alt=media&token=0690bf72-1222-45cd-8006-312d2098a4aa


        Glide.with(context).load(user.getImage()).asBitmap().centerCrop().placeholder(R.drawable.user).into(new BitmapImageViewTarget(holder.image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.image.setImageDrawable(circularBitmapDrawable);
            }
        });


//        if (!user.getImage().equalsIgnoreCase("")) {
//            String tempImg = user.getImage().substring(user.getImage().lastIndexOf("/") + 1, user.getImage().length());
////            String userImage = "https://firebasestorage.googleapis.com/v0/b/groupexpensetracker-9142a.appspot.com/o/photos%2F" + tempImg + "?alt=media&token=0690bf72-1222-45cd-8006-312d2098a4aa";
//
////            Log.e("sweny","image url --->>> "+userImage);
//
//
////            storageReference.child(Uri.decode(user.getImage())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                @Override
////                public void onSuccess(Uri uri) {
////                    // Got the download URL for 'users/me/profile.png'
////                    Glide.with(context).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.image){
////                        @Override
////                        protected void setResource(Bitmap resource) {
////                            super.setResource(resource);
////                            RoundedBitmapDrawable circularBitmapDrawable =
////                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
////                            circularBitmapDrawable.setCircular(true);
////                            holder.image.setImageDrawable(circularBitmapDrawable);
////                        }
////                    });
////                }
////            }).addOnFailureListener(new OnFailureListener() {
////                @Override
////                public void onFailure(@NonNull Exception exception) {
////                    // Handle any errors
////                    Log.e("krunal", exception.getMessage());
////                }
////            });
//
//
////            Glide.with(context).load(userImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.image) {
////                @Override
////                protected void setResource(Bitmap resource) {
////                    RoundedBitmapDrawable circularBitmapDrawable =
////                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
////                    circularBitmapDrawable.setCircular(true);
////                    holder.image.setImageDrawable(circularBitmapDrawable);
////                }
////            });
//        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
