package com.lecet.app.domain;

import android.support.annotation.IntDef;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Jurisdiction;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
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
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: ProjectDomain Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDomain {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ENGINEERING, BUILDING, HOUSING, UTILITIES})
    public @interface BidGroup {
    }

    public static final int ENGINEERING = 101;
    public static final int BUILDING = 102;
    public static final int HOUSING = 103;
    public static final int UTILITIES = 105;

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public ProjectDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    /**
     * API
     **/

    public Call<Project> getProjectDetail(long projectID, Callback<Project> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String filter = "{\"include\":[{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}},\"secondaryProjectTypes\",\"projectStage\",{\"bids\":[\"company\",\"contact\"]},{\"contacts\":[\"contactType\",\"company\"]}]}";

        Call<Project> call = lecetClient.getProjectService().project(token, projectID, filter);
        call.enqueue(callback);

        return call;
    }

    public void getProjectsHappeningSoon(Date startDate, Date endDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);
        String formattedEnd = sdf.format(endDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"and\":[{\"bidDate\":{\"gte\":\"%s\"}},{\"bidDate\":{\"lte\":\"%s\"}}]}," +
                " \"limit\":%d, \"order\":\"firstPublishDate DESC\",\"dashboardTypes\":true}", formattedStart, formattedEnd, limit);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);
    }


    public void getProjectsHappeningSoon(int limit, Callback<List<Project>> callback) {

        Date current = new Date();
        Date endDate = DateUtility.addDays(30);
        getProjectsHappeningSoon(current, endDate, limit, callback);
    }


    public void getProjectsHappeningSoon(Callback<List<Project>> callback) {

        Date current = new Date();
        Date endDate = DateUtility.getLastDateOfTheCurrentMonth();
        int limit = 150;

        getProjectsHappeningSoon(current, endDate, limit, callback);
    }

    public void getProjectsRecentlyAdded(Date startDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"firstPublishDate\":{\"gte\":\"%s\"}}, \"limit\":%d, \"order\":\"firstPublishDate DESC\",\"dashboardTypes\":true}", formattedStart, limit);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);
    }


    public void getProjectsRecentlyAdded(int limit, Callback<List<Project>> callback) {

        Date endDate = DateUtility.addDays(-30);

        getProjectsRecentlyAdded(endDate, limit, callback);
    }

    public void getProjectsRecentlyAdded(Callback<List<Project>> callback) {

        int limit = 250;

        getProjectsRecentlyAdded(limit, callback);
    }


    public void getBidsRecentlyAdded(Date startDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"firstPublishDate\":{\"gte\":\"%s\"}}, \"limit\":%d, \"order\":\"firstPublishDate DESC\",\"dashboardTypes\":true}", formattedStart, limit);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);
    }


    public void getBidsRecentlyAdded(int limit, Callback<List<Project>> callback) {

        Date endDate = DateUtility.addDays(-30);

        getBidsRecentlyAdded(endDate, limit, callback);
    }


    public void getBidsRecentlyAdded(Callback<List<Project>> callback) {

        int limit = 150;

        getBidsRecentlyAdded(limit, callback);
    }


    public void getProjectsRecentlyUpdated(Date publishDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(publishDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], \"where\":{\"lastPublishDate\":{\"lte”:”%s”}}," +
                "\"limit\":%d,\"dashboardTypes\":true,\"order\":\"firstPublishDate DESC\"}", formattedStart, limit);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);
    }


    public void getProjectsRecentlyUpdated(int limit, Callback<List<Project>> callback) {

        Date publishDate = DateUtility.addDays(-30);
        getProjectsRecentlyUpdated(publishDate, limit, callback);
    }


    public void getProjectsRecentlyUpdated(Callback<List<Project>> callback) {

        int limit = 150;
        Date publishDate = DateUtility.addDays(-30);
        getProjectsRecentlyUpdated(publishDate, limit, callback);
    }

    public void getProjectsNear(double lat, double lng, int distance, Callback<ProjectsNearResponse> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String filter = "{\"include\":[\"projectStage\",{\"contacts\":[\"company\"]}],\"limit\":200, \"order\":\"id DESC\"}";
        Call<ProjectsNearResponse> call = lecetClient.getProjectService().projectsNear(token, lat, lng, distance, filter);
        call.enqueue(callback);
    }

    public void getProjectJurisdiction(long projectId, Callback<List<Jurisdiction>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<Jurisdiction>> call = lecetClient.getProjectService().projectJurisdiction(token, projectId);
        call.enqueue(callback);
    }

    public void hideProject(long projectId, Callback<ResponseBody> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<ResponseBody> call = lecetClient.getProjectService().hide(token, projectId);
        call.enqueue(callback);
    }

    public void unhideProject(long projectId, Callback<ResponseBody> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<ResponseBody> call = lecetClient.getProjectService().unhide(token, projectId);
        call.enqueue(callback);
    }

    public void getHiddenProjects(long userID, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<Project>> call = lecetClient.getProjectService().hiddenProjects(token, userID);
        call.enqueue(callback);
    }

    /**
     * Persisted
     **/

    public void removeChangeListeners(RealmChangeListener listener) {

        realm.removeChangeListener(listener);
    }

    public Project fetchProjectById(Realm realm, long id) {

        return realm.where(Project.class).equalTo("id", id).findFirst();
    }

    public Project fetchProjectById(long id) {

        return fetchProjectById(realm, id);
    }

    public RealmResults<Project> fetchProjectsHappeningSoon(Date startDate, Date endDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .between("bidDate", startDate, endDate)
                .findAll();

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsByBidDate(Date start, Date end) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .between("bidDate", start, end)
                .findAll();

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsRecentlyAdded(Date publishDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .greaterThanOrEqualTo("firstPublishDate", publishDate)
                .findAllSorted("firstPublishDate", Sort.DESCENDING);

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsRecentlyAdded(Date publishDate, int categoryId) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .greaterThanOrEqualTo("firstPublishDate", publishDate)
                .equalTo("primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("hidden", false)
                .findAllSorted("firstPublishDate", Sort.DESCENDING);

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsRecentlyUpdated(Date lastPublishDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .greaterThanOrEqualTo("lastPublishDate", lastPublishDate)
                .findAll();

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsRecentlyUpdated(Date lastPublishDate, int categoryId) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .greaterThanOrEqualTo("lastPublishDate", lastPublishDate)
                .equalTo("primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("hidden", false)
                .findAllSorted("lastPublishDate", Sort.DESCENDING);

        return projectsResult;
    }

    public RealmResults<PrimaryProjectType> fetchProjectTypeAsync(long primaryProjectTypeId, RealmChangeListener<RealmResults<PrimaryProjectType>> listener) {

        RealmResults<PrimaryProjectType> result = realm.where(PrimaryProjectType.class).equalTo("id", primaryProjectTypeId).findAllAsync();
        result.addChangeListener(listener);

        return result;
    }

    public RealmResults<Contact> fetchProjectContacts(long projectID) {

        RealmResults<Contact> contactsResult = realm.where(Contact.class)
                .equalTo("projectId", projectID)
                .findAllSorted("contactTypeId", Sort.DESCENDING);

        return contactsResult;
    }


    public RealmResults<Bid> fetchProjectBids(long projectID) {

        RealmResults<Bid> bidsResult = realm.where(Bid.class)
                .equalTo("projectId", projectID)
                .findAllSorted("amount", Sort.ASCENDING);

        return bidsResult;
    }

    public RealmResults<ActivityUpdate> fetchProjectActivityUpdates(long projectId, Date updateMinDate, RealmChangeListener<RealmResults<ActivityUpdate>> listener) {

        RealmResults<ActivityUpdate> result = realm.where(ActivityUpdate.class).equalTo("projectId", projectId).greaterThanOrEqualTo("updatedAt", updateMinDate).findAllAsync();
        result.addChangeListener(listener);

        return result;
    }

    public RealmResults<ActivityUpdate> fetchCompanyActivityUpdates(long projectId, Date updateMinDate, RealmChangeListener<RealmResults<ActivityUpdate>> listener) {

        RealmResults<ActivityUpdate> result = realm.where(ActivityUpdate.class).equalTo("companyId", projectId).greaterThanOrEqualTo("updatedAt", updateMinDate).findAllAsync();
        result.addChangeListener(listener);

        return result;
    }

    public RealmResults<Project> fetchHiddenProjects() {

        return realm.where(Project.class).equalTo("hidden", true).findAll();
    }

    public void setProjectHidden(Project project, boolean hidden) {
        realm.beginTransaction();
        project.setHidden(true);
        realm.commitTransaction();
    }

    public Project copyToRealmTransaction(Project project) {

        realm.beginTransaction();
        Project persistedProject = realm.copyToRealmOrUpdate(project);
        realm.commitTransaction();

        return persistedProject;
    }


    public List<Project> copyToRealmTransaction(List<Project> projects) {

        realm.beginTransaction();
        List<Project> persistedProjects = realm.copyToRealmOrUpdate(projects);
        realm.commitTransaction();
        return persistedProjects;
    }

    public void asyncCopyToRealm(final List<Project> projects, final boolean hidden, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (Project project : projects) {

                    Project storedProject = realm.where(Project.class).equalTo("id", project.getId()).findFirst();

                    if (storedProject != null) {

                        storedProject.updateProject(realm, storedProject, hidden);

                    } else {

                        realm.copyToRealm(project);
                    }
                }
            }
        }, onSuccess, onError);
    }

    public void asyncCopyToRealm(final List<Project> projects, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (Project project : projects) {

                    Project storedProject = realm.where(Project.class).equalTo("id", project.getId()).findFirst();

                    if (storedProject != null) {

                        storedProject.updateProject(realm, storedProject);

                    } else {

                        realm.copyToRealm(project);
                    }
                }
            }
        }, onSuccess, onError);
    }

    public void asyncCopyToRealm(final Project project, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Project storedProject = realm.where(Project.class).equalTo("id", project.getId()).findFirst();

                if (storedProject != null) {

                    storedProject.updateProject(realm, project);

                } else {

                    realm.copyToRealm(project);
                }
            }
        }, onSuccess, onError);
    }

    public RealmResults<Project> queryResult(@BidGroup int categoryId, RealmResults<Project> result, String sortFieldName) {

        return result.where()
                .equalTo("primaryProjectType.projectCategory.projectGroupId", categoryId)
                .equalTo("hidden", false)
                .findAllSorted(sortFieldName, Sort.DESCENDING);
    }



    /**
     * Utility
     **/
    private TreeMap<Long, TreeSet<Project>> sortRealmResults(RealmResults<Project> result, Comparator<Project> projectComparator, String sortFieldName) {

        RealmResults<Project> engineering = queryResult(ENGINEERING, result, sortFieldName);
        RealmResults<Project> building = queryResult(BUILDING, result, sortFieldName);
        RealmResults<Project> housing = queryResult(HOUSING, result, sortFieldName);
        RealmResults<Project> utilities = queryResult(UTILITIES, result, sortFieldName);

        TreeMap<Long, TreeSet<Project>> treeMap = new TreeMap<>();

        // Cycle through engineering bids
        if (engineering.size() > 0) {

            TreeSet<Project> projects = new TreeSet<>(projectComparator);

            for (int i = 0; i < engineering.size(); i++) {
                projects.add(engineering.get(i));
            }

            treeMap.put(Long.valueOf(ENGINEERING), projects);
        }

        // Cycle through building bids
        if (building.size() > 0) {

            TreeSet<Project> bids = new TreeSet<>(projectComparator);
            for (int i = 0; i < building.size(); i++) {
                bids.add(building.get(i));
            }

            treeMap.put(Long.valueOf(BUILDING), bids);
        }

        // Cycle through housing bids
        if (housing.size() > 0) {

            TreeSet<Project> bids = new TreeSet<>(projectComparator);
            for (int i = 0; i < housing.size(); i++) {
                bids.add(housing.get(i));
            }

            treeMap.put(Long.valueOf(HOUSING), bids);
        }

        // Cycle through utilities bids
        if (engineering.size() > 0) {

            TreeSet<Project> bids = new TreeSet<>(projectComparator);
            for (int i = 0; i < utilities.size(); i++) {
                bids.add(utilities.get(i));
            }

            treeMap.put(Long.valueOf(UTILITIES), bids);
        }

        return treeMap;
    }


    public TreeMap<Long, TreeSet<Project>> sortRealmResultsByFirstPublished(RealmResults<Project> result) {

        Comparator<Project> projectComparator = new Comparator<Project>() {
            @Override
            public int compare(Project proj, Project t1) {
                return proj.getFirstPublishDate().after(t1.getFirstPublishDate()) ? 1 : -1;
            }
        };

        return sortRealmResults(result, projectComparator, "firstPublishDate");
    }


    public TreeMap<Long, TreeSet<Project>> sortRealmResultsByLastPublished(RealmResults<Project> result) {

        Comparator<Project> projectComparator = new Comparator<Project>() {
            @Override
            public int compare(Project proj, Project t1) {
                return proj.getLastPublishDate().after(t1.getLastPublishDate()) ? 1 : -1;
            }
        };

        return sortRealmResults(result, projectComparator, "lastPublishDate");
    }
}
