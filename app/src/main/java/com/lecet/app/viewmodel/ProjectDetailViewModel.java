package com.lecet.app.viewmodel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.AddProjectActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectDetailFragment;
import com.lecet.app.content.ProjectNotesAndUpdatesFragment;
import com.lecet.app.contentbase.BaseMapObservableViewModel;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.SimpleLecetDefaultAlert;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * File: ProjectDetailViewModel Created: 11/9/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailViewModel extends BaseMapObservableViewModel implements BaseMapObservableViewModel.OnBaseViewModelMapReady {

    private static final String TAG = "ProjectDetailViewModel";

    private boolean mapReady;
    private long projectId;
    private String title;
    private String address;
    private Activity activity;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;

    private Project project;


    public ProjectDetailViewModel(ProjectDetailActivity activity, long projectId, String mapsApiKey) {
        super(activity, new GoogleMapOptions().rotateGesturesEnabled(false)
                        .rotateGesturesEnabled(false)
                        .scrollGesturesEnabled(false)
                        .tiltGesturesEnabled(false)
                        .zoomControlsEnabled(false)
                        .zoomGesturesEnabled(false),
                R.id.map_container);

        this.activity = activity;
        this.projectId = projectId;
        setListener(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: Inside ProjDetailViewModel");
        if (resultCode == RESULT_OK && requestCode == ProjectNotesAndUpdatesViewModel.NOTE_REQUEST_CODE) {
            ProjectNotesAndUpdatesFragment fragment = (ProjectNotesAndUpdatesFragment) findFragmentByPosition(ViewPagerAdapter.NOTES_POSITION);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* Observable variables */

    @Bindable
    public String getAddress() {
        return address;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public boolean isEditable() {
        if(project == null || activity == null){
            return false;
        }
        return LecetSharedPreferenceUtil.getInstance(activity).getId() == project.getUserId();
    }


    /* Helper */
    public void onProjectReady(Project project) {

        this.project = project;

        if(project.getGeocode() == null){
            SimpleLecetDefaultAlert alert = SimpleLecetDefaultAlert.newInstance(activity , SimpleLecetDefaultAlert.CUSTIOM_DIALOG);
            alert.setMessage("Internal Data Error.");
            alert.show();
        }

        if (mapReady) {

            // If the project does not have dodge number, then we know it is user
            // created.
            if (project.getDodgeNumber() == null) {

                addMarker(R.drawable.ic_user_project, project.getGeocode().toLatLng(), 16);

            } else {

                addMarker(R.drawable.ic_yellow_marker, project.getGeocode().toLatLng(), 16);
            }

        }

        setTitle(project.getTitle());
        setAddress(getProjectAddress(project));
        notifyChange();
    }

    public void onNotesReady(int count) {

        if (mapReady) {
            clearMap();

            // If the notes count is greater than zero, then we can assume there
            // was an update to the project.
            if (count > 0) {

                // If the project does not have dodge number, then we know it is user
                // created.
                if (project.getDodgeNumber() == null) {

                    addMarker(R.drawable.ic_user_project_updates, project.getGeocode().toLatLng(), 16);

                } else {

                    addMarker(R.drawable.ic_dodge_project_updates, project.getGeocode().toLatLng(), 16);
                }

            }
        }
    }

    public String getProjectAddress(Project project) {

        String address = "";

        if (project.getAddress1() != null) {

            address = project.getAddress1();
        }

        if (project.getState() != null) {

            address = address + " " + project.getState();
        }

        return address;
    }

    /* View Management */

    public void setupTabLayoutWithViewPager(TabLayout tabLayout, ViewPager viewPager) {

        this.viewPager = viewPager;

        AppCompatActivity activity = getActivityWeakReference().get();
        adapter = new ViewPagerAdapter(activity, activity.getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        setUpTabLayout(tabLayout, this.viewPager);

    }

    private void setUpTabLayout(TabLayout tabLayout, ViewPager viewPager) {

        this.tabLayout = tabLayout;
        this.tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onMapSetup(GoogleMap googleMap) {

        mapReady = true;
    }

    public Fragment findFragmentByPosition(int position) {
        AppCompatActivity activity = getActivityWeakReference().get();

        return activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":"
        + adapter.getItemId(position));
    }

    public void onEditButtonClick(View view) {
        Log.d(TAG, "onEditButtonClick");

        // pass the projectId to the Add Project Activity for editing
        Intent intent = new Intent(view.getContext(), AddProjectActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, projectId);
        view.getContext().startActivity(intent);
    }


    /**
     * Inner ViewPagerAdapter class
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public static final int DETAIL_POSITION = 0;
        public static final int NOTES_POSITION = 1;

        private static final int ITEM_COUNT = 2;
        private Context context;
        private List<Fragment> fragmentList = new ArrayList<Fragment>();

        public ViewPagerAdapter(Context context, FragmentManager manager) {
            super(manager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ProjectDetailFragment.newInstance(projectId);
                case 1:
                    return ProjectNotesAndUpdatesFragment.newInstance(projectId);
                default:
                    return ProjectDetailFragment.newInstance(projectId);
            }
        }

        @Override
        public int getCount() {
            return ITEM_COUNT;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getResources().getString(R.string.location_info);
                case 1:
                    return context.getResources().getString(R.string.notes_and_updates);
                default:
                    return context.getResources().getString(R.string.notes_and_updates);
            }
        }

    }
}
