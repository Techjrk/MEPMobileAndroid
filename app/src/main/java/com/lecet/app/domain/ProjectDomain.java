package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: ProjectDomain Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public ProjectDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    /** API **/

    public void getProjectsHappeningSoon(Date startDate, Date endDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);
        String formattedEnd = sdf.format(endDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"and\":[{\"bidDate\":{\"gte\":\"%s\"}},{\"bidDate\":{\"lte\":\"%s\"}}]}," +
                " \"limit\":%d, \"order\":\"firstPublishDate DESC\"}", formattedStart, formattedEnd, limit);

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
        Date endDate = DateUtility.addDays(30);
        int limit = 150;

        getProjectsHappeningSoon(current, endDate, limit, callback);
    }

    public void getProjectsRecentlyAdded(Date startDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"firstPublishDate\":{\"gte\":\"%s\"}}, \"limit\":%d, \"order\":\"firstPublishDate DESC\"}", formattedStart, limit);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);
    }


    public void getProjectsRecentlyAdded(int limit, Callback<List<Project>> callback) {

        Date endDate = DateUtility.addDays(-30);

        getProjectsRecentlyAdded(endDate, limit, callback);
    }

    public void getProjectsRecentlyAdded(Callback<List<Project>> callback) {

        int limit = 150;

        getProjectsRecentlyAdded(limit, callback);
    }


    public void getBidsRecentlyAdded(Date startDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"firstPublishDate\":{\"gte\":\"%s\"}}, \"limit\":%d, \"order\":\"firstPublishDate DESC\"}", formattedStart, limit);

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

    public void getProjectsNear(double lat, double lng, int distance, Callback<ProjectsNearResponse> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String filter = "{\"include\":[\"projectStage\",{\"contacts\":[\"company\"]}],\"limit\":200, \"order\":\"id DESC\"}";
        Call<ProjectsNearResponse> call = lecetClient.getProjectService().projectsNear(token, lat, lng, distance, filter);
        call.enqueue(callback);
    }

    /** Persisted **/

    public RealmResults<Project> fetchProjectsHappeningSoon(Date startDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .greaterThan("bidDate", startDate)
                .findAll();

        return projectsResult;
    }

    public RealmResults<Project> fetchProjectsByBidDate(Date bidDate) {

        RealmResults<Project> projectsResult = realm.where(Project.class)
                .equalTo("hidden", false)
                .greaterThan("bidDate", bidDate)
                .findAll();

        return projectsResult;
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

}
