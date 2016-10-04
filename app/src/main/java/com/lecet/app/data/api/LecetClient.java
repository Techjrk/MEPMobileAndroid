package com.lecet.app.data.api;

import com.lecet.app.data.api.service.AssetService;
import com.lecet.app.data.api.service.AuthService;
import com.lecet.app.data.api.service.CheckInService;
import com.lecet.app.data.api.service.DeviceService;
import com.lecet.app.data.api.service.List1Service;
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

    private AssetService assetService;
    private CheckInService checkInService;
    private DeviceService deviceService;
    private List1Service list1Service;
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

        assetService = retrofit.create(AssetService.class);
        checkInService = retrofit.create(CheckInService.class);
        deviceService = retrofit.create(DeviceService.class);
        list1Service = retrofit.create(List1Service.class);
        userService = retrofit.create(UserService.class);
    }


    public AssetService getAssetService() {
        return assetService;
    }

    public DeviceService getDeviceService() {
        return deviceService;
    }

    public CheckInService getCheckInService() {
        return checkInService;
    }

    public List1Service getList1Service() {
        return list1Service;
    }

    public UserService getUserService() {
        return userService;
    }

}
