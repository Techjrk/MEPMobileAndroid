package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;

import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.utility.DateUtility;

import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: CompanyTrackingListViewModel Created: 12/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class CompanyTrackingListViewModel extends TrackingListViewModel {

    private final CompanyDomain companyDomain;
    private final TrackingListDomain trackingListDomain;

    public CompanyTrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, TrackingListDomain trackingListDomain, CompanyDomain companyDomain) {
        super(appCompatActivity, listItemId, LIST_TYPE_COMPANY);

        this.trackingListDomain = trackingListDomain;
        this.companyDomain = companyDomain;
        getCompanies(listItemId);
    }

    private void getCompanies(long listId) {

        CompanyTrackingList companyList = trackingListDomain.fetchCompanyTrackingList(listId);
        if (companyList != null) {
            RealmList<Company> companies = companyList.getCompanies();
            Company[] data = companies != null ? companies.toArray(new Company[companies.size()]) : new Company[0];

            getAdapterData().addAll(Arrays.asList(data));
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
}
