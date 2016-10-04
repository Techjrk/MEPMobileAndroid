package com.lecet.app.data.api.service;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * File: AssetService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface BidService {

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: image/jpeg"
    })
    @POST("images")
    Call<Response> uploadImage(@Header("Authorization") String authorization, @Body String base64);
}
