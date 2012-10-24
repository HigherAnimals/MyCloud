package com.higheranimals.mycloud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
        String authResponse = intent.getStringExtra("authResponse");
        Log.v(TAG, "onStartCommand");
        if (authResponse != null) {
            try {
                JSONObject authJson = new JSONObject(authResponse);
                JSONArray serviceCatalog = authJson.getJSONObject("access")
                        .getJSONArray("serviceCatalog");
                for (int i = 0; i < serviceCatalog.length(); i++) {
                    JSONObject service = serviceCatalog.getJSONObject(i);
                    Log.v(TAG, service.toString());
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
