package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectListRecyclerViewAdapter;
import com.lecet.app.domain.ProjectDomain;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * File: ProjectTrackingListViewModel
 * Created: 11/2/16
 * Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProjectTrackingListViewModel extends BaseObservable {

    private final ProjectDomain projectDomain;
    private final AppCompatActivity appCompatActivity;
    private ProjectListRecyclerViewAdapter projectListAdapter;
    private List<RealmObject> adapterData;


    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, ProjectDomain projectDomain) {

        this.appCompatActivity = appCompatActivity;
        this.projectDomain = projectDomain;

        initializeAdapter();
    }

    /**
     * Adapter Data Management
     **/

    private void initializeAdapter() {

        adapterData = new ArrayList<>();

        RecyclerView recyclerView = getProjectRecyclerView(R.id.project_tracking_list_view);
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

}
