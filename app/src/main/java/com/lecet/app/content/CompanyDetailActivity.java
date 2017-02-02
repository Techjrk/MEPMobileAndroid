package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
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
        setContentView(R.layout.activity_company_detail);

        long companyId = getIntent().getLongExtra(COMPANY_ID_EXTRA, -1);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        CompanyDomain companyDomain = new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new CompanyDetailViewModel(this, companyId, companyDomain, projectDomain);
        viewModel.getCompanyDetail();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        viewModel.cancelGetCompanyDetailRequest();
    }

}
