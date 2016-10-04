package com.lecet.app.content;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;

/**
 * MobileCoDetailProjBidsActivity
 * Created by jasonm on 8/15/16.
 */
public class MobileCoDetailProjBidsActivity extends NavigationBaseActivity {

    private final String TAG = "MobCoDetlProjBidsActvy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_mobile_co_detail_proj_bids);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
