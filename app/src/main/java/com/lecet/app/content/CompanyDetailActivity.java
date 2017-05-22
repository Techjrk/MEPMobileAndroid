package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityCompanyDetailBinding;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.CompanyDetailViewModel;

import io.realm.Realm;

public class CompanyDetailActivity extends LecetBaseActivity {

    public static final String COMPANY_ID_EXTRA = "com.lecet.app.content.CompanyDetailActivity.company.id.extra";

    private CompanyDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompanyDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_company_detail);

        long companyId = getIntent().getLongExtra(COMPANY_ID_EXTRA, -1);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        CompanyDomain companyDomain = new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new CompanyDetailViewModel(this, companyId, companyDomain, projectDomain);

        binding.setViewModel(viewModel);
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getCompanyDetail();
    }

    @Override
    public void onPause() {
        super.onPause();

        viewModel.cancelGetCompanyDetailRequest();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        viewModel.cancelGetCompanyDetailRequest();
    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();
            //View searchBarView = inflater.inflate(R.layout.projects_near_me_search_bar_layout, null);
            //viewModel.setToolbar(searchBarView);
            //  actionBar.setCustomView(searchBarView);
            //actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }
}
