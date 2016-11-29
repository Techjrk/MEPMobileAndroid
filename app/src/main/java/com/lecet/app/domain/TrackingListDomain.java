package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.MoveProjectFromListRequest;
import com.lecet.app.data.api.response.ProjectTrackingListDetailResponse;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.models.ProjectUpdate;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.interfaces.LecetCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: TrackingListDomain Created: 11/3/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class TrackingListDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private ProjectDomain projectDomain;

    @Deprecated
    public TrackingListDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }


    public TrackingListDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm, RealmChangeListener listener) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        this.realm.addChangeListener(listener);
    }

    public TrackingListDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm, RealmChangeListener listener, ProjectDomain projectDomain) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        this.realm.addChangeListener(listener);
        this.projectDomain = projectDomain;
    }

    // Realm Management
    public void removeRealmChangeListener(RealmChangeListener listener) {
        realm.removeChangeListener(listener);
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

    public void getProjectTrackingListDetails(long projectTrackingListID, Callback<List<ProjectTrackingListDetailResponse>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String filter = "{\"include\":[\"updates\",{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}]}";

        Call<List<ProjectTrackingListDetailResponse>> call = lecetClient.getTrackingListService().getProjectTrackingListDetail(token, projectTrackingListID, filter);
        call.enqueue(callback);
    }

    private Call<ProjectTrackingList> moveProjectsFromProjectTrackingList(long projectTrackingListId, List<Long> projectIds , Callback<ProjectTrackingList> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        MoveProjectFromListRequest body = new MoveProjectFromListRequest(projectIds);

        Call<ProjectTrackingList> call = lecetClient.getTrackingListService().moveProjectsForProjectTrackingList(token, projectTrackingListId, body);
        call.enqueue(callback);

        return call;
    }

    public Call<ProjectTrackingList> removeProjectsFromProjectTrackingList(long trackingListID, List<Long> removedProjectIds, Callback<ProjectTrackingList> callback) {

       return moveProjectsFromProjectTrackingList(trackingListID, removedProjectIds, callback);
    }

    public void moveProjectsToDestinationTrackingList(long sourceProjectTrackingListId, final long destinationTrackingListId, final List<Long> movedProjectIds, final LecetCallback callback) {

        // First remove from source tracking list
        removeProjectsFromProjectTrackingList(sourceProjectTrackingListId, movedProjectIds, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    // Now we have to move the projects to destination
                    moveProjectsFromProjectTrackingList(destinationTrackingListId, movedProjectIds, new Callback<ProjectTrackingList>() {
                        @Override
                        public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                            if (response.isSuccessful()) {

                                callback.onSuccess(null);
                            } else {

                                callback.onFailure(response.code(), response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                            callback.onFailure(-1, "Network Failure");
                        }
                    });

                } else {

                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                    callback.onFailure(-1, "Network Failure");
            }
        });
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

    public void fetchProjectTrackingListAsync(long id, RealmChangeListener<RealmModel> listener) {

        ProjectTrackingList result = realm.where(ProjectTrackingList.class).equalTo("id", id).findFirstAsync();
        result.addChangeListener(listener);
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

    // Project Update Mapping

    public void asyncMapUpdatesToProjects(final List<ProjectUpdate> updates, Realm.Transaction.OnSuccess successCallback,
                                          final Realm.Transaction.OnError errorCallback) {

        final WeakReference<ProjectDomain> domainWeakReference = new WeakReference<>(projectDomain);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // First we need add the updates to Realm
                List<ProjectUpdate> insertedUpdates = realm.copyToRealmOrUpdate(updates);

                // If we don't have a ProjectDomain then we exit.
                ProjectDomain domain = domainWeakReference.get();
                if (domain == null) {

                    // Notify error callback if its not null and cancel execution
                    if (errorCallback != null) errorCallback.onError(new Throwable("ProjectDomain is null!"));

                    return;
                }

                // Now let's cycle through the updates and associate with their respective Project
                for (ProjectUpdate update : insertedUpdates) {

                    Project project = domain.fetchProjectById(realm, update.getProjectId());
                    if (project != null) {

                        RealmList<ProjectUpdate> projectUpdates = project.getUpdates();
                        if (projectUpdates == null) {
                            projectUpdates = new RealmList<>();
                        }

                        projectUpdates.add(update);

                        // Let's get the latest project update and assign it as most recent update for
                        // project
                        ProjectUpdate recentUpdate = projectUpdates.sort("updatedAt", Sort.DESCENDING).first();
                        project.setRecentUpdate(recentUpdate);
                    }
                }
            }

        }, successCallback, errorCallback);
    }

    // Sorting

    public RealmResults<Project> sortProjectList(RealmList<Project> projects, String field, Sort sortType) {

        return projects.sort(field, sortType);
    }


    public RealmResults<Project> sortProjectListByBidDate(RealmList<Project> projects) {

        return sortProjectList(projects, "bidDate", Sort.DESCENDING);
    }


    public RealmResults<Project> sortProjectListByLastUpdated(RealmList<Project> projects) {

        return sortProjectList(projects, "lastPublishDate", Sort.DESCENDING);
    }


    public RealmResults<Project> sortProjectListByDateAdded(RealmList<Project> projects) {

        return sortProjectList(projects, "firstPublishDate", Sort.DESCENDING);
    }


    public RealmResults<Project> sortProjectListByValue(RealmList<Project> projects, Sort sortType) {

        return sortProjectList(projects, "estLow", sortType);
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
