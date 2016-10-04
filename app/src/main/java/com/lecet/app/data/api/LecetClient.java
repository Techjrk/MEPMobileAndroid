package com.lecet.app.data.api;

import com.lecet.app.data.api.service.BidService;
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
    private static final String STAGING_ENDPOINT = "http://lecet.dt-staging.com/";
    private static final String PRODUCTION_ENDPOINT = "https://mepmobile.lecet.org/";
    private static final String ENDPOINT = IS_PRODUCTION ? PRODUCTION_ENDPOINT : STAGING_ENDPOINT;

    private static LecetClient ourInstance = new LecetClient();

    private BidService bidService;
    private UserService userService;

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

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ENDPOINT)
                .client(httpClient.build())
                .build();

        bidService = retrofit.create(BidService.class);
        userService = retrofit.create(UserService.class);
    }


    public BidService getBidService() {
        return bidService;
    }

    public UserService getUserService() {
        return userService;
    }

}
