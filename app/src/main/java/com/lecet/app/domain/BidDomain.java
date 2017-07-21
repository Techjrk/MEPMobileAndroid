package com.lecet.app.domain;

import android.support.annotation.IntDef;
import android.util.Log;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmQuery;
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
    @IntDef({ENGINEERING, BUILDING, HOUSING, UTILITIES, CONSOLIDATED_CODE_B, CONSOLIDATED_CODE_H})
    public @interface BidGroup {
    }

    private static final int DASHBOARD_CALL_LIMIT = 100;

    public static final int ENGINEERING = 101;
    public static final int BUILDING = 102;
    public static final int HOUSING = 103;
    public static final int UTILITIES = 105;
    public static final int CONSOLIDATED_CODE_B = 901;
    public static final int CONSOLIDATED_CODE_H = 902;

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

    public Call<List<Bid>> getBidsRecentlyMade(Date startDate, int limit, Callback<List<Bid>> callback) {
        //Log.d("BidDomain", "getBidsRecentlyMade() called with: startDate = [" + startDate + "], limit = [" + limit + "], callback = [" + callback + "]");

        String token = sharedPreferenceUtil.getAccessToken();

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);

//        String filter = String.format("{\"include\":[{\"contact\":\"company\"},{\"project\":{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}}], " +
//                "\"limit\":%d, \"where\":{\"and\":[{\"createDate\":{\"gt\":\"%s\"}}, {\"rank\":1}]},\"dashboardTypes\":true}", limit, formattedDate);
/*
        String rawQuery = "{\"fields\":[\"id\",\"createDate\",\"projectId\",\"companyId\" ,\"amount\"], \"include\":[\"company\",{\"relation\":\"project\",\"scope\":{\"fields\":[\"id\",\"title\",\"bidDate\",\"city\",\"state\",\"zip5\",\"geocode\",\"unionDesignation\",\"primaryProjectTypeId\"],\"include\":{\"relation\":\"primaryProjectType\",\"scope\":{\"fields\":[\"id\",\"title\",\"projectCategoryId\"],\"include\":{\"projectCategory\":\"projectGroup\"}}}}}]" +
                ", \"limit\":%d, \"where\":{\"and\":[{\"createDate\":{\"gt\":\"%s\"}},{\"rank\":1}]},\"dashboardType\":true}";
*/

        String rawQuery = "{\"fields\":[\"id\",\"createDate\",\"projectId\",\"companyId\" ,\"amount\"], \"include\":[\"company\",{\"relation\":\"project\",\"scope\":{\"fields\":[\"id\",\"title\",\"bidDate\",\"city\",\"state\",\"zip5\",\"geocode\",\"unionDesignation\",\"primaryProjectTypeId\"],\"include\":{\"relation\":\"primaryProjectType\",\"scope\":{\"fields\":[\"id\",\"title\",\"projectCategoryId\"],\"include\":{\"projectCategory\":\"projectGroup\"}}}}}]" +
                ", \"limit\":%d, \"order\":\"createDate DESC\", \"where\":{\"and\":[{\"createDate\":{\"gt\":\"%s\"}},{\"rank\":1}]},\"dashboardType\":true}";


        String filter = String.format(rawQuery, limit, formattedDate);

        Call<List<Bid>> call = lecetClient.getBidService().bids(token, filter);
        call.enqueue(callback);

        return call;
    }


    public Call<List<Bid>> getBidsRecentlyMade(int limit, Callback<List<Bid>> callback) {
        //Log.d("BidDomain", "getBidsRecentlyMade() called with: limit = [" + limit + "], callback = [" + callback + "]");

        Date startDate = DateUtility.addDays(-30);
        return getBidsRecentlyMade(startDate, limit, callback);
    }

    public Call<List<Bid>> getBidsRecentlyMade(Callback<List<Bid>> callback) {
        //Log.d("BidDomain", "getBidsRecentlyMade() called with: callback = [" + callback + "]");

        int limit = DASHBOARD_CALL_LIMIT;
        return getBidsRecentlyMade(limit, callback);
    }

    /**
     * Persisted
     **/

    public RealmResults<Bid> fetchBidsByCompany(long companyID, String sortFilter, Sort sort) {

        return realm.where(Bid.class)
                .equalTo("companyId", companyID)
                .findAllSorted(sortFilter, sort);
    }

    public RealmResults<Bid> fetchBidsByCompanyLessThanDate(long companyID, String sortFilter, Sort sort, Date cutoffDate) {

        return realm.where(Bid.class)
                .lessThan("project.bidDate", cutoffDate)
                .equalTo("companyId", companyID)
                .findAllSorted(sortFilter, sort);
    }

    public RealmResults<Bid> fetchBidsByCompanyGreaterThanDate(long companyID, String sortFilter, Sort sort, Date cutoffDate) {

        return realm.where(Bid.class)
                .greaterThanOrEqualTo("project.bidDate", cutoffDate)
                .equalTo("companyId", companyID)
                .findAllSorted(sortFilter, sort);
    }

    public RealmResults<Bid> fetchBids(Date cutoffDate) {

        RealmResults<Bid> bids = realm.where(Bid.class)
                .greaterThan("createDate", cutoffDate)
                .equalTo("project.hidden", false)
                .findAllSorted("createDate", Sort.DESCENDING);

        //Log.d("BidDomain", "fetchBids() called with: cutoffDate = [" + cutoffDate + "]. Size: " + bids.size());
        return bids;
    }

    public List<Bid> fetchBidsSortedByProjectBidDate(Date cutoffDate) {

        RealmResults<Bid> results = fetchBids(cutoffDate);

        List<Bid> convertedList = convertRealmResultsToList(results);
        Collections.sort(convertedList, new BidDateComparator());

        return convertedList;
    }

    public RealmResults<Bid> fetchBids(@BidGroup int categoryId) {
        //Log.d("BidDomain", "fetchBids() called with: categoryId = [" + categoryId + "]");

        RealmResults<Bid> bids;

        if (categoryId == BidDomain.CONSOLIDATED_CODE_B) {

            bids = realm.where(Bid.class)
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.HOUSING)
                    .or()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.BUILDING)
                    .or()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("project.primaryProjectType.buildingOrHighway", "B")
                    .endGroup()
                    .endGroup()
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);

        } else if (categoryId == BidDomain.CONSOLIDATED_CODE_H) {

            bids = realm.where(Bid.class)
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.ENGINEERING)
                    .or()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("project.primaryProjectType.buildingOrHighway", "H")
                    .endGroup()
                    .endGroup()
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);

        } else {

            bids = realm.where(Bid.class)
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);
        }



        return bids;
    }

    public List<Bid> fetchBidsSortedByProjectBidDate(@BidGroup int categoryId) {

        RealmResults<Bid> results = fetchBids(categoryId);

        List<Bid> convertedList = convertRealmResultsToList(results);
        Collections.sort(convertedList, new BidDateComparator());

        return convertedList;
    }

    public RealmResults<Bid> fetchBids(@BidGroup int categoryId, Date cutoffDate) {
        //Log.d("BidDomain", "fetchBids() called with: categoryId = [" + categoryId + "], cutoffDate = [" + cutoffDate + "]");

        RealmResults<Bid> bids;

        if (categoryId == BidDomain.CONSOLIDATED_CODE_B) {

            bids = realm.where(Bid.class)
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.HOUSING)
                    .or()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.BUILDING)
                    .or()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("project.primaryProjectType.buildingOrHighway", "B")
                    .endGroup()
                    .endGroup()
                    .equalTo("project.hidden", false)
                    .greaterThan("createDate", cutoffDate)
                    .findAllSorted("createDate", Sort.DESCENDING);

            //Log.d("BidDomain", "fetchBids: CONSOLIDATED_CODE_B: filtering for Building. Size: " + bids.size());
        }

        else if (categoryId == BidDomain.CONSOLIDATED_CODE_H) {

            bids = realm.where(Bid.class)
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.ENGINEERING)
                    .or()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("project.primaryProjectType.buildingOrHighway", "H")
                    .endGroup()
                    .endGroup()
                    .equalTo("project.hidden", false)
                    .greaterThan("createDate", cutoffDate)
                    .findAllSorted("createDate", Sort.DESCENDING);

            //Log.d("BidDomain", "fetchBids: CONSOLIDATED_CODE_H: filtering for Heavy Highway. Size: " + bids.size());

        } else {

            bids = realm.where(Bid.class)
                    .greaterThan("createDate", cutoffDate)
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);
            //Log.d("BidDomain", "fetchBids: Neither CONSOLIDATED_CODE_B or CONSOLIDATED_CODE_H was selected. Size: " + bids.size());
        }


        return bids;
    }

    public List<Bid> fetchBidsSortedByProjectBidDate(@BidGroup int categoryId, Date cutoffDate) {
        //Log.d("BidDomain", "fetchBidsSortedByProjectBidDate() called with: categoryId = [" + categoryId + "], cutoffDate = [" + cutoffDate + "]");

        RealmResults<Bid> results = fetchBids(categoryId, cutoffDate);

        List<Bid> convertedList = convertRealmResultsToList(results);
        Collections.sort(convertedList, new BidDateComparator());

        return convertedList;
    }


    public Company getBidCompany(long companyID) {

        return realm.where(Company.class).equalTo("id", companyID).findFirst();
    }

    public Project getBidProject(long projectID) {

        return realm.where(Project.class).equalTo("id", projectID).findFirst();
    }

    public RealmResults<Bid> queryResult(@BidGroup int categoryId, RealmResults<Bid> result) {
        //Log.d("BidDomain", "queryResult() called with: categoryId = [" + categoryId + "], result = [" + result + "]");

        if (categoryId == BidDomain.CONSOLIDATED_CODE_B) {

            return result.where()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.HOUSING)
                    .or()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.BUILDING)
                    .or()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("project.primaryProjectType.buildingOrHighway", "B")
                    .endGroup()
                    .endGroup()
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);

        } else if (categoryId == BidDomain.CONSOLIDATED_CODE_H) {

            return result.where()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.ENGINEERING)
                    .or()
                    .beginGroup()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("project.primaryProjectType.buildingOrHighway", "H")
                    .endGroup()
                    .endGroup()
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);

        } else {

            return result.where()
                    .equalTo("project.primaryProjectType.projectCategory.projectGroupId", categoryId)
                    .equalTo("project.hidden", false)
                    .findAllSorted("createDate", Sort.DESCENDING);
        }

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

    public void asyncCopyToRealm(final List<Bid> bids, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealmOrUpdate(bids);
            }
        }, onSuccess, onError);
    }

    /** RealmList Conversion **/

    private List<Bid> convertRealmResultsToList(RealmResults<Bid> results) {

        List<Bid> data = new ArrayList<>();
        Bid[] bids = new Bid[results.size()];
        results.toArray(bids);
        data.addAll(new ArrayList<>(Arrays.asList(bids)));

        return data;
    }

    /** Sorting **/

    private class BidDateComparator implements Comparator<Bid> {
        @Override
        public int compare(Bid a, Bid b) {
            return a.getProject().getBidDate().after(b.getProject().getBidDate()) ? 1 : -1;
        }
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
