package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.data.models.Contact;

/**
 * File: ProjectDetailContactViewModel Created: 1/24/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectDetailContactViewModel extends BaseObservable {

    private final AppCompatActivity appCompatActivity;
    private final Contact contact;


    public ProjectDetailContactViewModel(AppCompatActivity appCompatActivity, Contact contact) {
        this.appCompatActivity = appCompatActivity;
        this.contact = contact;
    }

    @Bindable
    public String getInfo() {
        return contact.getCompany().getName() + " \n " + contact.getCompany().getCity() + ", " + contact.getCompany().getState();
    }

    public String getTitle() {
        return contact.getContactType().getCategory();
    }

    public void onContactSelected(View view) {

        Intent intent = new Intent(appCompatActivity, CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, contact.getCompanyId());
        appCompatActivity.startActivity(intent);
    }
}
