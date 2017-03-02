package com.lecet.app.data.api.service;

import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * File: SearchService domandtom 2016
 */
public interface SearchService {
    //*** User recently viewed
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
    Call<SearchProject> getSearchProjectQuery(@Header("Authorization") String token, @Query("q") String vq, @Query("filter") String filter);


    //***Company Search
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Companies/search")
    Call<SearchCompany> getSearchCompanyQuery(@Header("Authorization") String token, @Query("q") String vq, @Query("filter") String filter);

    //***Contacts Search
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Contacts/search")
    Call<SearchContact> getSearchContactQuery(@Header("Authorization") String token, @Query("q") String vq, @Query("filter") String filter);

    //*** Jurisdiction content items
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("Regions/tree")
    Call<List<SearchFilterJurisdictionMain>> getSearchFilterJurisdictionItems();

    //*** Project Types content items
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("ProjectGroups")
    Call<List<SearchFilterProjectTypesMain>> getSearchFilterProjectTypesItems(@Header("Authorization") String token, @Query("filter[include][projectCategories]") String filter);

    //*** Stage content items
    @GET("ProjectParentStages")
    Call<List<SearchFilterStagesMain>> getSearchFilterStagesItems(@Header("Authorization") String token, @Query("filter[include]") String filter);

    //*** Save User recently viewed
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("Activities")
    Call<ResponseBody> saveRecent(@Header("Authorization") String token, @Body RequestBody body);

    // Save Search
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("Searches")
    Call<ResponseBody> saveProjectSearch(@Header("Authorization") String token, @Body RequestBody body);

}
