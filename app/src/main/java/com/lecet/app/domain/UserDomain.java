package com.lecet.app.domain;

import android.content.Intent;
import android.text.TextUtils;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Access;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: UserDomain Created: 8/23/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class UserDomain {

    private final LecetClient lecetClient;

    public UserDomain(LecetClient lecetClient) {
        this.lecetClient = lecetClient;
    }

    /**
     * VALIDATION
     **/

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidPassword(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 1;
    }

    public boolean isNameValid(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 1;
    }

    /**
     * API
     **/

    public void login(String email, String password, Callback<Access> callback) {

        Call<Access> call = lecetClient.getUserService().login(email, password);
        call.enqueue(callback);
    }


}
