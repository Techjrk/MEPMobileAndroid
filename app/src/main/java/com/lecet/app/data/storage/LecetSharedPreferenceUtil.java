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
    private static final String OAUTH_HEADER_NAME = "oAuthHeader";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private static final String ID = "id";
    private static LecetSharedPreferenceUtil mInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String accesToken;

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

    private Integer getIntPreferences(String name) {
        return mSharedPreferences.getInt(name, -1);
    }

    private void putIntPreferences(String name, Integer value) {
        mEditor.putInt(name, value).apply();
    }

    public void setAuthorization(String value) {
        putStringPreferences(OAUTH_HEADER_NAME, value);
    }

    public String getAuthorization() {
        return getStringPreferences(OAUTH_HEADER_NAME);
    }

    public void setAccessToken(String value) {
        putStringPreferences(ACCESS_TOKEN, value);
    }

    public String getAccessToken() {
        return getStringPreferences(ACCESS_TOKEN);
    }

    public void setRefreshToken(String value) {
        putStringPreferences(REFRESH_TOKEN, value);
    }

    public String getRefreshToken() {
        return getStringPreferences(REFRESH_TOKEN);
    }

    public void setId(Integer value) {
        putIntPreferences(ID, value);
    }

    public Integer getId() {
        return getIntPreferences(ID);
    }
}
