package com.lapic.thomas.remote_control_app;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "app_pref_file";
    private static final String IP = "ip";
    private static final String PORT = "port";

    private static SharedPreferences mPref;

    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putIp(String value) {
        mPref.edit().putString(IP, value).apply();
    }

    public String getIp() {
        return mPref.getString(IP,null);
    }

    public void putPort(int value) {
        mPref.edit().putInt(PORT, value).apply();
    }

    public int getPort() {
        return mPref.getInt(PORT,-1);
    }


}
