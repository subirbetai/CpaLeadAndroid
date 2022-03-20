package com.cpalead.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.cpalead.app.utils.AppPref;
import com.cpalead.app.utils.Log;
import com.cpalead.app.utils.Toasty;
import com.cpalead.app.utils.UserPref;
import com.cpalead.app.utils.Variables;
import com.cpalead.app.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    public final static String TAG = ProfileActivity.class.getSimpleName();
    private TextView tv_full_name, tv_mobile;
    private String access_token;
    private AppPref appPref;
    private UserPref userPref;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        appPref = new AppPref(this);
        userPref = new UserPref(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        access_token = userPref.getAccessToken();

        tv_full_name = findViewById(R.id.tv_full_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        ImageView iv_back = findViewById(R.id.iv_back);
        Button btn_logout = findViewById(R.id.btn_logout);

        String fullName = userPref.getFullName();
        String mobile = userPref.getMobile();

        tv_full_name.setText(fullName);
        tv_mobile.setText(mobile);

        iv_back.setOnClickListener(v -> ProfileActivity.super.onBackPressed());

        btn_logout.setOnClickListener(v -> {

            appPref.clearAll();
            userPref.clearAll();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        Profile();
    }

    private void Profile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.APP_TOKEN_VALIDATE, response -> {

            Log.d(TAG, "Profile:onResponse - " + response);

            try {
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.getString("status").equals("success")) {

                    JSONObject data = jsonResponse.getJSONObject("data");
                    String full_name = data.getString("full_name");
                    String mobile = data.getString("mobile");

                    userPref.setFullName(full_name);
                    userPref.setMobile(mobile);

                    tv_full_name.setText(full_name);
                    tv_mobile.setText(mobile);

                } else {
                    Toasty.error(ProfileActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();
                    userPref.clearAll();

                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            Log.d(TAG, "Profile:onErrorResponse - Unable to get profile !!");

            if (error instanceof TimeoutError) {
                Toasty.error(ProfileActivity.this, "Oops, Connection timeout !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toasty.error(ProfileActivity.this, "Oops, Please check your internet connection !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {

                if (error.networkResponse.data.length != 0) {

                    String response = new String(error.networkResponse.data);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        Toasty.error(ProfileActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();

                    } catch (JSONException e) {

                        Toasty.error(ProfileActivity.this, response, Toasty.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {

                    Toasty.error(ProfileActivity.this, "Oops, Unauthorized Access !!", Toasty.LENGTH_SHORT).show();
                }

            } else if (error instanceof ServerError) {
                Toasty.error(ProfileActivity.this, "Oops, Server error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toasty.error(ProfileActivity.this, "Oops, Network error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toasty.error(ProfileActivity.this, "Oops, Data parsing error !!", Toasty.LENGTH_SHORT).show();
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