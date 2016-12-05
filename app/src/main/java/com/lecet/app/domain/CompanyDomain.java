package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * File: CompanyDomain Created: 12/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class CompanyDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public CompanyDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    public RealmResults<ActivityUpdate> fetchCompanyActivityUpdates(long projectId, Date updateMinDate, RealmChangeListener<RealmResults<ActivityUpdate>> listener) {

        RealmResults<ActivityUpdate> result = realm.where(ActivityUpdate.class).equalTo("companyId", projectId).greaterThanOrEqualTo("updatedAt", updateMinDate).findAllAsync();
        result.addChangeListener(listener);

        return result;
    }
}
