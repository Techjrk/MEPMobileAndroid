package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.CreateUserRequest;
import com.lecet.app.data.api.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * File: UserService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface UserService {

    @POST("users")
    Call<UserResponse> create(@Body CreateUserRequest user);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @GET("me")
    Call<UserResponse> me(@Header("Authorization") String authorization);

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @GET("users/email/{user_email}")
    Call<UserResponse> userByEmail(@Header("Authorization") String authorization, @Path("user_email") String userEmail);


    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @FormUrlEncoded
    @POST("users/forgot-password")
    Call<UserResponse> forgotPassword(@Header("Authorization") String authorization, @Field("email") String email);

}
