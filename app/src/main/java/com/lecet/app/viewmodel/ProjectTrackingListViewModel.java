package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectTrackingListAdapter;
import com.lecet.app.adapters.TrackingListAdapter;
import com.lecet.app.content.ModifyTrackingListActivity;
import com.lecet.app.content.TrackingListActivity;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
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

    private static final int SORT_BID_DATE = 0;
    private static final int SORT_LAST_UPDATE = 1;
    private static final int SORT_DATE_ADDED = 2;
    private static final int SORT_VALUE_HIGH = 3;
    private static final int SORT_VALUE_LOW = 4;


    private final TrackingListDomain trackingListDomain;
    private final ProjectDomain projectDomain;

    private String filter;

    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, ProjectDomain projectDomain, TrackingListDomain trackingListDomain) {
        super(appCompatActivity, listItemId);

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

        ProjectTrackingList projectList = trackingListDomain.fetchProjectTrackingList(trackingListId);

        if (projectList != null) {
            RealmList<Project> projects = projectList.getProjects();

            setAdapterData(projects.sort("title"));
            getListAdapter().notifyDataSetChanged();
        }
    }


    @Override
    public String[] sortMenuOptions() {

        return getAppCompatActivity().getResources().getStringArray(R.array.mobile_tracking_list_sort_menu);
    }

    @Override
    public void handleSortSelection(int position) {

        Sort sort = Sort.DESCENDING;

        switch (position) {
            case SORT_BID_DATE:
                filter = "bidDate";
                break;
            case SORT_DATE_ADDED:
                filter = "firstPublishDate";
                break;
            case SORT_LAST_UPDATE:
                filter = "lastPublishDate";
                break;
            case SORT_VALUE_HIGH:
                filter = "estLow";
                break;
            case SORT_VALUE_LOW:
                filter = "estLow";
                sort = Sort.ASCENDING;
                break;
            default:
                filter = "title";
                break;
        }

        RealmResults<Project> sorted = getAdapterData().sort(filter, sort);
        setAdapterData(sorted);
        getListAdapter().notifyDataSetChanged();
    }

    @Override
    public TrackingListAdapter recyclerViewAdapter() {
        return new ProjectTrackingListAdapter(getAdapterData(), getAppCompatActivity());
    }

    @Override
    public void handleEditMode() {

        long listItemId = getAppCompatActivity().getIntent().getLongExtra(TrackingListActivity.PROJECT_LIST_ITEM_ID, -1);
        String listItemTitle = getAppCompatActivity().getIntent().getStringExtra(TrackingListActivity.PROJECT_LIST_ITEM_TITLE);
        int listItemSize = getAppCompatActivity().getIntent().getIntExtra(TrackingListActivity.PROJECT_LIST_ITEM_SIZE, 0);
        //ModifyTrackingListActivity.startActivityForResult(getAppCompatActivity(), listItemId, listItemTitle, listItemSize, filter);
    }
}
