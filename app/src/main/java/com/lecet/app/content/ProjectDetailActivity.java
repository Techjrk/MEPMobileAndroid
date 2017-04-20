package com.lecet.app.content;

import android.content.Intent;
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
import com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel;

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
        viewModel.getProjectDetail();

        binding.setViewModel(viewModel);

        // set up ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager_project_detail);
        setupViewPager(viewPager, projectId, projectDomain);

        // set up TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_project_detail);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ProjectNotesAndUpdatesViewModel.NOTE_REQUEST_CODE) {
            viewModel.getAdditionalNotes(true);
            // TODO - conditions as per notes above
            viewModel.getAdditionalImages(true);
            Log.d("ProjectDetailActivity", "Result Success");
        }

    }

    private void setupViewPager(ViewPager viewPager, long projectId, ProjectDomain projectDomain) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProjectLocationFragment.newInstance(projectId), getResources().getString(R.string.location_info));
        adapter.addFragment(ProjectNotesAndUpdatesFragment.newInstance(projectId, projectDomain), getResources().getString(R.string.notes_and_updates));
        viewPager.setAdapter(adapter);
    }

    /**
     * Inner ViewPagerAdapter class
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
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
