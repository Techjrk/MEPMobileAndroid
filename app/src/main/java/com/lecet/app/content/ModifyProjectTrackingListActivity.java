package com.lecet.app.content;

import android.support.v7.app.AppCompatActivity;

import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.ModifyProjectTrackingListViewModel;
import com.lecet.app.viewmodel.ModifyTrackingListViewModel;

/**
 * File: ModifyProjectTrackingList Created: 12/8/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ModifyProjectTrackingListActivity extends ModifyTrackingListActivity<ProjectTrackingList> {

    @Override
    public ProjectTrackingList getTrackingList(TrackingListDomain trackingListDomain, long listItemId) {
        return trackingListDomain.fetchProjectTrackingList(listItemId);
    }

    @Override
    public ModifyTrackingListViewModel getModifyListViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, ProjectTrackingList trackingList, @ModifyTrackingListViewModel.TrackingSort int sort) {
        return new ModifyProjectTrackingListViewModel(appCompatActivity, trackingList, sort, trackingListDomain);
    }
}
