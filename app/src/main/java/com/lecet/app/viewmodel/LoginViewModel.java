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
import com.lecet.app.content.DashboardIntermediaryActivity;
import com.lecet.app.content.MainActivity;
import com.lecet.app.contentbase.BaseObservableViewModel;
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
public class LoginViewModel extends BaseObservableViewModel {

    private static final String TAG = "LoginViewModel";

    private final UserDomain userDomain;

    private String email;
    private String password;
    private boolean emailValid;
    private boolean passwordValid;

    public LoginViewModel(AppCompatActivity activity, UserDomain ld) {
        super(activity);
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

        setEmailValid(userDomain.isValidEmail(email));
        setPasswordValid(userDomain.isValidPassword(password));

        if (emailValid && passwordValid) {

            userDomain.login(email, password, new Callback<Access>() {
                @Override
                public void onResponse(Call<Access> call, Response<Access> response) {

                    if (response.isSuccessful()) {

                        if (isActivityAlive()) {

                            Access r = response.body();

                            AppCompatActivity appCompatActivity = getActivityWeakReference().get();

                            // Store "ID" variable as session token and "userID" as
                            LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(appCompatActivity.getApplication());
                            sharedPreferenceUtil.setAccessToken(r.getId());
                            sharedPreferenceUtil.setId(r.getUserId());

                            getUser(r.getUserId());
                        }

                    } else {

                        if (isActivityAlive()) {

                            AppCompatActivity appCompatActivity = getActivityWeakReference().get();
                            String title = appCompatActivity.getString(R.string.error_login_title);
                            String message = appCompatActivity.getString(R.string.error_login_message);
                            showCancelAlertDialog(title, message);
                        }

                    }
                }

                @Override
                public void onFailure(Call<Access> call, Throwable t) {

                    if (isActivityAlive()) {

                        AppCompatActivity appCompatActivity = getActivityWeakReference().get();
                        String title = appCompatActivity.getString(R.string.error_network_title);
                        String message = appCompatActivity.getString(R.string.error_network_message);
                        showCancelAlertDialog(title, message);
                    }

                }
            });
        } else {

            if (isActivityAlive()) {

                if (!emailValid) {

                    AppCompatActivity appCompatActivity = getActivityWeakReference().get();
                    String title = appCompatActivity.getString(R.string.error_login_title);
                    String message = appCompatActivity.getString(R.string.error_invalid_email);
                    showCancelAlertDialog(title, message);

                } else if (!passwordValid) {

                    AppCompatActivity appCompatActivity = getActivityWeakReference().get();
                    String title = appCompatActivity.getString(R.string.error_login_title);
                    String message = appCompatActivity.getString(R.string.error_invalid_password_length);
                    showCancelAlertDialog(title, message);
                }

            }
        }
    }

    public String emailErrorMessage() {
        return getActivityWeakReference().get().getString(R.string.error_invalid_email);
    }

    public void getUser(long userId) {

        userDomain.getUser(userId, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    if (isActivityAlive()) {

                        AppCompatActivity appCompatActivity = getActivityWeakReference().get();

                        User r = response.body();
                        userDomain.copyToRealmTransaction(r);

                        Intent intent = new Intent(appCompatActivity, DashboardIntermediaryActivity.class);
                        appCompatActivity.startActivity(intent);
                        appCompatActivity.finish();
                    }

                } else {

                    if (isActivityAlive()) {

                        if (isSessionUnauthorized(response)) {

                            displayUnauthorizedUserDialog();
                        }

                        AppCompatActivity appCompatActivity = getActivityWeakReference().get();
                        String title = appCompatActivity.getString(R.string.error_login_title);
                        String message = appCompatActivity.getString(R.string.error_access_denied);
                        showCancelAlertDialog(title, message);
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if (isActivityAlive()) {

                    AppCompatActivity appCompatActivity = getActivityWeakReference().get();
                    String title = appCompatActivity.getString(R.string.error_login_title);
                    String message = appCompatActivity.getString(R.string.error_network_message);
                    showCancelAlertDialog(title, message);
                }
            }
        });
    }
}
