package com.example.user.groupexpensetracker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
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

public class GroupDetailsActivity extends AppCompatActivity {

    TextView textGroupName, textParticipants;
    RecyclerView recyclerParticipants;
    Toolbar toolbar;
    private UserAdapter userAdapter;
    private List<User> userList;
    Firebase firebase;
    private DatabaseReference mDatabase;
    String phonenumber;
    Query firebaseQuery;
    ArrayList<String> userMembersMobile;
    List<User> groupData;
    ArrayList<String> groupIdslist;
    int hasNoOfGroup = 0;
    LinearLayout llNoGroupFound;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        textGroupName = (TextView) findViewById(R.id.textGroupName);
        textParticipants = (TextView) findViewById(R.id.textParticipants);
        recyclerParticipants = (RecyclerView) findViewById(R.id.recyclerParticipants);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Group Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        textGroupName.setText(intent.getStringExtra("groupName"));
        textParticipants.setText(intent.getStringExtra("participants"));
        id = intent.getStringExtra("groupId");
        intent.getStringExtra(id);


        llNoGroupFound = (LinearLayout) findViewById(R.id.llNoGroupFound);
        Firebase.setAndroidContext(GroupDetailsActivity.this);
        firebase = new Firebase(Config.FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userMembersMobile = new ArrayList<>();

        groupData = new ArrayList<>();
        Firebase.setAndroidContext(GroupDetailsActivity.this);
        userList = new ArrayList<>();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");


        getUserMembers();
    }


    private void getUserMembers() {

        Config.showDialog(GroupDetailsActivity.this, getString(R.string.loadingFriend), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(id);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("niral", "FRIENDS  " + snapshot.toString());

                if (snapshot.getChildrenCount() > 0) {
                    userMembersMobile.clear();
                    groupData.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {

                            hasNoOfGroup++;
                            userMembersMobile.add(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString());
                            //groupData.add(new User(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString()));
                        }
                    }
                    if (userMembersMobile.size() > 0) {
                        getMemberDetails();
                    } else {

                        Config.cancelDialog();
//                        llNoFriendFound.setVisibility(View.VISIBLE);
//                        recyclerParticipants.setVisibility(View.GONE);
                    }
                } else {
                    Config.cancelDialog();
//                    llNoFriendFound.setVisibility(View.VISIBLE);
//                    recyclerParticipants.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMemberDetails() {

        for (int i = 0; i < userMembersMobile.size(); i++) {

            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
            firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO).equalTo(userMembersMobile.get(i).toString());
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {

                        Log.i("kinjal", "FRIENDS  " + snapshot.toString());

//                        groupData.clear();
                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
                                groupData.add(new User(postSnapshot.child(Config.USER_NAME).getValue().toString(), postSnapshot.child(Config.USER_PHONE_NO).getValue().toString(), postSnapshot.child(Config.USER_IMAGE).getValue().toString()));
                                //Log.i("Kinjal", "FRIENDS " + groupData.size());
                            }
//                            for (int i = 0; i < userMembersMobile.size(); i++) {
//
//
//                                if (userMembersMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
//                                    groupData.get(i).setImage(postSnapshot.child(Config.USER_IMAGE).getValue().toString());
//                                }
//                            }
                            //                       }
                        }
                        if (hasNoOfGroup == groupData.size()) {

                            userAdapter = new UserAdapter(GroupDetailsActivity.this, groupData);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupDetailsActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerParticipants.setLayoutManager(linearLayoutManager);
                            recyclerParticipants.setAdapter(userAdapter);
                            Config.cancelDialog();


                        }


                    } else {
                        Config.cancelDialog();
//                        llNoFriendFound.setVisibility(View.VISIBLE);
//                        recyclerParticipants.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Config.cancelDialog();
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


