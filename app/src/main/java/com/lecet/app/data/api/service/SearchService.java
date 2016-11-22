package com.lecet.app.data.api.service;

import com.lecet.app.data.models.Access;
import com.lecet.app.data.models.SearchList;
import com.lecet.app.data.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * File: SearchService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface SearchService {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}/activities")
    Call<List<SearchList>> getSearchRecentlyViewed(@Header("Authorization") String token, @Path("userId") long userId);


}
