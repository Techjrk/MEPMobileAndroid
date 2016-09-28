package com.lecet.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.CreateUserRequest;
import com.lecet.app.data.api.response.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: UserDomain Created: 8/23/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class UserDomain {

    private final Context context;
    private final LecetClient lecetClient;

    public UserDomain(Context context, LecetClient lecetClient) {
        this.context = context;
        this.lecetClient = lecetClient;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidPassword(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 1;
    }

    public boolean isNameValid(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 1;
    }

    public void postUser(@NonNull CreateUserRequest user, Callback<UserResponse> callback) {

        Call<UserResponse> call = lecetClient.getUserService().create(user);
        call.enqueue(callback);
    }

    public void getUserByEmail(@NonNull String token, @NonNull String email, Callback<UserResponse> callback) {

        Call<UserResponse> call = lecetClient.getUserService().userByEmail(token, email);
        call.enqueue(callback);
    }
}
