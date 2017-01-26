package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.CompanyAssociatedProjectsViewModel;

import io.realm.Realm;

public class CompanyAssociatedProjectsActivity extends NavigationBaseActivity {

    public static final String COMPANY_ID_EXTRA = "com.lecet.app.content.CompanyAssociatedProjectsActivity.company.id.extra";

    private CompanyAssociatedProjectsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_associated_projects);

        long companyId = getIntent().getLongExtra(COMPANY_ID_EXTRA, -1);

        CompanyDomain companyDomain = new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        Company company = companyDomain.fetchCompany(companyId).first();

        viewModel = new CompanyAssociatedProjectsViewModel(this, company);

        setupToolbar(company.getName(), company.getProjects().size());
    }

    private void setupToolbar(String title, int listItemSize) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);

            // subtitle, handle plural or singular
            String subTitle = getString(R.string.associated_projects) + " " + listItemSize;

            viewModel.setToolbar(tb, title, subTitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
