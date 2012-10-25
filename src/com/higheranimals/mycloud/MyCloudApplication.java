package com.higheranimals.mycloud;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyCloudApplication extends Application {
    public synchronized boolean hasAuthInfo() {
        SharedPreferences prefs = getSharedPreferences("authDetails",
                MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        return !(username == null && password == null);
    }

    public synchronized void setAuthInfo(String username, String password) {
        // This needs WAY more thought before release; works for now.
        SharedPreferences prefs = getSharedPreferences("authDetails",
                MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }
}
