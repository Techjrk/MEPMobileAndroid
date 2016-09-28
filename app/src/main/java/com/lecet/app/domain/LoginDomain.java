package com.lecet.app.domain;

import android.text.TextUtils;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.AuthRequest;
import com.lecet.app.data.api.request.LogoutRequest;
import com.lecet.app.data.models.Access;
import com.lecet.app.data.models.User;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: LoginDomain Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class LoginDomain {

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_CLIENT = "client_credentials";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";

    private final LecetClient lecetClient;

    public LoginDomain(LecetClient client) {

        this.lecetClient = client;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidPassword(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 1;
    }

    public void login(String email, String password, Callback<Access> callback) {

        final AuthRequest r = new AuthRequest.Builder(GRANT_TYPE_PASSWORD)
                .clientID(LecetClient.CLIENT_ID)
                .clientSecret(LecetClient.CLIENT_SECRET)
                .userName(email)
                .password(password)
                .build();

        Call<Access> call = lecetClient.getAuthService().login(r);
        call.enqueue(callback);
    }

    public void refreshToken(String rt, Callback<Access> callback) {

        final AuthRequest r = new AuthRequest.Builder(GRANT_TYPE_REFRESH)
                .clientID(LecetClient.CLIENT_ID)
                .clientSecret(LecetClient.CLIENT_SECRET)
                .refreshToken(rt)
                .build();

        Call<Access> call = lecetClient.getAuthService().refreshToken(r);
        call.enqueue(callback);
    }

    public void clientAccess(Callback<Access> callback) {

        final AuthRequest r = new AuthRequest.Builder(GRANT_TYPE_CLIENT)
                .clientID(LecetClient.CLIENT_ID)
                .clientSecret(LecetClient.CLIENT_SECRET)
                .build();

        Call<Access> call = lecetClient.getAuthService().login(r);
        call.enqueue(callback);
    }

    public void logout(Callback<User> callback) {

        final LogoutRequest r = new LogoutRequest("1");                     //TODO - update

//        Call<Access> call = lecetClient.getAuthService().logout(r);
//        call.enqueue(callback);
    }

}
