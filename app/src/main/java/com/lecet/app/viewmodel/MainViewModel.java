package com.lecet.app.viewmodel;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.utility.DateUtility;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: MainViewModel Created: 10/6/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MainViewModel {

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final Calendar calendar;
    private final AppCompatActivity appCompatActivity;

    public MainViewModel(AppCompatActivity appCompatActivity, BidDomain bidDomain, ProjectDomain projectDomain, Calendar calendar) {

        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;
        this.calendar = calendar;
        this.appCompatActivity = appCompatActivity;
    }

    /**
     * Public
     **/
    public void getBidsRecentlyMade(@NonNull final Date cutoffDate, @NonNull final LecetCallback<TreeMap<Long, TreeSet<Bid>>> callback) {

        getBidsRecentlyMade(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {

                    // Store in Realm
                    bidDomain.copyToRealmTransaction(response.body());

                    // Fetch Realm managed Projects
                    RealmResults<Bid> realmResults = bidDomain.fetchBids(cutoffDate);
                    callback.onSuccess(bidDomain.sortRealmResults(realmResults));

                } else {

                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {

                Log.d("MBR", t.getLocalizedMessage());
                callback.onFailure(-1, getString(R.string.error_network_message));
            }
        });
    }

    public void getProjectsHappeningSoon(@NonNull final LecetCallback<Project[]> callback) {

        getProjectsHappeningSoon(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    // Store in Realm
                    List<Project> body = response.body();
                    projectDomain.copyToRealmTransaction(body);

                    // Fetch Realm managed Projects
                    RealmResults<Project> realmResults = fetchProjectsHappeningSoon();
                    callback.onSuccess(realmResults.toArray(new Project[realmResults.size()]));

                    // TODO: Add Logic to notify ListFragment

                } else {

                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                callback.onFailure(-1, "Network Failure");
            }
        });
    }

    /** Private **/
    private String getString(@StringRes int stringID) {

        return appCompatActivity.getString(stringID);
    }

    /**
     * API
     **/

    private void getBidsRecentlyMade(Callback<List<Bid>> callback) {

        bidDomain.getBidsRecentlyMade(callback);
    }

    private void getProjectsHappeningSoon(Callback<List<Project>> callback) {

        projectDomain.getProjectsHappeningSoon(callback);
    }

    /**
     * Persisted
     **/

    private RealmResults<Bid> fetchBids(@BidDomain.BidGroup int bidGroup) {

        return bidDomain.fetchBids(bidGroup);
    }

    private RealmResults<Project> fetchProjectsHappeningSoon() {

        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return projectDomain.fetchProjectsHappeningSoon(calendar.getTime());
    }

}
