package com.lecet.app.data.api.service;

import com.lecet.app.data.models.ProjectTrackingList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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


}
