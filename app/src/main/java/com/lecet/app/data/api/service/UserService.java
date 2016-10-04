package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.CreateUserRequest;
import com.lecet.app.data.api.response.UserResponse;
import com.lecet.app.data.models.Access;

import okhttp3.Response;
import okhttp3.ResponseBody;
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

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("LecetUsers/login")
    @FormUrlEncoded
    Call<Access> login(@Field("email") String email, @Field("password") String password);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("LecetUsers/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

}
