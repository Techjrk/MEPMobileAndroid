package com.lecet.app.content;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectImageChooserBinding;
import com.lecet.app.viewmodel.ProjectImageChooserViewModel;
import com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel;
import com.lecet.app.viewmodel.ProjectSelectLibraryPhotoViewModel;
import com.lecet.app.viewmodel.ProjectTakeCameraPhotoViewModel;
import com.lecet.app.viewmodel.ProjectTakeCameraPhotoViewModelApi21;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectImageChooserActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectImageChooserAct";

    public static final String PROJECT_REPLACE_IMAGE_EXTRA = "com.lecet.app.content.ProjectImageChooserActivity.extra.replace.image";

    private ProjectImageChooserViewModel viewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean replaceImage = false;
    private ProjectTakeCameraPhotoFragment takePhotoFragment;
    private ProjectTakeCameraPhotoFragmentAPI21 takePhotoFragmentAPI21;
    private ProjectSelectLibraryPhotoFragment selectPhotoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        replaceImage = extras.getBoolean(PROJECT_REPLACE_IMAGE_EXTRA);

        Log.d(TAG, "onCreate: replaceImage: " + replaceImage);

        setupBinding();
        setupFragments();
        setUpViewPager();
        setUpTabLayout();
    }

    private void setupBinding() {
        ActivityProjectImageChooserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_image_chooser);
        viewModel = new ProjectImageChooserViewModel(this, replaceImage);
        binding.setViewModel(viewModel);
    }

    private void setupFragments() {
        if(Build.VERSION.SDK_INT >= 21) {
            takePhotoFragmentAPI21 = ProjectTakeCameraPhotoFragmentAPI21.newInstance();
        }
        else {
            takePhotoFragment = ProjectTakeCameraPhotoFragment.newInstance();
        }
        selectPhotoFragment = ProjectSelectLibraryPhotoFragment.newInstance();
    }

    private void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_take_photo);
        setupViewPagerAdapter(viewPager);
    }

    private void setUpTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_take_photo);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                handleTabSelection(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                handleTabSelection(tab);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    private void handleTabSelection(TabLayout.Tab tab) {
        // take photo
        if(tab.getText() != null && tab.getText().toString().equals(getResources().getString(R.string.photo))) {
            //takePhotoFragment.initImageChooser();
        }
        // library
        else if(tab.getText() != null && tab.getText().toString().equals(getResources().getString(R.string.library))) {
            if(Build.VERSION.SDK_INT < 21){
                if(takePhotoFragment.viewModel != null) {
                    takePhotoFragment.viewModel.releaseCamera();
                }
            }else{
                if(takePhotoFragmentAPI21.viewModel != null) {
                    takePhotoFragmentAPI21.viewModel.releaseCamera();
                }
            }// release the camera which may have been activated in the Take Photo tab
            selectPhotoFragment.initImageChooser();
        }
    }

    private void setupViewPagerAdapter(ViewPager viewPager) {
        ProjectImageChooserActivity.ViewPagerAdapter adapter = new ProjectImageChooserActivity.ViewPagerAdapter(getSupportFragmentManager());

        if(takePhotoFragment != null) {
            // add the Take Photo fragment
            adapter.addFragment(takePhotoFragment, getResources().getString(R.string.photo));
        }else{
            adapter.addFragment(takePhotoFragmentAPI21, getResources().getString(R.string.photo));
        }

        // add the Select Image from Library fragment
        adapter.addFragment(selectPhotoFragment, getResources().getString(R.string.library));

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode);

        if(resultCode == RESULT_OK) {
            if(requestCode == ProjectNotesAndUpdatesViewModel.NOTE_REQUEST_CODE){

            }else if(requestCode == ProjectSelectLibraryPhotoViewModel.REQUEST_CODE_GALLERY_IMAGE){
                setResult(RESULT_OK);
                finish();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Log.d(TAG, "onActivityResult: RESULT_OK, " + resultCode);
            Uri uri = data.getData();
            Log.d(TAG, "onActivityResult: uri: " + uri);
            viewModel.setBitmapFromUri(uri);
        }
        else if (resultCode == RESULT_CANCELED) {
            tabLayout.getTabAt(0).select();//Return to cameraTab
            Log.d(TAG, "onActivityResult: RESULT_CANCELED, " + resultCode);
        }
        else Log.e(TAG, "onActivityResult: WARNING: RESULT CODE NOT SUPPORTED: " + resultCode);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
