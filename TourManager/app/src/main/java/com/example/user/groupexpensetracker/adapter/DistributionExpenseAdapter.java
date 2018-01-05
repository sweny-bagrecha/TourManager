package com.example.user.groupexpensetracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.bean.User;

import java.util.ArrayList;

/**
 * Created by Tanuja on 29-09-2016.
 */
public class DistributionExpenseAdapter extends BaseAdapter {

    Context context;
    public static EditText edtexpense;
    LayoutInflater layoutInflater;
    public static ArrayList<User> Userlist;
   // public static ArrayList<EditText> editTextArrayList;
    String userAmount;


    public DistributionExpenseAdapter(Context context, ArrayList<User> Userlist) {
        this.context = context;
        this.Userlist = Userlist;
        this.layoutInflater = layoutInflater.from(context);
     //   editTextArrayList=new ArrayList<>();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.expense_members, parent, false);
        final User userlist = Userlist.get(position);
        TextView txtview = (TextView) convertView.findViewById(R.id.txtview);
        edtexpense = (EditText) convertView.findViewById(R.id.edtexpense);
       // editTextArrayList.add(edtexpense);
//        txtview.setText(Userlist.get(position).getName());
//        edtexpense.setHint(Userlist.get(position).getAmount() + "");
//
//
//        for (int i = 0; i < Userlist.size(); i++)
//
//        {
//            if (Userlist.get(i).getAmount() > getCount()) {
//                Toast.makeText(context, "message", Toast.LENGTH_LONG).show();
//            } else {
//
//            }
//        }
            edtexpense.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {


                    Userlist.get(position).setNumber(editable.toString());
                   userAmount=Userlist.get(position).getNumber();
                    Log.i("KINJAL---","EDTEXPENSE " +userAmount);
                    // notifyDataSetChanged();


                }
            });

        txtview.setText(Userlist.get(position).getName());
        edtexpense.setHint(Userlist.get(position).getNumber() + "");


        for (int i = 0; i < Userlist.size(); i++)

        {
            if (Userlist.get(i).getAmount() > getCount()) {
                Toast.makeText(context, "message", Toast.LENGTH_LONG).show();
            } else {

            }
        }
            return convertView;
        }

    @Override
    public int getCount() {
        return Userlist.size();
    }

    @Override
    public Object getItem(int position) {
        return Userlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getAmount(int position){

        Log.i("KINJAL","USERAMOUNT----" +userAmount);
        return Userlist.get(position).getNumber();}

    public String getName(int pos){return Userlist.get(pos).getName();}}