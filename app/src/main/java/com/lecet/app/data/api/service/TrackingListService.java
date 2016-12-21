package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.MoveCompanyFromListRequest;
import com.lecet.app.data.api.request.MoveProjectFromListRequest;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * File: TrackingListService Created: 11/3/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface TrackingListService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}/projectTrackingLists")
    Call<List<ProjectTrackingList>> getUserProjectTrackingList(@Header("Authorization") String token, @Path("userId") long userId, @Query("filter") String filter);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}/companyTrackingLists")
    Call<List<CompanyTrackingList>> getUserCompanyTrackingList(@Header("Authorization") String token, @Path("userId") long userId, @Query("filter") String filter);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("projectlists/{project_tracking_list_id}/projects")
    Call<List<Project>> getProjectTrackingListDetail(@Header("Authorization") String token, @Path("project_tracking_list_id") long project_tracking_list_id, @Query("filter") String filter);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("projectlists/{project_tracking_list_id}/projects")
    Call<List<ActivityUpdate>> getProjectTrackingListUpdates(@Header("Authorization") String token, @Path("project_tracking_list_id") long project_tracking_list_id, @Query("filter") String filter);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("companylists/{company_tracking_list_id}/updates")
    Call<List<ActivityUpdate>> getCompanyTrackingListUpdates(@Header("Authorization") String token, @Path("company_tracking_list_id") long company_tracking_list_id, @Query("filter") String filter);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("companylists/{company_tracking_list_id}/syncItems")
    Call<CompanyTrackingList> syncCompanyTrackingList(@Header("Authorization") String token, @Path("company_tracking_list_id") long company_tracking_list_id, @Body MoveCompanyFromListRequest body);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("projectlists/{project_tracking_list_id}/syncItems")
    Call<ProjectTrackingList> syncProjectTrackingList(@Header("Authorization") String token, @Path("project_tracking_list_id") long project_tracking_list_id, @Body MoveProjectFromListRequest body);

}
