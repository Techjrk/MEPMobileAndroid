package com.lecet.app.data.api.service;

import com.lecet.app.data.models.Company;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * File: CompanyService Created: 1/23/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public interface CompanyService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Companies/{companyID}")
    Call<Company> company(@Header("Authorization") String authorization, @Path("companyID") long companyID, @Query("filter") String filter);
}
