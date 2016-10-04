package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.CreatePasswordActivity;
import com.lecet.app.data.api.request.CreateUserRequest;
import com.lecet.app.data.api.response.UserResponse;
import com.lecet.app.data.models.Access;
import com.lecet.app.domain.LoginDomain;
import com.lecet.app.domain.UserDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: CreateAccountViewModel Created: 8/23/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class CreateAccountViewModel extends BaseObservable {

    private final AppCompatActivity activity;
    private final LoginDomain loginDomain;
    private final UserDomain userDomain;

    private String email;
    private String firstName;
    private String lastName;
    private boolean emailValid;
    private boolean firstNameValid;
    private boolean lastNameValid;

    public CreateAccountViewModel(@NonNull AppCompatActivity activity, @NonNull UserDomain userDomain, @NonNull LoginDomain loginDomain) {

        this.activity = activity;
        this.loginDomain = loginDomain;
        this.userDomain = userDomain;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
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
    public boolean isFirstNameValid() {
        return firstNameValid;
    }

    public void setFirstNameValid(boolean firstNameValid) {
        this.firstNameValid = firstNameValid;
        notifyPropertyChanged(BR.firstNameValid);
    }

    @Bindable
    public boolean isLastNameValid() {
        return lastNameValid;
    }

    public void setLastNameValid(boolean lastNameValid) {
        this.lastNameValid = lastNameValid;
        notifyPropertyChanged(BR.lastNameValid);
    }

    public void onNextClicked(View view) {
        firstNameValid = userDomain.isNameValid(firstName);
        lastNameValid = userDomain.isNameValid(lastName);
        emailValid = userDomain.isValidEmail(email);

    }
}
