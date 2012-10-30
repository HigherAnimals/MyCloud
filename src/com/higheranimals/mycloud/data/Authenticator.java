package com.higheranimals.mycloud.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.higheranimals.mycloud.network.AuthenticationException;
import com.higheranimals.mycloud.network.NetworkException;
import com.higheranimals.mycloud.network.NetworkUtilities;

public class Authenticator {

    private static final String TAG = "Authenticator";

    public static void authenticate(Context context, String username,
            String password) throws AuthenticationException, NetworkException {
        Log.v(TAG, "authenticate");

        // Authenticate.
        String response = NetworkUtilities.getAuth(username, password);

        // Parse response and populate database with account and services.
        SQLiteDatabase db = new DbHelper(context).getWritableDatabase();
        try {
            JSONObject accessJson = new JSONObject(response)
                    .getJSONObject("access");
            parseUserJson(db, accessJson.getJSONObject("user"));
            parseServicesJson(db, accessJson.getJSONArray("serviceCatalog"));
        } catch (JSONException e) {
            // Needs a better exception.
            throw new NetworkException("Error parsing JSON in auth response.");
        } finally {
            db.close();
        }
    }

    private static void parseUserJson(SQLiteDatabase db, JSONObject user)
            throws JSONException {
        Log.v(TAG, "parseUserJson");
        ContentValues values = new ContentValues();
        values.put(DbHelper.ACCOUNT_NAME, user.getString("name"));
        values.put(DbHelper.ACCOUNT_NO, user.optString("id")); // Note type.
        try {
            db.insertOrThrow(DbHelper.ACCOUNT_TABLE, null, values);
        } catch (SQLException e) {
            Log.v(TAG, "skipping user");
        }
    }

    private static void parseServicesJson(SQLiteDatabase db, JSONArray services)
            throws JSONException {
        Log.v(TAG, "parseServicesJson");
        for (int i = 0; i < services.length(); i++) {
            parseServiceJson(db, services.getJSONObject(i));
        }
    }

    private static void parseServiceJson(SQLiteDatabase db, JSONObject service)
            throws JSONException {
        Log.v(TAG, "parseServiceJson");
        ContentValues values = new ContentValues();
        values.put(DbHelper.SERVICE_NAME, service.getString("name"));
        values.put(DbHelper.SERVICE_TYPE, service.getString("type"));
        try {
            db.insertOrThrow(DbHelper.SERVICE_TABLE, null, values);
        } catch (SQLException e) {
            Log.v(TAG, "skipping service");
        }
    }
}
