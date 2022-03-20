package com.cpalead.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity {

    public final static String TAG = LoginActivity.class.getSimpleName();
    private String username, password;
    private EditText et_username, et_password;
    private UserPref userPref;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPref = new UserPref(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
            username = et_username.getText().toString();
            password = et_password.getText().toString();

            if (checkLoginDetails()) {
                Login();
            }
        });
    }

    private boolean checkLoginDetails() {

        if (et_username.getText().toString().isEmpty()) {
            Toasty.error(LoginActivity.this, "Username can't be empty .", Toasty.LENGTH_SHORT).show();
            et_username.requestFocus();
            return false;
        } else if (et_password.getText().toString().isEmpty()) {
            Toasty.error(LoginActivity.this, "Password field can't be empty !!", Toasty.LENGTH_SHORT).show();
            et_password.requestFocus();
            return false;
        }

        return true;
    }

    private void Login() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.APP_LOGIN, response -> {

            Log.d(TAG, "Login:onResponse - " + response);

            try {
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.getString("status").equals("success")) {

                    JSONObject token = jsonResponse.getJSONObject("token");
                    String access_token = token.getString("access_token");

                    JSONObject data = jsonResponse.getJSONObject("data");
                    String full_name = data.getString("full_name");
                    String mobile = data.getString("mobile");
                    String balance = data.getString("balance");

                    userPref.setAccessToken(access_token);
                    userPref.setFullName(full_name);
                    userPref.setMobile(mobile);
                    userPref.setBalance(balance);

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    Toasty.error(LoginActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            Log.e(TAG, "Login:onErrorResponse - Unable to Login !!");

            if (error instanceof TimeoutError) {
                Toasty.error(LoginActivity.this, "Oops, Connection timeout !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toasty.error(LoginActivity.this, "Oops, Please check your internet connection !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {

                if (error.networkResponse.data.length != 0) {

                    String response = new String(error.networkResponse.data);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        Toasty.error(LoginActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();

                    } catch (JSONException e) {

                        Toasty.error(LoginActivity.this, response, Toasty.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {

                    Toasty.error(LoginActivity.this, "Oops, Unauthorized Access !!", Toasty.LENGTH_SHORT).show();
                }

            } else if (error instanceof ServerError) {
                Toasty.error(LoginActivity.this, "Oops, Server error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toasty.error(LoginActivity.this, "Oops, Network error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toasty.error(LoginActivity.this, "Oops, Data parsing error !!", Toasty.LENGTH_SHORT).show();
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
                    jsonObject.put(Variables.USER_USERNAME, username);
                    jsonObject.put(Variables.USER_PASSWORD, password);
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