package com.lecet.app.content;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;

/**
 * MobileProjTrackingListActivity
 * Created by jasonm on 8/15/16.
 */

public class MobileProjTrackingListActivity extends NavigationBaseActivity {

    private final String TAG = "MobileProjTrackListAtvy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_mobile_proj_tracking_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
