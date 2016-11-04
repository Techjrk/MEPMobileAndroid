package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;

/**
 * ProjectTrackingListActivity
 * Created by jasonm on 11/2/16.
 */

public class ProjectTrackingListActivity extends NavigationBaseActivity {

    private final String TAG = "ProjectTrackingListAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_project_tracking_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
