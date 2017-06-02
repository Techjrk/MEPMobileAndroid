package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.content.MainActivity;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.County;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.LecetCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: DashboardIntermediaryViewModel Created: 2/6/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class DashboardIntermediaryViewModel extends BaseObservableViewModel {

    private static final String TAG = "DashboardIntermediaryVM";

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final SearchDomain searchDomain;
    private final TrackingListDomain trackingListDomain;

    private List<Call> calls;

    private boolean displayContent;
    private boolean fetchedStageList;
    private boolean fetchedProjectType;
    private boolean fetchedJurisdiction;
    private boolean fetchedCounties;
    private boolean fetchedMBR;
    private boolean fetchedMHS;
    private boolean fetchedMRA;
    private boolean fetchedMRU;
    private boolean fetchedCompTracking;
    private boolean fetchedProjTracking;
    private boolean launched;


    public DashboardIntermediaryViewModel(AppCompatActivity appCompatActivity, BidDomain bidDomain, ProjectDomain projectDomain, SearchDomain searchDomain, TrackingListDomain trackingListDomain) {
        super(appCompatActivity);

        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;
        this.searchDomain = searchDomain;
        this.trackingListDomain = trackingListDomain;

        this.calls = new ArrayList<>();

        // First lets delete any existing data
        trackingListDomain.deleteAllCompanyTrackingLists();
        trackingListDomain.deleteAllProjectTrackingLists();
    }

    /* Navigation Logic */
    private void checkDataDownloaded() {

        if (fetchedJurisdiction && fetchedProjectType && fetchedStageList && fetchedCounties && fetchedMBR && fetchedMHS && fetchedMRA && fetchedMRU && fetchedCompTracking && fetchedProjTracking && !launched) {

            launched = true;

            AppCompatActivity appCompatActivity = getActivityWeakReference().get();
            if (appCompatActivity != null) {

                Intent intent = new Intent(appCompatActivity, MainActivity.class);
                appCompatActivity.startActivity(intent);
                appCompatActivity.finish();
            }
        }
    }

    /* Networking */
    public void getData() {

        // Add the requests to the tracked calls list
        calls.add(getSearchStageList());
        calls.add(getJurisdictionList());
        calls.add(getProjectTypeList());
        calls.add(getCounties());
        calls.add(getBidsRecentlyMade());
        calls.add(getProjectsHappeningSoon());
        calls.add(getProjectsRecentlyAdded());
        calls.add(getProjectsRecentlyUpdated());
        calls.add(getUserProjectTrackingList());
        calls.add(getUserCompanyTrackingList());
    }

    public void cancelDataDownload() {

        for (Call call : calls) {

            call.cancel();
        }
    }


    /* Search */

    private Call<List<SearchFilterStagesMain>> getSearchStageList() {

        return searchDomain.generateRealmStageList(new LecetCallback<List<SearchFilterStagesMain>>() {
            @Override
            public void onSuccess(List<SearchFilterStagesMain> result) {

                fetchedStageList = true;
                checkDataDownloaded();
            }

            @Override
            public void onFailure(int code, String message) {

                if (code == 401) {

                    displayUnauthorizedUserDialog();
                } else {

                    fetchedStageList = true;
                    checkDataDownloaded();
                }

            }
        });
    }

    private Call<List<SearchFilterProjectTypesMain>> getProjectTypeList() {

        return searchDomain.generateRealmProjectTypesList(new LecetCallback<List<SearchFilterProjectTypesMain>>() {
            @Override
            public void onSuccess(List<SearchFilterProjectTypesMain> result) {

                fetchedProjectType = true;
                checkDataDownloaded();
            }

            @Override
            public void onFailure(int code, String message) {

                if (code == 401) {

                    displayUnauthorizedUserDialog();
                } else {

                    fetchedProjectType = true;
                    checkDataDownloaded();
                }
            }
        });
    }

    private Call<List<SearchFilterJurisdictionMain>> getJurisdictionList() {

        return searchDomain.generateRealmJurisdictionList(new LecetCallback<List<SearchFilterJurisdictionMain>>() {
            @Override
            public void onSuccess(List<SearchFilterJurisdictionMain> result) {

                fetchedJurisdiction = true;
                checkDataDownloaded();
            }

            @Override
            public void onFailure(int code, String message) {

                if (code == 401) {

                    displayUnauthorizedUserDialog();
                } else {

                    fetchedJurisdiction = true;
                    checkDataDownloaded();
                }
            }
        });
    }

    private Call<List<County>> getCounties() {

        return searchDomain.generateRealmCountyList(new LecetCallback<List<County>>() {
            @Override
            public void onSuccess(List<County> result) {
                fetchedCounties = true;
                checkDataDownloaded();
            }

            @Override
            public void onFailure(int code, String message) {

                if (code == 401) {

                    displayUnauthorizedUserDialog();      //TODO - ******* REMOVE AFTER BACKEND FIX IS IMPLEMENTED ALLOWING ALL USERS TO RECEIVE COUNTY DATA *******
                    //fetchedCounties = true;
                    //checkDataDownloaded();
                } else {

                    fetchedCounties = true;
                    checkDataDownloaded();
                }
            }

        });
    }

    /* Dashboard */

    private Call<List<Bid>> getBidsRecentlyMade() {

        return bidDomain.getBidsRecentlyMade(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {

                if (response.isSuccessful()) {

                    bidDomain.asyncCopyToRealm(response.body(), new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            fetchedMBR = true;
                            checkDataDownloaded();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            fetchedMBR = true;
                            checkDataDownloaded();
                        }
                    });

                } else {

                    if (isSessionUnauthorized(response)) {

                        displayUnauthorizedUserDialog();

                    } else {

                        fetchedMBR = true;
                        checkDataDownloaded();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {

                fetchedMBR = true;
                checkDataDownloaded();
            }
        });
    }

    private Call<List<Project>> getProjectsHappeningSoon() {

        return projectDomain.getProjectsHappeningSoon(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    // Store in Realm
                    List<Project> body = response.body();
                    projectDomain.asyncCopyToRealm(body, null, new Boolean(true), null, null, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            // data received, lets see if we should display main content
                            fetchedMHS = true;

                            // data received, lets see if we should display main content
                            checkDataDownloaded();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            Log.e(TAG, "getProjectsHappeningSoon Failed: "  + error.getMessage());

                            // data received, lets see if we should display main content
                            fetchedMHS = true;

                            // data received, lets see if we should display main content
                            checkDataDownloaded();
                        }
                    });


                } else {

                    Log.e(TAG, "getProjectsHappeningSoon Failed: "  + response.message());

                    // data received, lets see if we should display main content
                    if (isSessionUnauthorized(response)) {

                        displayUnauthorizedUserDialog();

                    } else {

                        fetchedMHS = true;
                        checkDataDownloaded();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                // data received, lets see if we should display main content
                fetchedMHS = true;
                checkDataDownloaded();
            }
        });
    }

    private Call<List<Project>> getProjectsRecentlyAdded() {

        return projectDomain.getProjectsRecentlyAdded(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    // Store in Realm
                    List<Project> body = response.body();
                    projectDomain.asyncCopyToRealm(body, null, null, new Boolean(true), null, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            // data received, lets see if we should display main content
                            fetchedMRA = true;

                            // data received, lets see if we should display main content
                            checkDataDownloaded();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            // data received, lets see if we should display main content
                            fetchedMRA = true;

                            // data received, lets see if we should display main content
                            checkDataDownloaded();
                        }
                    });


                } else {

                    Log.e(TAG, "getProjectsRecentlyAdded Failed: "  + response.message());

                    // data received, lets see if we should display main content
                    if (isSessionUnauthorized(response)) {

                        displayUnauthorizedUserDialog();

                    } else {

                        fetchedMRA = true;
                        checkDataDownloaded();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                Log.e(TAG, "getProjectsRecentlyAdded Failed: "  + t.getMessage());

                // data received, lets see if we should display main content
                fetchedMRA = true;
                checkDataDownloaded();
            }
        });
    }

    private Call<List<Project>> getProjectsRecentlyUpdated() {

        return projectDomain.getProjectsRecentlyUpdated(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    // data received, lets see if we should display main content
                    fetchedMRU = true;

                    // Store in Realm
                    List<Project> body = response.body();

                    projectDomain.asyncCopyToRealm(body, null, null, null, new Boolean(true), new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            // data received, lets see if we should display main content
                            checkDataDownloaded();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            Log.e(TAG, "getProjectsRecentlyUpdated : " + error.getMessage());
                            // data received, lets see if we should display main content
                            checkDataDownloaded();
                        }
                    });

                } else {

                    Log.e(TAG, "getProjectsRecentlyUpdated : " + response.message());

                    // data received, lets see if we should display main content
                    if (isSessionUnauthorized(response)) {

                        displayUnauthorizedUserDialog();

                    } else {

                        fetchedMRU = true;
                        checkDataDownloaded();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                Log.e(TAG, "getProjectsRecentlyUpdated : " + t.getMessage());

                // data received, lets see if we should display main content
                fetchedMRU = true;
                checkDataDownloaded();
            }
        });
    }

    /* Tracking List */


    public Call<List<ProjectTrackingList>> getUserProjectTrackingList() {

        return trackingListDomain.getUserProjectTrackingLists(new Callback<List<ProjectTrackingList>>() {
            @Override
            public void onResponse(Call<List<ProjectTrackingList>> call, Response<List<ProjectTrackingList>> response) {

                if (response.isSuccessful()) {

                    fetchedProjTracking = true;
                    List<ProjectTrackingList> data = response.body();
                    trackingListDomain.copyProjectTrackingListsToRealmTransaction(data);

                } else {

                    Log.e(TAG, "getUserProjectTrackingList : " + response.message());

                    // data received, lets see if we should display main content
                    if (isSessionUnauthorized(response)) {

                        displayUnauthorizedUserDialog();

                    } else {

                        fetchedProjTracking = true;
                        checkDataDownloaded();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<ProjectTrackingList>> call, Throwable t) {

                Log.e(TAG, "getUserProjectTrackingList : " + t.getMessage());

                fetchedProjTracking = true;
                checkDataDownloaded();
            }
        });
    }


    public Call<List<CompanyTrackingList>> getUserCompanyTrackingList() {

        return trackingListDomain.getUserCompanyTrackingLists(new Callback<List<CompanyTrackingList>>() {
            @Override
            public void onResponse(Call<List<CompanyTrackingList>> call, Response<List<CompanyTrackingList>> response) {

                if (response.isSuccessful()) {

                    fetchedCompTracking = true;
                    List<CompanyTrackingList> data = response.body();
                    trackingListDomain.copyCompanyTrackingListsToRealmTransaction(data);

                } else {

                    Log.e(TAG, "getUserCompanyTrackingList : " + response.message());

                    // data received, lets see if we should display main content
                    if (isSessionUnauthorized(response)) {

                        displayUnauthorizedUserDialog();

                    } else {

                        fetchedCompTracking = true;
                        checkDataDownloaded();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CompanyTrackingList>> call, Throwable t) {

                Log.e(TAG, "getUserCompanyTrackingList : " + t.getMessage());

                fetchedCompTracking = true;
                checkDataDownloaded();
            }
        });
    }

}
