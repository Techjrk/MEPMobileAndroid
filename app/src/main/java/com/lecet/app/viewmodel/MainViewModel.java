package com.lecet.app.viewmodel;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.BidsHappeningSoonAdapter;
import com.lecet.app.adapters.RecentBidsAdapter;
import com.lecet.app.content.MainActivity;
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

    @IntDef({DASHBOARD_POSITION_MBR, DASHBOARD_POSITION_MHS, DASHBOARD_POSITION_MRA, DASHBOARD_POSITION_MRU})
    @interface DashboardPosition {}
    private static final int DASHBOARD_POSITION_MBR = 0;
    private static final int DASHBOARD_POSITION_MHS = 1;
    private static final int DASHBOARD_POSITION_MRA = 2;
    private static final int DASHBOARD_POSITION_MRU = 3;

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final Calendar calendar;
    private final AppCompatActivity appCompatActivity;

    private @DashboardPosition int dashboardPosition;
    private Date lastFetchedMBR;
    private Date lastFetchedMHS;
    private Date lastFetchedMRA;
    private Date lastFetchedMRU;

    private RealmResults<Bid> realmResultsMBR;
    private RealmResults<Project> realmResultsMHS;


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

        // Check if data has been recently fetched and display those results from Realm
        if (lastFetchedMBR == null || lastFetchedMBR != null && minutesElapsed(new Date(), lastFetchedMBR) > 3) {

            getBidsRecentlyMade(new Callback<List<Bid>>() {
                @Override
                public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                    if (response.isSuccessful()) {

                        lastFetchedMBR = new Date();

                        // Store in Realm
                        bidDomain.copyToRealmTransaction(response.body());

                        // Fetch Realm managed Projects
                        realmResultsMBR = bidDomain.fetchBids(cutoffDate);
                        callback.onSuccess(bidDomain.sortRealmResults(realmResultsMBR));

                        // Display all Bids until set is selected
                        if (dashboardPosition == DASHBOARD_POSITION_MBR) {

                           setupMBRAdapter(realmResultsMBR);
                        }

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

        } else {

            // Fetch Realm managed Projects
            realmResultsMBR = bidDomain.fetchBids(cutoffDate);
            callback.onSuccess(bidDomain.sortRealmResults(realmResultsMBR));

            if (dashboardPosition == DASHBOARD_POSITION_MBR) {

                setupMBRAdapter(realmResultsMBR);
            }
        }
    }

    public void fetchBidsRecentlyMade(@BidDomain.BidGroup int bidGroup, @NonNull final Date cutoffDate, @NonNull final LecetCallback<TreeMap<Long, TreeSet<Bid>>> callback) {

        realmResultsMBR = bidDomain.fetchBids(bidGroup, cutoffDate);
        callback.onSuccess(bidDomain.sortRealmResults(realmResultsMBR));
    }

    public void getProjectsHappeningSoon(@NonNull final LecetCallback<Project[]> callback) {

        // Check if data has been recently fetched and display those results from Realm
        if (lastFetchedMHS == null || lastFetchedMHS != null && minutesElapsed(new Date(), lastFetchedMHS) > 3) {

            getProjectsHappeningSoon(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                    if (response.isSuccessful()) {

                        lastFetchedMHS = new Date();

                        // Store in Realm
                        List<Project> body = response.body();
                        projectDomain.copyToRealmTransaction(body);

                        // Fetch Realm managed Projects
                        realmResultsMHS = fetchProjectsHappeningSoon();
                        callback.onSuccess(realmResultsMHS.toArray(new Project[realmResultsMHS.size()]));

                        if (dashboardPosition == DASHBOARD_POSITION_MHS) {

                            setupMHSAdapter(realmResultsMHS);
                        }

                    } else {

                        callback.onFailure(response.code(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {

                    callback.onFailure(-1, "Network Failure");
                }
            });

        } else {

            // Fetch Realm managed Projects
            realmResultsMHS = fetchProjectsHappeningSoon();
            callback.onSuccess(realmResultsMHS.toArray(new Project[realmResultsMHS.size()]));

            if (dashboardPosition == DASHBOARD_POSITION_MHS) {

                setupMHSAdapter(realmResultsMHS);
            }
        }
    }

    public void fetchProjectsByBidDate(Date bidDate) {

        Date start = DateUtility.setDateToStartOfDate(bidDate);
        Date end = DateUtility.addDays(start, 1);

        realmResultsMHS = projectDomain.fetchProjectsByBidDate(start, end);
        displayAdapter(dashboardPosition);
    }

    /** Private **/

    private String getString(@StringRes int stringID) {

        return appCompatActivity.getString(stringID);
    }

    private long minutesElapsed(Date start, Date end) {

        long diff = start.getTime() - end.getTime();

        return diff / (60 * 1000) % 60;
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

        return projectDomain.fetchProjectsHappeningSoon(calendar.getTime(), DateUtility.getLastDateOfTheCurrentMonth());
    }

    /**
     * OnClick handlers
     **/

    @SuppressWarnings("unused")
    public void onPageLeftClicked(View view) {

        //Log.d(TAG, "onPageLeftClicked");
        MainActivity activity = (MainActivity) appCompatActivity;
        activity.prevViewPage();
    }

    @SuppressWarnings("unused")
    public void onPageRightClicked(View view) {

        //Log.d(TAG, "onPageRightClicked");
        MainActivity activity = (MainActivity) appCompatActivity;
        activity.nextViewPage();
    }

    /** ViewPager Event Handling **/

    public void currentPagerPosition(int position) {

        if (dashboardPosition == position) return;

        dashboardPosition = position;
        displayAdapter(dashboardPosition);
    }

    /** RecyclerView Management **/

    private void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getProjectRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) appCompatActivity.findViewById(recyclerView);
    }

    private RecyclerView.Adapter getMBRAdapter(Bid[] data) {

        return new RecentBidsAdapter(data);
    }

    private RecyclerView.Adapter getMHSAdapter(Project[] data) {

        return new BidsHappeningSoonAdapter(data);
    }

    /** Adapter Data Management **/

    private void displayAdapter(@DashboardPosition int dashboardPosition) {

        if (dashboardPosition == DASHBOARD_POSITION_MBR) {

            setupMBRAdapter(realmResultsMBR);

        } else if (dashboardPosition == DASHBOARD_POSITION_MHS) {

            setupMHSAdapter(realmResultsMHS);

        } else if (dashboardPosition == DASHBOARD_POSITION_MRA) {

        } else if (dashboardPosition == DASHBOARD_POSITION_MRU) {

        }
    }

    private void setupMBRAdapter(RealmResults<Bid> realmResults) {

        RecyclerView recyclerView = getProjectRecyclerView(R.id.recycler_view);
        setupRecyclerView(recyclerView);
        RecyclerView.Adapter adapter = realmResults != null ? getMBRAdapter(realmResults.toArray(new Bid[realmResults.size()])) : getMBRAdapter(new Bid[0]);
        recyclerView.setAdapter(adapter);
    }

    private void setupMHSAdapter(RealmResults<Project> realmResults) {

        RecyclerView recyclerView = getProjectRecyclerView(R.id.recycler_view);
        setupRecyclerView(recyclerView);
        RecyclerView.Adapter adapter = realmResults != null ? getMHSAdapter(realmResults.toArray(new Project[realmResults.size()])) : getMHSAdapter(new Project[0]);
        recyclerView.setAdapter(adapter);
    }
}
