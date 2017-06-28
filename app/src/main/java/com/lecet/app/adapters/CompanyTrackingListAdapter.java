package com.lecet.app.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ListItemCompanyTrackingViewModel;
import com.lecet.app.viewmodel.TrackingListItem;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * File: CompanyTrackingListAdapter Created: 12/6/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class CompanyTrackingListAdapter extends TrackingListAdapter<RealmResults<Company>> {


    public CompanyTrackingListAdapter(RealmResults<Company> data, AppCompatActivity appCompatActivity) {
        super(data, appCompatActivity);
    }

    @Override
    public TrackingListItem viewModelForPosition(Context context, String apiKey, int position, boolean showUpdates) {

        return new ListItemCompanyTrackingViewModel(context, new ProjectDomain(LecetClient.getInstance(),
                LecetSharedPreferenceUtil.getInstance(getAppCompatActivity()),
                Realm.getDefaultInstance()), getData().get(position), apiKey, showUpdates);
    }
}
