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
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectsBiddersViewModel;

import io.realm.Realm;

public class ProjectBiddersActivity extends LecetBaseActivity {

    public static final String PROJECT_ID_EXTRA = "com.lecet.app.content.ProjectBiddersActivity.project.id.extra";

    private ProjectsBiddersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_bidders);

        long projectID = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        Project project = projectDomain.fetchProjectById(projectID);
        viewModel = new ProjectsBiddersViewModel(this, projectDomain, projectID);

        setupToolbar(project.getTitle(), getString(R.string.project_bidders));
    }

    private void setupToolbar(String title, String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);

            viewModel.setToolbar(tb, title, subTitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
