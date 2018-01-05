package com.example.user.groupexpensetracker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class OTPActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "c1vcgIctPUgF14rk9tdmeIYC8";
    private static final String TWITTER_SECRET = "gM6QuUyIo6kiqSpRRRabaMhLYeDvIOhYSrQfoT3OVzC96ggGPI";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DigitsAuthButton digitsButton;
    Button buttonVerify;
    Firebase ref;
    private DatabaseReference mDatabase;
    boolean isRecordFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Digits.Builder digitsBuilder = new Digits.Builder().withTheme(R.style.CustomDigitsTheme);
        Fabric.with(this, new TwitterCore(authConfig), digitsBuilder.build());

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_otp);
        buttonVerify = (Button) findViewById(R.id.buttonVerify);

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitsButton.performClick();
            }
        });

        ref = new Firebase(Config.FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        sharedPreferences = getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Log.i("islogin", "phonenumber " + phoneNumber);

                editor.putBoolean(GlobalData.isLogin, true);
                editor.putString(GlobalData.Phonenumber, phoneNumber);

                editor.commit();
                checkMobile();
            }


            @Override
            public void failure(DigitsException error) {

                editor.putBoolean(GlobalData.isLogin, false);
                editor.commit();
            }
        });


    }

    private void checkMobile() {
        Config.showDialog(this, getString(R.string.checkingphoneno), getString(R.string.please_wait));
        isRecordFound = false;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.USER_PHONE_NO).equalTo(sharedPreferences.getString(GlobalData.Phonenumber, ""));

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("niral", "snapshot" + dataSnapshot);
                if (dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String tempStr = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");

                        if (tempStr.equalsIgnoreCase(postSnapshot.child(Config.USER_PHONE_NO).getValue().toString())) {
                            isRecordFound = true;
                            Intent intent = new Intent(OTPActivity.this, RegisterActivity.class);

                            Log.i("niral", "KEY   " + postSnapshot.getKey());
                            Log.i("niral", "NAME   " + postSnapshot.child(Config.USER_NAME).getValue().toString());
                            Log.i("niral", "PHONE NO   " + postSnapshot.child(Config.USER_PHONE_NO).getValue().toString());
                            Log.i("niral", "IMAGE URL   " + postSnapshot.child(Config.USER_IMAGE).getValue().toString());

                            editor.putString(GlobalData.userName, postSnapshot.child(Config.USER_NAME).getValue().toString());
                            editor.putString(GlobalData.userImage, postSnapshot.child(Config.USER_IMAGE).getValue().toString());
                            editor.putString(GlobalData.userKey, postSnapshot.getKey());

                            editor.commit();


                            intent.putExtra("key", postSnapshot.getKey());

                            Log.i("kinjal","key from OTP " +postSnapshot.getKey());
                            intent.putExtra("imageurl", postSnapshot.child(Config.USER_IMAGE).getValue().toString());
                            intent.putExtra("name", postSnapshot.child(Config.USER_NAME).getValue().toString());
                            intent.putExtra("mobile_number", postSnapshot.child(Config.USER_PHONE_NO).getValue().toString());
                            intent.putExtra("isExits", true);
                            Config.cancelDialog();
                            startActivity(intent);
                            finish();
                            break;
                        } else {
                        }


                    }
                    if (!isRecordFound) {
                        Intent intent = new Intent(OTPActivity.this, RegisterActivity.class);
                        intent.putExtra("isExits", false);
                        Config.cancelDialog();
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Intent intent = new Intent(OTPActivity.this, RegisterActivity.class);
                    intent.putExtra("isExits", false);
                    Config.cancelDialog();
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), getString(R.string.toastServerError), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
