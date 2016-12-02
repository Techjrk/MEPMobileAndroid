package com.lecet.app.data.api.service;

import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * File: SearchService domandtom 2016
 */
public interface SearchService {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}/activities")
    Call<List<SearchResult>> getSearchRecentlyViewed(@Header("Authorization") String token, @Path("userId") long userId);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}/activities")
    Call<List<SearchResult>> getSearchRecentlyViewedWithFilter(@Header("Authorization") String token, @Path("userId") long userId, @Query("filter") String filter);

    //***SavedSearch
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("LecetUsers/{userId}/searches")
    Call<List<SearchSaved>> getSearchSaved(@Header("Authorization") String token, @Path("userId") long userId);

    //***Project Search
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Projects/search")
    Call<SearchProject> getSearchProjectInit(@Header("Authorization") String token, @Query("q") String vq, @Query("filter") String filter);

    //***Project SearchDetail
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Projects/{pId}")
    Call<Project> getProjectDetail(@Header("Authorization") String token, @Path("pId") long pId);

    //***Project Search
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Projects/search")
    Call<List<Project>> getSearchProject(@Header("Authorization") String token, @Query("q") String vq, @Query("filter") String filter);


    //***Company Search
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Companies/search")
    Call<List<Company>> getSearchCompany(@Header("Authorization") String token, @Query("q") String vq, @Query("filter") String filter);

}
