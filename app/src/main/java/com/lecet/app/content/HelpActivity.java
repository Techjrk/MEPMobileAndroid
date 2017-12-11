package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.utility.Log;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.support.SupportActivity;


/**
 * HelpActivity Created by jasonm on 8/17/16.
 * 'Zendesk Note: Don't call your activity "SupportActivity" because it'll conflict with some SDK resource names.'
 */

public class HelpActivity extends LecetBaseActivity {

    private final String TAG = "HelpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        initZendesk();
        showZendesk();
    }

    private void initZendesk() {
        // Support site to view these values: https://lecet.zendesk.com/agent/admin/mobile_sdk

        ZendeskConfig.INSTANCE.init(this, "https://lecet.zendesk.com", "ed763d8b11a15c4a4b1a6b47e3bc9f08bc95426b46095699", "mobile_sdk_client_8d4371dd0f8c080b525d");

        ZendeskConfig.INSTANCE.setCoppaEnabled(true);
        Identity identity = new AnonymousIdentity.Builder().build();
        ZendeskConfig.INSTANCE.setIdentity(identity);
    }

    private void showZendesk() {
        new SupportActivity.Builder().show(HelpActivity.this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    @Override
    public void onKeyboardEditorActionSelected() {

    }

}
