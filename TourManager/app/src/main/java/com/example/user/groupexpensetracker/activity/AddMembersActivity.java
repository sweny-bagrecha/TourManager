package com.example.user.groupexpensetracker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.groupexpensetracker.R;
//import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.bean.User;
import com.example.user.groupexpensetracker.fragment.MembersFragment;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class AddMembersActivity extends AppCompatActivity {
    Toolbar toolbar;
    int REQUEST_CAMERA = 1, REQUEST_GALLERY = 2;
    EditText edtName, edtNumber;
    Button btnSave;
    String members;
    ArrayList<User> users;
    FloatingActionButton floatingEdit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Uri uri;
    StorageReference storageReference;
    Firebase firebase;
    private DatabaseReference mDatabase;
    boolean isRecordFound = false;
    boolean isMemberExists = true;
    //  TextView textView3;
    String phonenumber;
    Query firebaseQuery;
    TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);


        btnSave = (Button) findViewById(R.id.btnSave);
        textMessage = (TextView) findViewById(R.id.textMessage);

        edtName = (EditText) findViewById(R.id.edtName);
        edtNumber = (EditText) findViewById(R.id.edtNumber);

        Firebase.setAndroidContext(AddMembersActivity.this);
        sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebase = new Firebase(Config.FIREBASE_URL);

        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, phonenumber);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isValidInput()) {

                    checkIfMemberExists();

                }

            }
        });

        try {
            Firebase.setAndroidContext(this);

        } catch (Exception e) {

        }

        Intent intent = getIntent();
        if (intent.getBooleanExtra("NotAvailble", false)) {

        } else {
            edtName.setText(intent.getStringExtra("name"));

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ADD MEMBERS");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void insertMember(String mobNumber, String friendsNumber, String friendsName) {


        Config.showDialog(AddMembersActivity.this, getString(R.string.addingFriend), getString(R.string.please_wait));

        Firebase ref = new Firebase(Config.FIREBASE_URL);
        Firebase memberRef = ref.child(Config.FRIENDS_TABLE);
        Map<String, String> memberData = new HashMap<String, String>();
        memberData.put(Config.USER_WHO_ADDED_FRIEND_PHONE_NO, mobNumber);
        memberData.put(Config.FRIEND_PHONE_NO, "+91" + friendsNumber);
        memberData.put(Config.FRIEND_NAME, friendsName);

        memberRef.push().setValue(memberData);
        memberRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

//                Config.cancelDialog();
                finish();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Config.cancelDialog();

            }
        });

    }


    private void checkIfMemberExists() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.FRIENDS_TABLE);
        firebaseQuery = mDatabase.orderByChild(Config.USER_WHO_ADDED_FRIEND_PHONE_NO).equalTo(phonenumber);

        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() > 0) {
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.FRIEND_PHONE_NO)) {
                            String tempStr = "+91" + edtNumber.getText().toString();
                            Log.e("kinjal", tempStr + " ===== " + postSnapshot.child(Config.FRIEND_PHONE_NO).getValue().toString());

                            if (tempStr.equalsIgnoreCase(postSnapshot.child(Config.FRIEND_PHONE_NO).getValue().toString()) || tempStr.equalsIgnoreCase(postSnapshot.child(Config.USER_WHO_ADDED_FRIEND_PHONE_NO).getValue().toString())) {
                                isMemberExists = true;
                                Log.e("kinjal", tempStr);
                                break;
                            } else {
                                Log.e("kinjal:", tempStr);
                                isMemberExists = false;
                            }
                        }
                    }

                        if (!isMemberExists) {
                            Log.e("kinjal===", edtNumber.getText().toString());
                            firebaseQuery.removeEventListener(this);
                            insertMember(phonenumber, edtNumber.getText().toString(), edtName.getText().toString());

                        } else {
                            Log.e("kinjal:::", edtNumber.getText().toString());
                            firebaseQuery.removeEventListener(this);
                            Toast.makeText(AddMembersActivity.this, getString(R.string.alreadyFriend), Toast.LENGTH_LONG).show();
                            textMessage.setText(getString(R.string.alreadyFriend));

                        }


                } else {
                    firebaseQuery.removeEventListener(this);
                    insertMember(phonenumber, edtNumber.getText().toString(), edtName.getText().toString());
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isValidInput() {
        if (edtName.getText().toString().trim().length() == 0) {

            edtName.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.toastEnterName), Toast.LENGTH_SHORT).show();
            textMessage.setText(getString(R.string.toastEnterName));
            return false;
        } else if (edtNumber.getText().toString().trim().length() == 0) {

            edtNumber.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.toastEnterNumber), Toast.LENGTH_SHORT).show();
            textMessage.setText(getString(R.string.toastEnterNumber));

            return false;
        } else if (edtNumber.getText().toString().trim().length() != 10) {

            edtNumber.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.toastEnterNumberLength), Toast.LENGTH_SHORT).show();
            textMessage.setText(getString(R.string.toastEnterNumberLength));

            return false;
        }
        return true;
    }
}
