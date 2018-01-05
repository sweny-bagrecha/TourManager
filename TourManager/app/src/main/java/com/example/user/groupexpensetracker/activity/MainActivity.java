package com.example.user.groupexpensetracker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.digits.sdk.android.Digits;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.bean.ServiceTask;
import com.example.user.groupexpensetracker.commanclasses.LocationGetService;
import com.example.user.groupexpensetracker.fragment.AddExpenseFragment;
import com.example.user.groupexpensetracker.fragment.ChatFragment;
import com.example.user.groupexpensetracker.fragment.TrackingFragment;
import com.example.user.groupexpensetracker.fragment.GroupFragment;
import com.example.user.groupexpensetracker.fragment.HomeFragment;
import com.example.user.groupexpensetracker.fragment.MembersFragment;
import com.example.user.groupexpensetracker.fragment.TripDetailsFragment;
import com.example.user.groupexpensetracker.fragment.TripMemoriesFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
 private static final String TWITTER_KEY = "qAnXmShh3v9FJPLL1wGVC9Unf";
    private static final String TWITTER_SECRET = "Hsm6vFsNBZn1cMEfZDv5c1eZpO3ZJg1kBS7C01lJeWrX9Cw4R8";
    TextView textViewUserName, textPhoneNumber;
    SharedPreferences sharedPreferences;
    ImageView profile_image;
    Toolbar toolbar;
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_main);
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
       mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("niral", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("niral", "onAuthStateChanged:signed_out");
                }
            }
        };
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.HOME));
        sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        startService();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Log.i("niral", "NAME" + sharedPreferences.getString(GlobalData.userName, ""));
        Log.i("niral", "PHONE" + sharedPreferences.getString(GlobalData.Phonenumber, ""));
        Log.i("niral", "IMAGE" + sharedPreferences.getString(GlobalData.userImage, ""));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new HomeFragment()).commit();
        textViewUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserName);
        textPhoneNumber = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textPhoneNumber);
        profile_image = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        textViewUserName.setText(sharedPreferences.getString(GlobalData.userName, ""));
        textPhoneNumber.setText(sharedPreferences.getString(GlobalData.Phonenumber, ""));
        Glide.with(MainActivity.this).load(sharedPreferences.getString(GlobalData.userImage, "")).into(profile_image);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            toolbar.setTitle(getString(R.string.HOME));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new HomeFragment()).commit();
        } else if (id == R.id.nav_members) {

            toolbar.setTitle(getString(R.string.FRIENDS));

//            Toast.makeText(getApplicationContext(), "Add Members", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new MembersFragment()).commit();


        } else if (id == R.id.nav_addexpense) {

            toolbar.setTitle(getString(R.string.EXPENSES));

//            Toast.makeText(getApplicationContext(), "Add Expenses", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new AddExpenseFragment()).commit();


        } else if (id == R.id.nav_track_your_friend) {

            toolbar.setTitle(getString(R.string.TRACKING));

//            Toast.makeText(getApplicationContext(), "Track a friend", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new TrackingFragment()).commit();


        }
//        else if (id == R.id.nav_tripdetails) {
//
//            toolbar.setTitle(getString(R.string.TRIPDETAILS));
//
////            Toast.makeText(getApplicationContext(), "Trip Details", Toast.LENGTH_LONG).show();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new TripDetailsFragment()).commit();
//
//
//        }
        else if (id == R.id.nav_group) {

            toolbar.setTitle(getString(R.string.GROUP));

//            Toast.makeText(getApplicationContext(), "Create Group", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new GroupFragment()).commit();
        } else if (id == R.id.nav_tripmemories) {

            toolbar.setTitle(getString(R.string.TRIPMEMORIES));

            Toast.makeText(getApplicationContext(), "Trip Memories", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new TripMemoriesFragment()).commit();


        } else if (id == R.id.nav_chat) {

            toolbar.setTitle(getString(R.string.CHAT));

            Toast.makeText(getApplicationContext(), "Chat", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new ChatFragment()).commit();

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            //Toast.makeText(getApplicationContext(),"Share to Contacts",Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_invite) {
            Toast.makeText(getApplicationContext(), "Invite your Friends", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_feedback) {
            Intent in = new Intent(MainActivity.this, ShowWebView.class);
            startActivity(in);
            Toast.makeText(getApplicationContext(), "Give your Feedback", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_aboutus) {
            Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(MainActivity.this,AboutUs.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startService() {
        Log.i("niral", "Call..");

        startService(new Intent(getBaseContext(), LocationGetService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), LocationGetService.class));
    }


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
                Toast.makeText(this, getString(R.string.loginFail), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("niral", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("niral", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("niral", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
                    }
                });
    }


    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

//        mAuth.
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("niral", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, getString(R.string.googleError), Toast.LENGTH_SHORT).show();

    }
}
