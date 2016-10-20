package com.lecet.app.data.api.service;

import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * File: ProjectService Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface ProjectService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects")
    Call<List<Project>> projects(@Header("Authorization") String authorization, @Query("filter") String filter);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects/near")
    Call<ProjectsNearResponse> projectsNear(@Header("Authorization") String authorization, @Query("lat") double lat, @Query("lng") double lng, @Query("dist") int dist, @Query("filter") String filter);
}
