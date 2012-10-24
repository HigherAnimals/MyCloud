package com.higheranimals.mycloud;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.higheranimals.mycloud.network.AuthenticationException;
import com.higheranimals.mycloud.network.NetworkException;
import com.higheranimals.mycloud.network.NetworkUtilities;

public class AuthenticateActivity extends Activity implements OnClickListener {

    private static final String TAG = "AuthenticateActivity";
    private Button button;
    private EditText usernameField;
    private EditText passwordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        // Get UI views.
        usernameField = ((EditText) findViewById(R.id.username));
        passwordField = ((EditText) findViewById(R.id.password));
        button = (Button) findViewById(R.id.auth_submit);

        // Set listeners.
        button.setOnClickListener(this);
        ((Button) findViewById(R.id.auth_cancel)).setOnClickListener(this);
    }

    public void enableViews(boolean enable) {
        // TODO make this do something other than look stupid.
        usernameField.setEnabled(enable);
        passwordField.setEnabled(enable);
        button.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {

        // If cancel clicked, set failure result and finish.
        if (v.getId() == R.id.auth_cancel) {
            setResult(0);
            finish();
            return;
        }

        // Do basic validation.
        Boolean err = false;
        final String username = usernameField.getText().toString().trim();
        if (username.trim().equals("")) {
            usernameField.setError(getString(R.string.username_empty));
            err = true;
        }
        final String password = passwordField.getText().toString().trim();
        if (password.equals("")) {
            passwordField.setError(getString(R.string.password_empty));
            err = true;
        }
        if (err) {
            return;
        }

        // Send request.
        new AuthAsyncTask(this, username, password).execute();
    }

    private class AuthAsyncTask extends AsyncTask<Void, Void, String> {

        private final String username;
        private final String password;
        private String error = null;
        private final AuthenticateActivity activity;

        public AuthAsyncTask(AuthenticateActivity activity, String username,
                String password) {
            super();
            this.activity = activity;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            activity.enableViews(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Clean this up.
            try {
                return NetworkUtilities.getAuth(username, password);
            } catch (AuthenticationException e) {
                error = getString(R.string.auth_failed);
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NetworkException e) {
                error = getString(R.string.network_failed);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            // Re-enable views.
            activity.enableViews(true);

            // Show error and exit, if necessary.
            if (response == null) {
                if (error != null) {
                    Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
                }
                return;
            }

            // Start polling service.
            Intent intent = new Intent(activity, PollingService.class);
            intent.putExtra("authResponse", response);
            activity.startService(intent);
        }
    }
}
