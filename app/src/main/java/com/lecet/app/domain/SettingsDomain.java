package com.lecet.app.domain;

import android.text.TextUtils;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.LogoutRequest;
import com.lecet.app.data.api.request.SetPasswordRequest;

import retrofit2.Callback;

/**
 * File: SettingsDomain Created: 8/24/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class SettingsDomain {

    private final LecetClient lecetClient;

    public SettingsDomain(LecetClient client) {

        this.lecetClient = client;
    }


    /////////////////////////////////////////
    // Password

    public boolean isValidPassword(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 1;
    }

    public void setNewPassword(String password, Callback<SetPasswordRequest> callback) {

        final SetPasswordRequest request = new SetPasswordRequest("1", password);                   //TODO - update uuid

//        Call<SetPasswordRequest> call = lecetClient.getUserService().setPassword(request);     //TODO - add method
//        call.enqueue(callback);
    }


    /////////////////////////////////////////
    // Logout

    public void logout(Callback<LogoutRequest> callback) {

        final LogoutRequest request = new LogoutRequest("1");                                       //TODO - update uuid

//        Call<LogoutRequest> call = lecetClient.getUserService().logout(request);               //TODO - add method
//        call.enqueue(callback);
    }


}
