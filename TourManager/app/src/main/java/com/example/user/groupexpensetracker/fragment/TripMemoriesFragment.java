package com.example.user.groupexpensetracker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.user.groupexpensetracker.adapter.CustomTripAdapter;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.bean.Group;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripMemoriesFragment extends Fragment {
    int REQUEST_CAMERA = 1, REQUEST_GALLERY = 2;
    FloatingActionButton floatingAddImage;
    ImageView imageTrip;
    RecyclerView recycler;
    ArrayList<String> arrayList;
    ArrayList<User> tripImage;
    String phonenumber;
    SharedPreferences sharedPreferences;
    ArrayList<String> groupIdslist;
    int hasNoOfGroup = 0;
    ArrayList<String> userTripImage;
    private DatabaseReference mDatabase;
    Query firebaseQuery;
    List<Group> groupdata=new ArrayList<>();
    private String filePath = "";
    StorageReference storageReference;
    Uri uri;
    FirebaseStorage storage;
    Spinner spinnerGroup;
    boolean isExits = false;
    private String group_id = "";
    private GridLayoutManager lLayout;
    ArrayAdapter<String> arrayAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        arrayList=new ArrayList<>();
//        arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sharedPreferences = getActivity().getSharedPreferences(GlobalData.mysharedpreference, Context.MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
        final View v = inflater.inflate(R.layout.fragment_trip_memories, container, false);
        imageTrip=(ImageView)v.findViewById(R.id.imageTrip);
        recycler=(RecyclerView)v.findViewById(R.id.recycler);
        Intent intent=getActivity().getIntent();

        groupIdslist=new ArrayList<>();
        userTripImage=new ArrayList<>();
        tripImage=new ArrayList<>();


        spinnerGroup=(Spinner)v.findViewById(R.id.spinnerGroup);
        getGroupsFromGroupMapping();

        //lLayout = new GridLayoutManager(getActivity(), 3);


        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                group_id=groupdata.get(i).getGroupid();
                Log.i("kinjal","groupid " + group_id);
                getTripImage();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        floatingAddImage = (FloatingActionButton) v.findViewById(R.id.floatingAddImage);

        Glide.with(getActivity()).load(intent.getStringExtra("imageurl")).placeholder(R.drawable.user);


        try {
            storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(getActivity());

            storageReference = storage.getReferenceFromUrl(Config.FILEUPLOAD_URL);
        } catch (Exception e) {

        }




        floatingAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Alert");
                builder.setItems(R.array.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, REQUEST_CAMERA);

                        } else if (which == 1) {
                            Intent i = new Intent();
                            i.setType("image/*");
                            i.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(i, "select file"), REQUEST_GALLERY);

                        }
                    }
                });
                builder.show();
            }
        });
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);




        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_GALLERY) {

                if (resultCode == Activity.RESULT_OK) {

                    uri = data.getData();
                    imageTrip.setImageURI(uri);
                    uploadImage();

                }
            }

        }

    }

    private void uploadImage() {
        //Config.showDialog(getActivity(), getString(R.string.loadingUser), getString(R.string.please_wait));
        StorageReference userImages = storageReference.child(Config.TRIP_IMAGE_FOLDER + "/" +group_id + ".jpg");

        // Get the data from an ImageView as bytes
        imageTrip.setDrawingCacheEnabled(true);
        imageTrip.buildDrawingCache();
        Bitmap bitmap = imageTrip.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImages.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                filePath = downloadUrl.toString();
                insertImageUrl(filePath);
            }
        });
    }

    private void insertImageUrl(String imagePath) {
        if (isExits) {


            Firebase ref = new Firebase(Config.FIREBASE_URL);
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously
            Map<String, Object> post1 = new HashMap<String, Object>();

            post1.put(Config.TRIP_IMAGE, imagePath);
            post1.put(Config.TRIP_ID, group_id);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + Config.TRIP_MEMORIES_TABLE + "/" + group_id, post1);


//            editor.putString(GlobalData.userImage, imagePath);
//
//            editor.commit();

            ref.updateChildren(childUpdates);

        } else {


            Firebase ref = new Firebase(Config.FIREBASE_URL);
            final Firebase postRef = ref.child(Config.TRIP_MEMORIES_TABLE);

            Map<String, String> post1 = new HashMap<String, String>();
            post1.put(Config.TRIP_ID, group_id);
            post1.put(Config.TRIP_IMAGE, imagePath);


            postRef.push().setValue(post1);


        }
    }
    private void getGroupsFromGroupMapping() {
        //Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
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
                        }

                    }

                    getgroups();

                } else {
//                    Config.cancelDialog();


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
        arrayList.clear();
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
                                arrayList.add(postSnapshot.child(Config.GROUP_NAME).getValue().toString());
                                groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(),postSnapshot.child(Config.GROUP_NAME).getValue().toString(),temp + " participants"));
                                Log.e("sweny", String.valueOf(arrayList));
                                if (hasNoOfGroup == arrayList.size()) {


                                    arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spinnerGroup.setAdapter(arrayAdapter);
                                    spinnerGroup.setSelection(0);

                                    group_id=groupdata.get(0).getGroupid();

                                    // Config.cancelDialog();

                                }
                            }

                        }
                    } else {
                        Config.cancelDialog();

                    }

                    firebaseQuery.removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Config.cancelDialog();
                }
            });
        }
    }

        private void getTripImage() {


            //Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.TRIP_MEMORIES_TABLE);
            final Query firebase_Query = mDatabase.orderByChild(Config.TRIP_ID).equalTo(group_id);

            firebase_Query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {


                    if (snapshot.getChildrenCount() > 0) {
                        userTripImage.clear();
                        tripImage.clear();
                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.TRIP_IMAGE)) {

                                userTripImage.add(postSnapshot.child(Config.TRIP_IMAGE).getValue().toString());
                                tripImage.add(new User("","",postSnapshot.child(Config.TRIP_IMAGE).getValue().toString()));
                            }
                        }
                        if (userTripImage.size() > 0) {
                            getTripDetails();
                        } else {
                            //Config.cancelDialog();

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

        private void getTripDetails() {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.TRIP_MEMORIES_TABLE);
            firebaseQuery = mDatabase.orderByChild(Config.TRIP_IMAGE);
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {

                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.TRIP_IMAGE)) {
                                for (int i = 0; i < userTripImage.size(); i++) {

                                    if (userTripImage.get(i).equalsIgnoreCase(postSnapshot.child(Config.TRIP_IMAGE).getValue().toString())) {
                                        tripImage.get(i).setImage(postSnapshot.child(Config.TRIP_IMAGE).getValue().toString());
                                        tripImage.get(i).setSelected(false);

                                    }
                                }
                            }
                        }

//                        recycler.setHasFixedSize(true);
//                        recycler.setLayoutManager(lLayout);
//
//                        CustomTripAdapter ctAdapter = new CustomTripAdapter(getActivity(), tripImage);
//                        recycler.setAdapter(ctAdapter);
//                        Config.cancelDialog();
                        recycler.setHasFixedSize(true);
                        CustomTripAdapter ctAdapter = new CustomTripAdapter(getActivity(),tripImage);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
                        recycler.setLayoutManager(gridLayoutManager);
                        recycler.setAdapter(ctAdapter);


                    }
                    else {
                            Config.cancelDialog();
                    }
                    }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Config.cancelDialog();

                        }
                    });





                    }}