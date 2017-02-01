package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.MainActivity;
import com.lecet.app.data.models.Access;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.UserDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: LoginViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class LoginViewModel extends BaseObservable {

    private static final String TAG = "LoginViewModel";

    private final AppCompatActivity activity;
    private final UserDomain userDomain;

    private String email;
    private String password;
    private boolean emailValid;
    private boolean passwordValid;

    public LoginViewModel(AppCompatActivity activity, UserDomain ld) {

        this.activity = activity;
        this.userDomain = ld;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public boolean isEmailValid() {
        return emailValid;
    }

    public void setEmailValid(boolean emailValid) {
        this.emailValid = emailValid;
        notifyPropertyChanged(BR.emailValid);
    }

    @Bindable
    public boolean isPasswordValid() {
        return passwordValid;
    }

    public void setPasswordValid(boolean passwordValid) {
        this.passwordValid = passwordValid;
        notifyPropertyChanged(BR.passwordValid);
    }

    /**
     * OnClick handlers
     **/

    public void onLoginClicked(View view) {

        emailValid = userDomain.isValidEmail(email);
        passwordValid = userDomain.isValidPassword(password);

        if (emailValid && passwordValid) {

            userDomain.login(email, password, new Callback<Access>() {
                @Override
                public void onResponse(Call<Access> call, Response<Access> response) {

                    if (response.isSuccessful()) {

                        Access r = response.body();

                        Log.d(TAG, "Access ID = " + r.getUserId());

                        // Store "ID" variable as session token and "userID" as
                        LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(activity.getApplication());
                        sharedPreferenceUtil.setAccessToken(r.getId());
                        sharedPreferenceUtil.setId(r.getUserId());

                        getUser(r.getUserId());

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getString(R.string.error_login_title));
                        builder.setMessage(activity.getString(R.string.error_login_message));
                        builder.setNegativeButton(activity.getString(R.string.ok), null);

                        builder.show();
                    }
                }

                @Override
                public void onFailure(Call<Access> call, Throwable t) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.error_network_title));
                    builder.setMessage(activity.getString(R.string.error_network_message));
                    builder.setNegativeButton(activity.getString(R.string.ok), null);

                    builder.show();
                }
            });
        } else {

            if (!emailValid) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.error_login_title));
                builder.setMessage(activity.getString(R.string.error_invalid_email));
                builder.setNegativeButton(activity.getString(R.string.ok), null);

                builder.show();

            } else if (!passwordValid) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.error_login_title));
                builder.setMessage(activity.getString(R.string.error_invalid_password_length));
                builder.setNegativeButton(activity.getString(R.string.ok), null);

                builder.show();
            }

        }
    }

    public String emailErrorMessage() {
        return activity.getString(R.string.error_invalid_email);
    }

    public void getUser(long userId) {

        userDomain.getUser(userId, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User r = response.body();
                    userDomain.copyToRealmTransaction(r);

                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.error_login_title));
                    builder.setMessage(activity.getString(R.string.error_access_denied));
                    builder.setNegativeButton(activity.getString(R.string.ok), null);

                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.error_network_title));
                builder.setMessage(activity.getString(R.string.error_network_message));
                builder.setNegativeButton(activity.getString(R.string.ok), null);

                builder.show();
            }
        });
    }
}
