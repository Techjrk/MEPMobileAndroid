package com.lecet.app.adapters;

import android.support.v7.app.AppCompatActivity;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ListItemProjectTrackingViewModel;
import com.lecet.app.viewmodel.TrackingListItem;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * File: ProjectTrackingListAdapter Created: 12/6/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectTrackingListAdapter extends TrackingListAdapter<RealmResults<Project>> {


    public ProjectTrackingListAdapter(RealmResults<Project> data, AppCompatActivity appCompatActivity) {
        super(data, appCompatActivity);
    }

    @Override
    public TrackingListItem viewModelForPosition(String mapsApiKey, int position, boolean showUpdates) {

        ListItemProjectTrackingViewModel viewModel = new ListItemProjectTrackingViewModel(new ProjectDomain(LecetClient.getInstance(),
                LecetSharedPreferenceUtil.getInstance(getAppCompatActivity()),
                Realm.getDefaultInstance()), getData().get(position), mapsApiKey, showUpdates);

        return viewModel;
    }

}
