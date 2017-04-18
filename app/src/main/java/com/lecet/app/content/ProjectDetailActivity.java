package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectDetailBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.lecet.app.R.string.google_api_key;

public class ProjectDetailActivity extends LecetBaseActivity {

    public static final String PROJECT_ID_EXTRA = "com.lecet.app.content.ProjectDetailActivity.project.id.extra";

    private ProjectDetailViewModel viewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ProjectDetailActivity", "onCreate");

        ActivityProjectDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail);

        long projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new ProjectDetailViewModel(this, projectId, getResources().getString(google_api_key), projectDomain);

        binding.setViewModel(viewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getProjectDetail();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.cancelGetProjectDetailRequest();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

        if (isConnected) {

            // Check if we need to retry the network request to populate the screen

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        viewModel.cancelGetProjectDetailRequest();
    }
}
