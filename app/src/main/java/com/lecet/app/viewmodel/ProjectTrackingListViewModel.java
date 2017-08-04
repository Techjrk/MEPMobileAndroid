package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectTrackingListAdapter;
import com.lecet.app.adapters.TrackingListAdapter;
import com.lecet.app.content.ModifyProjectTrackingListActivity;
import com.lecet.app.content.ModifyTrackingListActivity;
import com.lecet.app.content.TrackingListActivity;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;

import java.util.List;

import io.realm.Realm;
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
    private int selectedSort;

    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, ProjectDomain projectDomain, TrackingListDomain trackingListDomain) {
        super(appCompatActivity, listItemId);

        this.projectDomain = projectDomain;
        this.trackingListDomain = trackingListDomain;
    }

    /**
     * DATA
     **/

    public void getProjectTrackingListUpdates(final long projectTrackingListId) {

        showProgressDialog();

        trackingListDomain.getProjectTrackingListDetails(projectTrackingListId, new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    List<Project> data = response.body();

                    if (data.size() != 1) {
                        updateToolbarSubTitle(data.size(), getAppCompatActivity().getResources().getString(R.string.projects));
                    } else {
                        updateToolbarSubTitle(data.size(), getAppCompatActivity().getResources().getString(R.string.project));
                    }

                    trackingListDomain.asyncCopyProjectTrackingListUpdatesToRealm(projectTrackingListId, data, new Realm.Transaction.OnSuccess() {

                        @Override
                        public void onSuccess() {

                            initProjectTrackingList(projectTrackingListId);
                            dismissProgressDialog();
                        }

                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            dismissProgressDialog();
                            showCancelAlertDialog(getAppCompatActivity().getString(R.string.app_name), error.getMessage());
                        }
                    });

                } else {

                    dismissProgressDialog();
                    showCancelAlertDialog(getAppCompatActivity().getString(R.string.app_name), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                dismissProgressDialog();
                showCancelAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    private void initProjectTrackingList(long trackingListId) {

        ProjectTrackingList projectList = trackingListDomain.fetchProjectTrackingList(trackingListId);
        RealmList<Project> projects = projectList.getProjects();

        selectedSort = SORT_BID_DATE;
        setAdapterData(projects.sort("bidDate", Sort.DESCENDING));
        getListAdapter().notifyDataSetChanged();
    }

    private void updateProjectTrackingList(long trackingListId) {

        ProjectTrackingList projectList = trackingListDomain.fetchProjectTrackingList(trackingListId);

        RealmList<Project> projects = projectList.getProjects();
        setAdapterData(projects.sort(filter, selectedSort == SORT_VALUE_LOW ? Sort.ASCENDING : Sort.DESCENDING));
        getListAdapter().notifyDataSetChanged();
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
                selectedSort = SORT_BID_DATE;
                filter = "bidDate";
                break;
            case SORT_DATE_ADDED:
                selectedSort = SORT_DATE_ADDED;
                filter = "firstPublishDate";
                break;
            case SORT_LAST_UPDATE:
                selectedSort = SORT_LAST_UPDATE;
                filter = "lastPublishDate";
                break;
            case SORT_VALUE_HIGH:
                selectedSort = SORT_VALUE_HIGH;
                filter = "estLow";
                break;
            case SORT_VALUE_LOW:
                selectedSort = SORT_VALUE_LOW;
                filter = "estLow";
                sort = Sort.ASCENDING;
                break;
            default:
                selectedSort = SORT_BID_DATE;
                filter = "bidDate";
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
        @ModifyTrackingListViewModel.TrackingSort int sort = getSortType(selectedSort);

        Intent intent = ModifyTrackingListActivity.intentForResult(getAppCompatActivity(), ModifyProjectTrackingListActivity.class, listItemId, listItemTitle, listItemSize, sort);
        getAppCompatActivity().startActivityForResult(intent, TrackingListViewModel.MODIFY_TRACKING_LIST_REQUEST_CODE);
    }

    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TrackingListViewModel.MODIFY_TRACKING_LIST_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                if (data != null && data.getBooleanExtra(RESULT_EXTRA_ITEMS_EDITED, false)) {

                    setSort(data.getIntExtra(ProjectTrackingListViewModel.RESULT_EXTRA_SELECTED_SORT, -1));
                    updateProjectTrackingList(getListItemId());
                }
            }
        }
    }

    private
    @ModifyTrackingListViewModel.TrackingSort
    int getSortType(int type) {

        switch (type) {
            case SORT_BID_DATE:
                return ModifyTrackingListViewModel.SORT_BID_DATE;
            case SORT_DATE_ADDED:
                return ModifyTrackingListViewModel.SORT_DATE_ADDED;
            case SORT_LAST_UPDATE:
                return ModifyTrackingListViewModel.SORT_LAST_UPDATE;
            case SORT_VALUE_HIGH:
                return ModifyTrackingListViewModel.SORT_VALUE_HIGH;
            case SORT_VALUE_LOW:
                return ModifyTrackingListViewModel.SORT_VALUE_LOW;
            default:
                return ModifyTrackingListViewModel.SORT_BID_DATE;
        }
    }

    private void setSort(int sortType) {

        switch (sortType) {
            case SORT_BID_DATE:
                selectedSort = SORT_BID_DATE;
                filter = "bidDate";
                break;
            case SORT_DATE_ADDED:
                selectedSort = SORT_DATE_ADDED;
                filter = "firstPublishDate";
                break;
            case SORT_LAST_UPDATE:
                selectedSort = SORT_LAST_UPDATE;
                filter = "lastPublishDate";
                break;
            case SORT_VALUE_HIGH:
                selectedSort = SORT_VALUE_HIGH;
                filter = "estLow";
                break;
            case SORT_VALUE_LOW:
                selectedSort = SORT_VALUE_LOW;
                filter = "estLow";
                break;
            default:
                selectedSort = SORT_BID_DATE;
                filter = "bidDate";
                break;
        }

    }

}
