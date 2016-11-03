package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: TrackingListDomain Created: 11/3/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class TrackingListDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;


    public TrackingListDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    // API

    public void getUserProjectTrackingLists(Callback<List<ProjectTrackingList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        long userID = sharedPreferenceUtil.getId();

        String filter = "{\"include\":[\"projects\"]}";

        Call<List<ProjectTrackingList>> call = lecetClient.getTrackingListService().getUserProjectTrackingList(token, userID, filter);
        call.enqueue(callback);
    }

    // Persisted
    public RealmResults<ProjectTrackingList> fetchUserProjectTrackingList() {

        RealmResults<ProjectTrackingList> results = realm.where(ProjectTrackingList.class).findAll();
        return results;
    }

    public ProjectTrackingList copyToRealmTransaction(ProjectTrackingList trackingList) {

        realm.beginTransaction();
        ProjectTrackingList persistedTrackingList = realm.copyToRealmOrUpdate(trackingList);
        realm.commitTransaction();

        return persistedTrackingList;
    }

    public List<ProjectTrackingList> copyToRealmTransaction(List<ProjectTrackingList> trackingLists) {

        realm.beginTransaction();
        List<ProjectTrackingList> persistedTrackingLists = realm.copyToRealmOrUpdate(trackingLists);
        realm.commitTransaction();
        return persistedTrackingLists;
    }

    public void deleteAllProjectTrackingLists() {

        final RealmResults<ProjectTrackingList> results = fetchUserProjectTrackingList();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                results.deleteAllFromRealm();
            }
        });
    }
}
