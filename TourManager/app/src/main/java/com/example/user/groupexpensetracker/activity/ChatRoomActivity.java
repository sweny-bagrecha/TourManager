package com.example.user.groupexpensetracker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.groupexpensetracker.Message;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.adapter.MessagesListAdapter;
import com.example.user.groupexpensetracker.adapter.UserAdapter;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.groupexpensetracker.Util.Config.CHAT_GROUP_ID;
import static com.example.user.groupexpensetracker.Util.Config.CHAT_MESSAGE_KEY;
import static com.example.user.groupexpensetracker.Util.Config.FIREBASE_URL;
import static com.example.user.groupexpensetracker.Util.Config.GROUP_ID;

public class ChatRoomActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listMessage;
    EditText editChat;
    Button buttonChat;
    String noOfParticipants="";
    String groupid="";
    SharedPreferences sharedPreferences;
    String phonenumber;
    String userImage;
    private DatabaseReference mDatabase;
    Query firebaseQuery;
    Firebase firebase;
    ArrayList<String> userMessage;
    ArrayList<Message> messageList;
    String messageKey;
    MessagesListAdapter messagesListAdapter;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Intent i = getIntent();
        toolbar.setTitle(i.getStringExtra("grpName"));

        groupid=i.getStringExtra("groupId");
        i.getStringExtra(groupid);

        Firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
        userImage=sharedPreferences.getString(GlobalData.userImage,"N/A");

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatRoomActivity.this,GroupDetailsActivity.class);
                intent.putExtra("groupName",i.getStringExtra("grpName"));

                noOfParticipants=i.getStringExtra("participants");
                intent.putExtra("participants",noOfParticipants);

                groupid=i.getStringExtra("groupId");
                intent.putExtra("groupId",groupid);
                startActivity(intent);

            }
        });

        try {
            Firebase.setAndroidContext(this);


        }
        catch (Exception e) {

        }

        final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


        listMessage=(ListView)findViewById(R.id.listMessage);
        editChat = (EditText) findViewById(R.id.editChat);

        userMessage=new ArrayList<>();
        messageList=new ArrayList<>();



        listMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure ?");

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //listchat.setSelected(true);
//                        listMessage.setSelection(position);
//                        view.setSelected(true);
//                        if(true){
//                            messageList.remove(position);
//
//
//                        }
//
//                    }
//                        String removing = messageList.get(position).getMessage();
//                        Log.i("sweny", "message:" + removing);
//                        listMessage.setSelection(position);
//                        view.setSelected(true);
//                        if (true) {
//                            messageList.remove(removing);
//                        }
//                        getDeleteMessage();
//                    }
//
//                    });

                        //  }

                        //});

//                        new Firebase(FIREBASE_URL).orderByChild(Config.CHAT_MESSAGE)
//                                .equalTo((String)listMessage.getItemAtPosition(position))
//                                .addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.hasChildren()) {
//                                            Log.i("kinjal","position:" +((String)listMessage.getItemAtPosition(position)));
//                                            com.firebase.client.DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
//                                            firstChild.getRef().removeValue();
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(FirebaseError firebaseError) {
//
//                                    }
//                                });
                        //String removing = messageList.get(position).getMessage();
                        //Log.i("sweny", "message:" + removing);
//                        listMessage.setSelection(position);
//                        view.setSelected(true);
//                        if (true) {
//                            getDeleteMessage();
//
//                            //messageList.remove(removing);
//                        }
//                    }

                        getDeleteMessage();
                    }

                   });



                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });


        buttonChat = (Button) findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertChat(groupid,editChat.getText().toString(),mydate);
                editChat.setText("");

            }
        });
        getMessage();
    }



//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.CHAT_TABLE);
//        final Query firebase_Query = mDatabase.orderByChild(Config.CHAT_MESSAGE);
//        Log.i("sweny","i am in getDeleteMessage");
//        firebase_Query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot snapshot) {
//
//                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    private void getMessage() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.CHAT_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.CHAT_GROUP_ID).equalTo(groupid);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("niral", "FRIENDS  " + snapshot.toString());

                if (snapshot.getChildrenCount() > 0) {
                    userMessage.clear();
                    messageList.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.CHAT_MESSAGE)) {

                            userMessage.add(postSnapshot.child(Config.CHAT_MESSAGE).getValue().toString());
                         if(postSnapshot.child(Config.CHAT_USER_PHONE).getValue().toString().equalsIgnoreCase(phonenumber)){

                             messageList.add(new Message(postSnapshot.child(Config.CHAT_USER_IMAGE).getValue().toString(), postSnapshot.child(Config.CHAT_MESSAGE).getValue().toString(),true));
                         }else {
                             messageList.add(new Message(postSnapshot.child(Config.CHAT_USER_IMAGE).getValue().toString(), postSnapshot.child(Config.CHAT_MESSAGE).getValue().toString(),false));
                         }
//                            if ((postSnapshot.child(Config.CHAT_GROUP_ID).getValue().toString())
//                                    .equalsIgnoreCase(groupid));
//                            {
//                                messageKey=postSnapshot.getKey();
//                                Log.i("kinjal","okkk " + postSnapshot.getKey());
//
//                            }

                        }
                    }
                    if (userMessage.size() > 0) {
                        messagesListAdapter = new MessagesListAdapter(getApplicationContext(), messageList);
                        listMessage.setAdapter(messagesListAdapter);
                        Config.cancelDialog();
                    }
                  else {

                        Config.cancelDialog();

                    }

                } else {
                    Config.cancelDialog();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void insertChat(String groupid,String message,String datetime) {

        Firebase ref = new Firebase(FIREBASE_URL);
        Firebase postRef = ref.child(Config.CHAT_TABLE);
        key = postRef.child(Config.CHAT_TABLE).push().getKey();
        Log.i("KINJAL","Message id===== " +key);
        Map<String, String> post1 = new HashMap<String, String>();
        post1.put(Config.CHAT_GROUP_ID, groupid);
        post1.put(Config.CHAT_MESSAGE_KEY, key);
        post1.put(Config.CHAT_MESSAGE,message);
        post1.put(Config.CHAT_MESSAGE_DATE_TIME,datetime);
        post1.put(Config.CHAT_USER_PHONE, phonenumber);
        post1.put(Config.CHAT_USER_IMAGE, userImage);

        postRef.push().setValue(post1);

    }

    private void getDeleteMessage() {
        Log.i("KINJAL", "GROUPID " + key);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.CHAT_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(CHAT_MESSAGE_KEY).equalTo(key);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().setValue(null);
                    Log.i("KINJAL","=== " + snapshot.toString());

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
            }
        });
    }


}


//    private void getMemberDetails() {
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.CHAT_TABLE);
//        firebaseQuery = mDatabase.orderByChild(Config.CHAT_USER_PHONE);
//        firebaseQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount() > 0) {
//
//
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        if (postSnapshot.hasChild(Config.CHAT_USER_PHONE)) {
//                            for (int i = 0; i < userMembersMobile.size(); i++) {
//
//
//                                if (userMembersMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.CHAT_USER_PHONE).getValue().toString())) {
//                                    messageList.get(i).setFromImage(postSnapshot.child(Config.CHAT_USER_IMAGE).getValue().toString());
//                                }
//
//                            }
//                        }
//                    }
//
//                    MessagesListAdapter messagesListAdapter = new MessagesListAdapter(getApplicationContext(), messageList);
//                    listMessage.setAdapter(messagesListAdapter);
//                    Config.cancelDialog();
//
//                } else {
//                    Config.cancelDialog();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Config.cancelDialog();
//
//            }
//        });
//    }

