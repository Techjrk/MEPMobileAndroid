package com.lecet.app.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.lecet.app.data.api.service.BidService;
import com.lecet.app.data.api.service.ProjectService;
import com.lecet.app.data.api.service.TrackingListService;
import com.lecet.app.data.api.service.UserService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * File: LecetClient Created: 8/16/16 Author: domandtom <p> This code is copyright (c) 2016 Dom &
 * Tom Inc.
 */
public class LecetClient {

    private static final boolean IS_PRODUCTION = false;
    private static final String STAGING_ENDPOINT = "http://lecet.dt-staging.com/api/";
    private static final String PRODUCTION_ENDPOINT = "https://mepmobile.lecet.org/";
    private static final String ENDPOINT = IS_PRODUCTION ? PRODUCTION_ENDPOINT : STAGING_ENDPOINT;

    private static LecetClient ourInstance = new LecetClient();

    private BidService bidService;
    private UserService userService;
    private ProjectService projectService;
    private TrackingListService trackingListService;

    public static LecetClient getInstance() {
        return ourInstance;
    }

    private LecetClient() {

        initRetrofit();
    }

    private void initRetrofit() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Add loggin interceptor
        httpClient.addInterceptor(logging);

        // Custom GSON for date conversion
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ENDPOINT)
                .client(httpClient.build())
                .build();

        bidService = retrofit.create(BidService.class);
        projectService = retrofit.create(ProjectService.class);
        userService = retrofit.create(UserService.class);
        trackingListService = retrofit.create(TrackingListService.class);
    }


    public BidService getBidService() {
        return bidService;
    }

    public ProjectService getProjectService() {
        return projectService;
    }

    public UserService getUserService() {
        return userService;
    }

    public TrackingListService getTrackingListService() {
        return trackingListService;
    }
}
