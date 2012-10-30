package com.higheranimals.mycloud;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PollingService extends Service {

    private final String TAG = "PollingService";

    Thread updaterThread;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updaterThread = new Thread(new Updater());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class Updater implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

        }
    }
}
