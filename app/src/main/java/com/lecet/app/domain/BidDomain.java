package com.lecet.app.domain;

import android.support.annotation.IntDef;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: BidDomain Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class BidDomain {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ENGINEERING, BUILDING, HOUSING, UTILITIES})
    public @interface BidGroup {}
    public static final int ENGINEERING = 101;
    public static final int BUILDING = 102;
    public static final int HOUSING = 103;
    public static final int UTILITIES = 105;

    private final Realm realm;
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;

    public BidDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    /** API **/

    public void getBidsRecentlyMade(Date startDate, int limit, Callback<List<Bid>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String filter = String.format("{\"include\":[\"contact\",{\"project\":{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}}], " +
                "\"limit\":%d, \"where\":{\"and\":[{\"createDate\":{\"gt\":\"%s\"}}, {\"rank\":1}]}}", limit, formattedDate);

        Call<List<Bid>> call = lecetClient.getBidService().bids(token, filter);
        call.enqueue(callback);
    }


    public void getBidsRecentlyMade(int limit, Callback<List<Bid>> callback) {

        Date startDate = DateUtility.addDays(-30);
        getBidsRecentlyMade(startDate, limit, callback);
    }

    public void getBidsRecentlyMade(Callback<List<Bid>> callback) {

        int limit = 100;

        getBidsRecentlyMade(limit, callback);
    }

    public Bid copyToRealmTransaction(Bid bid) {

        realm.beginTransaction();
        Bid persistedBid = realm.copyToRealm(bid);
        realm.commitTransaction();

        return persistedBid;
    }

    public List<Bid> copyToRealmTransaction(List<Bid> bids) {

        realm.beginTransaction();
        List<Bid> persistedBids = realm.copyToRealm(bids);
        realm.commitTransaction();
        return persistedBids;
    }

    /** Persisted **/

    public RealmResults<Bid> fetchBids(@BidGroup int categoryId) {

        RealmResults<Bid> bids = realm.where(Bid.class)
                .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("project.hidden", false)
                .findAll();

        return bids;
    }
}
