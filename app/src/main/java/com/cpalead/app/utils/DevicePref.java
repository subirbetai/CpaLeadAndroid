package com.cpalead.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class DevicePref {

    private static final String PREF_NAME = "device";
    private static SharedPreferences prf;
    private static SharedPreferences.Editor editor;

    String latitude, longitude;
    String userAgent;

    @SuppressLint("CommitPrefEdits")
    public DevicePref(Context ctx) {
        int PRIVATE_MODE = 0;
        prf = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prf.edit();
    }

    public String getDeviceName() {
        return prf.getString(Variables.DEVICE_NAME, "");
    }

    public void setDeviceName(String deviceName) {
        editor.putString(Variables.DEVICE_NAME, deviceName);
        editor.apply();
    }

    public String getDeviceImei() {
        return prf.getString(Variables.DEVICE_IMEI, "");
    }

    public void setDeviceImei(String deviceImei) {
        editor.putString(Variables.DEVICE_IMEI, deviceImei);
        editor.apply();
    }

    public String getDeviceAndroidId() {
        return prf.getString(Variables.DEVICE_ANDROID_ID, "");
    }

    public void setDeviceAndroidId(String deviceAndroidId) {
        editor.putString(Variables.DEVICE_ANDROID_ID, deviceAndroidId);
        editor.apply();
    }

    public String getDeviceAdvertisingId() {
        return prf.getString(Variables.DEVICE_ADVERTISING_ID, "");
    }

    public void setDeviceAdvertisingId(String deviceAdvertisingId) {
        editor.putString(Variables.DEVICE_ADVERTISING_ID, deviceAdvertisingId);
        editor.apply();
    }

    public String getDeviceMacAddress() {
        return prf.getString(Variables.DEVICE_MAC_ADDRESS, "");
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        editor.putString(Variables.DEVICE_MAC_ADDRESS, deviceMacAddress);
        editor.apply();
    }

    public String getDeviceGcmId() {
        return prf.getString(Variables.DEVICE_GCM_ID, "");
    }

    public void setDeviceGcmId(String deviceGcmId) {
        editor.putString(Variables.DEVICE_GCM_ID, deviceGcmId);
        editor.apply();
    }

    public String getDeviceUuid() {
        return prf.getString(Variables.DEVICE_UUID, "");
    }

    public void setDeviceUuid(String deviceUuid) {
        editor.putString(Variables.DEVICE_UUID, deviceUuid);
        editor.apply();
    }

    public String getDeviceSimSerial() {
        return prf.getString(Variables.DEVICE_SIM_SERIAL, "");
    }

    public void setDeviceSimSerial(String deviceSimSerial) {
        editor.putString(Variables.DEVICE_SIM_SERIAL, deviceSimSerial);
        editor.apply();
    }

    public String getDeviceModel() {
        return prf.getString(Variables.DEVICE_MODEL, "");
    }

    public void setDeviceModel(String deviceModel) {
        editor.putString(Variables.DEVICE_MODEL, deviceModel);
        editor.apply();
    }

    public String getDeviceBrand() {
        return prf.getString(Variables.DEVICE_BRAND, "");
    }

    public void setDeviceBrand(String deviceBrand) {
        editor.putString(Variables.DEVICE_BRAND, deviceBrand);
        editor.apply();
    }

    public String getDeviceBuild() {
        return prf.getString(Variables.DEVICE_BUILD, "");
    }

    public void setDeviceBuild(String build) {
        editor.putString(Variables.DEVICE_BUILD, build);
        editor.apply();
    }

    public String getDeviceFingerprint() {
        return prf.getString(Variables.DEVICE_FINGERPRINT, "");
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        editor.putString(Variables.DEVICE_FINGERPRINT, deviceFingerprint);
        editor.apply();
    }

    public String getDeviceVersion() {
        return prf.getString(Variables.DEVICE_VERSION, "");
    }

    public void setDeviceVersion(String deviceVersion) {
        editor.putString(Variables.DEVICE_VERSION, deviceVersion);
        editor.apply();
    }

    public String getDeviceLatitude() {
        return prf.getString(Variables.DEVICE_LATITUDE, "");
    }

    public void setDeviceLatitude(String latitude) {
        editor.putString(Variables.DEVICE_LATITUDE, latitude);
        editor.apply();
    }

    public String getDeviceLongitude() {
        return prf.getString(Variables.DEVICE_LONGITUDE, "");
    }

    public void setDeviceLongitude(String longitude) {
        editor.putString(Variables.DEVICE_LONGITUDE, longitude);
        editor.apply();
    }

    public String getUserAgent() {
        return prf.getString(Variables.DEVICE_USERAGENT, "");
    }

    public void setUserAgent(String userAgent) {
        editor.putString(Variables.DEVICE_USERAGENT, userAgent);
        editor.apply();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
