package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import retrofit2.Callback;

/**
 * File: MainViewModel Created: 10/6/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MainViewModel {

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final Calendar calendar;

    public MainViewModel(BidDomain bidDomain, ProjectDomain projectDomain, Calendar calendar) {

        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;
        this.calendar = calendar;
    }

    /** API **/

    public void getBidsRecentlyMade(Callback<List<Bid>> callback) {

        bidDomain.getBidsRecentlyMade(callback);
    }

    public void getProjectsHappeningSoon(Callback<List<Project>> callback) {

        projectDomain.getProjectsHappeningSoon(callback);
    }

    /** Persisted **/

    public RealmResults<Bid> fetchBids(@BidDomain.BidGroup int bidGroup) {

        return bidDomain.fetchBids(bidGroup);
    }

    public RealmResults<Project> fetchProjectsHappeningSoon() {

        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return projectDomain.fetchProjectsRecentlyPublished(calendar.getTime());
    }
}
