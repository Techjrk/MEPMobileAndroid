package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Company;

/**
 * File: ProjectDetailBidViewModel Created: 1/24/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectDetailBidViewModel extends BaseObservable {

    private final AppCompatActivity appCompatActivity;
    private final Bid bid;

    public ProjectDetailBidViewModel(AppCompatActivity appCompatActivity, Bid bid) {
        this.appCompatActivity = appCompatActivity;
        this.bid = bid;
    }

    @Bindable
    public String getInfo() {

        Company company = bid.getCompany();
        if (company != null) {
            return company.getName() + " \n " + company.getCity() + ", " + company.getState();
        }

        return "";
    }

    public String getTitle() {
        return String.format("$ %,.0f", bid.getAmount());
    }

    public void onBidSelected(View view) {

        Intent intent = new Intent(appCompatActivity, CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, bid.getCompany().getId());
        appCompatActivity.startActivity(intent);
    }
}
