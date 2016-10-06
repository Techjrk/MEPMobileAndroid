package com.lecet.app.data.api.service;

import android.support.test.runner.AndroidJUnit4;

import com.lecet.app.data.models.Access;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * File: UserServiceTest Created: 10/3/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */


@RunWith(AndroidJUnit4.class)
public class UserServiceTest {

    private MockWebServer server;
    private Retrofit retrofit;
    private UserService userService;

    @Before
    public void setUp() throws Exception {

        server = new MockWebServer();
        server.start();

        HttpUrl url = server.url("/");

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url.toString())
                .build();

        userService = retrofit.create(UserService.class);
    }

    @Test
    public void loginSuccess() throws Exception {

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" + "\"id\": \"kn0bxmGnboaWoUQn2EXCpDGjKpeJ5LgDWfcSqw4G8o2GP7lxm8ATAWLv01Tq6sVS\"," +
                        "\"ttl\": 1209600," + "\"created\": \"2016-10-03T20:47:20.536Z\"," + "\"userId\": 5" + "}"));


        Call<Access> call = userService.login("test@test.com", "password");
        Response<Access> response = call.execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(5, response.body().getUserId());
    }

    @Test
    public void logoutSuccess() throws Exception {

        server.enqueue(new MockResponse()
                .setResponseCode(204)
                .setBody(""));

        Call<ResponseBody> call = userService.logout("TOKEN");
        Response<ResponseBody> response = call.execute();

        Assert.assertEquals(204, response.code());
    }

    @After
    public void tearDown() throws Exception {

        server.shutdown();
    }
}
