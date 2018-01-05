package com.example.user.groupexpensetracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
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
import com.twitter.sdk.android.core.models.Card;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nteam on 7/12/16.
 */

public class ChooseGroupMemberAdapter extends RecyclerView.Adapter<ChooseGroupMemberAdapter.MyViewHolder> {
    private Context context;
    private List<User> userList;
    public static ArrayList<User> selectedUserList;
    public static ArrayList<User> notselectedUserList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtNumber;
        public ImageView image, imgisSelected;
        private CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            image = (ImageView) itemView.findViewById(R.id.image);
            imgisSelected = (ImageView) itemView.findViewById(R.id.imgisSelected);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

    }

    public ChooseGroupMemberAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        selectedUserList = new ArrayList<>();

    }




    @Override
    public ChooseGroupMemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_member_cardview_selection, parent, false);
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(parent.getContext());

        } catch (Exception e) {

        }
        return new ChooseGroupMemberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChooseGroupMemberAdapter.MyViewHolder holder, int position) {
        final User user = userList.get(position);
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


        if (user.isSelected()) {

            holder.imgisSelected.setImageResource(R.drawable._is_checked);

        } else {
            holder.imgisSelected.setImageResource(R.drawable.not_selected);

        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (user.isSelected()) {

                    holder.imgisSelected.setImageResource(R.drawable.not_selected);
                    user.setSelected(false);
                    selectedUserList.add(user);


                } else {
                    holder.imgisSelected.setImageResource(R.drawable._is_checked);
                    user.setSelected(true);
                    selectedUserList.add(user);


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}


































