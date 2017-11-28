package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivitySettingsBinding;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.viewmodel.SettingsViewModel;

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    @Override
    public void onKeyboardEditorActionSelected() {


    }
}
