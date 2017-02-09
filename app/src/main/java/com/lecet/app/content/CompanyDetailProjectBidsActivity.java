package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;

/**
 * CompanyDetailProjectBidsActivity
 * Created by jasonm on 8/15/16.
 */
public class CompanyDetailProjectBidsActivity extends LecetBaseActivity {

    private final String TAG = "CompanyDetailPrjBidsAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_company_detail_project_bids);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
