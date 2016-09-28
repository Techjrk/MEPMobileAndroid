package com.lecet.app.data.api.service;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by domandtom on 8/17/16.
 */

public interface DeviceService {

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @POST("device")
    Call<Response> register(@Header("Authorization") String authorization, @Field("token_device") String token_device);
}
