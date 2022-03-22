package com.cpalead.app.activities;

import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cpalead.app.R;
import com.cpalead.app.config.Urls;
import com.cpalead.app.utils.AppPref;
import com.cpalead.app.utils.DeviceInfo;
import com.cpalead.app.utils.DevicePref;
import com.cpalead.app.utils.Log;
import com.cpalead.app.utils.Toasty;
import com.cpalead.app.utils.UserPref;
import com.cpalead.app.utils.Variables;
import com.cpalead.app.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardActivity extends AppCompatActivity {

    public final static String TAG = DashboardActivity.class.getSimpleName();
    public String imei, androidId, advertisingId, macAddress, gcmId, uuid, simSerial, deviceBrand, deviceUserAgent, deviceModel;
    private String access_token;
    private String actionUrl, o18Url;
    private TextView tv_device_brand, tv_device_model, tv_android_id, tv_imei;
    private EditText et_url;
    private LottieAnimationView progress_bar;
    private AppPref appPref;
    private DeviceInfo deviceInfo;
    private DevicePref devicePref;
    private RequestQueue requestQueue;
    private CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        appPref = new AppPref(this);
        UserPref userPref = new UserPref(this);
        deviceInfo = new DeviceInfo(this);
        devicePref = new DevicePref(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        access_token = userPref.getAccessToken();

        ImageView iv_menu = findViewById(R.id.iv_menu);
        ImageView iv_paste = findViewById(R.id.iv_paste);
        progress_bar = findViewById(R.id.progress_bar);
        et_url = findViewById(R.id.et_url);
        Button btn_install = findViewById(R.id.btn_install);

        //Device
        tv_device_brand = findViewById(R.id.tv_device_name);
        tv_device_model = findViewById(R.id.tv_device_model);
        tv_android_id = findViewById(R.id.tv_android_id);
        tv_imei = findViewById(R.id.tv_imei);

        deviceInfoUpdate();

        String url = appPref.getChingari();
        if (!url.isEmpty()) {
            et_url.setText(url);
        }

        iv_menu.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(DashboardActivity.this, R.anim.fadeout, R.anim.fadein);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, activityOptions.toBundle());
        });

        iv_paste.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            try {
                CharSequence paste = clipboard.getPrimaryClip().getItemAt(0).getText();
                et_url.setText(paste);
            } catch (Exception e) {
                Toasty.error(DashboardActivity.this, "Clipboard  is empty !!", Toasty.LENGTH_SHORT).show();
            }
        });

        btn_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_url.getText().toString().isEmpty()) {
                    deviceInfoUpdate();

                    cookieManager.getCookieStore().removeAll();

                    actionUrl = et_url.getText().toString().trim();
                    appPref.setChingari(actionUrl);
                    GetUrl();
                } else {
                    Toasty.error(DashboardActivity.this, "URL cannot be empty !!", Toasty.LENGTH_SHORT);
                }
            }
        });
    }

    public void deviceInfoUpdate() {
        deviceInfo.setImei("false");
        deviceInfo.setAndroidId("false");
        deviceInfo.setAdvertisingId("false");
        deviceInfo.setMacAddress("false");
        deviceInfo.setGcmId("false");
        deviceInfo.setUuId("false");
        deviceInfo.setSimSerial("false");
        deviceInfo.setModel("false");

        // Device ID
        imei = deviceInfo.getImei();
        androidId = deviceInfo.getAndroidId();
        advertisingId = deviceInfo.getAdvertisingId();
        macAddress = deviceInfo.getMacAddress();
        gcmId = deviceInfo.getGcmId();
        uuid = deviceInfo.getUuId();
        simSerial = deviceInfo.getSimSerial();
        deviceModel = deviceInfo.getModel();
        deviceBrand = deviceInfo.getBrand(deviceModel);
        deviceUserAgent = deviceInfo.getUserAgent();

        tv_device_brand.setText(deviceBrand);
        tv_device_model.setText(deviceModel);
        tv_android_id.setText(androidId);
        tv_imei.setText(imei);

        // Shared Preferences
        devicePref.setDeviceImei(imei);
        devicePref.setDeviceAndroidId(androidId);
        devicePref.setDeviceAdvertisingId(advertisingId);
        devicePref.setDeviceMacAddress(macAddress);
        devicePref.setDeviceGcmId(gcmId);
        devicePref.setDeviceUuid(uuid);
        devicePref.setDeviceSimSerial(simSerial);
        devicePref.setDeviceModel(deviceModel);
        devicePref.setDeviceBrand(deviceBrand);
        devicePref.setUserAgent(deviceUserAgent);
    }

    private void GetUrl() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.APP_GET_URL, response -> {

            Log.d(TAG, "GetUrl:onResponse - " + response);

            try {
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.getString("status").equals("success")) {
                    String url = jsonResponse.getString("url");
                    onURL(url, "MAIN");
                } else {
                    Toasty.error(DashboardActivity.this, "No URL found to start installation !!", Toasty.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            Log.d(TAG, "GetUrl:onErrorResponse - Unable to get profile !!");

            if (error instanceof TimeoutError) {
                Toasty.error(DashboardActivity.this, "Oops, Connection timeout !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toasty.error(DashboardActivity.this, "Oops, Please check your internet connection !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {

                if (error.networkResponse.data.length != 0) {

                    String response = new String(error.networkResponse.data);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        Toasty.error(DashboardActivity.this, jsonResponse.getString("message"), Toasty.LENGTH_SHORT).show();

                    } catch (JSONException e) {

                        Toasty.error(DashboardActivity.this, response, Toasty.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {

                    Toasty.error(DashboardActivity.this, "Oops, Unauthorized Access !!", Toasty.LENGTH_SHORT).show();
                }

            } else if (error instanceof ServerError) {
                Toasty.error(DashboardActivity.this, "Oops, Server error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toasty.error(DashboardActivity.this, "Oops, Network error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toasty.error(DashboardActivity.this, "Oops, Data parsing error !!", Toasty.LENGTH_SHORT).show();
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
                    jsonObject.put(Variables.APP_INSTALL_URL, actionUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return jsonObject.toString().getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void OpenUrl(String url, String tag) {

        Log.e("UEL", url);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.VISIBLE);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress_bar.setVisibility(View.INVISIBLE);
                }
            });
            onResponse(response, tag);
        }, error -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress_bar.setVisibility(View.INVISIBLE);
                }
            });

            if (error instanceof TimeoutError) {
                Toasty.error(DashboardActivity.this, "Oops, Connection timeout !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toasty.error(DashboardActivity.this, "Oops, Please check your internet connection !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                Toasty.error(DashboardActivity.this, "Oops, Unauthorized Access !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                onError(error, tag);
            } else if (error instanceof NetworkError) {
                Toasty.error(DashboardActivity.this, "Oops, Network error !!", Toasty.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toasty.error(DashboardActivity.this, "Oops, Data parsing error !!", Toasty.LENGTH_SHORT).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Connection", "Keep-Alive");
                params.put("User-Agent", devicePref.getUserAgent());
                params.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress_bar.setVisibility(View.INVISIBLE);
                    }
                });

                onNetworkResponse(response, tag);
                return super.parseNetworkResponse(response);
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @SuppressWarnings("SameParameterValue")
    private void onURL(String url, String tag) {
        OpenLocation(url, tag);
    }

    private void onResponse(String response, String tag) {
        Log.e(TAG, "onResponse : " + response);

        if (tag.equals("FINAL")) {
            Toasty.success(DashboardActivity.this, "Offer Installation Successful !!", Toasty.LENGTH_SHORT).show();
        }
    }

    private void onError(VolleyError error, String tag) {
        final int statusCode = error.networkResponse.statusCode;

        if (tag.equals("O18")) {
            onO18(o18Url);
        } else if (HttpURLConnection.HTTP_MOVED_PERM == statusCode || HttpURLConnection.HTTP_MOVED_TEMP == statusCode || HttpURLConnection.HTTP_SEE_OTHER == statusCode) {
            if (error.networkResponse.headers != null) {
                onRedirect(error, tag);
            } else {
                Log.e(TAG, "onError : No header found");
            }
        }
    }

    private void onNetworkResponse(NetworkResponse response, String tag) {
        String html = new String(response.data);
        FindRedirectUrl(html, tag);
    }

    private void onRedirect(VolleyError error, String tag) {
        if (error.networkResponse.headers != null) {
            if (error.networkResponse.headers.get("Location") != null) {
                String url = error.networkResponse.headers.get("Location");
                if (url != null) {
                    OpenLocation(url, tag);
                }
            } else {
                Log.e(TAG, "onRedirect : Redirect URL not found");
            }
        }
    }

    private void FindRedirectUrl(String html, String tag) {
        if (html.contains("http-equiv=\"refresh\"")) {
            String url = ExtractRefreshUrl(html).trim();
            OpenLocation(url, tag);
        } else if (html.contains("location.href")) {
            String url = ExtractLocationUrl(html).trim();
            OpenLocation(url, tag);
        }
    }

    private void OpenLocation(String url, String tag) {
        try {
            URL uri = new URL(url);
            if (uri.getHost().contains("app.adjust.net.in") || uri.getHost().contains("app.adjust.world")) {
                OpenAdjustUrl(url);
            } else if (uri.getHost().contains("o18.click")) {
                OpenO18Url(url);
            } else {
                if (uri.getProtocol().equals("http") || uri.getProtocol().equals("https")) {
                    OpenUrl(url, "REDIRECT");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void OpenAdjustUrl(String url) {
        // OpenUrl(url, "ADJUST");

        // Parse URL
        Uri uri = Uri.parse(url);
        ArrayList<String> urls = new ArrayList<>();
        for (String string : uri.getQueryParameterNames()) {
            if (string.contains("install")) {
                urls.add(uri.getQueryParameter(string).trim());
            } else if (string.contains("event_callback")) {
                urls.add(uri.getQueryParameter(string).trim());
            }
        }

        int sl = 0;
        for (String location : urls) {
            sl++;

            if (sl == urls.size()) {
                OpenUrl(location, "FINAL");
            } else {
                OpenUrl(location, "REDIRECT");
            }
        }
    }

    private void OpenO18Url(String url) {
        o18Url = url.replace("/c?", "/p?") + "&t=i";

        OpenUrl(url, "O18");
    }

    private String ExtractRefreshUrl(String string) {
        Pattern p = Pattern.compile("content=\"0;\\s?(url|URL)=(.*?)\"");
        Matcher m = p.matcher(string);
        if (m.find()) {
            if (m.group(2) != null) {
                return m.group(2);
            }
        }
        return "";
    }

    private String ExtractLocationUrl(String string) {
        Pattern p = Pattern.compile("location.href\\s?=\\s?\"(.*?)\"");
        Matcher m = p.matcher(string);
        if (m.find()) {
            if (m.group(1) != null) {
                return m.group(1);
            }
        }
        return "";
    }

    private void onO18(String url) {
        OpenUrl(url, "FINAL");
    }
}