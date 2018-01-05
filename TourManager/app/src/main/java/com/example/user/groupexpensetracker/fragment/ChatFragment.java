package com.example.user.groupexpensetracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.activity.ChatRoomActivity;
import com.example.user.groupexpensetracker.bean.Group;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {
    ListView listView;
    TextView txtgroupName, txtparticipants;
    FloatingActionButton floatingAdd;
    Firebase firebase;
    private DatabaseReference mDatabase;
    ArrayList<User> groupid;
    List<Group> groupdata = new ArrayList<>();
    Query firebaseQuery;
    ArrayList<Group> groups;
    String phonenumber;
    SharedPreferences sharedPreferences;
    ArrayList<String> groupIdslist;
    int hasNoOfGroup = 0;
    LinearLayout llNoGroupFound;
    String groupname="";
    String participants="";
    String groupId="";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_group, container, false);

        llNoGroupFound = (LinearLayout) v.findViewById(R.id.llNoGroupFound);
        listView = (ListView) v.findViewById(R.id.listview);
        txtgroupName = (TextView) v.findViewById(R.id.txtgroupName);
        txtparticipants = (TextView) v.findViewById(R.id.txtparticipants);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase(Config.FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupIdslist = new ArrayList<>();

        Firebase.setAndroidContext(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(GlobalData.mysharedpreference, Context.MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
        groups = new ArrayList<>();
        groupid=new ArrayList<>();

        getGroupsFromGroupMapping();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),ChatRoomActivity.class);
                groupname=groupdata.get(i).getGroupname();
                intent.putExtra("grpName",groupname);

                participants=groupdata.get(i).getGroupparticipant();
                intent.putExtra("participants",participants);

                groupId=groupdata.get(i).getGroupid();
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        });
        return v;
    }

    private void getGroupsFromGroupMapping() {
        Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        firebaseQuery = mDatabase.orderByChild(Config.GROUP_MEMBER_PHONE_NO).equalTo(phonenumber);
        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() > 0) {
                    groupIdslist.clear();

                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.GROUP_ID)) {
                            String temp = postSnapshot.child(Config.GROUP_ID).getValue().toString();

                            if (!groupIdslist.contains(temp)) {
                                groupIdslist.add(temp);
                                hasNoOfGroup++;

                            }

                        } else {
                            Config.cancelDialog();
//                            llNoGroupFound.setVisibility(View.VISIBLE);
//                            listView.setVisibility(View.GONE);
                        }

                    }

                    getgroups();

                } else {
                    Config.cancelDialog();
//                    llNoGroupFound.setVisibility(View.VISIBLE);
//                    listView.setVisibility(View.GONE);

                }
                firebaseQuery.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Config.cancelDialog();

            }
        });

    }

    private void getgroups() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_TABLE);
        groupdata.clear();

        for (int i = 0; i < groupIdslist.size(); i++) {

            firebaseQuery = mDatabase.orderByChild(Config.GROUP_ID).equalTo(groupIdslist.get(i));
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.getChildrenCount() > 0) {

                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.GROUP_NAME)) {
                                String temp = postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString();

                                groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp + " participants"));
                                if (hasNoOfGroup == groupIdslist.size()) {

                                    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
                                    for (int i = 0; i < groupdata.size(); i++) {

                                        Map<String, String> datum = new HashMap<String, String>(2);

                                        datum.put("groupname", groupdata.get(i).getGroupname());
                                        //datum.put("groupparticipant", groupdata.get(i).getGroupparticipant());

                                        data.add(datum);
                                    }
                                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
                                            android.R.layout.simple_list_item_2,
                                            new String[]{"groupname", ""},
                                            new int[]{android.R.id.text1,
                                                    android.R.id.text2});

                                    listView.setAdapter(adapter);
                                    Config.cancelDialog();

                                }
                            }

                        }
                    } else {
                        Config.cancelDialog();
//                        llNoGroupFound.setVisibility(View.VISIBLE);
//                        listView.setVisibility(View.GONE);
                    }

                    firebaseQuery.removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Config.cancelDialog();
//                    llNoGroupFound.setVisibility(View.VISIBLE);
//                    listView.setVisibility(View.GONE);
                }
            });


        }


    }


     }


