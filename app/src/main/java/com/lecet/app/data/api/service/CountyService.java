package com.lecet.app.data.api.service;

import com.lecet.app.data.models.County;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by getdevs on 05/07/2017.
 */

public interface CountyService {

    @GET("Counties")
    Call<List<County>> getCounty(@Header("Authorization") String authorization , @Query("filter") String filter);
}
