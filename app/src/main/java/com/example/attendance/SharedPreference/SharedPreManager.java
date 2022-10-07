package com.example.attendance.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreManager {
    private static final String KEY_ACCOUNT_NO = "accountNo";
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String SHARD_PERFNAME = "myshardperf624";
    private static Context mct;
    private static SharedPreManager minst;

    private SharedPreManager(Context context) {
        mct = context;
    }

    public static synchronized SharedPreManager getInstans(Context context) {
        SharedPreManager sharedPreManager;
        synchronized (SharedPreManager.class) {
            if (minst == null) {
                minst = new SharedPreManager(context);
            }
            sharedPreManager = minst;
        }
        return sharedPreManager;
    }

    public boolean userLogin(String id, String accountNo, String password) {
        SharedPreferences.Editor editor = mct.getSharedPreferences(SHARD_PERFNAME, 0).edit();
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ACCOUNT_NO, accountNo);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
        return true;
    }

    public boolean isLogin() {
        if (mct.getSharedPreferences(SHARD_PERFNAME, 0).getString(KEY_ID, (String) null) != null) {
            return true;
        }
        return false;
    }

    public boolean logout() {
        SharedPreferences.Editor editor = mct.getSharedPreferences(SHARD_PERFNAME, 0).edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getId() {
        return mct.getSharedPreferences(SHARD_PERFNAME, 0).getString(KEY_ID, (String) null);
    }

    public String getaccountNo() {
        return mct.getSharedPreferences(SHARD_PERFNAME, 0).getString(KEY_ACCOUNT_NO, (String) null);
    }

    public String getpassword() {
        return mct.getSharedPreferences(SHARD_PERFNAME, 0).getString(KEY_PASSWORD, (String) null);
    }
}
