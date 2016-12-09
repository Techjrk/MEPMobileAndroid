package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;

import com.lecet.app.adapters.ModifyCompanyListAdapter;
import com.lecet.app.adapters.ModifyListAdapter;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToCompanyListAdapter;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.MTMMenuCallback;

import io.realm.RealmResults;

/**
 * File: ModifyCompanyTrackingListViewModel Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ModifyCompanyTrackingListViewModel extends ModifyTrackingListViewModel<CompanyTrackingList, Company> {

    private static final int TYPE_LAST_UPDATE = 0;
    private static final int TYPE_ALPHABETICAL = 1;

    private TrackingListDomain trackingListDomain;

    public ModifyCompanyTrackingListViewModel(AppCompatActivity appCompatActivity, CompanyTrackingList trackingList, @TrackingSort int sortBy, TrackingListDomain trackingListDomain) {
        super(appCompatActivity, trackingList, sortBy);

        this.trackingListDomain = trackingListDomain;
    }

    @Override
    public RealmResults<Company> getData(CompanyTrackingList trackingList, @TrackingSort int sortBy) {
        return filterRealmOrderedCollection(trackingList.getCompanies(), sortBy);
    }

    @Override
    public int getSortBySelectedPosition(int position) {

        switch (position) {
            case TYPE_LAST_UPDATE:
                return SORT_COMPANY_UPDATED;
            case TYPE_ALPHABETICAL:
                return SORT_COMPANY_NAME;
            default:
                return SORT_COMPANY_UPDATED;
        }
    }

    @Override
    public ModifyListAdapter getListAdapter(AppCompatActivity appCompatActivity, RealmResults<Company> dataItems) {
        return new ModifyCompanyListAdapter(appCompatActivity, dataItems);
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MTMMenuCallback callback, RealmResults<CompanyTrackingList> lists) {
        return new MoveToCompanyListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public RealmResults<CompanyTrackingList> getUserTrackingListsExcludingCurrentList(CompanyTrackingList currentTrackingList) {
        return trackingListDomain.fetchCompanyTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }
}
