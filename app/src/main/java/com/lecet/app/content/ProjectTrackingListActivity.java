package com.lecet.app.content;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.ProjectTrackingListViewModel;
import com.lecet.app.viewmodel.TrackingListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * File: ProjectTrackingListActivity Created: 12/6/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectTrackingListActivity extends TrackingListActivity<ProjectTrackingListViewModel> {

    @Override
    protected void onResume() {
        super.onResume();

        getViewModel().getProjectTrackingListUpdates(getListItemId());
    }

    @Override
    public ProjectTrackingListViewModel buildViewModel(long listItemId) {

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance(), new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

            }
        }, projectDomain);

        return new ProjectTrackingListViewModel(this, listItemId, projectDomain, trackingListDomain);
    }

    @Override
    public String getActionBarSubtitle(int dataSize) {

        // subtitle, handle plural or singular
        StringBuilder subtitleSb = new StringBuilder();
        subtitleSb.append(dataSize);
        subtitleSb.append(" ");

        if (dataSize != 1) {
            subtitleSb.append(getResources().getString(R.string.projects));
        } else subtitleSb.append(getResources().getString(R.string.project));

        return subtitleSb.toString();
    }

}


