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
import com.lecet.app.data.models.Company;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityCompanyProjectBidsBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.viewmodel.CompanyProjectBidsViewModel;

import io.realm.Realm;

public class CompanyProjectBidsActivity extends LecetBaseActivity {

    public static final String COMPANY_ID_EXTRA = "com.lecet.app.content.CompanyProjectBidsActivity.company.id.extra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompanyProjectBidsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_company_project_bids);

        long companyId = getIntent().getLongExtra(COMPANY_ID_EXTRA, -1);

        CompanyDomain companyDomain = new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        Company company = companyDomain.fetchCompany(companyId).first();
        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());

        CompanyProjectBidsViewModel viewModel = new CompanyProjectBidsViewModel(this, company, bidDomain);
        binding.setViewModel(viewModel);

        setupToolbar(viewModel, company.getName(), getString(R.string.project_bids));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    private void setupToolbar(CompanyProjectBidsViewModel viewModel, String title, String subtitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);

            viewModel.setToolbar(tb, title, subtitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

}
