package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.Bid;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * File: CompanyDetailBidViewModel Created: 1/23/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailBidViewModel extends BaseObservable {

    private final Bid bid;
    private final String mapsApiKey;
    private final AppCompatActivity activity;


    public CompanyDetailBidViewModel(AppCompatActivity activity, String mapsApiKey, Bid bid) {
        this.bid = bid;
        this.mapsApiKey = mapsApiKey;
        this.activity = activity;
    }

    public String getBidAmount() {

        DecimalFormat formatter = new DecimalFormat("$ #,###");

        return formatter.format(bid.getAmount());
    }

    public String getStartDateString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        return simpleDateFormat.format(bid.getProject().getBidDate());
    }

    public String getProjectName() {

        return bid.getProject().getTitle();
    }

    public String getClientLocation() {

        return bid.getProject().getCity() + " , " + bid.getProject().getState();
    }

    public String getMapUrl() {

        if (bid.getProject().getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=300x300&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", bid.getProject().getGeocode().getLat(), bid.getProject().getGeocode().getLng(),
                bid.getProject().getGeocode().getLat(), bid.getProject().getGeocode().getLng(), mapsApiKey);
    }

    public void onBidSelected(View view) {

        Intent intent = new Intent(activity, ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, bid.getProject().getId());
        activity.startActivity(intent);
    }

}
