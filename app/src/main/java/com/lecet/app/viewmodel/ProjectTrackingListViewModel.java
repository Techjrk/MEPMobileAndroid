package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;

import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectTrackingListViewModel Created: 11/29/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectTrackingListViewModel extends TrackingListViewModel {

    private static final String TAG = "ProjectTrackingListVM";

    private final TrackingListDomain trackingListDomain;
    private final ProjectDomain projectDomain;

    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, ProjectDomain projectDomain, TrackingListDomain trackingListDomain) {
        super(appCompatActivity, listItemId, LIST_TYPE_PROJECT);

        this.projectDomain = projectDomain;
        this.trackingListDomain = trackingListDomain;
        getProjectTrackingListUpdates(listItemId);
    }

    /**
     * DATA
     **/

    public void getProjectTrackingListUpdates(final long projectTrackingListId) {

        trackingListDomain.getProjectTrackingListDetails(projectTrackingListId, new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    List<Project> data = response.body();

                    projectDomain.copyToRealmTransaction(data);

                    getProjectTrackingList(projectTrackingListId);

                } else {

                    Log.d(TAG, "Response Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                Log.d(TAG, t.toString());
            }
        });
    }

    private void getProjectTrackingList(long trackingListId) {

        getAdapterData().clear();

        ProjectTrackingList projectList = trackingListDomain.fetchProjectTrackingList(trackingListId);

        if (projectList != null) {
            RealmList<Project> projects = projectList.getProjects();
            Project[] data = projects != null ? projects.toArray(new Project[projects.size()]) : new Project[0];

            getAdapterData().addAll(Arrays.asList(data));
            getListAdapter().notifyDataSetChanged();
        }
    }
}
