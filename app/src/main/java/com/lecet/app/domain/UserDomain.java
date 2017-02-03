package com.lecet.app.domain;

import android.text.TextUtils;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.ChangePasswordRequest;
import com.lecet.app.data.api.request.UpdateUserProfileRequest;
import com.lecet.app.data.models.Access;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: UserDomain Created: 8/23/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class UserDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public UserDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    /**
     * VALIDATION
     **/

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidPassword(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() >= 6;
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

    public void getUser(long userId, Callback<User> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<User> call = lecetClient.getUserService().getUser(token, userId);
        call.enqueue(callback);
    }

    public void updateUser(long userID, UpdateUserProfileRequest body, Callback<User> callback) {

        // Passing a Realm object directly doesn't seem to work in Retrofit due to variable
        // lazy loading.
        String token = sharedPreferenceUtil.getAccessToken();

        Call<User> call = lecetClient.getUserService().updateUser(token, userID, body);
        call.enqueue(callback);
    }

    public void logout(Callback<ResponseBody> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<ResponseBody> call = lecetClient.getUserService().logout(token);
        call.enqueue(callback);
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword, Callback<User> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        ChangePasswordRequest body = new ChangePasswordRequest(oldPassword, newPassword, confirmPassword);
        Call<User> call = lecetClient.getUserService().changePassword(token, fetchLoggedInUser().getId(), body);
        call.enqueue(callback);
    }

    /**
     * Persisted
     **/

    public User copyToRealmTransaction(User user) {

        realm.beginTransaction();
        User persistedUser = realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();

        return persistedUser;
    }

    public User fetchUser(long userID) {

        return realm.where(User.class).equalTo("id", userID).findFirst();
    }

    public User fetchLoggedInUser() {

        long userId = sharedPreferenceUtil.getId();

        return fetchUser(userId);
    }
}
