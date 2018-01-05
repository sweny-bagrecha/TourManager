package com.example.user.groupexpensetracker.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
import com.example.user.groupexpensetracker.adapter.TripDetailsAdapter;
import com.example.user.groupexpensetracker.adapter.UserAdapter;
import com.example.user.groupexpensetracker.bean.Group;
import com.example.user.groupexpensetracker.bean.User;
import com.example.user.groupexpensetracker.fragment.GroupFragment;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

public class TripDetailsActivity extends AppCompatActivity {

    TextView textGroupName, textParticipants;
    ImageView ImageviewEditGroupName;
    RecyclerView recyclerParticipants;
    Toolbar toolbar;
    private List<User> userList;
    Firebase firebase;
    private DatabaseReference mDatabase;
    String phonenumber;
    Query firebaseQuery;
    ArrayList<String> userMembersMobile;
    List<User> groupData;
    int hasNoOfGroup = 0;
    LinearLayout llNoGroupFound;
    String id;
    String participants;
    String groupKey;
    Button ButtonDeleteExit;
    TripDetailsAdapter tripDetailsAdapter;
    public static String number;
    String editedGroupName;
    String description;
    String mappingId;
    int flag=0;
    String grpMappingKeyList;
        String groupparticipants;
    String idMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        textGroupName = (TextView) findViewById(R.id.textGroupName);
        textParticipants = (TextView) findViewById(R.id.textParticipants);
        recyclerParticipants = (RecyclerView) findViewById(R.id.recyclerParticipants);
        ButtonDeleteExit = (Button) findViewById(R.id.ButtonDeleteExit);

        ButtonDeleteExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag==0) {
                    deleteGroupMapping();
                    deleteGroup();
                    finish();
                }

                else {

                    exitGroupMapping();
                   //exitGroup();
                    finish();
                }
            }
        });

        ImageviewEditGroupName = (ImageView) findViewById(R.id.ImageviewEditGroupName);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        textGroupName.setText(intent.getStringExtra("grpName"));
        textParticipants.setText(intent.getStringExtra("participants"));
        id = intent.getStringExtra("groupId");
        intent.getStringExtra(id);

        groupparticipants=intent.getStringExtra("groupParticipants");
        intent.getStringExtra(groupparticipants);

        groupKey=intent.getStringExtra("groupKey");
        intent.getStringExtra(groupKey);

//        mappingId=intent.getStringExtra("mappingId");
//        intent.getStringExtra(mappingId);


        llNoGroupFound = (LinearLayout) findViewById(R.id.llNoGroupFound);
        Firebase.setAndroidContext(TripDetailsActivity.this);
        firebase = new Firebase(Config.FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userMembersMobile = new ArrayList<>();

        groupData = new ArrayList<>();
        Firebase.setAndroidContext(TripDetailsActivity.this);
        userList = new ArrayList<>();


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");

        getUserMembers();

        ImageviewEditGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(TripDetailsActivity.this);

                dialog.setContentView(R.layout.prompts);
                dialog.setTitle("Enter group name");

                final EditText editGroupName = (EditText) dialog.findViewById(R.id.editGroupName);
                Button save = (Button) dialog.findViewById(R.id.save);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textGroupName.setVisibility(View.VISIBLE);
                        dialog.cancel();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textGroupName.setText(editGroupName.getText().toString());
                        editedGroupName = editGroupName.getText().toString();
                        updateGroupName(editedGroupName);
                        dialog.cancel();
                    }
                });

            }
        });
    }

    private void updateGroupName(String groupName) {

            Firebase ref = new Firebase(Config.FIREBASE_URL);

            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously
            Map<String, Object> post1 = new HashMap<String, Object>();

        post1.put(Config.GROUP_ID, id);
        post1.put(Config.GROUP_NAME, groupName);
        post1.put(Config.GROUP_PARTICIPANTS,groupparticipants);
        post1.put(Config.USER_WHO_ADDED_GROUP_PHONE_NO,phonenumber);
        post1.put(Config.GROUP_DESCRIPTION,description);


        Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + Config.GROUP_TABLE + "/" + groupKey, post1);


            ref.updateChildren(childUpdates);

            ref.addValueEventListener(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                    finish();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
    }






    private void getUserMembers() {

        Config.showDialog(TripDetailsActivity.this, getString(R.string.loadingFriend), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(id);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                Log.i("niral", "FRIENDS  " + snapshot.toString());

                if (snapshot.getChildrenCount() > 0) {
                    userMembersMobile.clear();
                    groupData.clear();
                    for (final com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {

                            hasNoOfGroup++;
                            userMembersMobile.add(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString());


                            //groupData.add(new User(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString()));

//                            Log.i("kinjal","===========" +phonenumber);
//                            Log.i("kinjal",":::::::::::" +postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).toString());


                            if ((postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString()).equalsIgnoreCase(phonenumber))
                            {
                                Log.i("kinjal", "===========" + phonenumber);
                                Log.i("kinjal", ":::::::::::" + postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).toString());

                                grpMappingKeyList = postSnapshot.getKey();
                                Log.i("kinjal", "okkk " + grpMappingKeyList);
                                idMap=postSnapshot.getKey();
                                Log.i("kinjal", "equals " + idMap);



                            }


                            if ((postSnapshot.child(Config.USER_WHO_ADDED_FRIEND_IN_GROUP_PHONE_NO).getValue().toString())
                                    .equalsIgnoreCase(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString())) {

                                number = postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString();
                                Log.i("kinjal", "NUMBER " + number);

                            }

                            if ((postSnapshot.child(Config.USER_WHO_ADDED_FRIEND_IN_GROUP_PHONE_NO).getValue().toString())
                                    .equalsIgnoreCase(phonenumber)) {

                                flag=0;
                                ButtonDeleteExit.setText("Delete Group");

                            }
                                else {
                                    flag=1;
                                    ButtonDeleteExit.setText("Exit Group");
                                }

                        }



                    }




                    if (userMembersMobile.size() > 0) {
                        getMemberDetails();
                    } else  {

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

                            tripDetailsAdapter = new TripDetailsAdapter(TripDetailsActivity.this, groupData);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TripDetailsActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerParticipants.setLayoutManager(linearLayoutManager);
                            recyclerParticipants.setAdapter(tripDetailsAdapter);
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
        private void deleteGroupMapping() {
            Log.i("KINJAL", "KEY for delete group mapping" + id);
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
            final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(id);

            firebase_Query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                }
            });
        }


    private void deleteGroup() {
        Log.i("KINJAL", "KEY for delete group" + id);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(id);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
            }
        });

    }

    private void exitGroupMapping() {
        Log.i("KINJAL", "KEY======" + grpMappingKeyList);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(idMap).equalTo(grpMappingKeyList);
        Log.i("KINJAL", "KEY2222======" + grpMappingKeyList);


        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("KINJAL", "KEY333333======" + grpMappingKeyList);

                    snapshot.getRef().removeValue();

                    Log.i("KINJAL", "KEY444444======" + grpMappingKeyList);

                    Log.i("KINJAL","EXIT GROUP=====" +String.valueOf(snapshot.getRef().removeValue()));


                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void exitGroup() {
        Log.i("KINJAL", "KEY" + id);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(id);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();
    }
    }

