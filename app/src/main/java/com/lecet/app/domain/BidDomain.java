package com.lecet.app.domain;

import android.support.annotation.IntDef;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
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
    public @interface BidGroup {
    }

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

    /**
     * API
     **/

    public void getBidsRecentlyMade(Date startDate, int limit, Callback<List<Bid>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String filter = String.format("{\"include\":[{\"contact\":\"company\"},{\"project\":{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}}], " +
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

    /**
     * Persisted
     **/

    public RealmResults<Bid> fetchBids(Date cutoffDate) {

        RealmResults<Bid> bids = realm.where(Bid.class)
                .greaterThan("createDate", cutoffDate)
                .equalTo("project.hidden", false)
                .findAllSorted("createDate", Sort.DESCENDING);

        return bids;
    }

    public RealmResults<Bid> fetchBids(@BidGroup int categoryId) {

        RealmResults<Bid> bids = realm.where(Bid.class)
                .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("project.hidden", false)
                .findAllSorted("createDate", Sort.DESCENDING);

        return bids;
    }

    public RealmResults<Bid> fetchBids(@BidGroup int categoryId, Date cutoffDate) {

        RealmResults<Bid> bids = realm.where(Bid.class)
                .greaterThan("createDate", cutoffDate)
                .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("project.hidden", false)
                .findAllSorted("createDate", Sort.DESCENDING);

        return bids;
    }

    public RealmResults<Bid> queryResult(@BidGroup int categoryId, RealmResults<Bid> result) {

        return result.where()
                .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("project.hidden", false)
                .findAllSorted("createDate", Sort.DESCENDING);
    }

    public Bid copyToRealmTransaction(Bid bid) {

        realm.beginTransaction();
        Bid persistedBid = realm.copyToRealmOrUpdate(bid);
        realm.commitTransaction();

        return persistedBid;
    }

    public List<Bid> copyToRealmTransaction(List<Bid> bids) {

        realm.beginTransaction();
        List<Bid> persistedBids = realm.copyToRealmOrUpdate(bids);
        realm.commitTransaction();
        return persistedBids;
    }

    /**
     * Utility
     **/
    public TreeMap<Long, TreeSet<Bid>> sortRealmResults(RealmResults<Bid> result) {

        RealmResults<Bid> engineering = queryResult(ENGINEERING, result);
        RealmResults<Bid> building = queryResult(BUILDING, result);
        RealmResults<Bid> housing = queryResult(HOUSING, result);
        RealmResults<Bid> utilities = queryResult(UTILITIES, result);

        TreeMap<Long, TreeSet<Bid>> treeMap = new TreeMap<>();

        Comparator<Bid> bidComparator = new Comparator<Bid>() {
            @Override
            public int compare(Bid bid, Bid t1) {
                return bid.getCreateDate().after(t1.getCreateDate()) ? 1 : -1;
            }
        };

        // Cycle through engineering bids
        if (engineering.size() > 0) {

            TreeSet<Bid> bids = new TreeSet<>(bidComparator);

            for (int i = 0; i < engineering.size(); i++) {
                bids.add(engineering.get(i));
            }

            treeMap.put(Long.valueOf(ENGINEERING), bids);
        }

        // Cycle through building bids
        if (building.size() > 0) {

            TreeSet<Bid> bids = new TreeSet<>(bidComparator);
            for (int i = 0; i < building.size(); i++) {
                bids.add(building.get(i));
            }

            treeMap.put(Long.valueOf(BUILDING), bids);
        }

        // Cycle through housing bids
        if (housing.size() > 0) {

            TreeSet<Bid> bids = new TreeSet<>(bidComparator);
            for (int i = 0; i < housing.size(); i++) {
                bids.add(housing.get(i));
            }

            treeMap.put(Long.valueOf(HOUSING), bids);
        }

        // Cycle through utilities bids
        if (engineering.size() > 0) {

            TreeSet<Bid> bids = new TreeSet<>(bidComparator);
            for (int i = 0; i < utilities.size(); i++) {
                bids.add(utilities.get(i));
            }

            treeMap.put(Long.valueOf(UTILITIES), bids);
        }

        return treeMap;
    }
}
