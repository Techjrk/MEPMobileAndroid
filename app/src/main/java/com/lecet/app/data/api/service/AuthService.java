package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.AuthRequest;
import com.lecet.app.data.models.Access;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * File: AuthService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface AuthService {

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @POST("oauth/authorize")
    Call<Access> login(@Body AuthRequest authRequest);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @POST("auth/refresh-token")
    Call<Access> refreshToken(@Body AuthRequest authRequest);
}
