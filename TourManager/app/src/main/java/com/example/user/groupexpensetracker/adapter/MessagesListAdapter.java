package com.example.user.groupexpensetracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.groupexpensetracker.Message;
import com.example.user.groupexpensetracker.R;

import java.util.List;

/**
 * Created by user on 12/22/2016.
 */

public class MessagesListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messagesItems;
    ImageView lblFrom;
    TextView txtMsg;

    public MessagesListAdapter(Context context, List<Message> messagesItems) {
        this.context = context;
        this.messagesItems = messagesItems;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Message m = messagesItems.get(i);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Identifying the message owner
        if (messagesItems.get(i).isSelf()) {
            // message belongs to you, so load the right aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    null);
        } else {
            // message belongs to other person, load the left aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    null);
        }

        lblFrom = (ImageView) convertView.findViewById(R.id.lblMsgFrom);
        txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        txtMsg.setText(m.getMessage());
        //lblFrom.setImageResource(R.drawable.user);

        Glide.with(context).load(m.getFromImage()).placeholder(R.drawable.user).into(lblFrom);

        return convertView;

    }
    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int i) {
        return messagesItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
