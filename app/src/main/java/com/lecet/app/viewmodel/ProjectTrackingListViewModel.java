package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectListRecyclerViewAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * File: ProjectTrackingListViewModel
 * Created: 11/2/16
 * Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProjectTrackingListViewModel extends BaseObservable {

    private final static String TAG = "ProjectTrackingListVM";

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private ProjectListRecyclerViewAdapter projectListAdapter;
    private List<RealmObject> adapterData;
    private RealmResults<Project> projectListResults;
    private TextView titleTextView;
    private TextView subtitleTextView;


    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, ProjectTrackingList projectList, BidDomain bidDomain, ProjectDomain projectDomain) {

        Log.d(TAG, "Constructor");

        this.appCompatActivity = appCompatActivity;
        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;

        initializeAdapter();
        setupAdapterWithProjectList(projectList);
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView    = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);

        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    /**
     * Adapter Data Management
     **/

    private void initializeAdapter() {

        adapterData = new ArrayList<>();

        recyclerView = getProjectRecyclerView(R.id.project_tracking_recycler_view);
        setupRecyclerView(recyclerView);
        projectListAdapter = new ProjectListRecyclerViewAdapter(adapterData, 1);  //TODO - allow for multiple init types, default to 1 for now
        recyclerView.setAdapter(projectListAdapter);
    }

    /**
     * RecyclerView Management
     **/

    private void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getProjectRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) appCompatActivity.findViewById(recyclerView);
    }

    private void setupAdapterWithProjectList(ProjectTrackingList projectTrackingList) {

        RealmList<Project> projects = projectTrackingList.getProjects();
        Project[] data = projects != null ? projects.toArray(new Project[projects.size()]) : new Project[0];

        adapterData.clear();
        adapterData.addAll(Arrays.asList(data));
        projectListAdapter.setAdapterType(1);       //TODO - needed? support multiple types? also see initializeAdapter()
        projectListAdapter.notifyDataSetChanged();
    }

    /*private void setupAdapterWithProjects(RealmResults<Project> realmResults) {

        Project[] data = realmResults != null ? realmResults.toArray(new Project[realmResults.size()]) : new Project[0];
        adapterData.clear();
        adapterData.addAll(Arrays.asList(data));
        projectListAdapter.setAdapterType(1);       //TODO - needed? support multiple types? also see initializeAdapter()
        projectListAdapter.notifyDataSetChanged();
    }*/

}
