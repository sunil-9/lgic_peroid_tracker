package com.example.periodtracker.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "Period";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;
    public static String pushRID = "0";
    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGIN_ID = "LOGIN";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void setPeriodlength(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPeriodlength(String key) {
        return pref.getString(key, "");
    }

    public void setPeriodCycle(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPeriodCycle(String key) {
        return pref.getString(key, "");
    }

    public void setPrefValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPrefValue(String key) {
        return pref.getString(key, "");
    }

}
