package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * File: CompanyDetailBidViewModel Created: 1/23/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailBidViewModel extends BaseObservable {

    private final Bid bid;
    private final Project project;
    private final String mapsApiKey;
    private final AppCompatActivity activity;

    public CompanyDetailBidViewModel(AppCompatActivity activity, String mapsApiKey, Bid bid) {
        this.bid = bid;
        this.mapsApiKey = mapsApiKey;
        this.activity = activity;
        this.project = bid.getProject();
    }

    public String getBidAmount() {

        DecimalFormat formatter = new DecimalFormat("$ #,###");

        return formatter.format(bid.getAmount());
    }

    public String getStartDateString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (project.getBidDate() == null) return ""; //NOTE: Based on HockeyApp, dated 2017-05-26 . added this code just in case it was not resolved yet to prevent NullPointerException error from keep crashing the app.
        return simpleDateFormat.format(project.getBidDate());
    }

    public String getProjectName() {
        if (project.getTitle() == null) return ""; //NOTE: Based on HockeyApp, dated 2017-04-03 . added this code just in case it was not resolved yet to prevent NullPointerException error from keep crashing the app.
        return project.getTitle();
    }

    public String getClientLocation() {

        return project.getCity() + " , " + project.getState();
    }

    public String getMapUrl() {

        if (project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=300x300&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    public void onBidSelected(View view) {

        Intent intent = new Intent(activity, ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        activity.startActivity(intent);
    }

}
