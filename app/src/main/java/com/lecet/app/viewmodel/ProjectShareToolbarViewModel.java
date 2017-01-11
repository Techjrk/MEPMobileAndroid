package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToProjectListAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MoveToListCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectShareToolbarViewModel Created: 1/9/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectShareToolbarViewModel extends ShareToolbarViewModel<Project, ProjectTrackingList> {


    public ProjectShareToolbarViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, Project trackedObject) {
        super(appCompatActivity, trackingListDomain, trackedObject);
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<ProjectTrackingList> lists) {
        return new MoveToProjectListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public ProjectTrackingList getAssociatedTrackingList(Project trackedObject) {

        RealmResults<ProjectTrackingList> results = getTrackingListDomain().fetchProjectTrackingListsContainingProject(trackedObject.getId());
        if (results.size() > 0) {

            return results.first();
        }

        return null;
    }

    @Override
    public RealmResults<ProjectTrackingList> getUserTrackingListsExcludingCurrentList(ProjectTrackingList currentTrackingList) {
        return getTrackingListDomain().fetchProjectTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }

    @Override
    public RealmResults<ProjectTrackingList> getAllUserTrackingLists() {
        return getTrackingListDomain().fetchUserProjectTrackingList();
    }

    @Override
    public RealmList<Project> getTrackedItems(ProjectTrackingList trackingList) {
        return trackingList.getProjects();
    }

    @Override
    public void removeTrackedObjectFromTrackingList(long trackingListId, List<Long> trackedIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        getTrackingListDomain().syncProjectTrackingList(trackingListId, trackedIds, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    List<Long> selectedItems = new ArrayList<>();
                    selectedItems.add(getTrackedObject().getId());

                    // Remove items from Tracking list relationship
                    asyncDeleteProjects(getTrackedObject().getId(), selectedItems);

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    @Override
    public void addTrackedObjectToTrackingList(long trackingListId, List<Long> trackedIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        getTrackingListDomain().syncProjectTrackingList(trackingListId, trackedIds, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    dismissProgressDialog();

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    /* Delete */
    private void asyncDeleteProjects(long trackingListId, List<Long> toBeDeletedIds) {

        getTrackingListDomain().deleteProjectsFromTrackingListAsync(trackingListId, toBeDeletedIds, new LecetCallback<ProjectTrackingList>() {
            @Override
            public void onSuccess(ProjectTrackingList result) {

                dismissProgressDialog();
            }

            @Override
            public void onFailure(int code, String message) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), message);
            }
        });
    }
}
