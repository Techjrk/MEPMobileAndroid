package com.lecet.app.adapters;

import android.content.Context;

import com.lecet.app.R;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.interfaces.MTMMenuCallback;

import io.realm.RealmResults;

/**
 * File: MoveToProjectListAdapter Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MoveToProjectListAdapter extends MoveToAdapter<ProjectTrackingList> {


    public MoveToProjectListAdapter(Context context, String title, RealmResults<ProjectTrackingList> trackingLists, MTMMenuCallback callback) {
        super(context, title, trackingLists, callback);
    }

    @Override
    public String itemPrimaryDetail(ProjectTrackingList object) {
        return object.getName();
    }

    @Override
    public String itemSecondaryDetail(ProjectTrackingList object) {
        return String.format(getContext().getString(R.string.mtm_menu_number_projects), object.getProjects().size());
    }
}
