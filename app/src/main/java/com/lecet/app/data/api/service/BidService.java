package com.lecet.app.data.api.service;

import com.lecet.app.data.models.Bid;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * File: AssetService Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public interface BidService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Bids")
    Call<List<Bid>> bids(@Header("Authorization") String authorization, @Query("filter") String filter);
}
