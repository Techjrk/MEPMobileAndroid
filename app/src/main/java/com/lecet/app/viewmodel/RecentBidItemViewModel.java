package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * File: RecentBidItemViewModel Created: 10/13/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class RecentBidItemViewModel {

    private final Bid bid;
    private final String mapsApiKey;

    public RecentBidItemViewModel(Bid bid, String mapsApiKey) {
        this.bid = bid;
        this.mapsApiKey = mapsApiKey;
    }

    public String getBidAmount() {

        DecimalFormat formatter = new DecimalFormat("$ #,###");

        return formatter.format(bid.getAmount());
    }


    public String getBidCompany() {

        return null;
    }

    public String getProjectName() {

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

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                "markers=color:blue|%.6f,%.6f&key=%s", bid.getProject().getGeocode().getLat(), bid.getProject().getGeocode().getLng(),
                bid.getProject().getGeocode().getLat(), bid.getProject().getGeocode().getLng(), mapsApiKey);
    }

    public String getStartDateString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d");

        return simpleDateFormat.format(bid.getCreateDate());
    }

    public boolean isUnion() {

        Project project = bid.getProject();

        return project.getUnionDesignation() != null && project.getUnionDesignation().length() > 0;
    }


}
