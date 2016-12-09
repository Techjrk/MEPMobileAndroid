package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.CompanyTrackingListAdapter;
import com.lecet.app.adapters.ModifyCompanyListAdapter;
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

import java.util.Arrays;
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

            setAdapterData(companies.sort("name"));
            getListAdapter().notifyDataSetChanged();
        }

        getUpdates(listId);
    }

    private void getUpdates(long listId) {

        trackingListDomain.getCompanyTrackingListUpdates(listId, DateUtility.addDays(-1), new Callback<List<ActivityUpdate>>() {
            @Override
            public void onResponse(Call<List<ActivityUpdate>> call, Response<List<ActivityUpdate>> response) {

                if (response.isSuccessful()) {

                    trackingListDomain.copyActivityUpdatesToRealmTransaction(response.body());
                    getListAdapter().notifyDataSetChanged();

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<ActivityUpdate>> call, Throwable t) {

            }
        });
    }


    @Override
    public String[] sortMenuOptions() {
        return  getAppCompatActivity().getResources().getStringArray(R.array.mobile_company_tracking_list_sort_menu);
    }

    @Override
    public void handleSortSelection(int position) {

        String filter;
        Sort sort = Sort.DESCENDING;

        switch (position) {
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
        getAppCompatActivity().startActivity(intent);
    }

    private @ModifyTrackingListViewModel.TrackingSort int getSortType(int type) {

        switch (type) {
            case SORT_ALPHABETICAL:
                return ModifyTrackingListViewModel.SORT_COMPANY_NAME;
            case SORT_LAST_UPDATE:
                return ModifyTrackingListViewModel.SORT_COMPANY_UPDATED;
            default:
                return ModifyTrackingListViewModel.SORT_COMPANY_NAME;
        }
    }
}
