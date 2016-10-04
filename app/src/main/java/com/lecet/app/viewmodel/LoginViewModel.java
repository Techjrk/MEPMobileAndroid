package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.CreateAccountActivity;
import com.lecet.app.content.MainActivity;
import com.lecet.app.data.models.Access;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.LoginDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: LoginViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class LoginViewModel extends BaseObservable {

    private final AppCompatActivity activity;
    private final LoginDomain loginDomain;

    private String email;
    private String password;
    private boolean emailValid;
    private boolean passwordValid;

    public LoginViewModel(AppCompatActivity activity, LoginDomain ld) {

        this.activity = activity;
        this.loginDomain = ld;
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

        emailValid = loginDomain.isValidEmail(email);
        passwordValid = loginDomain.isValidPassword(password);

        if (emailValid && passwordValid) {

            loginDomain.login(email, password, new Callback<Access>() {
                @Override
                public void onResponse(Call<Access> call, Response<Access> response) {

                    if (response.isSuccessful()) {

                        //TODO: Handle Response
                        Access r = response.body();


                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();

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
        }
    }

    public String emailErrorMessage() {
        return activity.getString(R.string.error_invalid_email);
    }

    public void onForgotPasswordClicked(View view) {

    }

    public void onCreateAccountClicked(View view) {

        Intent intent = new Intent(activity, CreateAccountActivity.class);
        activity.startActivity(intent);
    }
}
