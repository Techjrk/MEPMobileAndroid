package com.lecet.app.content;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.CompanyTrackingListViewModel;
import com.lecet.app.viewmodel.TrackingListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * File: CompanyTrackingListActivity Created: 12/6/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class CompanyTrackingListActivity extends TrackingListActivity {


    @Override
    public TrackingListViewModel buildViewModel(long listItemId) {

        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance(), new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

            }
        }, null);

        return new CompanyTrackingListViewModel(this, listItemId, trackingListDomain, null);
    }
}
