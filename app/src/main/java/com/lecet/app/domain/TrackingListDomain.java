package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.CompanyTrackingList;
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

    public void getUserCompanyTrackingLists(Callback<List<CompanyTrackingList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        long userID = sharedPreferenceUtil.getId();

        String filter = "{\"include\":[\"companies\"]}";

        Call<List<CompanyTrackingList>> call = lecetClient.getTrackingListService().getUserCompanyTrackingList(token, userID, filter);
        call.enqueue(callback);
    }

    /** Persisted **/

    // Fetch

    public RealmResults<ProjectTrackingList> fetchUserProjectTrackingList() {

        RealmResults<ProjectTrackingList> results = realm.where(ProjectTrackingList.class).findAll();
        return results;
    }


    public RealmResults<CompanyTrackingList> fetchUserCompanyTrackingList() {

        RealmResults<CompanyTrackingList> results = realm.where(CompanyTrackingList.class).findAll();
        return results;
    }

    
    public ProjectTrackingList fetchProjectTrackingList(long id) {

        return realm.where(ProjectTrackingList.class).equalTo("id", id).findFirst();
    }


    public CompanyTrackingList fetchCompanyTrackingList(long id) {

        return realm.where(CompanyTrackingList.class).equalTo("id", id).findFirst();
    }


    // Delete

    public void deleteAllProjectTrackingLists() {

        final RealmResults<ProjectTrackingList> results = fetchUserProjectTrackingList();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                results.deleteAllFromRealm();
            }
        });
    }


    public void deleteAllCompanyTrackingLists() {

        final RealmResults<ProjectTrackingList> results = fetchUserProjectTrackingList();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                results.deleteAllFromRealm();
            }
        });
    }

    // Realm transactions

    public ProjectTrackingList copyToRealmTransaction(ProjectTrackingList trackingList) {

        realm.beginTransaction();
        ProjectTrackingList persistedTrackingList = realm.copyToRealmOrUpdate(trackingList);
        realm.commitTransaction();

        return persistedTrackingList;
    }


    public List<CompanyTrackingList> copyCompanyTrackingListsToRealmTransaction(List<CompanyTrackingList> trackingLists) {

        realm.beginTransaction();
        List<CompanyTrackingList> persistedTrackingLists = realm.copyToRealmOrUpdate(trackingLists);
        realm.commitTransaction();
        return persistedTrackingLists;
    }


    public List<ProjectTrackingList> copyProjectTrackingListsToRealmTransaction(List<ProjectTrackingList> trackingLists) {

        realm.beginTransaction();
        List<ProjectTrackingList> persistedTrackingLists = realm.copyToRealmOrUpdate(trackingLists);
        realm.commitTransaction();
        return persistedTrackingLists;
    }
}
