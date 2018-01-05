//package com.example.user.groupexpensetracker.fragment;
//
//import android.Manifest;
//import android.app.Activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.example.user.groupexpensetracker.R;
//import com.example.user.groupexpensetracker.Util.Config;
//import com.example.user.groupexpensetracker.Util.GlobalData;
//import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
//import com.example.user.groupexpensetracker.bean.Group;
//import com.example.user.groupexpensetracker.bean.ServiceTask;
//import com.example.user.groupexpensetracker.bean.User;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//public class TrackingFragment extends Fragment implements ConnectionCallbacks,
//        OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
//
//    MapView mMapView;
//    private GoogleMap googleMap;
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    private static final String TAG = "TrackingFragment";
//    // RequestTask requestTask;
//    ArrayList<Group> groupdata=new ArrayList<>();
//    ArrayList<String> arrayList;
//    String phonenumber;
//    ArrayList<String> groupIdslist;
//    int hasNoOfGroup = 0;
//    SharedPreferences sharedPreferences;
//    private DatabaseReference mDatabase;
//    Query firebaseQuery;
//    ServiceTask serviceTask;
//    String location;
//    int i;
//    Spinner spinner;
//    private String group_id = "";
//    ArrayList<String> userMembersMobile;
//    List<User> groupData;
//    int hasNumberOfGroup = 0;
//
//
//
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//
//    private Location mLastLocation;
//
//    // Google client to interact with Google API
//    private GoogleApiClient mGoogleApiClient;
//
//    // boolean flag to toggle periodic location updates
//    private boolean mRequestingLocationUpdates = false;
//
//    private LocationRequest mLocationRequest;
//
//    // Location updates intervals in sec
//    private static int UPDATE_INTERVAL = 10000; // 10 sec
//    private static int FATEST_INTERVAL = 5000; // 5 sec
//    private static int DISPLACEMENT = 10; // 10 meters
//
//    // GPSTracker class
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View rootView = inflater.inflate(R.layout.fragment_tracking, container, false);
//
//
//        userMembersMobile = new ArrayList<>();
//        groupData = new ArrayList<>();
//
//
//
//        spinner = (Spinner) rootView.findViewById(R.id.spinner);
//
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                group_id = groupdata.get(i).getGroupid();
//                Log.i("niral", "groupid " + group_id);
//
//                getUserMembers();
//                // }
//
////
//
//                //}
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//
//        serviceTask = new ServiceTask();
//        groupIdslist = new ArrayList<>();
//        arrayList = new ArrayList<>();
//        sharedPreferences = getActivity().getSharedPreferences(GlobalData.mysharedpreference, Context.MODE_PRIVATE);
//        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
//
//        Intent intent = new Intent(getActivity(), ServiceTask.class);
//        intent.putExtra("location ", location);
//        getActivity().startService(intent);
//
//
//        mMapView = (MapView) rootView.findViewById(R.id.mapView);
//        mMapView.onCreate(savedInstanceState);
//
//        // First we need to check availability of play services
//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//            createLocationRequest();
//        }
//
//        getGroupsFromGroupMapping();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            checkLocationPermission();
//        } else {
//
//            checkLocationPermission();
//        }
//
//        mMapView.onResume(); // needed to get the map to display immediately
//
//        try {
//
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//
//                                 @RequiresApi(api = Build.VERSION_CODES.M)
//                                 @Override
//                                 public void onMapReady(final GoogleMap mMap) {
//
//                                     googleMap = mMap;
//                                     // For showing a move to my location button
//                                     //if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                     if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                         ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
//                                                 android.Manifest.permission.ACCESS_FINE_LOCATION
//                                         }, 10);
//
//
//                                         // TODO: Consider calling
//                                         //    ActivityCompat#requestPermissions
//                                         // here to request the missing permissions, and then overriding
//                                         //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                         //                                          int[] grantResults)
//                                         // to handle the case where the user grants the permission. See the documentation
//                                         // for ActivityCompat#requestPermissions for more details.
//                                         return;
//                                     }
//                                     googleMap.setMyLocationEnabled(false);
//
//
//                                 }
//                             }
//
//        );
//
//
//        return rootView;
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
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//        stopLocationUpdates();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }
//
//    public boolean checkLocationPermission() {
//
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private void getGroupsFromGroupMapping() {
//
//        //Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
//        firebaseQuery = mDatabase.orderByChild(Config.GROUP_MEMBER_PHONE_NO).equalTo(phonenumber);
//        firebaseQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                if (snapshot.getChildrenCount() > 0) {
//
//                    groupIdslist.clear();
//
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        if (postSnapshot.hasChild(Config.GROUP_ID)) {
//                            String temp = postSnapshot.child(Config.GROUP_ID).getValue().toString();
//
//                            if (!groupIdslist.contains(temp)) {
//                                groupIdslist.add(temp);
//                                hasNoOfGroup++;
//
//                            }
//
//                        } else {
//                            Config.cancelDialog();
//                        }
//
//                    }
//
//                    getgroups();
//
//                } else {
////                    Config.cancelDialog();
//
//
//                }
//                firebaseQuery.removeEventListener(this);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Config.cancelDialog();
//
//            }
//        });
//
//    }
//
//    private void getgroups() {
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_TABLE);
//        arrayList.clear();
//
//        for (int i = 0; i < groupIdslist.size(); i++) {
//            groupdata.clear();
//            firebaseQuery = mDatabase.orderByChild(Config.GROUP_ID).equalTo(groupIdslist.get(i));
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//
//                    if (snapshot.getChildrenCount() > 0) {
//
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.GROUP_NAME)) {
//                                String temp = postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString();
//
//                                arrayList.add(postSnapshot.child(Config.GROUP_NAME).getValue().toString());
//                                groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp + " participants"));
//                                Log.e("kinjal", String.valueOf(arrayList));
//                                if (hasNoOfGroup == arrayList.size()) {
//
//
//                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
//                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                                    spinner.setAdapter(arrayAdapter);
//                                    spinner.setSelection(0);
//                                    group_id = groupdata.get(0).getGroupid();
//                                    //getTripImages();
//                                    // Config.cancelDialog();
//
//                                }
//                            }
//
//                        }
//                    } else {
////                        Config.cancelDialog();
//
//                    }
//
//                    firebaseQuery.removeEventListener(this);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                    Config.cancelDialog();
//                }
//            });
//
//
//        }
//    }
//
//    private void getUserMembers() {
//
//        //Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
//        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(group_id);
//
//        firebase_Query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                Log.i("niral", "FRIENDS " + group_id);
//                Log.i("niral", "FRIENDS  " + snapshot.toString());
//
//                if (snapshot.getChildrenCount() > 0) {
//                    userMembersMobile.clear();
//                    groupData.clear();
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {
//
//                            hasNumberOfGroup++;
//                            Log.i("niral", "no of groups " + hasNumberOfGroup);
//                            userMembersMobile.add(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString());
//                            //groupData.add(new User(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString()));
//                        }
//                    }
//                    if (userMembersMobile.size() > 0) {
//                        Log.i("niral", "FRIENDS " + group_id);
//                        getMemberDetails();
//                    } else {
//
//                        Config.cancelDialog();
////                        llNoFriendFound.setVisibility(View.VISIBLE);
////                        recyclerParticipants.setVisibility(View.GONE);
//                    }
//                } else {
////                    Config.cancelDialog();
////                    llNoFriendFound.setVisibility(View.VISIBLE);
////                    recyclerParticipants.setVisibility(View.GONE);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void getMemberDetails() {
//
//        for (int i = 0; i < userMembersMobile.size(); i++) {
//
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
//            firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO).equalTo(userMembersMobile.get(i).toString());
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount() > 0) {
//
//                        Log.i("niral", "FRIENDS===  " + snapshot.toString());
//
////                        groupData.clear();
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
//                                groupData.add(new User(postSnapshot.child(Config.USER_NAME).getValue().toString(), postSnapshot.child(Config.USER_PHONE_NO).getValue().toString(), postSnapshot.child(Config.USER_IMAGE).getValue().toString()));
//                                Log.i("niral", "FRIENDS::::: " + groupData.size());
//                            }
//                        }
//                        if (hasNumberOfGroup == groupData.size()) {
//
//                            displayLocation();
//                            Log.i("niral", "I am in if=== " + groupData.size());
//
////                            userAdapter = new ChooseGroupMemberAdapter(getActivity(), groupData);
////                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
////                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
////                            recyclerList.setLayoutManager(linearLayoutManager);
////                            recyclerList.setAdapter(userAdapter);
////                            Config.cancelDialog();
//
//
//                        }
//
//
//                    } else {
////                        Config.cancelDialog();
////                        llNoFriendFound.setVisibility(View.VISIBLE);
////                        recyclerParticipants.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Config.cancelDialog();
//                }
//            });
//
//        }
//    }
//
////
////
////    public class RequestTask extends AsyncTask<String, String, String> {
////
////        @Override
////        protected String doInBackground(String... uri) {
////            HttpClient httpclient = new DefaultHttpClient();
////            HttpResponse response;
////            String responseString = null;
////            try {
////                response = httpclient.execute(new HttpGet("http://ip-api.com/json"));
////                org.apache.http.StatusLine statusLine = response.getStatusLine();
////                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
////                    ByteArrayOutputStream out = new ByteArrayOutputStream();
////                    response.getEntity().writeTo(out);
////                    responseString = out.toString();
////                    Log.i("sweny",responseString);
////                    out.close();
////                } else{
////                    //Closes the connection.
////                    response.getEntity().getContent().close();
////                    throw new IOException(statusLine.getReasonPhrase());
////                }
////            } catch (ClientProtocolException e) {
////                //TODO Handle problems..
////            } catch (IOException e) {
////                //TODO Handle problems..
////            }
////            return responseString;
////        }
////
////        @Override
////        protected void onPostExecute(String result) {
////            super.onPostExecute(result);
////
////            try {
////                JSONObject jsonObject = new JSONObject(result);
////                 int lat, lang;
////
////                lat = jsonObject.getInt("lat");
////                lang=jsonObject.getInt("lang");
////
////                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lang)).title(jsonObject.getString("regionName")).snippet(jsonObject.getString("city") + jsonObject.getString("country")));
////
////
////                // For zooming automatically to the location of the marker
////                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lang)).zoom(12).build();
////                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////
////            }
////            catch (JSONException e) {
////                e.printStackTrace();
////
////
////
////
////            }
////            //Do anything with response..
////        }
////    }
//
//
//    /**
//     * Creating google api client object
//     */
//    protected synchronized void buildGoogleApiClient() {
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Creating location request object
//     */
//    protected void createLocationRequest() {
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//    /**
//     * Method to verify google play services on the device
//     */
//    private boolean checkPlayServices() {
//
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(getActivity());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//
//                Toast.makeText(getActivity(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Starting the location updates
//     */
//    protected void startLocationUpdates() {
//
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(),
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
//    /**
//     * Stopping location updates
//     */
//    protected void stopLocationUpdates() {
//
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//
//        displayLocation();
//        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        // Assign the new location
//        mLastLocation = location;
//        Toast.makeText(getActivity(), "Location changed!", Toast.LENGTH_SHORT).show();
//        displayLocation();
//    }
//
//    /**
//     * Method to display the location on UI
//     */
//    private void displayLocation() {
//
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//
//            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//
//            Log.i("kinjal","LOCATION " +latLng);
//
//            for (i=0;i<userMembersMobile.size();i++) {
//                googleMap.addMarker(new MarkerOptions().position(latLng).title("My Current Location").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        }
//    }
//}


































//package com.example.user.groupexpensetracker.fragment;
//
//import android.Manifest;
//import android.app.Activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.example.user.groupexpensetracker.R;
//import com.example.user.groupexpensetracker.Util.Config;
//import com.example.user.groupexpensetracker.Util.GlobalData;
//import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
//import com.example.user.groupexpensetracker.adapter.UserAdapter;
//import com.example.user.groupexpensetracker.bean.Group;
////import com.example.user.groupexpensetracker.bean.LatiLongi;
//import com.example.user.groupexpensetracker.bean.LatiLongi;
//import com.example.user.groupexpensetracker.bean.ServiceTask;
//import com.example.user.groupexpensetracker.bean.User;
//import com.firebase.client.Firebase;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//public class TrackingFragment extends Fragment implements ConnectionCallbacks,
//        OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
//
//    ArrayList<User> user = new ArrayList<>();
//    MapView mMapView;
//    String key="";
//    private GoogleMap googleMap;
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    private static final String TAG = "TrackingFragment";
//    // RequestTask requestTask;
//    ArrayList<String> userMembersMobile = new ArrayList<>();
//    ArrayList<String> userTrackMobile = new ArrayList<>();
//
//    // int hasNoOfGroup = 0;
//    int hasNumberOfGroup = 0;
//    int hasNumOfGroup = 0;
//    int hasNumbOfGroup = 0;
//
//    //  private String group_id = "";
////    ArrayList<String> userMembersMobile;
//    // List<User> membersData;
//    // private ChooseGroupMemberAdapter userAdapter;
//    List<User> groupData = new ArrayList<>();
//    ArrayList<LatiLongi> latilongis = new ArrayList<>();
//    // ArrayList<LatiLongi> longitude = new ArrayList<>();
//
//    ArrayList<Group> groupdata;
//    ArrayList<String> arrayList;
//    // ArrayList<LatiLongi>latitude=new ArrayList<>();
//    //ArrayList<LatiLongi>longitude=new ArrayList<>();
//    String phonenumber;
//    ArrayList<String> groupIdslist;
//    int hasNoOfGroup = 0;
//    SharedPreferences sharedPreferences;
//    private DatabaseReference mDatabase;
//    Query firebaseQuery;
//    ServiceTask serviceTask;
//    ArrayList<String>groupIDslist=new ArrayList<>();
//    String location;
//    String value;
//    Spinner spinner;
//    private String group_id = "";
//    List<Marker> markers = new ArrayList<>();
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    double lat;
//    double lon;
//    String users;
//    String number;
//    String usersname;
//    String usersnumber;
//    Double lat1;
//    Double lon1;
//
//    private Location mLastLocation;
//
//    // Google client to interact with Google API
//    private GoogleApiClient mGoogleApiClient;
//
//    // boolean flag to toggle periodic location updates
//    private boolean mRequestingLocationUpdates = false;
//
//    private LocationRequest mLocationRequest;
//
//    // Location updates intervals in sec
//    private static int UPDATE_INTERVAL = 10000; // 10 sec
//    private static int FATEST_INTERVAL = 5000; // 5 sec
//    private static int DISPLACEMENT = 10; // 10 meters
//
//    // GPSTracker class
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View rootView = inflater.inflate(R.layout.fragment_tracking, container, false);
//        groupdata = new ArrayList<>();
//        // getActivity().startService(new Intent(getContext(), ServiceTask.class));
//        Firebase.setAndroidContext(getActivity());
//
////        lat = mLastLocation.getLatitude();
////        lon = mLastLocation.getLongitude();
//
//        spinner = (Spinner) rootView.findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                group_id = groupdata.get(i).getGroupid();
//                Log.i("niral", "groupid " + group_id);
//
//                getUserMembers();
//                // }
//
////
//
//                //}
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        //serviceTask = new ServiceTask();
//        //getActivity().startService(new Intent(getContext(), ServiceTask.class));
//        groupIdslist = new ArrayList<>();
//        arrayList = new ArrayList<>();
//        sharedPreferences = getActivity().getSharedPreferences(GlobalData.mysharedpreference, Context.MODE_PRIVATE);
//        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
//        //phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
////        Double lat=mLastLocation.getLatitude();
////        Double lon=mLastLocation.getLongitude();
//        mMapView = (MapView) rootView.findViewById(R.id.mapView);
//        mMapView.onCreate(savedInstanceState);
//
//
//
//        // First we need to check availability of play services
//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//            createLocationRequest();
//        }
//        // UserLatLon(Double.parseDouble(String.valueOf(mLastLocation.getLatitude())),Double.parseDouble(String.valueOf(mLastLocation.getLongitude())));
//
//
////        UserLatLon(String.valueOf(lat),String.valueOf(lon));
//        //      Log.i("niral","latlon"+ String.valueOf(lat));
//        //    Log.i("niral","latlon"+ String.valueOf(lon));
//
//
//        getGroupsFromGroupMapping();
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            checkLocationPermission();
//        } else {
//
//            checkLocationPermission();
//        }
//
//        mMapView.onResume(); // needed to get the map to display immediately
//
//        try {
//
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
////        for(int i=0;i<latitude.size();i++)
////        {
////            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
////
////            value   = String.valueOf(latitude.add(new LatiLongi("",mLastLocation.getLongitude(),mLastLocation.getLatitude())));
////            Log.i("tanuja",String.valueOf(mLastLocation.getLatitude()));
////            Log.i("tanuja",String.valueOf(mLastLocation.getLongitude()));
////            googleMap.addMarker(new MarkerOptions().position().title("My Current Location").snippet("Marker Description"));
////
////
////            // For zooming automatically to the location of the marker
////            CameraPosition cameraPosition = new CameraPosition.Builder().target(value).zoom(12).build();
////            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////
////
////
////        }
//
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//
//                                 @RequiresApi(api = Build.VERSION_CODES.M)
//                                 @Override
//                                 public void onMapReady(final GoogleMap mMap) {
//
//                                     googleMap = mMap;
//                                     // For showing a move to my location button
//                                     //if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                     if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                         ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
//                                                 android.Manifest.permission.ACCESS_FINE_LOCATION
//                                         }, 10);
//                                         return;
//                                     }
//                                     googleMap.setMyLocationEnabled(false);
//
//
//                                 }
//                             }
//
//        );
//
//
//        return rootView;
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
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//        stopLocationUpdates();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }
//
//    public boolean checkLocationPermission() {
//
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//
//    private void getGroupsFromGroupMapping() {
//
//        //Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
//        firebaseQuery = mDatabase.orderByChild(Config.GROUP_MEMBER_PHONE_NO).equalTo(phonenumber);
//        firebaseQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                if (snapshot.getChildrenCount() > 0) {
//
//                    groupIdslist.clear();
//
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        if (postSnapshot.hasChild(Config.GROUP_ID)) {
//                            String temp = postSnapshot.child(Config.GROUP_ID).getValue().toString();
//
//                            if (!groupIdslist.contains(temp)) {
//                                groupIdslist.add(temp);
//                                hasNoOfGroup++;
//
//                            }
//
//                        } else {
//                            Config.cancelDialog();
//                        }
//
//                    }
//
//                    getgroups();
//
//                } else {
////                    Config.cancelDialog();
//
//
//                }
//                firebaseQuery.removeEventListener(this);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Config.cancelDialog();
//
//            }
//        });
//
//    }
//
//    private void getgroups() {
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_TABLE);
//        arrayList.clear();
//
//        for (int i = 0; i < groupIdslist.size(); i++) {
//            groupdata.clear();
//            firebaseQuery = mDatabase.orderByChild(Config.GROUP_ID).equalTo(groupIdslist.get(i));
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//
//                    if (snapshot.getChildrenCount() > 0) {
//
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.GROUP_NAME)) {
//                                String temp = postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString();
//
//                                arrayList.add(postSnapshot.child(Config.GROUP_NAME).getValue().toString());
//                                groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp));
//                                Log.e("sweny", String.valueOf(arrayList));
//                                if (hasNoOfGroup == arrayList.size()) {
//
//
//                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
//                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                                    spinner.setAdapter(arrayAdapter);
//                                    spinner.setSelection(0);
//                                    group_id = groupdata.get(0).getGroupid();
//                                    Log.i("niral", "tanuja" + group_id);
//                                    //getTripImages();
//                                    // Config.cancelDialog();
//
//                                }
//                            }
//
//                        }
//                    } else {
////                        Config.cancelDialog();
//
//                    }
//
//                    firebaseQuery.removeEventListener(this);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                    Config.cancelDialog();
//                }
//            });
//
//
//        }
//    }
//
//    private void getUserMembers() {
//
//        //Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
//        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(group_id);
//
//        firebase_Query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                Log.i("niral", "FRIENDS " + group_id);
//                Log.i("niral", "FRIENDS  " + snapshot.toString());
//
//                if (snapshot.getChildrenCount() > 0) {
//                    userMembersMobile.clear();
//                    groupData.clear();
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {
//
//                            hasNumberOfGroup++;
//                            Log.i("niral", "no of groups " + hasNumberOfGroup);
//                            userMembersMobile.add(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString());
//                            //groupData.add(new User(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString()));
//                        }
//                    }
//                    if (userMembersMobile.size() > 0) {
//                        Log.i("niral", "FRIENDS " + group_id);
//                        getMemberDetails();
//                    } else {
//
//                        Config.cancelDialog();
////                        llNoFriendFound.setVisibility(View.VISIBLE);
////                        recyclerParticipants.setVisibility(View.GONE);
//                    }
//                } else {
////                    Config.cancelDialog();
////                    llNoFriendFound.setVisibility(View.VISIBLE);
////                    recyclerParticipants.setVisibility(View.GONE);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void getMemberDetails() {
//
//        for (int i = 0; i < userMembersMobile.size(); i++) {
//
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
//            firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO).equalTo(userMembersMobile.get(i).toString());
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount() > 0) {
//
//                        Log.i("niral", "FRIENDS===  " + snapshot.toString());
//
////                        groupData.clear();
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
//                                groupData.add(new User(postSnapshot.child(Config.USER_NAME).getValue().toString(), postSnapshot.child(Config.USER_PHONE_NO).getValue().toString(), postSnapshot.child(Config.USER_IMAGE).getValue().toString()));
//                                Log.i("niral", "FRIENDS::::: " + groupData.size());
//
//                                for (int m=0;m<groupdata.size();m++) {
//                                    users = postSnapshot.child(Config.USER_NAME).getValue().toString();
//
//                                    Log.i("niral","users " +users);
//                                    number = postSnapshot.child(Config.USER_PHONE_NO).getValue().toString();
//
//                                    Log.i("niral","number " +number);
//
////                                    Log.i("niral","===>>>>>>>>>>>>>>  " +usersname);
////                                    Log.i("niral","===>>>>>>>>>>>>> " +usersnumber);
//
//
//                                }
//
//
////                            for (int i = 0; i < userMembersMobile.size(); i++) {
////
////
////                                if (userMembersMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
////                                    groupData.get(i).setImage(postSnapshot.child(Config.USER_IMAGE).getValue().toString());
////                                }
////                            }
////                                                   }
//                        }
//                        if (hasNumberOfGroup == groupData.size()) {
//
//                            //displayLocation();
//                            getTrackMembers();
//                            Log.i("niral", "I am in if=== " + groupData.size());
//                            UserAdapter userAdapter = new UserAdapter (getActivity(), groupData);
//                            Toast.makeText(getContext(), "your work done", Toast.LENGTH_LONG).show();
//                            //  Log.i("TANUJA",userAdapter.toString());
//                            displayLocation();
////
//
//                        } else {
////                        Config.cancelDialog();
////                        llNoFriendFound.setVisibility(View.VISIBLE);
////                        recyclerParticipants.setVisibility(View.GONE);
//                        }
//
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Config.cancelDialog();
//                }
//            });
//
//        }
//    }
//
//
////
////    protected Marker createMarker(double latitude, double longitude) {
////
////        return googleMap.addMarker(new MarkerOptions()
////                .position(new LatLng(latitude, longitude)));
////
////////        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
////////        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////}
//////
//
//    /**
//     * Creating google api client object
//     */
//    protected synchronized void buildGoogleApiClient() {
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Creating location request object
//     */
//    protected void createLocationRequest() {
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//    /**
//     * Method to verify google play services on the device
//     */
//    private boolean checkPlayServices() {
//
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(getActivity());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//
//                Toast.makeText(getActivity(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Starting the location updates
//     */
//    protected void startLocationUpdates() {
//
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(),
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
//    /**
//     * Stopping location updates
//     */
//    protected void stopLocationUpdates() {
//
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
////displayLocation();
//        if (mRequestingLocationUpdates) {
//            //displayLocation();
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        // Assign the new location
//        mLastLocation = location;
//        Toast.makeText(getActivity(), "Location changed!", Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void displayLocation() {
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//
//            for (int i=0;i<userMembersMobile.size();i++) {
//
//
//                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//
//                Log.i("niral","arraylist size " +userMembersMobile.size());
//                Log.i("niral","lat" +mLastLocation.getLatitude());
//                Log.i("niral","long" +mLastLocation.getLongitude());
//
//                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))); //...
//                markers.add(marker);
//                markers.size();
//
//                Log.i("niral", "marker size " + markers.size());
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                //UserLatLon(lat, lon,users,number);
////                Log.i("niral","===>>>>>>>>>>>>>>  " +users);
////                Log.i("niral","===>>>>>>>>>>>>> " +number);
//
//            }
//
//        }
//    }
//
//    //}            //}
//        private void getTrackMembers() {
//
//            Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_LAT_LON_TABLE);
//            final Query firebase_Query = mDatabase.orderByChild(Config.USER_LATLON_ID).equalTo(key);
//            Log.i("niral",firebase_Query.toString());
//Log.i("niral","keyzzzzzz" + key);
//            firebase_Query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//
//                    Log.i("niral", "FRIENDS  " + snapshot.toString());
//
//                    if (snapshot.getChildrenCount() > 0) {
//                        userTrackMobile.clear();
//                        latilongis.clear();
//                        groupIdslist.clear();
//
//                        //longitude.clear();
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_MOBILE_NO)) {
//
//
//                                userTrackMobile.add(postSnapshot.child(Config.USER_MOBILE_NO).getValue().toString());
//
//                                latilongis.add(new LatiLongi("",Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()),Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString())));
//                               Log.i("niral", "latitude longitude" +latilongis.size());
//
//                                // longitude.add(new LatiLongi("",Double.parseDouble(""),Double.parseDouble( postSnapshot.child(Config.USER_LONGITUDE).getValue().toString())));
//                            }
//                        }
//                        if (userTrackMobile.size() > 0) {
//                            getTrackDetails();
//                        } else {
//
//                            Config.cancelDialog();
////                            llNoFriendFound.setVisibility(View.VISIBLE);
////                            recyclerView.setVisibility(View.GONE);
//                        }
//                    } else {
//                        Config.cancelDialog();
//                      //  llNoFriendFound.setVisibility(View.VISIBLE);
//                        //recyclerView.setVisibility(View.GONE);
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
////
//        private void getTrackDetails() {
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
//            firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO);
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount() > 0) {
//
//
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
//                                for (int i = 0; i < userTrackMobile.size(); i++) {
//                                    if (userTrackMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
//                             latilongis.get(i).setLongitude(Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString()));
//                                        latilongis.get(i).setLatitude(Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()));
//                                    }
//                                }
//                            }
//                        }
////
//                    } else {
//                        Config.cancelDialog();
////                        llNoFriendFound.setVisibility(View.VISIBLE);
////                        recyclerView.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Config.cancelDialog();
//
//                }
//            });
//
//        }
//
////    private void getTrackMembers() {
////        //Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
////        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_LAT_LON_TABLE);
////        firebaseQuery = mDatabase.orderByChild(Config.USER_MOBILE_NO).equalTo(phonenumber);
////
////
////        Log.i("niral",firebaseQuery.toString());
////        firebaseQuery.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot snapshot) {
////
////                if (snapshot.getChildrenCount() > 0) {
////                    groupIDslist.clear();
////                    Log.i("niral","latlon"+snapshot.toString());
////                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
////                        if (postSnapshot.hasChild(Config.USER_MOBILE_NO)) {
////                            String temp = postSnapshot.child(Config.USER_MOBILE_NO).getValue().toString();
////
////                            if (!groupIDslist.contains(temp)) {
////                                groupIDslist.add(temp);
////                                hasNumOfGroup++;
////
////                            }
////
////                        } else {
////                            Config.cancelDialog();
////                        }
////
////                    }
////
////                    getTrackDetails();
////
////                } else {
//////                    Config.cancelDialog();
////
////
////                }
////                firebaseQuery.removeEventListener(this);
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                Config.cancelDialog();
////
////            }
////        });
////
////    }
////
////    private void getTrackDetails() {
////
////        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
////        //arrayList.clear();
////        // groupdata.clear();
////        latilongis.clear();
////        for (int i = 0; i < groupIDslist.size(); i++) {
////
////            firebaseQuery = mDatabase.orderByChild(Config.GROUP_MAPPING_ID).equalTo(groupIDslist.get(i));
////            firebaseQuery.addValueEventListener(new ValueEventListener() {
////                @Override
////                public void onDataChange(DataSnapshot snapshot) {
////
////                    if (snapshot.getChildrenCount() > 0) {
////
////                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
////                            if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {
////                                String temp =postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString();
////                                latilongis.add(new LatiLongi("",Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()),Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString())));
////                                Log.i("niral", "latitude longitude" +latilongis.size());
//////
////
////                                //  groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp ) );
////                                //getParticipantsData.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp));
////
////                                // arrayList.add(postSnapshot.child(Config.U).getValue().toString());
////                                Log.i("niral", String.valueOf(arrayList));
////                                if (hasNumOfGroup == latilongis.size()) {
////
////
//////                                    displayLocation();
////
//////
//////                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
//////                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//////
//////                                    spinnergroup.setAdapter(arrayAdapter);
//////                                    spinnergroup.setSelection(0);
//////
//////                                    group_id = groupdata.get(0).getGroupid();
//////                                    // group_id=groupdata.get(i).getGroupid();
//////                                    // participant=groupdata.get(0).getGroupparticipant();
//////                                    // Config.cancelDialog();
////
////                                }
////                            }
////
////                        }
////                    } else {
//////                        Config.cancelDialog();
////
////                    }
////
////                    firebaseQuery.removeEventListener(this);
////
////                }
////
////                @Override
////                public void onCancelled(DatabaseError databaseError) {
////
////                    Config.cancelDialog();
////                }
////            });
////        }
//    }
//
//
//
////    public void UserLatLon(double lat, double lon,String name,String numbers)
////    {
////        Firebase ref = new Firebase(Config.FIREBASE_URL);
////        final Firebase postRef = ref.child(Config.USER_LAT_LON_TABLE);
////        final String key = postRef.child(Config.USER_LAT_LON_TABLE).push().getKey();
////
////        Map<String, String> post1 = new HashMap<String, String>();
////        post1.put(Config.USER_LATLON_ID,key);
////
////        post1.put(Config.USER_MOBILE_NO, numbers);
////        post1.put(Config.USER_NAMES,name);
////        post1.put(Config.USER_LATITUDE, String.valueOf(lat));
////        post1.put(Config.USER_LONGITUDE,String.valueOf(lon));
////        postRef.push().setValue(post1);
////
////        Log.i("niral","user entry " +users);
////        Log.i("niral","number entry " +number);
////
////    }
//
////}


















































package com.example.user.groupexpensetracker.fragment;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
import com.example.user.groupexpensetracker.adapter.UserAdapter;
import com.example.user.groupexpensetracker.bean.Group;
//import com.example.user.groupexpensetracker.bean.LatiLongi;
import com.example.user.groupexpensetracker.bean.LatiLongi;
import com.example.user.groupexpensetracker.bean.ServiceTask;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class TrackingFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    ArrayList<User> user = new ArrayList<>();
    MapView mMapView;
    String key = "";
    private GoogleMap googleMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "TrackingFragment";
    // RequestTask requestTask;
    ArrayList<String> userMembersMobile = new ArrayList<>();
    ArrayList<String> userTrackMobile = new ArrayList<>();

    // int hasNoOfGroup = 0;
    int hasNumberOfGroup = 0;
    int hasNumOfGroup = 0;
    int hasNumbOfGroup = 0;

    //  private String group_id = "";
    // ArrayList<String> userMembersMobile;
    // List<User> membersData;
    // private ChooseGroupMemberAdapter userAdapter;
    List<User> groupData = new ArrayList<>();
    ArrayList<LatiLongi> latilongis = new ArrayList<>();
    // ArrayList<LatiLongi> longitude = new ArrayList<>();

    ArrayList<Group> groupdata;
    ArrayList<String> arrayList;
    // ArrayList<LatiLongi>latitude=new ArrayList<>();
    //ArrayList<LatiLongi>longitude=new ArrayList<>();
    String phonenumber;
    ArrayList<String> groupIdslist;
    int hasNoOfGroup = 0;
    SharedPreferences sharedPreferences;
    private DatabaseReference mDatabase;
    Query firebaseQuery;
    ServiceTask serviceTask;
    ArrayList<String> groupIDslist = new ArrayList<>();
    String location;
    String value;
    Spinner spinner;
    private String id = "";
    String number;
    private String group_id = "";
    List<Marker> markers = new ArrayList<>();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    double lat;
    double lon;
    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // GPSTracker class


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tracking, container, false);
        groupdata = new ArrayList<>();
        // getActivity().startService(new Intent(getContext(), ServiceTask.class));
        Firebase.setAndroidContext(getActivity());

//        lat = mLastLocation.getLatitude();
//        lon = mLastLocation.getLongitude();

        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//id=latilongis.get(i).getId();
                group_id = groupdata.get(i).getGroupid();
                Log.i("niral", "groupid " + group_id);

                getUserMembers();
                // }

//

                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //serviceTask = new ServiceTask();
        //getActivity().startService(new Intent(getContext(), ServiceTask.class));
        groupIdslist = new ArrayList<>();
        arrayList = new ArrayList<>();
//        //id=latilongis.g
//        Intent intent=new Intent();
//        intent.putExtra("id",id);

        sharedPreferences = getActivity().getSharedPreferences(GlobalData.mysharedpreference, Context.MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
        //phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");
//        Double lat=mLastLocation.getLatitude();
//        Double lon=mLastLocation.getLongitude();
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // First we need to check availability of play services
        if (checkPlayServices()) {
//displayLocation();
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
//        // UserLatLon(Double.parseDouble(String.valueOf(mLastLocation.getLatitude())),Double.parseDouble(String.valueOf(mLastLocation.getLongitude())));
//

//        UserLatLon(String.valueOf(lat),String.valueOf(lon));
        //      Log.i("niral","latlon"+ String.valueOf(lat));
        //    Log.i("niral","latlon"+ String.valueOf(lon));

        getGroupsFromGroupMapping();

        //  getTrackMembers();


//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
        } else {

            checkLocationPermission();
        }

        mMapView.onResume(); // needed to get the map to display immediately

        try {

            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


//        for(int i=0;i<latitude.size();i++)
//        {
//            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//
//            value   = String.valueOf(latitude.add(new LatiLongi("",mLastLocation.getLongitude(),mLastLocation.getLatitude())));
//            Log.i("tanuja",String.valueOf(mLastLocation.getLatitude()));
//            Log.i("tanuja",String.valueOf(mLastLocation.getLongitude()));
//            googleMap.addMarker(new MarkerOptions().position().title("My Current Location").snippet("Marker Description"));
//
//
//            // For zooming automatically to the location of the marker
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(value).zoom(12).build();
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//
//
//        }


        mMapView.getMapAsync(new OnMapReadyCallback() {

                                 @RequiresApi(api = Build.VERSION_CODES.M)
                                 @Override
                                 public void onMapReady(final GoogleMap mMap) {

                                     googleMap = mMap;
                                     // For showing a move to my location button
                                     //if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                     if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                         ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                                                 android.Manifest.permission.ACCESS_FINE_LOCATION
                                         }, 10);
                                         return;
                                     }
                                     googleMap.setMyLocationEnabled(false);

//displayLocation();
                                 }
                             }

        );


        return rootView;
    }
    //
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            //startLocationUpdates();
        }
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
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

        for (int i = 0; i < groupIdslist.size(); i++) {
            groupdata.clear();
            firebaseQuery = mDatabase.orderByChild(Config.GROUP_ID).equalTo(groupIdslist.get(i));
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.getChildrenCount() > 0) {

                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.GROUP_NAME)) {
                                String temp = postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString();

                                arrayList.add(postSnapshot.child(Config.GROUP_NAME).getValue().toString());
                                groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp));
                                Log.e("sweny", String.valueOf(arrayList));
                                if (hasNoOfGroup == arrayList.size()) {


                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
                                    spinner.setAdapter(arrayAdapter);
                                    spinner.setSelection(0);
                                    group_id = groupdata.get(0).getGroupid();
                                    Log.i("niral", "tanuja" + group_id);
                                    //getTripImages();
                                    // Config.cancelDialog();

                                }
                            }

                        }
                    } else {
//                        Config.cancelDialog();

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

    private void getUserMembers() {

        //Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(group_id);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("niral", "FRIENDS " + group_id);
                Log.i("niral", "FRIENDS  " + snapshot.toString());

                if (snapshot.getChildrenCount() > 0) {
                    userMembersMobile.clear();
                    groupData.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {

                            hasNumberOfGroup++;
                            Log.i("niral", "no of groups " + hasNumberOfGroup);
                            userMembersMobile.add(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString());
                            //groupData.add(new User(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString()));
                        }
                    }

                    if (userMembersMobile.size() > 0) {
                        Log.i("niral", "FRIENDS " + group_id);
                        getMemberDetails();
                    } else {

                        Config.cancelDialog();
//                        llNoFriendFound.setVisibility(View.VISIBLE);
//                        recyclerParticipants.setVisibility(View.GONE);
                    }
                } else {
//                    Config.cancelDialog();
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

                        Log.i("niral", "FRIENDS===  " + snapshot.toString());

//                        groupData.clear();
                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
                                groupData.add(new User(postSnapshot.child(Config.USER_NAME).getValue().toString(), postSnapshot.child(Config.USER_PHONE_NO).getValue().toString(), postSnapshot.child(Config.USER_IMAGE).getValue().toString()));
                                Log.i("niral", "FRIENDS::::: " + groupData.size());
                            }


                            for (int i = 0; i < groupData.size(); i++) {

                                number = postSnapshot.child(Config.USER_PHONE_NO).getValue().toString();

                                Log.i("niral", "members" + number);
                            }

//
//                                if (userMembersMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
//                                    groupData.get(i).setImage(postSnapshot.child(Config.USER_IMAGE).getValue().toString());
//                                }
//                            }
                            //                       }
                        }
                        if (hasNumberOfGroup == groupData.size()) {

                            Log.i("niral", "I am in if=== " + groupData.size());
                            UserAdapter userAdapter = new UserAdapter(getActivity(), groupData);
                           // Toast.makeText(getContext(), "your work done", Toast.LENGTH_LONG).show();
                            //getTrackMembers();
                            //  Log.i("TANUJA",userAdapter.toString());
                              displayLocation();
//

                        } else {
//                        Config.cancelDialog();
//                        llNoFriendFound.setVisibility(View.VISIBLE);
//                        recyclerParticipants.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Config.cancelDialog();
                }
            });

        }
    }


//
//    protected Marker createMarker(double latitude, double longitude) {
//
//        return googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitude)));
//
//////        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
//////        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//}
////

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        //   displayLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        // Assign the new location
        mLastLocation = location;
        Toast.makeText(getActivity(), "Location changed!", Toast.LENGTH_SHORT).show();
        //displayLocation();
    }

//    private void insertMarkers() {
//        List<LatiLongi> list=new ArrayList<>();
//        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        for (int i = 0; i < list.size(); i++) {
//            LatLng position = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
//            final MarkerOptions options = new MarkerOptions().position(position).title(user.get(i).getName());
//
//            googleMap.addMarker(options);
//
//
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(12).build();
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//        }
//
//    }
//

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
//lat=latilongis.get(i).getLatitude()
            // for (int i = 0; i < latilongis.size(); i++) {
            //       lat=latilongis.get(i).getLatitude().toString();
//lon=latilongis.get(i).getLongitude().toString();

//
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();

            //     for (int i = 0; i < latilongis.size(); i++) {


            // LatLng latLng = new LatLng(latilongis.get(i).getLatitude(), latilongis.get(i).getLongitude());
            //    options.position(latLng);
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))); //...
            markers.add(marker);
            markers.size();
            Log.i("niral", "marker" + markers.size());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                UserLatLon(lat, lon);
        }
    }


    //}            //}
//        private void getTrackMembers() {
//
//            Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_LAT_LON_TABLE);
//            final Query firebase_Query = mDatabase.orderByChild(Config.USER_MOBILE_NO).equalTo(phonenumber);
//            Log.i("niral",firebase_Query.toString());
//Log.i("niral","keyzzzzzz" + key);
//            firebase_Query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//
//                    Log.i("niral", "FRIENDS  " + snapshot.toString());
//
//                    if (snapshot.getChildrenCount() > 0) {
//                        userTrackMobile.clear();
//                        latilongis.clear();
//                        //groupIdslist.clear();
//
//                        //longitude.clear();
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_MOBILE_NO)) {
//
//
//                                userTrackMobile.add(postSnapshot.child(Config.USER_MOBILE_NO).getValue().toString());
//
//                                latilongis.add(new LatiLongi("",Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()),Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString())));
//                               Log.i("niral", "latitude longitude" +latilongis.size());
//
//                                // longitude.add(new LatiLongi("",Double.parseDouble(""),Double.parseDouble( postSnapshot.child(Config.USER_LONGITUDE).getValue().toString())));
//                            }
//                        }
//                        if (userTrackMobile.size() > 0) {
//                            getTrackDetails();
//                        } else {
//
//                            Config.cancelDialog();
////                            llNoFriendFound.setVisibility(View.VISIBLE);
////                            recyclerView.setVisibility(View.GONE);
//                        }
//                    } else {
//                        Config.cancelDialog();
//                      //  llNoFriendFound.setVisibility(View.VISIBLE);
//                        //recyclerView.setVisibility(View.GONE);
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//        private void getTrackDetails() {
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
//            firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO);
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount() > 0) {
//
//
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
//                                for (int i = 0; i < userTrackMobile.size(); i++) {
//                                    if (userTrackMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
//                             latilongis.get(i).setLongitude(Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString()));
//                                        latilongis.get(i).setLatitude(Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()));
//                                    }
//                                }
//                            }
//                        }
////
//                    } else {
//                        Config.cancelDialog();
////                        llNoFriendFound.setVisibility(View.VISIBLE);
////                        recyclerView.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Config.cancelDialog();
//
//                }
//            });
//
//        }

//    private void getTrackMembers() {
//        //Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
//       // for (int i = 0; i < latilongis.size(); i++) {
//            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_LAT_LON_TABLE);
//            firebaseQuery = mDatabase.orderByChild(Config.USER_MOBILE_NO).equalTo(phonenumber);
//
//Log.i("niral","keyzzzzzzzz"+key);
//            Log.i("niral", firebaseQuery.toString());
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//
//                    if (snapshot.getChildrenCount() > 0) {
//                        groupIDslist.clear();
//                        Log.i("niral", "latlon" + snapshot.toString());
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.USER_MOBILE_NO)) {
//                                String temp = postSnapshot.child(Config.USER_MOBILE_NO).getValue().toString();
//
//                                if (!groupIDslist.contains(temp)) {
//                                    groupIDslist.add(temp);
//                                    hasNumOfGroup++;
//
//                                }
//
//                            } else {
//                                Config.cancelDialog();
//                            }
//
//                        }
//
//                        getTrackDetails();
//
//                    } else {
////                    Config.cancelDialog();
//
//
//                    }
//                    firebaseQuery.removeEventListener(this);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Config.cancelDialog();
//
//                }
//            });
//
//        }
//
//    private void getTrackDetails() {
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);//arrayList.clear();
//       // groupdata.clear();
//latilongis.clear();
//        for (int i = 0; i < groupIDslist.size(); i++) {
//
//            firebaseQuery = mDatabase.orderByChild(Config.GROUP_ID).equalTo(groupIDslist.get(i));
//            firebaseQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//
//                    if (snapshot.getChildrenCount() > 0) {
//
//                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {
//                                String temp = postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString();
//                               latilongis.add(new LatiLongi("","", Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()), Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString()),temp));
//                                Log.i("niral", "latitude longitude" + latilongis.size());
////
//
//                                //  groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp ) );
//                                //getParticipantsData.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp));
//
//                                // arrayList.add(postSnapshot.child(Config.U).getValue().toString());
//                                Log.i("niral", String.valueOf(arrayList));
//                                if (hasNumOfGroup == latilongis.size()) {
//
//
//                                    for (int i = 0; i < userTrackMobile.size(); i++) {
//                                        if (userTrackMobile.get(i).equalsIgnoreCase(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString())) {
//                                latilongis.get(i).setLongitude(Double.parseDouble(postSnapshot.child(Config.USER_LONGITUDE).getValue().toString()));
//                                            latilongis.get(i).setLatitude(Double.parseDouble(postSnapshot.child(Config.USER_LATITUDE).getValue().toString()));
//
//
////
//                                            //                                  displayLocation();
//
////
////                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
////                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////
////                                    spinnergroup.setAdapter(arrayAdapter);
////                                    spinnergroup.setSelection(0);
////
////                                    group_id = groupdata.get(0).getGroupid();
////                                    // group_id=groupdata.get(i).getGroupid();
////                                    // participant=groupdata.get(0).getGroupparticipant();
////                                    // Config.cancelDialog();
//
//                                        }
//                                    }
//
//                                }
//                            } else {
////                        Config.cancelDialog();
//
//                            }
//
//                            firebaseQuery.removeEventListener(this);
//
//                        }
//                    }
//                }
//
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                    Config.cancelDialog();
//                }
//            });
//        }
//    }
//
//
//
//    public void UserLatLon(double lat, double lon)
//    {
//        Firebase ref = new Firebase(Config.FIREBASE_URL);
//        final Firebase postRef = ref.child(Config.USER_LAT_LON_TABLE);
//        final String key = postRef.child(Config.USER_LAT_LON_TABLE).push().getKey();
//
//        Map<String, String> post1 = new HashMap<String, String>();
//        post1.put(Config.USER_LATLON_ID,key);
//
//        post1.put(Config.USER_MOBILE_NO, number);
//        post1.put(Config.USER_LATITUDE, String.valueOf(lat));
//        post1.put(Config.USER_LONGITUDE,String.valueOf(lon));
//        postRef.push().setValue(post1);
//
//    }
//
//}

}






































