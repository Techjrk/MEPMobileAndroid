package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.data.models.Contact;

/**
 * File: CompanyContactViewModel Created: 1/23/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailContactViewModel extends BaseObservable {

    private final Contact contact;
    private final AppCompatActivity appCompatActivity;
    private String contactName;
    private String companyName;

    public CompanyDetailContactViewModel(AppCompatActivity appCompatActivity, Contact contact) {

        this.appCompatActivity = appCompatActivity;
        this.contact = contact;
        setContactName(this.contact.getName() != null ? this.contact.getName() : "[ not given ]");
        setCompanyName(this.contact.getCompany() != null ? this.contact.getCompany().getName() : "[ not given ]");
    }

    public void setContactName(String contactName) {

        this.contactName = contactName;
        notifyPropertyChanged(BR.contactName);
    }

    @Bindable
    public String getContactName() {

        return contactName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
        notifyPropertyChanged(BR.companyName);
    }

    @Bindable
    public String getCompanyName() {
        return companyName;
    }

    public void onContactSelected(View view) {

        Intent intent = new Intent(appCompatActivity, CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, contact.getCompanyId());
        appCompatActivity.startActivity(intent);
    }
}
