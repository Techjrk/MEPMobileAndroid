package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectTakePhotoBinding;
import com.lecet.app.viewmodel.ProjectDetailTakePhotoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectDetailTakePhotoActivity extends LecetBaseActivity {

    private ProjectDetailTakePhotoViewModel viewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBinding();
        setUpViewPager();
        setUpTabLayout();
    }

    private void setupBinding() {
        ActivityProjectTakePhotoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_take_photo);
        viewModel = new ProjectDetailTakePhotoViewModel();
        binding.setViewModel(viewModel);
    }

    private void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_take_photo);
        setupViewPagerAdapter(viewPager);
    }

    private void setUpTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_take_photo);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPagerAdapter(ViewPager viewPager) {
        ProjectDetailTakePhotoActivity.ViewPagerAdapter adapter = new ProjectDetailTakePhotoActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProjectTakePhotoFragment(), "Photo");  //TODO - move to strings.xml
        adapter.addFragment(new ProjectSelectPhotoFragment(), "Library");
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

    }

}
