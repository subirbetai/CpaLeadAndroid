package com.cpalead.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {

    private static final String PREF_NAME = "app";
    private static SharedPreferences prf;
    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public AppPref(Context ctx) {
        int PRIVATE_MODE = 0;
        prf = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prf.edit();
    }

    public String getRizzle() {
        return prf.getString(Variables.APP_RIZZLE, "");
    }

    public void setRizzle(String rizzle) {
        editor.putString(Variables.APP_RIZZLE, rizzle);
        editor.apply();
    }

    public String getChingari() {
        return prf.getString(Variables.APP_CHINGARI, "");
    }

    public void setChingari(String chingari) {
        editor.putString(Variables.APP_CHINGARI, chingari);
        editor.apply();
    }

    public String getCoindcx() {
        return prf.getString(Variables.APP_COINDCX, "");
    }

    public void setCoindcx(String coindcx) {
        editor.putString(Variables.APP_COINDCX, coindcx);
        editor.apply();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
