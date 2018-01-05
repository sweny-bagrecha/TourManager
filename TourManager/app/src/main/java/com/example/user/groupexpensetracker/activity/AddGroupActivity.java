package com.example.user.groupexpensetracker.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
import com.example.user.groupexpensetracker.adapter.CustomAdapters;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.adapter.UserAdapter;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.bean.User;
import com.example.user.groupexpensetracker.fragment.GroupFragment;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.R.attr.key;
import static com.example.user.groupexpensetracker.R.id.recycler;
import static com.example.user.groupexpensetracker.R.id.textMessage;

public class AddGroupActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    Query firebaseQuery;
    Toolbar toolbar;
    ArrayList<String> userMembersMobile;
    List<User> membersData;
    private ChooseGroupMemberAdapter userAdapter;
    EditText editGroupName, editSearch,editDescription;
    Button button;
    RecyclerView recyclerFriends;
    String phonenumber;
    SharedPreferences sharedPreferences;
    int countOfUserWhichAdded = 0;
    int flag=0;
    String keyMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Participants");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userMembersMobile = new ArrayList<>();
        membersData = new ArrayList<>();

        sharedPreferences = getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");

        button=(Button)findViewById(R.id.button);
        final LinearLayout linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);
        final LinearLayout linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);
        recyclerFriends = (RecyclerView) findViewById(R.id.recyclerFriends);
        editGroupName = (EditText) findViewById(R.id.editGroupName);
        editSearch = (EditText) findViewById(R.id.editSearch);
        editDescription=(EditText)findViewById(R.id.editdescription);


        getUserMembers();
        addOnTextChangeListner();


            button.setText("Next");
        linearLayout1.setVisibility(findViewById(R.id.linearLayout1).VISIBLE);
        linearLayout2.setVisibility(findViewById(R.id.linearLayout2).GONE);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag==0) {
                    if (isValidInput()) {
                        button.setText("Create");
                        linearLayout2.setVisibility(view.VISIBLE);
                        linearLayout1.setVisibility(view.GONE);
                        flag=1;

                    }
                }
                else {
                    if (isAnyMember()) {
                        insertGroup(editGroupName.getText().toString(), ChooseGroupMemberAdapter.selectedUserList.size() + "", phonenumber, editDescription.getText().toString());

                    }
                }
               }


        });

    }


    private void insertGroup(String groupname, String noOfParticipants, final String phonenumber,String trip_description) {

        Config.showDialog(AddGroupActivity.this, getString(R.string.creatingGroup), getString(R.string.please_wait));
        ChooseGroupMemberAdapter.selectedUserList.add(new User(sharedPreferences.getString(GlobalData.userName, ""), sharedPreferences.getString(GlobalData.Phonenumber, ""), sharedPreferences.getString(GlobalData.userImage, "")));

        Firebase ref = new Firebase(Config.FIREBASE_URL);
        final Firebase postRef = ref.child(Config.GROUP_TABLE);
        final String key = postRef.child(Config.GROUP_TABLE).push().getKey();



        Log.i("kinjal",key);
        Map<String, String> post1 = new HashMap<String, String>();
        post1.put(Config.GROUP_NAME, groupname);
        post1.put(Config.GROUP_PARTICIPANTS, ChooseGroupMemberAdapter.selectedUserList.size() + "");
        post1.put(Config.USER_WHO_ADDED_GROUP_PHONE_NO, phonenumber);
        post1.put(Config.GROUP_DESCRIPTION,trip_description);
        post1.put(Config.GROUP_ID, key);
        postRef.push().setValue(post1);



        postRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                for (int i = 0; i < ChooseGroupMemberAdapter.selectedUserList.size(); i++) {

                    insertGroupMembers(key, ChooseGroupMemberAdapter.selectedUserList.get(i).getNumber(), phonenumber);

                }
                    postRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void insertGroupMembers(String groupId, String memberNumber, String added_mobile_number) {

        Firebase ref = new Firebase(Config.FIREBASE_URL);
        final Firebase postRefMapping = ref.child(Config.GROUP_MAPPING_TABLE);
        keyMapping = postRefMapping.child(Config.GROUP_MAPPING_TABLE).push().getKey();


//        Intent intent=new Intent(AddGroupActivity.this,TripDetailsActivity.class);
//        intent.putExtra("mappingId",keyMapping);
//        Log.i("KINJAL","MY MAPPING ID     " +keyMapping);
//        startActivity(intent);


        Map<String, String> post1 = new HashMap<String, String>();
        post1.put(Config.GROUP_MEMBER_PHONE_NO, memberNumber);
        post1.put(Config.USER_WHO_ADDED_FRIEND_IN_GROUP_PHONE_NO, phonenumber);
        post1.put(Config.GROUP_ID, groupId);
        post1.put(Config.GROUP_MAPPING_ID, keyMapping);
        postRefMapping.push().setValue(post1);

        postRefMapping.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                countOfUserWhichAdded++;

                if (countOfUserWhichAdded == ChooseGroupMemberAdapter.selectedUserList.size()) {
                    postRefMapping.removeEventListener(this);
                    Toast.makeText(getApplicationContext(), getString(R.string.toastGroupCreated), Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    postRefMapping.removeEventListener(this);
                    Config.cancelDialog();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void getUserMembers() {


        Config.showDialog(AddGroupActivity.this, getString(R.string.loadingFriend), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.FRIENDS_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.USER_WHO_ADDED_FRIEND_PHONE_NO).equalTo(phonenumber);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


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

                    }
                } else {
                    Config.cancelDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Config.cancelDialog();

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
                                    membersData.get(i).setSelected(false);

                                }
                            }
                        }
                    }

                    userAdapter = new ChooseGroupMemberAdapter(AddGroupActivity.this, membersData);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddGroupActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerFriends.setLayoutManager(linearLayoutManager);
                    recyclerFriends.setAdapter(userAdapter);
                    ChooseGroupMemberAdapter.selectedUserList.clear();
                    Config.cancelDialog();
                } else {
                    Config.cancelDialog();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Config.cancelDialog();

            }
        });

    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_group_menu, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.CREATE:
//
//                if (isValidInput()) {
//                    ChooseGroupMemberAdapter.selectedUserList.add(new User(sharedPreferences.getString(GlobalData.userName, ""), sharedPreferences.getString(GlobalData.Phonenumber, ""), sharedPreferences.getString(GlobalData.userImage, "")));
//                    insertGroup(editGroupName.getText().toString(), ChooseGroupMemberAdapter.selectedUserList.size() + "", phonenumber);
//                }
//
//                return true;
//        }

//        return true;
//    }

    private boolean isAnyMember()
    {
        if (ChooseGroupMemberAdapter.selectedUserList.size() == 0) {


            Toast.makeText(getApplicationContext(), getString(R.string.toastAddAtleastOneMember), Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;

    }


    private boolean isValidInput() {
        if (editGroupName.getText().toString().trim().length() == 0)
        {

            editGroupName.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.toastGroupName), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (editDescription.getText().toString().trim().length() == 0)
        {

            editDescription.requestFocus();
            Toast.makeText(getApplicationContext(),"Please enter group description", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (ChooseGroupMemberAdapter.selectedUserList.size() == 0) {
//
//
//            Toast.makeText(getApplicationContext(), getString(R.string.toastAddAtleastOneMember), Toast.LENGTH_SHORT).show();
//            return false;
//
//        }
        return true;
    }

    private void addOnTextChangeListner() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {


                query = query.toString().toLowerCase();

                final List<User> filteredList = new ArrayList<>();

                for (int i = 0; i < membersData.size(); i++) {

                    final String text = membersData.get(i).getName().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(membersData.get(i));
                    }
                }

                userAdapter = new ChooseGroupMemberAdapter(AddGroupActivity.this, filteredList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddGroupActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerFriends.setLayoutManager(linearLayoutManager);
                recyclerFriends.setAdapter(userAdapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
