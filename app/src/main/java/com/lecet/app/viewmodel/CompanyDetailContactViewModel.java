package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.content.ContactDetailActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.CompanyDomain;

import io.realm.Realm;

/**
 * File: CompanyContactViewModel Created: 1/23/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailContactViewModel extends BaseObservable {

    private final Company company;
    private final Contact contact;
    private final AppCompatActivity appCompatActivity;
    private String contactName;
    private String companyName;

    public CompanyDetailContactViewModel(AppCompatActivity appCompatActivity, Company company, Contact contact) {

        this.appCompatActivity = appCompatActivity;
        this.company = company;
        this.contact = contact;
        setContactName(this.contact.getName() != null ? this.contact.getName() : "[ not given ]");
        setCompanyName(findCompanyName());
    }

    private String findCompanyName() {

        if(this.companyName != null && !this.companyName.isEmpty()) {
            return this.companyName;
        }

        if(this.contact != null && this.contact.getCompany() != null && this.contact.getCompany().getName() != null && !this.contact.getCompany().getName().isEmpty()) {
            return this.contact.getCompany().getName();
        }

        if(this.company != null && this.company.getName() != null && !this.company.getName().isEmpty()) {
            return this.company.getName();
        }

        CompanyDomain companyDomain = new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this.appCompatActivity), Realm.getDefaultInstance());
        Company c = companyDomain.fetchCompany(this.contact.getCompanyId()).first();
        if(c != null && c.getName() != null && !c.getName().isEmpty()) {
            return c.getName();
        }

        return new String("[ not given ]");
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

        Intent intent = new Intent(appCompatActivity, ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.CONTACT_ID_EXTRA, contact.getId());
        appCompatActivity.startActivity(intent);
    }
}
