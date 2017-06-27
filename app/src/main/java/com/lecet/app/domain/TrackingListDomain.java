package com.lecet.app.domain;

import android.support.annotation.NonNull;
import android.util.Log;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.MoveCompanyFromListRequest;
import com.lecet.app.data.api.request.MoveProjectFromListRequest;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.utility.DateUtility;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: TrackingListDomain Created: 11/3/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class TrackingListDomain {

    private static final String TAG = "TrackingListDomain";

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private ProjectDomain projectDomain;


    public TrackingListDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    @Deprecated
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
    public void addRealmChangeListener(RealmChangeListener listener) {
        realm.addChangeListener(listener);
    }


    public void removeRealmChangeListener(RealmChangeListener listener) {
        realm.removeChangeListener(listener);
    }

    public ProjectDomain getProjectDomain() {
        return projectDomain;
    }

    // API

    public Call<List<ProjectTrackingList>> getUserProjectTrackingLists(Callback<List<ProjectTrackingList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        long userID = sharedPreferenceUtil.getId();

        String filter = "{\"include\":[\"projects\"]}";

        Call<List<ProjectTrackingList>> call = lecetClient.getTrackingListService().getUserProjectTrackingList(token, userID, filter);
        call.enqueue(callback);

        return call;
    }


    public Call<List<CompanyTrackingList>> getUserCompanyTrackingLists(Callback<List<CompanyTrackingList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        long userID = sharedPreferenceUtil.getId();

        String filter = "{\"include\":[\"companies\"]}";

        Call<List<CompanyTrackingList>> call = lecetClient.getTrackingListService().getUserCompanyTrackingList(token, userID, filter);
        call.enqueue(callback);

        return call;
    }

    public void getCompanyTrackingListUpdates(long companyTrackingListId, Date updateCutoffDate, Callback<List<ActivityUpdate>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(updateCutoffDate);
        String filter = String.format("{\"where\":{\"createdAt\":{\"gte\":\"%s\"}}}", formattedDate);

        Call<List<ActivityUpdate>> call = lecetClient.getTrackingListService().getCompanyTrackingListUpdates(token, companyTrackingListId, filter);
        call.enqueue(callback);
    }

    public void getProjectTrackingListDetails(long projectTrackingListID, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtility.addDays(-1));
        String unFormattedFilter = "{\"include\":[{\"relation\":\"updates\", \"scope\":{\"where\":{\"updatedAt\":{\"gte\":\"%s\"}}}},{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}]}";
        String filter = String.format(unFormattedFilter, formattedDate);

        Call<List<Project>> call = lecetClient.getTrackingListService().getProjectTrackingListDetail(token, projectTrackingListID, filter);
        call.enqueue(callback);
    }

    public Call<ProjectTrackingList> syncProjectTrackingList(long projectTrackingListId, List<Long> projectIds, Callback<ProjectTrackingList> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        MoveProjectFromListRequest body = new MoveProjectFromListRequest(projectIds);

        Call<ProjectTrackingList> call = lecetClient.getTrackingListService().syncProjectTrackingList(token, projectTrackingListId, body);
        call.enqueue(callback);

        return call;
    }

    public void moveProjectsToDestinationTrackingList(long sourceProjectTrackingListId, final long destinationTrackingListId, final List<Long> destinationIds, final List<Long> sourceIds, final LecetCallback callback) {

        // First remove from source tracking list
        syncProjectTrackingList(sourceProjectTrackingListId, sourceIds, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    // Now we have to move the projects to destination
                    syncProjectTrackingList(destinationTrackingListId, destinationIds, new Callback<ProjectTrackingList>() {
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

    public Call<CompanyTrackingList> syncCompanyTrackingList(long projectTrackingListId, List<Long> itemIds, Callback<CompanyTrackingList> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        MoveCompanyFromListRequest body = new MoveCompanyFromListRequest(itemIds);

        Call<CompanyTrackingList> call = lecetClient.getTrackingListService().syncCompanyTrackingList(token, projectTrackingListId, body);
        call.enqueue(callback);

        return call;
    }


    public void moveCompaniesToDestinationTrackingList(long sourceProjectTrackingListId, final long destinationTrackingListId, final List<Long> destinationIds, final List<Long> sourceIds, final LecetCallback callback) {

        // First remove from source tracking list
        syncCompanyTrackingList(sourceProjectTrackingListId, sourceIds, new Callback<CompanyTrackingList>() {
            @Override
            public void onResponse(Call<CompanyTrackingList> call, Response<CompanyTrackingList> response) {

                if (response.isSuccessful()) {

                    // Now we have to move the projects to destination
                    syncCompanyTrackingList(destinationTrackingListId, destinationIds, new Callback<CompanyTrackingList>() {
                        @Override
                        public void onResponse(Call<CompanyTrackingList> call, Response<CompanyTrackingList> response) {

                            if (response.isSuccessful()) {
                                CompanyTrackingList companyTrackingList = response.body();
                                CompanyTrackingList destination = fetchCompanyTrackingList(destinationTrackingListId);
                                addCompaniesToTrackingList(destination, companyTrackingList.getCompanies());
                                callback.onSuccess(null);
                            } else {

                                callback.onFailure(response.code(), response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyTrackingList> call, Throwable t) {

                            callback.onFailure(-1, "Network Failure");
                        }
                    });

                } else {

                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<CompanyTrackingList> call, Throwable t) {

                callback.onFailure(-1, "Network Failure");
            }
        });
    }

    /**
     * Persisted
     **/

    // Fetch
    public RealmResults<ProjectTrackingList> fetchUserProjectTrackingList() {

        RealmResults<ProjectTrackingList> results = realm.where(ProjectTrackingList.class).findAll();
        return results;
    }


    public RealmResults<CompanyTrackingList> fetchUserCompanyTrackingList() {

        RealmResults<CompanyTrackingList> results = realm.where(CompanyTrackingList.class).findAll();
        return results;
    }

    public RealmResults<ProjectTrackingList> fetchProjectTrackingListsExcludingCurrentList(long trackingListId) {

        RealmResults<ProjectTrackingList> results = realm.where(ProjectTrackingList.class).notEqualTo("id", trackingListId).findAll();

        return results;
    }

    public RealmResults<CompanyTrackingList> fetchCompanyTrackingListsExcludingCurrentList(long trackingListId) {

        RealmResults<CompanyTrackingList> results = realm.where(CompanyTrackingList.class).notEqualTo("id", trackingListId).findAll();
        return results;
    }

    public ProjectTrackingList fetchProjectTrackingList(long id) {

        return realm.where(ProjectTrackingList.class).equalTo("id", id).findFirst();
    }

    public RealmResults<ProjectTrackingList> fetchProjectTrackingListsContainingProject(long projectId) {

        return realm.where(ProjectTrackingList.class).equalTo("projects.id", projectId).findAll();
    }

    public CompanyTrackingList fetchCompanyTrackingList(long id) {

        return realm.where(CompanyTrackingList.class).equalTo("id", id).findFirst();
    }

    public RealmResults<CompanyTrackingList> fetchCompanyTrackingListsContainingCompany(long companyId) {

        return realm.where(CompanyTrackingList.class).equalTo("companies.id", companyId).findAll();
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

    public RealmList<Project> deleteProjectsFromTrackingList(ProjectTrackingList trackingList, List<Project> toBeDeleted) {

        RealmList<Project> projects = trackingList.getProjects();
        realm.beginTransaction();
        projects.removeAll(toBeDeleted);
        realm.commitTransaction();

        return projects;
    }

    public void deleteProjectsFromTrackingListAsync(final long trackingListId, final List<Long> toBeDeleted, @NonNull final LecetCallback<ProjectTrackingList> callback) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm asyncRealm) {

                ProjectTrackingList threadSafeTrackingList = asyncRealm.where(ProjectTrackingList.class).equalTo("id", trackingListId).findFirst();
                try {
                    RealmList<Project> trackedProjects = threadSafeTrackingList.getProjects();

                    Iterator<Project> projectIterator = trackedProjects.iterator();
                    while (projectIterator.hasNext()) {

                        Project project = projectIterator.next();

                        if (toBeDeleted.contains(project.getId())) {
                            projectIterator.remove();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "deleteProjectsFromTrackingListAsync", e); //decided to use try-catch because intermittent occurs if a simple null condition is used in using the async process.
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                callback.onSuccess(null);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

                callback.onFailure(666, error.getMessage());
                error.printStackTrace();
            }
        });
    }

    public RealmList<Company> deleteCompaniesFromTrackingList(CompanyTrackingList trackingList, List<Company> toBeDeleted) {

        RealmList<Company> companies = trackingList.getCompanies();
        realm.beginTransaction();
        companies.removeAll(toBeDeleted);
        realm.commitTransaction();

        return companies;
    }

    public void deleteCompaniesFromTrackingListAsync(final long trackingListId, final List<Long> toBeDeleted, @NonNull final LecetCallback<CompanyTrackingList> callback) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm asyncRealm) {
                CompanyTrackingList threadSafeTrackingList = asyncRealm.where(CompanyTrackingList.class).equalTo("id", trackingListId).findFirst();
                try {
              //  if (threadSafeTrackingList != null) {
                    RealmList<Company> trackedCompanies = threadSafeTrackingList.getCompanies();

                    Iterator<Company> projectIterator = trackedCompanies.iterator();
                    while (projectIterator.hasNext()) {

                        Company company = projectIterator.next();

                        if (toBeDeleted.contains(company.getId())) {
                            projectIterator.remove();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "deleteCompaniesFromTrackingListAsync", e); //decided to use try-catch because intermittent occurs if a simple null condition is used in using the async process.
                    }
            //}
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                callback.onSuccess(null);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

                callback.onFailure(666, error.getMessage());
                error.printStackTrace();
            }
        });
    }

    // ADD

    public RealmList<Project> addProjectsToTrackingList(ProjectTrackingList trackingList, List<Project> toBeDeleted) {

        RealmList<Project> projects = trackingList.getProjects();
        realm.beginTransaction();
        projects.addAll(toBeDeleted);
        realm.commitTransaction();

        return projects;
    }

    public RealmList<Company> addCompaniesToTrackingList(CompanyTrackingList trackingList, List<Company> tobeAdded) {
        RealmList<Company> companies = trackingList.getCompanies();
        realm.beginTransaction();
        companies.clear();
        companies.addAll(tobeAdded);
        realm.commitTransaction();
        return companies;
    }

    // Project Update Mapping

    public void asyncCopyProjectTrackingListUpdatesToRealm(final long projectTrackingListID ,final List<Project> projects, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // Clear existing relationship and add new ones
                ProjectTrackingList asyncList = realm.where(ProjectTrackingList.class).equalTo("id", projectTrackingListID).findFirst();
                asyncList.getProjects().clear();

                for (Project project : projects) {

                    Project storedProject = realm.where(Project.class).equalTo("id", project.getId()).findFirst();

                    if (storedProject != null) {

                        storedProject.updateProject(realm, project, null);
                        realm.copyToRealmOrUpdate(storedProject);
                        asyncList.getProjects().add(storedProject);

                    } else {

                        realm.copyToRealmOrUpdate(project);
                        asyncList.getProjects().add(project);
                    }

                }
            }
        }, onSuccess, onError);
    }

    public void asyncMapUpdatesToProjects(final List<ActivityUpdate> updates, Realm.Transaction.OnSuccess successCallback,
                                          final Realm.Transaction.OnError errorCallback) {

        final WeakReference<ProjectDomain> domainWeakReference = new WeakReference<>(projectDomain);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // First we need add the updates to Realm
                List<ActivityUpdate> insertedUpdates = realm.copyToRealmOrUpdate(updates);

                // If we don't have a ProjectDomain then we exit.
                ProjectDomain domain = domainWeakReference.get();
                if (domain == null) {

                    // Notify error callback if its not null and cancel execution
                    if (errorCallback != null)
                        errorCallback.onError(new Throwable("ProjectDomain is null!"));

                    return;
                }

                // Now let's cycle through the updates and associate with their respective Project
                for (ActivityUpdate update : insertedUpdates) {

                    Project project = domain.fetchProjectById(realm, update.getProjectId());
                    if (project != null) {

                        RealmList<ActivityUpdate> projectUpdates = project.getUpdates();
                        if (projectUpdates == null) {
                            projectUpdates = new RealmList<>();
                        }

                        projectUpdates.add(update);

                        // Let's get the latest project update and assign it as most recent update for
                        // project
                        ActivityUpdate recentUpdate = projectUpdates.sort("updatedAt", Sort.DESCENDING).first();
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

    public CompanyTrackingList copyToRealmTransaction(CompanyTrackingList trackingList) {

        realm.beginTransaction();
        CompanyTrackingList persistedTrackingList = realm.copyToRealmOrUpdate(trackingList);
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

    public List<ActivityUpdate> copyActivityUpdatesToRealmTransaction(List<ActivityUpdate> updates) {

        realm.beginTransaction();
        List<ActivityUpdate> persisted = realm.copyToRealmOrUpdate(updates);
        realm.commitTransaction();
        return persisted;
    }
}
