package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;

/**
 * SettingsActivity
 * Created by jasonm on 8/17/16.
 */

public class SettingsActivity extends NavigationBaseActivity {

    private final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
