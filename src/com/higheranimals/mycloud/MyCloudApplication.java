package com.higheranimals.mycloud;

import android.app.Application;
import android.content.SharedPreferences;

public class MyCloudApplication extends Application {
    public boolean hasAuthInfo() {
        SharedPreferences prefs = getSharedPreferences("user_details",
                MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        return !(username == null && password == null);
    }
}
