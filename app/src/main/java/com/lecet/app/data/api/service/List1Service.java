package com.lecet.app.data.api.service;

import com.lecet.app.data.api.response.Pojo3Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * File: List1Service Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface List1Service {

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @GET("tbd/list1/{obj_id}")  //TODO: replace
    Call<Pojo3Response> pending(@Header("Authorization") String authorization, @Path("obj_id") String obj_id);
}
