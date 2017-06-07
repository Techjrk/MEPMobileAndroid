package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectDetailBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectDetailViewModel;

import io.realm.Realm;

import static com.lecet.app.R.string.google_api_key;

public class ProjectDetailActivity extends LecetBaseActivity implements ProjectDetailFragment.ProjectDetailFragmentDataSource,
        ProjectDetailFragment.ProjectDetailFragmentListener, ProjectNotesAndUpdatesFragment.ProjectNotesFragmentDataSource,
        ProjectNotesAndUpdatesFragment.ProjectNotesFragmentListener {

    public static final String PROJECT_ID_EXTRA = "com.lecet.app.content.ProjectDetailActivity.project.id.extra";
    public static final int REQUEST_CODE_HOME = 1116;
    private ProjectDetailViewModel viewModel;
    private ProjectDomain projectDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProjectDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail);

        long projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new ProjectDetailViewModel(this, projectId, getResources().getString(google_api_key));
        binding.setViewModel(viewModel);

        // set up ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_project_detail);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_project_detail);
        viewModel.setupTabLayoutWithViewPager(tabLayout, viewPager);

        setupToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && REQUEST_CODE_HOME == requestCode){
            finish();
        }
        viewModel.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

        if (!isConnected) {

            // Check if we need to retry the network request to populate the screen
            showNetworkAlert();
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }

    @Override
    public ProjectDomain domainDataSource() {
        return projectDomain;
    }

    @Override
    public void onProjectDetailsReceived(Project project) {

        viewModel.onProjectReady(project);
    }


    @Override
    public void onPhotosAndNotesUpdated(int count) {

    }
}
