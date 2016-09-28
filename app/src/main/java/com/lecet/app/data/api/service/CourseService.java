package com.lecet.app.data.api.service;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Created by domandtom on 8/17/16.
 */

public interface CourseService {

    @Headers({
            "Accept: application/vnd.api.v1+json",
            "Content-Type: application/json"
    })
    @GET("courses")
    Call<Response> all(@Header("Authorization") String authorization);
}
