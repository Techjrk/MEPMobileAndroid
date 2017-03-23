package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.CompanyTrackingListAdapter;
import com.lecet.app.adapters.TrackingListAdapter;
import com.lecet.app.content.ModifyCompanyTrackingListActivity;
import com.lecet.app.content.ModifyTrackingListActivity;
import com.lecet.app.content.TrackingListActivity;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.utility.DateUtility;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: CompanyTrackingListViewModel Created: 12/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class CompanyTrackingListViewModel extends TrackingListViewModel<RealmResults<Company>> {

    private static final int SORT_LAST_UPDATE = 0;
    private static final int SORT_ALPHABETICAL = 1;

    private final CompanyDomain companyDomain;
    private final TrackingListDomain trackingListDomain;

    private int selectedSort;
    private String filter;

    public CompanyTrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, TrackingListDomain trackingListDomain, CompanyDomain companyDomain) {
        super(appCompatActivity, listItemId);

        this.trackingListDomain = trackingListDomain;
        this.companyDomain = companyDomain;
        getCompanies(listItemId);
    }

    private void getCompanies(long listId) {

        CompanyTrackingList companyList = trackingListDomain.fetchCompanyTrackingList(listId);
        if (companyList != null) {
            RealmList<Company> companies = companyList.getCompanies();

            selectedSort = SORT_ALPHABETICAL;
            setAdapterData(companies.sort("name", Sort.ASCENDING));
            getListAdapter().notifyDataSetChanged();
        }

        getUpdates(listId);
    }

    private void getUpdates(long listId) {

        showProgressDialog(getAppCompatActivity().getString(R.string.updating), "");

        trackingListDomain.getCompanyTrackingListUpdates(listId, DateUtility.addDays(-1), new Callback<List<ActivityUpdate>>() {
            @Override
            public void onResponse(Call<List<ActivityUpdate>> call, Response<List<ActivityUpdate>> response) {

                if (response.isSuccessful()) {

                    dismissProgressDialog();

                    trackingListDomain.copyActivityUpdatesToRealmTransaction(response.body());
                    getListAdapter().notifyDataSetChanged();

                } else {

                    dismissProgressDialog();
                    showCancelAlertDialog(getAppCompatActivity().getString(R.string.app_name), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ActivityUpdate>> call, Throwable t) {

                dismissProgressDialog();
                showCancelAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    private void updateTrackingList(long listId) {

        CompanyTrackingList companyList = trackingListDomain.fetchCompanyTrackingList(listId);
        if (companyList != null) {
            RealmList<Company> companies = companyList.getCompanies();
            setAdapterData(companies.sort(filter, selectedSort == SORT_ALPHABETICAL ? Sort.ASCENDING : Sort.DESCENDING));
            getListAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public String[] sortMenuOptions() {
        return getAppCompatActivity().getResources().getStringArray(R.array.mobile_company_tracking_list_sort_menu);
    }

    @Override
    public void handleSortSelection(int position) {

        Sort sort = Sort.DESCENDING;

        switch (position) {
            case SORT_ALPHABETICAL:
                sort = Sort.ASCENDING;
                selectedSort = SORT_ALPHABETICAL;
                filter = "name";
                break;
            case SORT_LAST_UPDATE:
                selectedSort = SORT_LAST_UPDATE;
                filter = "updatedAt";
                break;
            default:
                filter = "name";
                break;
        }

        RealmResults<Company> sorted = getAdapterData().sort(filter, sort);
        setAdapterData(sorted);
        getListAdapter().notifyDataSetChanged();
    }

    @Override
    public TrackingListAdapter recyclerViewAdapter() {
        return new CompanyTrackingListAdapter(getAdapterData(), getAppCompatActivity());
    }

    @Override
    public void handleEditMode() {

        long listItemId = getAppCompatActivity().getIntent().getLongExtra(TrackingListActivity.PROJECT_LIST_ITEM_ID, -1);
        String listItemTitle = getAppCompatActivity().getIntent().getStringExtra(TrackingListActivity.PROJECT_LIST_ITEM_TITLE);
        int listItemSize = getAppCompatActivity().getIntent().getIntExtra(TrackingListActivity.PROJECT_LIST_ITEM_SIZE, 0);
        @ModifyTrackingListViewModel.TrackingSort int sort = getSortType(selectedSort);

        Intent intent = ModifyTrackingListActivity.intentForResult(getAppCompatActivity(), ModifyCompanyTrackingListActivity.class, listItemId, listItemTitle, listItemSize, sort);
        getAppCompatActivity().startActivityForResult(intent, TrackingListViewModel.MODIFY_TRACKING_LIST_REQUEST_CODE);
    }

    private
    @ModifyTrackingListViewModel.TrackingSort
    int getSortType(int type) {

        switch (type) {
            case SORT_ALPHABETICAL:
                return ModifyTrackingListViewModel.SORT_COMPANY_NAME;
            case SORT_LAST_UPDATE:
                return ModifyTrackingListViewModel.SORT_COMPANY_UPDATED;
            default:
                return ModifyTrackingListViewModel.SORT_COMPANY_NAME;
        }
    }

    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TrackingListViewModel.MODIFY_TRACKING_LIST_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                if (data != null && data.getBooleanExtra(RESULT_EXTRA_ITEMS_EDITED, false)) {

                    setSort(data.getIntExtra(ProjectTrackingListViewModel.RESULT_EXTRA_SELECTED_SORT, -1));
                    updateTrackingList(getListItemId());
                }
            }
        }
    }

    private void setSort(int sortType) {

        switch (sortType) {
            case SORT_ALPHABETICAL:
                selectedSort = SORT_ALPHABETICAL;
                filter = "name";
                break;
            case SORT_LAST_UPDATE:
                selectedSort = SORT_LAST_UPDATE;
                filter = "updatedAt";
                break;
            default:
                filter = "name";
                break;
        }
    }
}
