package com.cpalead.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.cpalead.app.R;
import com.cpalead.app.config.Urls;
import com.cpalead.app.utils.Log;
import com.cpalead.app.utils.Toasty;
import com.cpalead.app.utils.UserPref;
import com.cpalead.app.utils.Variables;
import com.cpalead.app.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    public final static String TAG = SplashActivity.class.getSimpleName();
    private String access_token;
    private UserPref userPref;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userPref = new UserPref(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        access_token = userPref.getAccessToken();

        if (!access_token.isEmpty()) {

            ValidateToken();

        } else {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }, 0);
        }
    }

    private void ValidateToken() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.APP_TOKEN_VALIDATE, response -> {

            Log.d(TAG, "ValidateToken:onResponse - " + response);

            try {
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.getString("status").equals("success")) {

                    JSONObject token = jsonResponse.getJSONObject("token");
                    String access_token = token.getString("access_token");

                    JSONObject data = jsonResponse.getJSONObject("data");
                    String balance = data.getString("balance");

                    userPref.setAccessToken(access_token);
                    userPref.setBalance(balance);

                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Variables.USER_ACCESS_TOKEN, access_token);
                    startActivity(intent);
                } else {
                    Toasty.error(SplashActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();
                    userPref.clearAll();

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            Log.e(TAG, "ValidateToken:onErrorResponse - Unable to validate token !!");

            if (error instanceof TimeoutError) {
                Toasty.error(SplashActivity.this, "Oops, Connection timeout !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toasty.error(SplashActivity.this, "Oops, Please check your internet connection !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {

                if (error.networkResponse.data.length != 0) {

                    String response = new String(error.networkResponse.data);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        Toasty.error(SplashActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();

                    } catch (JSONException e) {

                        Toasty.error(SplashActivity.this, response, Toasty.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {

                    Toasty.error(SplashActivity.this, "Oops, Unauthorized Access !!", Toasty.LENGTH_SHORT).show();
                }

            } else if (error instanceof ServerError) {
                Toasty.error(SplashActivity.this, "Oops, Server error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toasty.error(SplashActivity.this, "Oops, Network error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toasty.error(SplashActivity.this, "Oops, Data parsing error !!", Toasty.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; Redmi Note 4 MIUI/7.2.9)");
                params.put("Connection", "Keep-Alive");
                return params;
            }

            @Override
            public byte[] getBody() {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Variables.USER_ACCESS_TOKEN, access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return jsonObject.toString().getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}