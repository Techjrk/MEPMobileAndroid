package com.lecet.app.domain;


import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Jurisdiction;
import com.lecet.app.data.models.NotePost;
import com.lecet.app.data.models.PhotoPost;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.data.models.ProjectPost;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;
import com.lecet.app.utility.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: ProjectDomain Created: 10/5/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDomain {

    private static final String TAG = "ProjectDomain";

    private static final int DASHBOARD_CALL_LIMIT = 250;

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

    private String filterMPN;

    public String getFilterMPN() {
        return filterMPN;
    }

    public void setFilterMPN(String filterMPN) {
        if (filterMPN.equals("default")) {
            initFilter();
        } else {
            this.filterMPN = filterMPN;
        }
    }

    public ProjectDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        initFilter();
    }

    void initFilter() {
        String filter = "{\"include\":[\"projectStage\",{\"contacts\":[\"company\"]},\"userNotes\",\"images\"],\"limit\":200, \"order\":\"id DESC\"}";
        setFilterMPN(filter);
    }

    public Realm getRealm() {
        return realm;
    }

    /**
     * API
     **/

    public Call<Project> postProject(ProjectPost projectPost) {
        String token = sharedPreferenceUtil.getAccessToken();
        Call<Project> call = lecetClient.getProjectService().addProject(token, projectPost);

        return call;
    }

    public Call<Project> updateProject(long projectId, ProjectPost projectPost) {
        //Log.d(TAG, "updateProject() called with: projectId = [" + projectId + "], projectPost = [" + projectPost + "]");

        String token = sharedPreferenceUtil.getAccessToken();
        Call<Project> call = lecetClient.getProjectService().updateProject(token, projectId, projectPost);

        return call;
    }

    public Call<Project> getProjectDetail(long projectID, Callback<Project> callback) {
        String token = sharedPreferenceUtil.getAccessToken();

        String filter = "{\"include\":[{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}},\"secondaryProjectTypes\",\"projectStage\",{\"bids\":[\"company\",\"contact\"]},{\"contacts\":[\"contactType\",\"company\"]}]}";

        Call<Project> call = lecetClient.getProjectService().project(token, projectID, filter);
        call.enqueue(callback);

        return call;
    }

    public Call<ProjectNote> postNote(long projectID, NotePost notePost){
        String token = sharedPreferenceUtil.getAccessToken();
        Call<ProjectNote> call = lecetClient.getProjectService().addNote(token, projectID, notePost);

        return call;
    }

    //TODO - move updateNote and deleteNote to a new NoteDomain class?
    public Call<ProjectNote> updateNote(long noteID, NotePost notePost){
        String token = sharedPreferenceUtil.getAccessToken();
        Call<ProjectNote> call = lecetClient.getProjectService().updateNote(token, noteID, notePost);

        return call;
    }

    public Call<ProjectNote> deleteNote(long noteID){
        String token = sharedPreferenceUtil.getAccessToken();
        Call<ProjectNote> call = lecetClient.getProjectService().deleteNote(token, noteID);

        return call;
    }

    public Call<ProjectPhoto> postPhoto(long projectID, PhotoPost photoPost){
        String token = sharedPreferenceUtil.getAccessToken();
        Call<ProjectPhoto> call = lecetClient.getProjectService().addPhoto(token, projectID, photoPost);

        return call;
    }

    //TODO - move updatePhoto and deletePhoto to a new PhotoDomain class?
    public Call<ProjectPhoto> updatePhoto(long photoID, PhotoPost photoPost){
        String token = sharedPreferenceUtil.getAccessToken();
        Call<ProjectPhoto> call = lecetClient.getProjectService().updatePhoto(token, photoID, photoPost);

        return call;
    }

    public Call<ProjectPhoto> deletePhoto(long photoID){
        String token = sharedPreferenceUtil.getAccessToken();
        Call<ProjectPhoto> call = lecetClient.getProjectService().deletePhoto(token, photoID);

        return call;
    }

    public Call<List<Project>> getProjectsHappeningSoon(Date startDate, Date endDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        //Note: Removed the time to match with iOS
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        String formattedStart = sdf.format(startDate);
        String formattedEnd = sdf.format(endDate);
 
        //To match with iOS filter, the order is in firstPublishDate
        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"and\":[{\"bidDate\":{\"gte\":\"%s\"}},{\"bidDate\":{\"lt\":\"%s\"}}]}," +
                " \"limit\":%d, \"order\":\"firstPublishDate DESC\",\"dashboardTypes\":true}", formattedStart, formattedEnd, limit);

        Log.d(TAG, "getProjectsHappeningSoon() called: formattedStart: " + formattedStart);
        Log.d(TAG, "getProjectsHappeningSoon() called: formattedEnd: " + formattedEnd);
        Log.d(TAG, "getProjectsHappeningSoon() called: filter: " + filter);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);

        return call;
    }

    public Call<List<Project>> getProjectsHappeningSoon(Callback<List<Project>> callback) {
        beforeUpdateRealm4HappeningSoon(getStartDateMidnightUTC(), getLastDateOfCurrentMonthUTC());
        return getProjectsHappeningSoon(getStartDateMidnightUTC(), getLastDateOfCurrentMonthUTC(), DASHBOARD_CALL_LIMIT, callback);
    }

    private Date getStartDateMidnightUTC() {
        Calendar startDateMidnight = new GregorianCalendar();
        startDateMidnight.set(Calendar.HOUR_OF_DAY, 0);
        startDateMidnight.set(Calendar.MINUTE, 0);
        startDateMidnight.set(Calendar.SECOND, 0);
        startDateMidnight.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.d("timezonedate","timezonedate"+": month="+startDateMidnight.get(Calendar.MONTH)+": hour_day="+startDateMidnight.get(Calendar.HOUR_OF_DAY)+" : hour=" +startDateMidnight.get(Calendar.HOUR)+": min="+startDateMidnight.get(Calendar.MINUTE));
        return startDateMidnight.getTime();
    }

    private Date getLastDateOfCurrentMonthUTC() {
        Date lastDayOfThisMonth = DateUtility.getLastDateOfTheCurrentMonth();

        Calendar lastDateOfCurrentMonth = new GregorianCalendar();
        lastDateOfCurrentMonth.setTime(lastDayOfThisMonth);
        lastDateOfCurrentMonth.set(Calendar.HOUR_OF_DAY, 23);
        lastDateOfCurrentMonth.set(Calendar.MINUTE, 59);
        lastDateOfCurrentMonth.set(Calendar.SECOND, 59);
        lastDateOfCurrentMonth.setTimeZone(TimeZone.getTimeZone("UTC"));
        return lastDateOfCurrentMonth.getTime();
    }


    public Call<List<Project>> getProjectsRecentlyAdded(Date startDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"firstPublishDate\":{\"gte\":\"%s\"}}, \"limit\":%d, \"order\":\"firstPublishDate DESC\",\"dashboardTypes\":true}", formattedStart, limit);

        Log.d(TAG, "getProjectsRecentlyAdded() called: filter: " + filter);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);

        return call;
    }


    public Call<List<Project>> getProjectsRecentlyAdded(int limit, Callback<List<Project>> callback) {

        Date endDate = DateUtility.addDays(-30);

        return getProjectsRecentlyAdded(endDate, limit, callback);
    }

    public Call<List<Project>> getProjectsRecentlyAdded(Callback<List<Project>> callback) {

        int limit = DASHBOARD_CALL_LIMIT;

        return getProjectsRecentlyAdded(limit, callback);
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

        int limit = DASHBOARD_CALL_LIMIT;

        getBidsRecentlyAdded(limit, callback);
    }


    public Call<List<Project>> getProjectsRecentlyUpdated(Date publishDate, int limit, Callback<List<Project>> callback) {
        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(publishDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"lastPublishDate\":{\"gte\":\"%s\"}}, \"limit\":%d, \"order\":\"lastPublishDate DESC\",\"dashboardTypes\":true}", formattedStart, limit);

        Log.d(TAG, "getProjectsRecentlyUpdated() called: filter: " + filter);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);

        return call;
    }


    public Call<List<Project>> getProjectsRecentlyUpdated(int limit, Callback<List<Project>> callback) {

        Date publishDate = DateUtility.addDays(-30);
        return getProjectsRecentlyUpdated(publishDate, limit, callback);
    }


    public Call<List<Project>> getProjectsRecentlyUpdated(Callback<List<Project>> callback) {

        int limit = DASHBOARD_CALL_LIMIT;
        Date publishDate = DateUtility.addDays(-30);
        return getProjectsRecentlyUpdated(publishDate, limit, callback);
    }

    public Call<ProjectsNearResponse> getProjectsNear(double lat, double lng, int distance, Callback<ProjectsNearResponse> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
//        String filter = "{\"include\":[\"projectStage\",{\"contacts\":[\"company\"]}],\"limit\":200, \"order\":\"id DESC\"}";
//        Call<ProjectsNearResponse> call = lecetClient.getProjectService().projectsNear(token, lat, lng, distance, filter);
        Call<ProjectsNearResponse> call = lecetClient.getProjectService().projectsNear(token, lat, lng, distance, getFilterMPN());
        call.enqueue(callback);
        return call;
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

    public Project fetchProjectAsync(final long id, RealmObjectChangeListener<Project> listener) {

        Project results  = realm.where(Project.class).equalTo("id", id).findFirstAsync();
        results.addChangeListener(listener);

        return results;
    }

    public RealmResults<Project> fetchProjectsHappeningSoon(Date startDate, Date endDate) {
        Log.d(TAG, "fetchProjectsHappeningSoon() called with: startDate = [" + startDate + "], endDate = [" + endDate + "]");
        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .equalTo("mbsItem", true)
                .between("bidDateCalendar", startDate, endDate)
                .findAllSorted(new String[]{"bidDateCalendar","title"},new Sort[]{Sort.ASCENDING,Sort.ASCENDING});

        Log.d(TAG, "fetchProjectsHappeningSoon() called projectsResult size: " + projectsResult.size());

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsByBidDate(Date start, Date end) {
        Log.d(TAG, "fetchProjectsByBidDate() called with: start = [" + start + "], end = [" + end + "]");

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .equalTo("mbsItem", true)
                .between("bidDateCalendar", start, end)
                .findAllSorted(new String[]{"bidDateCalendar","title"},new Sort[]{Sort.ASCENDING,Sort.ASCENDING});

        Log.d(TAG, "fetchProjectsByBidDate() called projectsResult size: " + projectsResult.size());

        return projectsResult;
    }

    public RealmResults<Project> fetchProjectsRecentlyAdded(Date publishDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .equalTo("mraItem", true)
                .greaterThanOrEqualTo("firstPublishDate", publishDate)
                .findAllSorted("firstPublishDate", Sort.DESCENDING);

        return projectsResult;
    }

    public RealmResults<Project> fetchProjectsRecentlyAdded(Date publishDate, int categoryId) {

        RealmResults<Project> projectsResult;

        if (categoryId == BidDomain.CONSOLIDATED_CODE_B) {

            projectsResult = realm.where(Project.class)
                    .greaterThanOrEqualTo("firstPublishDate", publishDate)
                    .equalTo("mraItem", true)
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.HOUSING)
                    .or()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.BUILDING)
                    .or()
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("primaryProjectType.buildingOrHighway", "B")
                    .endGroup()
                    .endGroup()
                    .equalTo("hidden", false)
                    .findAllSorted("firstPublishDate", Sort.DESCENDING);
        }
        else if (categoryId == BidDomain.CONSOLIDATED_CODE_H) {

            projectsResult = realm.where(Project.class)
                    .greaterThanOrEqualTo("firstPublishDate", publishDate)
                    .equalTo("mraItem", true)
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.ENGINEERING)
                    .or()
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("primaryProjectType.buildingOrHighway", "H")
                    .endGroup()
                    .endGroup()
                    .equalTo("hidden", false)
                    .findAllSorted("firstPublishDate", Sort.DESCENDING);
        }
        else {

            projectsResult = realm.where(Project.class)
                    .greaterThanOrEqualTo("firstPublishDate", publishDate)
                    .equalTo("mraItem", true)
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", categoryId)
                    .equalTo("hidden", false)
                    .findAllSorted("firstPublishDate", Sort.DESCENDING);
        }

        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsRecentlyUpdated(Date lastPublishDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .equalTo("mruItem", true)
                .greaterThanOrEqualTo("lastPublishDate", lastPublishDate)
                .findAllSorted("lastPublishDate", Sort.DESCENDING);
        return projectsResult;
    }


    public RealmResults<Project> fetchProjectsRecentlyUpdated(Date lastPublishDate, int categoryId) {

        RealmResults<Project> projectsResult;

        if (categoryId == BidDomain.CONSOLIDATED_CODE_B) {

            projectsResult = realm.where(Project.class)
                    .greaterThanOrEqualTo("lastPublishDate", lastPublishDate)
                    .equalTo("mruItem", true)
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.HOUSING)
                    .or()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.BUILDING)
                    .or()
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("primaryProjectType.buildingOrHighway", "B")
                    .endGroup()
                    .endGroup()
                    .equalTo("hidden", false)
                    .findAllSorted("lastPublishDate", Sort.DESCENDING);
        }
        else if (categoryId == BidDomain.CONSOLIDATED_CODE_H) {

            projectsResult = realm.where(Project.class)
                    .greaterThanOrEqualTo("lastPublishDate", lastPublishDate)
                    .equalTo("mruItem", true)
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.ENGINEERING)
                    .or()
                    .beginGroup()
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", BidDomain.UTILITIES)
                    .equalTo("primaryProjectType.buildingOrHighway", "H")
                    .endGroup()
                    .endGroup()
                    .equalTo("hidden", false)
                    .findAllSorted("lastPublishDate", Sort.DESCENDING);
        } else {

            projectsResult = realm.where(Project.class)
                    .greaterThanOrEqualTo("lastPublishDate", lastPublishDate)
                    .equalTo("mruItem", true)
                    .equalTo("primaryProjectType.projectCategory.projectGroupId", categoryId)
                    .equalTo("hidden", false)
                    .findAllSorted("lastPublishDate", Sort.DESCENDING);
        }


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

        RealmResults<ActivityUpdate> result = realm.where(ActivityUpdate.class)
                .equalTo("projectId", projectId).greaterThanOrEqualTo("updatedAt", updateMinDate)
                .findAllSortedAsync("updatedAt", Sort.DESCENDING);

        result.addChangeListener(listener);

        return result;
    }

    public RealmResults<ActivityUpdate> fetchCompanyActivityUpdates(long projectId, Date updateMinDate, RealmChangeListener<RealmResults<ActivityUpdate>> listener) {

        RealmResults<ActivityUpdate> result = realm.where(ActivityUpdate.class).equalTo("companyId", projectId)
                .greaterThanOrEqualTo("updatedAt", updateMinDate)
                .findAllSortedAsync("updatedAt", Sort.DESCENDING);
        result.addChangeListener(listener);

        return result;
    }

    public Call<List<ProjectNote>> fetchProjectNotes(long projectID, Callback<List<ProjectNote>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        String filter = "{\"include\":[\"author\"]}";

        Call<List<ProjectNote>> call = lecetClient.getProjectService().projectNotes(token, projectID, filter);
        call.enqueue(callback);

        return call;
    }

    public Call<List<ProjectPhoto>> fetchProjectImages(long projectID, Callback<List<ProjectPhoto>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        String filter = "{\"include\":[\"user\"]}";

        Call<List<ProjectPhoto>> call = lecetClient.getProjectService().projectImages(token, projectID, filter);
        call.enqueue(callback);

        return call;
    }

    public RealmResults<Project> fetchHiddenProjects() {

        return realm.where(Project.class).equalTo("hidden", true).findAll();
    }

    public void setProjectHidden(Project project, boolean hidden) {
        realm.beginTransaction();
        project.setHidden(hidden);
        realm.commitTransaction();
    }

    public void setProjectHiddenAsync(Project project, final boolean hidden) {

        final long projectID = project.getId();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm asyncRealm) {

                RealmResults<Project> results = asyncRealm.where(Project.class).equalTo("id", projectID).findAll();
                if (results.size() > 0) {

                    Project p = results.first();
                    p.setHidden(hidden);
                }
            }
        });
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

    private void beforeUpdateRealm4HappeningSoon(final Date startDate, final Date endDate) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Project> storedProject = realm.where(Project.class)
                        .between("bidDateCalendar", startDate, endDate)
                        .findAllSorted("bidDateCalendar", Sort.ASCENDING);
                for (Project project : storedProject) {

                    if (project != null) {
                        project.setMbsItem(false);
                        realm.copyToRealmOrUpdate(project);

                    } else {
                        realm.copyToRealmOrUpdate(project);
                    }
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("realmsoonsuccess","realm soon success");
            }},new Realm.Transaction.OnError() {

                @Override
                public void onError(Throwable error) {
                    Log.d("realmsoonerror","realm soon error");
                }});
    }
    public void asyncCopyToRealm(final List<Project> projects, final boolean hidden, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (Project project : projects) {

                    Project storedProject = realm.where(Project.class).equalTo("id", project.getId()).findFirst();

                    if (storedProject != null) {

                        storedProject.updateProject(realm, project, hidden);
                        realm.copyToRealmOrUpdate(storedProject);

                    } else {

                        realm.copyToRealmOrUpdate(project);
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

                        storedProject.updateProject(realm, project, null);
                        realm.copyToRealmOrUpdate(storedProject);

                    } else {

                        realm.copyToRealmOrUpdate(project);
                    }
                }
            }
        }, onSuccess, onError);
    }

    public void asyncCopyToRealm(final List<Project> projects, @Nullable final Boolean isHidden, @Nullable final Boolean mbsItem,
                                 @Nullable final Boolean mraItem, @Nullable final Boolean mruItem,
                                 @NonNull Realm.Transaction.OnSuccess onSuccess, @NonNull Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (Project project : projects) {

                    project.convertBidUTCDate2LocalDate();
                    Project storedProject = realm.where(Project.class).equalTo("id", project.getId()).findFirst();

                    if (storedProject != null) {

                        storedProject.updateProject(realm, project, isHidden, mbsItem, mraItem, mruItem);
                        realm.copyToRealmOrUpdate(storedProject);

                    } else {

                        if (isHidden != null) {

                            project.setHidden(isHidden.booleanValue());
                        }

                        if (mbsItem != null) {

                            project.setMbsItem(mbsItem.booleanValue());
                        }

                        if (mraItem != null) {

                            project.setMraItem(mraItem.booleanValue());
                        }

                        if (mruItem != null) {

                            project.setMruItem(mruItem.booleanValue());
                        }

                        realm.copyToRealmOrUpdate(project);
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

                    storedProject.updateProject(realm, project, null);
                    realm.copyToRealmOrUpdate(storedProject);

                } else {

                    realm.copyToRealmOrUpdate(project);
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
