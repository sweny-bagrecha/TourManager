package com.example.user.groupexpensetracker.commanclasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.user.groupexpensetracker.Util.GPSTracker;

/**
 * Created by nteam on 6/12/16.
 */

public class LocationGetService extends Service {
    GPSTracker gpsTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("niral", "SERIVCXE CALLED...");

        gpsTracker = new GPSTracker(getApplicationContext());
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Log.i("niral", "LONG  " + gpsTracker.getLongitude());
                Log.i("niral", "LATI  " + gpsTracker.getLatitude());

            }
        });
        thread.start();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
