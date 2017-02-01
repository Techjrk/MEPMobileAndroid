package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import io.realm.RealmList;

/**
 * File: RecentBidItemViewModel Created: 10/13/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class RecentBidItemViewModel {

    private static final String TAG = "RecentBidItemViewModel";

    private final Bid bid;
    private final String mapsApiKey;
    private final AppCompatActivity activity;

    public RecentBidItemViewModel(Bid bid, String mapsApiKey, AppCompatActivity activity) {
        this.bid = bid;
        this.mapsApiKey = mapsApiKey;
        this.activity = activity;
    }

    public String getBidAmount() {

        DecimalFormat formatter = new DecimalFormat("$ #,###");

        return formatter.format(bid.getAmount());
    }


    public String getBidCompany() {

        Contact contact = bid.getContact();

        if (contact != null) {

            Company company = contact.getCompany();

            if (company != null) {

                return company.getName();
            }

        }

        return "";
    }

    public String getProjectName() {

        Log.d("RECENTBID", "BID ID " + bid.getId());
        Log.d("RECENTBID", "Project ID " + bid.getProject().getId());
        return bid.getProject().getTitle();
    }

    public String getClientLocation() {

        return bid.getProject().getCity() + " , " + bid.getProject().getState();
    }

    public String getBidType() {

        StringBuilder sb = new StringBuilder();
        if (bid.getProject().getPrimaryProjectType() != null && bid.getProject().getPrimaryProjectType().getTitle() !=null) {

            sb.append(bid.getProject().getPrimaryProjectType().getTitle());

            if (bid.getProject().getPrimaryProjectType().getProjectCategory() != null && bid.getProject().getPrimaryProjectType().getProjectCategory().getTitle() != null) {

                sb.append(" ");
                sb.append(bid.getProject().getPrimaryProjectType().getProjectCategory().getTitle());

                if (bid.getProject().getPrimaryProjectType().getProjectCategory().getProjectGroup() != null && bid.getProject().getPrimaryProjectType().getProjectCategory().getProjectGroup().getTitle() != null) {

                    sb.append(" ");
                    sb.append(bid.getProject().getPrimaryProjectType().getProjectCategory().getProjectGroup().getTitle());
                }
            }
        }

        return sb.toString();
    }

    public String getMapUrl() {

        if (bid.getProject().getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x300&" +
                "markers=color:blue|%.6f,%.6f&key=%s", bid.getProject().getGeocode().getLat(), bid.getProject().getGeocode().getLng(),
                bid.getProject().getGeocode().getLat(), bid.getProject().getGeocode().getLng(), mapsApiKey);
    }

    public String getStartDateString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d");

        if (bid.getProject().getBidDate() == null) return "";

        return simpleDateFormat.format(bid.getProject().getBidDate());
    }

    public boolean isUnion() {

        Project project = bid.getProject();

        return project.getUnionDesignation() != null && project.getUnionDesignation().length() > 0;
    }

    /** OnClick **/
    public void onItemClick(View view) {

        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, bid.getProjectId());
        activity.startActivity(intent);
    }
}
