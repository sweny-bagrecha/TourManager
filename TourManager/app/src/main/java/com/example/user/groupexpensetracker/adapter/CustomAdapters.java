package com.example.user.groupexpensetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.bean.User;

import java.util.ArrayList;

/**
 * Created by Tanuja on 28-09-2016.
 */
public class CustomAdapters extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<User>remove;
   ImageView image;

    public CustomAdapters(Context context, ArrayList<User> remove) {
        this.context = context;
        this.remove = remove;

        this.layoutInflater = layoutInflater.from(context);
    }
/* @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        RemoveParticipant removable=remove.get(position);

        View rowView;
        rowView = layoutInflater.inflate(R.layout.remove_participants, null);
        holder.img=(ImageView) rowView.findViewById(R.id.image);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                holder.notify();
            }
        });
        return rowView;
    }
*/

   @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       convertView = layoutInflater.inflate(R.layout.remove_participants, parent, false);
     final  User removable = remove.get(position);
TextView textView=(TextView)convertView.findViewById(R.id.textName);
       textView.setText(removable.getName());
       image = (ImageView) convertView.findViewById(R.id.image);
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                remove.remove(position);
notifyDataSetChanged();
           }
       });






       return convertView;
   }



    @Override

    public int getCount() {
        return remove.size();
    }

    @Override
    public Object getItem(int position) {
        return remove.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}




