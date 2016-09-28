package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.CheckInRequest;
import com.lecet.app.data.api.response.CheckinResponse;
import com.lecet.app.data.models.Pojo4;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * File: CheckinService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface CheckInService {

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @GET("checkins")
    Call<List<Pojo4>> checkins(@Header("Authorization") String authorization);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @POST("checkins")
    Call<CheckinResponse> checkin(@Header("Authorization") String authorization, @Body CheckInRequest checkInRequest);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @GET("checkins/{udid}")
    Call<CheckinResponse> checkinByID(@Header("Authorization") String authorization, @Path("udid") String udid);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @PUT("checkins/{udid}")
    Call<CheckinResponse> update(@Header("Authorization") String authorization, @Path("udid") String udid);
}
