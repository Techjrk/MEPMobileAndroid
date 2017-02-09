package com.lecet.app.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * File: LecetSharedPreferenceUtil Created: 8/24/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class LecetSharedPreferenceUtil {

    private static final String NAME = "AppSharedPreference";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ID = "id";
    private static final String NOTIFICATIONS = "notification_settings";


    private static LecetSharedPreferenceUtil mInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static LecetSharedPreferenceUtil getInstance(Context context) {
        if (!(mInstance instanceof LecetSharedPreferenceUtil)) {
            mInstance = new LecetSharedPreferenceUtil(context);
        }
        return mInstance;
    }

    private LecetSharedPreferenceUtil(Context context) {
        initSharePreference(context);
    }

    private void initSharePreference(Context context) {
        if (!(mSharedPreferences instanceof SharedPreferences)) {
            mSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }

        if (!(mEditor instanceof SharedPreferences.Editor)) {
            mEditor = mSharedPreferences.edit();
        }
    }

    private String getStringPreferences(String name) {
        return mSharedPreferences.getString(name, "");
    }

    private void putStringPreferences(String name, String value) {

        Log.i(name, value);
        mEditor.putString(name, value).apply();
    }

    private Boolean getBooleanPreferences(String name) {
        return mSharedPreferences.getBoolean(name, false);
    }

    private void putBooleanPreferences(String name, Boolean value) {
        mEditor.putBoolean(name, value).apply();
    }

    private void putLongPreferences(String name, long value) {
        mEditor.putLong(name, value).apply();
    }

    private long getLongPreferences(String name) { return mSharedPreferences.getLong(name, -1);}

    public void setAccessToken(String value) {
        putStringPreferences(ACCESS_TOKEN, value);
    }

    public String getAccessToken() {
        return getStringPreferences(ACCESS_TOKEN);
    }

    public void setId(long value) {
        putLongPreferences(ID, value);
    }

    public long getId() {
        return getLongPreferences(ID);
    }

    public void setNotificationSetting(boolean value) {
        putBooleanPreferences(NOTIFICATIONS, value);
    }

    public Boolean getNotificationsSetting() {
        return getBooleanPreferences(NOTIFICATIONS);
    }

    public void clearPreferences() {
        mEditor.clear().commit();
    }
}
