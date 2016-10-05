package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.lecet.app.domain.SettingsDomain;


/**
 * File: ContactDetailViewModel Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ContactDetailViewModel extends BaseObservable {

    private static final String TAG = "ContactDetailViewModel";

    private final Context context;
    private final SettingsDomain settingsDomain;

    private String jobTitle;
    private String address1;
    private String address2;
    private String phone;
    private String email;


    public ContactDetailViewModel(Context context, SettingsDomain sd) {        //TODO - change domain
        this.context = context;
        this.settingsDomain = sd;

        Log.d(TAG, "Constructor");
    }

    @Bindable
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Bindable
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Bindable
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Bindable
    public String isPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
