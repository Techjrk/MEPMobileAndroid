package com.lecet.app.data.api.service;

import com.lecet.app.data.models.Access;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * File: AuthService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface AuthService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("LecetUsers/authorize")
    @FormUrlEncoded
    Call<Access> login(@Field("email") String email, @Field("password") String password);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @POST("auth/refresh-token")
    Call<Access> refreshToken(@Field("email") String email, @Field("password") String password);
}
