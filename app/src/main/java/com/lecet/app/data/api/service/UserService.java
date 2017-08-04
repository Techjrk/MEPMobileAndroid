package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.FirebaseTokenRequest;
import com.lecet.app.data.api.request.UpdateUserProfileRequest;
import com.lecet.app.data.models.Access;
import com.lecet.app.data.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * File: UserService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface UserService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("LecetUsers/login")
    @FormUrlEncoded
    Call<Access> login(@Field("email") String email, @Field("password") String password,
                       @Field("deviceToken") String deviceToken, @Field("deviceType") String deviceType);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("LecetUsers/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}")
    Call<User> getUser(@Header("Authorization") String token, @Path("userId") long userId);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("LecetUsers/{userId}")
    Call<User> updateUser(@Header("Authorization") String token, @Path("userId") long userId, @Body UpdateUserProfileRequest user);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @PUT("LecetUsers/{userId}")
    @FormUrlEncoded
    Call<User> changePassword(@Header("Authorization") String token, @Path("userId") long userId, @Field("oldPassword") String oldPassword, @Field("password") String password, @Field("confirmation") String confirmation);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("installations")
    Call<ResponseBody> registerFirebaseToken(@Header("Authorization") String authToken, @Body FirebaseTokenRequest request);
}
