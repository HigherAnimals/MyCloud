package com.higheranimals.mycloud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class ServiceListActivity extends SherlockFragmentActivity {

    private static final String TAG = "ServiceListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check for auth
        if (!((MyCloudApplication) getApplication()).hasAuthInfo()) {
            Intent intent = new Intent(this, AuthenticateActivity.class);
            startActivityForResult(intent, 0);
        }

        setContentView(R.layout.activity_service_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_service_list, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            Log.v(TAG, "canceling for failure to auth");
            finish();
        }
    }
}