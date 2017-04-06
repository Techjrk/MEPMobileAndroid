package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectDetailBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectDetailViewModel;

import io.realm.Realm;

import static com.lecet.app.R.string.google_api_key;

public class ProjectDetailActivity extends LecetBaseActivity {

    public static final String PROJECT_ID_EXTRA = "com.lecet.app.content.ProjectDetailActivity.project.id.extra";
    public static final String PROJECT_VALUE_EXTRA = "com.lecet.app.content.ProjectDetailActivity.project.value.extra";

    private ProjectDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProjectDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail);

        long projectId   = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);
        double bidAmount = getIntent().getDoubleExtra(PROJECT_VALUE_EXTRA, -1);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new ProjectDetailViewModel(this, projectId, bidAmount, getResources().getString(google_api_key), projectDomain);

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
