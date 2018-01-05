package com.example.user.groupexpensetracker.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.activity.AddGroupActivity;
import com.example.user.groupexpensetracker.activity.AddMembersActivity;
import com.example.user.groupexpensetracker.R;
//import com.example.user.groupexpensetracker.User;
import com.example.user.groupexpensetracker.adapter.UserAdapter;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MembersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    Firebase firebase;
    private DatabaseReference mDatabase;
    String phonenumber;
    Query firebaseQuery;
    ArrayList<String> userMembersMobile;
    List<User> membersData;
    LinearLayout llNoFriendFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_members, container, false);

        llNoFriendFound = (LinearLayout) v.findViewById(R.id.llNoFriendFound);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase(Config.FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userMembersMobile = new ArrayList<>();

        membersData = new ArrayList<>();


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        Firebase.setAndroidContext(getActivity());
        userList = new ArrayList<>();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");


        getUserMembers();

        FloatingActionButton floatingAdd = (FloatingActionButton) v.findViewById(R.id.floatingAdd);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddMembersActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }


    private void getUserMembers() {

        Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.FRIENDS_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.USER_WHO_ADDED_FRIEND_PHONE_NO).equalTo(phonenumber);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("niral", "FRIENDS  " + snapshot.toString());

                if (snapshot.getChildrenCount() > 0) {
                    userMembersMobile.clear();
                    membersData.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.FRIEND_PHONE_NO)) {


                            userMembersMobile.add(postSnapshot.child(Config.FRIEND_PHONE_NO).getValue().toString());
                            membersData.add(new User(postSnapshot.child(Config.FRIEND_NAME).getValue().toString(), postSnapshot.child(Config.FRIEND_PHONE_NO).getValue().toString(), ""));
                        }
                    }
                    if (userMembersMobile.size() > 0) {
                        getMemberDetails();
                    } else {

                        Config.cancelDialog();
                        llNoFriendFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Config.cancelDialog();
                    llNoFriendFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMemberDetails() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
        firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO);
        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {


                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
                            for (int i = 0; i < userMembersMobile.size(); i++) {


                                if (userMembersMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
                                    membersData.get(i).setImage(postSnapshot.child(Config.USER_IMAGE).getValue().toString());
                                }
                            }
                        }
                    }

                    userAdapter = new UserAdapter(getActivity(), membersData);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(userAdapter);
                    Config.cancelDialog();

                } else {
                    Config.cancelDialog();
                    llNoFriendFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Config.cancelDialog();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}