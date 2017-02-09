package com.lecet.app.content;

import android.support.v7.app.AppCompatActivity;

import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.ModifyCompanyTrackingListViewModel;
import com.lecet.app.viewmodel.ModifyTrackingListViewModel;

/**
 * File: ModifyCompanyTrackingList Created: 12/8/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ModifyCompanyTrackingListActivity extends ModifyTrackingListActivity<CompanyTrackingList> {

    @Override
    public CompanyTrackingList getTrackingList(TrackingListDomain trackingListDomain, long listItemId) {
        return trackingListDomain.fetchCompanyTrackingList(listItemId);
    }

    @Override
    public ModifyTrackingListViewModel getModifyListViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, CompanyTrackingList trackingList, @ModifyTrackingListViewModel.TrackingSort int sort) {
        return new ModifyCompanyTrackingListViewModel(appCompatActivity, trackingList, sort, trackingListDomain);
    }
}
