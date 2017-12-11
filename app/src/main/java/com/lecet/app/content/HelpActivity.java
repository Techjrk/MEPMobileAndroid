package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivitySettingsBinding;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.SettingsViewModel;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.support.SupportActivity;

import io.realm.Realm;

/**
 * HelpActivity Created by jasonm on 8/17/16.
 * 'Zendesk Note: Don't call your activity "SupportActivity" because it'll conflict with some SDK resource names.'
 */

public class HelpActivity extends LecetBaseActivity {

    private final String TAG = "HelpActivity";

//    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        initZendesk();

        Button mHelpButton;
        mHelpButton = (Button) findViewById(R.id.help_button);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZendesk();
            }
        });

        //showZendesk();


//        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
//        LecetSharedPreferenceUtil pref = LecetSharedPreferenceUtil.getInstance(this);
//        viewModel = new SettingsViewModel(this, new UserDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance()), pref);
//        binding.setViewModel(viewModel);
//        setupToolbar(viewModel, getString(R.string.title_activity_settings), "");
    }

    /*private void setupToolbar(SettingsViewModel viewModel, String title, String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_save, null);
            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);

            viewModel.setToolbar(tb, title, subTitle);
        }
    }*/


    private void initZendesk() {
        Log.d(TAG, "initZendesk");

        // Support site to view these values: https://lecet.zendesk.com/agent/admin/mobile_sdk

        ZendeskConfig.INSTANCE.init(this, "https://lecet.zendesk.com", "ed763d8b11a15c4a4b1a6b47e3bc9f08bc95426b46095699", "mobile_sdk_client_8d4371dd0f8c080b525d");

        ZendeskConfig.INSTANCE.setCoppaEnabled(true);
        Identity identity = new AnonymousIdentity.Builder().build();
        ZendeskConfig.INSTANCE.setIdentity(identity);
    }

    private void showZendesk() {
        Log.d(TAG, "showZendesk");
        new SupportActivity.Builder().show(HelpActivity.this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    @Override
    public void onKeyboardEditorActionSelected() {


    }
}
