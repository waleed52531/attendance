package com.example.attendance.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class DataProccessor {
    public static final String PREFS_NAME = "appname_prefs";
    private static Context context;

    public DataProccessor(Context context2) {
        context = context2;
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key) {
        return context.getSharedPreferences(PREFS_NAME, 0).getInt(key, 0);
    }

    public static void setStr(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStr(String key) {
        return context.getSharedPreferences(PREFS_NAME, 0).getString(key, "not found");
    }

    public static void setBool(String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBool(String key) {
        return context.getSharedPreferences(PREFS_NAME, 0).getBoolean(key, false);
    }
}
