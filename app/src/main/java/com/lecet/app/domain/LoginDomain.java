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

        Call<Access> call = lecetClient.getUserService().login(email, password);
        call.enqueue(callback);
    }


    public void logout(Callback<User> callback) {

        final LogoutRequest r = new LogoutRequest("1");                     //TODO - update

//        Call<Access> call = lecetClient.getAuthService().logout(r);
//        call.enqueue(callback);
    }

}
