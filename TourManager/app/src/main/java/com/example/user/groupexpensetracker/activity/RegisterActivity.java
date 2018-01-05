package com.example.user.groupexpensetracker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    int REQUEST_CAMERA = 1, REQUEST_GALLERY = 2;
    ImageView imageUser;
    EditText edtName;
    Button btnNext;
    FloatingActionButton floatingEdit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String filePath = "";
    StorageReference storageReference;
    Uri uri;
    TextView textViewPhoneNumber;
    String phonenumber;
    Firebase ref;
    boolean isExits = false;
    private String key = "";
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);

        textViewPhoneNumber = (TextView) findViewById(R.id.textViewPhoneNumber);
        imageUser = (ImageView) findViewById(R.id.imageUser);

        btnNext = (Button) findViewById(R.id.btnNext);

        edtName = (EditText) findViewById(R.id.edtName);

        Firebase.setAndroidContext(this);

        Intent intent = getIntent();
        sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, phonenumber);
        textViewPhoneNumber.setText(phonenumber);
        edtName.setText(sharedPreferences.getString(GlobalData.userName, ""));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("imageurl")).placeholder(R.drawable.user).into(imageUser);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidData()) {

                    uploadImage();

                }


            }
        });

        try {
            storage = FirebaseStorage.getInstance();
            Firebase.setAndroidContext(this);

            storageReference = storage.getReferenceFromUrl(Config.FILEUPLOAD_URL);
        } catch (Exception e) {

        }

        if (intent.getBooleanExtra("isExits", false) == false) {
            isExits = false;
            key = intent.getStringExtra("key");

        } else if (intent.getBooleanExtra("isExits", false) == true) {

            isExits = true;
            key = intent.getStringExtra("key");

        }

        floatingEdit = (FloatingActionButton) findViewById(R.id.floatingEdit);
        floatingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

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


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_GALLERY) {

                if (resultCode == Activity.RESULT_OK) {

                    uri = data.getData();
                    imageUser.setImageURI(uri);

                }
            }

        }

    }


    private void uploadImage() {
        Config.showDialog(this, getString(R.string.loadingUser), getString(R.string.please_wait));
        StorageReference userImages = storageReference.child(Config.USER_IMAGE_FOLDER + "/" + phonenumber + ".jpg");

        // Get the data from an ImageView as bytes
        imageUser.setDrawingCacheEnabled(true);
        imageUser.buildDrawingCache();
        Bitmap bitmap = imageUser.getDrawingCache();
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

            post1.put(Config.USER_IMAGE, imagePath);
            post1.put(Config.USER_NAME, edtName.getText().toString());
            post1.put(Config.USER_PHONE_NO, phonenumber);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + Config.USER_TABLE + "/" + key, post1);


            editor.putBoolean(GlobalData.isHavingName, true);
            editor.putString(GlobalData.userName, edtName.getText().toString());
            editor.putString(GlobalData.userImage, imagePath);

            editor.commit();

            ref.updateChildren(childUpdates);


            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    Config.cancelDialog();
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } else {


            Firebase ref = new Firebase(Config.FIREBASE_URL);
            final Firebase postRef = ref.child(Config.USER_TABLE);

            Map<String, String> post1 = new HashMap<String, String>();

            post1.put(Config.USER_IMAGE, imagePath);
            post1.put(Config.USER_NAME, edtName.getText().toString());
            post1.put(Config.USER_PHONE_NO, phonenumber);

            postRef.push().setValue(post1);

            editor.putBoolean(GlobalData.isHavingName, true);
            editor.putString(GlobalData.userName, edtName.getText().toString());
            editor.putString(GlobalData.userImage, imagePath);

            editor.commit();

            postRef.addValueEventListener(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot dataSnapshot) {


                                                  if (dataSnapshot.getChildrenCount() > 0) {

                                                      for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                          String tempStr = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
                                                          Log.e("sweny", tempStr + "  ======  " + postSnapshot.child(Config.USER_PHONE_NO).getValue().toString());

                                                          if (tempStr.equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {

                                                              editor.putString(GlobalData.userKey, postSnapshot.getKey());
                                                              editor.commit();

                                                              Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                              Config.cancelDialog();

                                                              startActivity(intent);
                                                              finish();
                                                          }

                                                      }
                                                  }


                                              }

                                              @Override
                                              public void onCancelled(FirebaseError firebaseError) {

                                              }
                                          }
            );


        }


    }

    private boolean isValidData() {

        if (edtName.getText().toString().trim().length() == 0) {
            edtName.requestFocus();
            edtName.setError(getString(R.string.toastEnterName));
            return false;
        }
        return true;


    }
}


//package com.example.user.groupexpensetracker.activity;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.location.Location;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.example.user.groupexpensetracker.R;
//import com.example.user.groupexpensetracker.Util.Config;
//import com.example.user.groupexpensetracker.Util.GlobalData;
//import com.example.user.groupexpensetracker.fragment.TrackingFragment;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageMetadata;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.ByteArrayOutputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
//
//    int REQUEST_CAMERA = 1, REQUEST_GALLERY = 2;
//    ImageView imageUser;
//    EditText edtName;
//    Button btnNext;
//    FloatingActionButton floatingEdit;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    public static String filePath = "";
//    StorageReference storageReference;
//    Uri uri;
//    TextView textViewPhoneNumber;
//    String phonenumber;
//    Firebase ref;
//    boolean isExits = false;
//    public static String key = "";
//    FirebaseStorage storage;
//    private static final String TAG = "RegisterActivity";
//    private boolean mRequestingLocationUpdates = false;
//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
//    private Location mLastLocation;
//    double lat;
//    double lon;
//    private static int UPDATE_INTERVAL = 10000; // 10 sec
//    private static int FATEST_INTERVAL = 5000; // 5 sec
//    private static int DISPLACEMENT = 10; // 10 meters
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    Double latUpdate;
//    Double lonUpdate;
//    String phonumber="";
//
//
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        Firebase.setAndroidContext(this);
//
//
//        textViewPhoneNumber = (TextView) findViewById(R.id.textViewPhoneNumber);
//        imageUser = (ImageView) findViewById(R.id.imageUser);
//
//        btnNext = (Button) findViewById(R.id.btnNext);
//
//        edtName = (EditText) findViewById(R.id.edtName);
//
//        Firebase.setAndroidContext(this);
//
//        Intent intent = getIntent();
//        sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//
//        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, phonenumber);
//        textViewPhoneNumber.setText(phonenumber);
//        edtName.setText(sharedPreferences.getString(GlobalData.userName, ""));
//        Glide.with(getApplicationContext()).load(intent.getStringExtra("imageurl")).placeholder(R.drawable.user).into(imageUser);
//
//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//            createLocationRequest();
//        }
//
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (isValidData()) {
//
//                    displayLocation();
//
//                    uploadImage();
//
//                }
//
//
//            }
//        });
//
//        try {
//            storage = FirebaseStorage.getInstance();
//            Firebase.setAndroidContext(this);
//
//            storageReference = storage.getReferenceFromUrl(Config.FILEUPLOAD_URL);
//        } catch (Exception e) {
//
//        }
//
//        if (intent.getBooleanExtra("isExits", false) == false) {
//            isExits = false;
//            key = intent.getStringExtra("key");
//
//            Log.i("kinjal","key of register acivity " +key);
//
//        } else if (intent.getBooleanExtra("isExits", false) == true) {
//
//            isExits = true;
//            key = intent.getStringExtra("key");
//            Log.i("kinjal","key of register acivity==== " +key);
//
//
//        }
//
//        floatingEdit = (FloatingActionButton) findViewById(R.id.floatingEdit);
//        floatingEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//
//                builder.setTitle("Alert");
//                builder.setItems(R.array.options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//
//                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(i, REQUEST_CAMERA);
//
//                        } else if (which == 1) {
//                            Intent i = new Intent();
//                            i.setType("image/*");
//                            i.setAction(Intent.ACTION_GET_CONTENT);
//                            startActivityForResult(Intent.createChooser(i, "select file"), REQUEST_GALLERY);
//
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        checkPlayServices();
//
//        // Resuming the periodic location updates
//        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
////        stopLocationUpdates();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (resultCode == Activity.RESULT_OK) {
//
//            if (requestCode == REQUEST_GALLERY) {
//
//                if (resultCode == Activity.RESULT_OK) {
//
//                    uri = data.getData();
//                    imageUser.setImageURI(uri);
//
//                }
//            }
//
//        }
//
//    }
//
//    private void uploadImage() {
//        Config.showDialog(this, getString(R.string.loadingUser), getString(R.string.please_wait));
//        StorageReference userImages = storageReference.child(Config.USER_IMAGE_FOLDER + "/" + phonenumber + ".jpg");
//
//        // Get the data from an ImageView as bytes
//        imageUser.setDrawingCacheEnabled(true);
//        imageUser.buildDrawingCache();
//        Bitmap bitmap = imageUser.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = userImages.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                filePath = downloadUrl.toString();
//                insertImageUrl(filePath);
//            }
//        });
//    }
//
//    public void insertImageUrl(String imagePath) {
//        if (isExits) {
//
//
//            Firebase ref = new Firebase(Config.FIREBASE_URL);
//
//            // Create new post at /user-posts/$userid/$postid and at
//            // /posts/$postid simultaneously
//            Map<String, Object> post1 = new HashMap<String, Object>();
//
//            post1.put(Config.USER_IMAGE, imagePath);
//            post1.put(Config.USER_NAME, edtName.getText().toString());
//            post1.put(Config.USER_PHONE_NO, phonenumber);
////            post1.put(Config.USER_LAT,latUpdate);
////            post1.put(Config.USER_LONG,lonUpdate);
//
//            Map<String, Object> childUpdates = new HashMap<>();
//            childUpdates.put("/" + Config.USER_TABLE + "/" + key, post1);
//
//
//            editor.putBoolean(GlobalData.isHavingName, true);
//            editor.putString(GlobalData.userName, edtName.getText().toString());
//            editor.putString(GlobalData.userImage, imagePath);
//
//            editor.commit();
//
//            ref.updateChildren(childUpdates);
//
//
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    Config.cancelDialog();
//                    startActivity(intent);
//                    finish();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });
//
//        } else {
//
//
//            Firebase ref = new Firebase(Config.FIREBASE_URL);
//            final Firebase postRef = ref.child(Config.USER_TABLE);
//
//            Map<String, String> post1 = new HashMap<String, String>();
//
//            post1.put(Config.USER_IMAGE, imagePath);
//            post1.put(Config.USER_NAME, edtName.getText().toString());
//            post1.put(Config.USER_PHONE_NO, phonenumber);
//            post1.put(Config.USER_LAT,String.valueOf(lat));
//            post1.put(Config.USER_LONG,String.valueOf(lon));
//
//            postRef.push().setValue(post1);
//
//            editor.putBoolean(GlobalData.isHavingName, true);
//            editor.putString(GlobalData.userName, edtName.getText().toString());
//            editor.putString(GlobalData.userImage, imagePath);
//
//            editor.commit();
//
//            postRef.addValueEventListener(new ValueEventListener() {
//                                              @Override
//                                              public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                                                  if (dataSnapshot.getChildrenCount() > 0) {
//
//                                                      for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                                                          String tempStr = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
//                                                          Log.e("niral", tempStr + "  ======  " + postSnapshot.child(Config.USER_PHONE_NO).getValue().toString());
//
//                                                          if (tempStr.equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
//
//                                                              editor.putString(GlobalData.userKey, postSnapshot.getKey());
//                                                              editor.commit();
//
//                                                              Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                                              Config.cancelDialog();
//
//                                                              startActivity(intent);
//                                                              finish();
//                                                          }
//
//                                                      }
//                                                  }
//
//
//                                              }
//
//                                              @Override
//                                              public void onCancelled(FirebaseError firebaseError) {
//
//                                              }
//                                          }
//            );
//
//
//        }
//
//
//    }
//
//    private boolean isValidData() {
//
//        if (edtName.getText().toString().trim().length() == 0) {
//            edtName.requestFocus();
//            edtName.setError(getString(R.string.toastEnterName));
//            return false;
//        }
//        return true;
//
//
//    }
//
//
//    protected synchronized void buildGoogleApiClient() {
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    protected void createLocationRequest() {
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//
//
//    private boolean checkPlayServices() {
//
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(getApplicationContext());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, RegisterActivity.this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//
//                Toast.makeText(getApplicationContext(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//
//
//
//    protected void startLocationUpdates() {
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//    }
//
//    protected void stopLocationUpdates() {
//
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + connectionResult.getErrorCode());
//    }
//
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//
//    }
//
//
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//
////        latUpdate=location.getLatitude();
////        lonUpdate=location.getLongitude();
////
////        Log.i("kinjal","updated lat " +latUpdate);
////        Log.i("kinjal","updated lon " +lonUpdate);
//
////        if(mGoogleApiClient != null) {
////            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
////        }
//
//            Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();
//
//
//    }
//
//    private void displayLocation() {
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//
//
//            lat = mLastLocation.getLatitude();
//            lon = mLastLocation.getLongitude();
//
//            Log.i("kinjal","lat " +mLastLocation.getLatitude());
//            Log.i("kinjal","lon " +mLastLocation.getLongitude());
//
//
//            }
//
//        }
//
//
//
//
//
//}
//
