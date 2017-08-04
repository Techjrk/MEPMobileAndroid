package com.lecet.app.data.api.service;

import com.lecet.app.data.models.geocoding.GeocodeAddress;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jasonm on 6/5/17.
 */

public interface LocationService {

    /*
     * Google Maps API: Get address from location lat/lng
     */
    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Call<GeocodeAddress> getAddressFromLocation(@Query("latlng") String latlng, @Query("result_type") String resultType, @Query("key") String key);


}
