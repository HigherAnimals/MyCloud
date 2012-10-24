package com.higheranimals.mycloud.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class NetworkUtilities {

    private static final String TAG = "NetworkUtilities";
    private static final String AUTH_URI = "https://auth.api.rackspacecloud.com/v2.0/tokens/";
    private static final int TIMEOUT = 5000;

    public static String getAuth(String username, String password)
            throws AuthenticationException, NetworkException {
        try {
            return getHttpPostResponse(
                    AUTH_URI,
                    String.format(
                            "{\"auth\":{\"passwordCredentials\":{\"username\":%s,\"password\":%s}}}",
                            JSONObject.quote(username),
                            JSONObject.quote(password)));
        } catch (UnauthorizedException e) {
            Log.v(TAG, "bad username/password");
            throw new AuthenticationException();
        }
    }

    private static String inputStreamToString(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static String executeHttpRequest(HttpRequestBase request)
            throws ClientProtocolException, IOException, UnauthorizedException {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
        HttpResponse response = httpClient.execute(request);
        // Add more response codes.
        switch (response.getStatusLine().getStatusCode()) {
        case 401:
            throw new UnauthorizedException("401, failed request to "
                    + request.getURI());
        case 200:
        default:
            break;
        }
        return inputStreamToString(response.getEntity().getContent());
    }

    public static String getHttpGetResponse(String url,
            List<NameValuePair> params) throws UnauthorizedException,
            NetworkException {
        // Create GET request.
        if (params != null) {
            url += "?" + URLEncodedUtils.format(params, "UTF-8");
        }
        try {
            return executeHttpRequest(new HttpGet(url));
        } catch (ClientProtocolException e) {
            Log.v(TAG, e.toString());
            throw new NetworkException();
        } catch (IOException e) {
            Log.v(TAG, e.toString());
            throw new NetworkException();
        }
    }

    private static String getHttpPostResponse(String url, String body)
            throws UnauthorizedException, NetworkException {
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            Log.v(TAG, e.toString());
            throw new NetworkException();
        }
        try {
            return executeHttpRequest(post);
        } catch (ClientProtocolException e) {
            Log.v(TAG, e.toString());
            throw new NetworkException();
        } catch (IOException e) {
            Log.v(TAG, e.toString());
            throw new NetworkException();
        }
    }
}
