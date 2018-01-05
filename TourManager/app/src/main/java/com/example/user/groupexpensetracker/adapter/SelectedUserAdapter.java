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
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/7/2016.
 */

public class SelectedUserAdapter extends RecyclerView.Adapter<SelectedUserAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;
    public static ArrayList<User> selectedUser;
    ArrayList<User> USER;
    StorageReference storageReference;
    SelectedUserAdapter selectedUserAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtNumber;
        public ImageView image,selectButton;
        public CardView cardView;



        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            image = (ImageView) itemView.findViewById(R.id.image);
            selectButton=(ImageView)itemView.findViewById(R.id.selectButton);
            cardView=(CardView)itemView.findViewById(R.id.cardView);

        }

    }

    public SelectedUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        selectedUser=new ArrayList<>();
        USER=new ArrayList<>();

    }


    @Override
    public SelectedUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_selected_user_adapter, parent, false);
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(parent.getContext());

            storageReference = storage.getReferenceFromUrl(Config.FILEUPLOAD_URL);
        } catch (Exception e) {

        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final User user = userList.get(position);
        holder.txtName.setText(user.getName());
        holder.txtNumber.setText(user.getNumber());

//        https://firebasestorage.googleapis.com/v0/b/groupexpensetracker-142017.appspot.com/o/photos%2F14302?alt=media&token=0690bf72-1222-45cd-8006-312d2098a4aa


        Glide.with(context).load(user.getImage()).asBitmap().centerCrop().placeholder(R.drawable.user_placeholder).into(new BitmapImageViewTarget(holder.image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.image.setImageDrawable(circularBitmapDrawable);
            }
        });

        if (user.isSelected())
        {
            holder.selectButton.setImageResource(R.drawable.ic_check_mark_btn);
        }
        else
        {
            holder.selectButton.setImageResource(R.drawable.ic_check_mark_button);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedUser.contains(user))
                {
                    holder.selectButton.setImageResource(R.drawable.ic_check_mark_button);
                    selectedUser.remove(user);
                }

                else {

                    holder.selectButton.setImageResource(R.drawable.ic_check_mark_btn);
                    selectedUser.add(user);
                }

            }

        });


    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
