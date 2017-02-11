package com.lecet.app.content;

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
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.viewmodel.CompanyContactsViewModel;

import io.realm.Realm;

public class CompanyContactsActivity extends LecetBaseActivity {

    public static final String COMPANY_ID_EXTRA = "com.lecet.app.content.CompanyContactsActivity.company.id.extra";
    public static final String COMPANY_NAME_EXTRA = "com.lecet.app.content.CompanyContactsActivity.company.name.extra";
    public static final String COMPANY_CONTACTS_COUNT_EXTRA = "com.lecet.app.content.CompanyContactsActivity.company.contacts.size.extra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_contacts);

        long companyID = getIntent().getLongExtra(COMPANY_ID_EXTRA, -1);
        int contactsCount = getIntent().getIntExtra(COMPANY_CONTACTS_COUNT_EXTRA, -1);
        String companyName = getIntent().getStringExtra(COMPANY_NAME_EXTRA);
        String subTitle = String.format(getString(R.string.contacts_count), contactsCount);

        CompanyContactsViewModel viewModel = new CompanyContactsViewModel(this, new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance()), companyID);
        setupToolbar(viewModel, companyName, subTitle);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    private void setupToolbar(CompanyContactsViewModel viewModel, String title, String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);

            viewModel.setToolbar(tb, title, subTitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

}
