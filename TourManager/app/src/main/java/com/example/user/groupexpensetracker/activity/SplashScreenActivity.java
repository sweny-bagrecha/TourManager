package com.example.user.groupexpensetracker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.activity.MainActivity;
import com.example.user.groupexpensetracker.activity.OTPActivity;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences=getApplicationContext().getSharedPreferences(GlobalData.mysharedpreference,MODE_PRIVATE);


        final Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {

                    Log.i("KINJAL::","LOGIN  "+sharedPreferences.getBoolean(GlobalData.isLogin,false));
                    Log.i("KINJAL::","NAME  "+sharedPreferences.getBoolean(GlobalData.isHavingName,false));

                    if (sharedPreferences.getBoolean(GlobalData.isLogin,false)==true)
                    {

                        if (sharedPreferences.getBoolean(GlobalData.isHavingName,false)==true)
                        {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else
                        {
                            Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    else
                    {
                        Intent i=new Intent(getApplicationContext(),OTPActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
        timerThread.start();
         }
}
