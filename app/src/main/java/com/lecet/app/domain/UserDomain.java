package com.lecet.app.domain;

import android.text.TextUtils;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Access;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import io.realm.Realm;
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

    public void getUser(long userId, Callback<User> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<User> call = lecetClient.getUserService().getUser(token, userId);
        call.enqueue(callback);
    }

    /**
     * Persisted
     **/

    public User copyToRealmTransaction(User bid) {

        realm.beginTransaction();
        User persistedUser = realm.copyToRealmOrUpdate(bid);
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
