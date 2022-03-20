package com.cpalead.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class UserPref {

    private static final String PREF_NAME = "user";
    private static SharedPreferences prf;
    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public UserPref(Context ctx) {
        int PRIVATE_MODE = 0;
        prf = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prf.edit();
    }

    public String getAccessToken() {
        return prf.getString(Variables.USER_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        editor.putString(Variables.USER_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getFullName() {
        return prf.getString(Variables.USER_FULL_NAME, "");
    }

    public void setFullName(String fullName) {
        editor.putString(Variables.USER_FULL_NAME, fullName);
        editor.apply();
    }

    public String getMobile() {
        return prf.getString(Variables.USER_MOBILE, "");
    }

    public void setMobile(String mobile) {
        editor.putString(Variables.USER_MOBILE, mobile);
        editor.apply();
    }

    public String getBalance() {
        return prf.getString(Variables.USER_BALANCE, "");
    }

    public void setBalance(String bonusBalance) {
        editor.putString(Variables.USER_BALANCE, bonusBalance);
        editor.apply();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
