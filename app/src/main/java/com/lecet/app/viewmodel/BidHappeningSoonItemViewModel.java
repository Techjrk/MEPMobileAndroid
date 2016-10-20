package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Project;

import java.text.SimpleDateFormat;

/**
 * File: BidHappeningSoonItemViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class BidHappeningSoonItemViewModel {

    private final Project project;
    private final String mapsApiKey;

    public BidHappeningSoonItemViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
    }


    public String getProjectName() {

        return project.getTitle();
    }

    public String getClientLocation() {

        return project.getCity() + " , " + project.getState();
    }

    public String getBidType() {

        StringBuilder sb = new StringBuilder();
        if (project.getPrimaryProjectType() != null && project.getPrimaryProjectType().getTitle() != null) {

            sb.append(project.getPrimaryProjectType().getTitle());

            if (project.getPrimaryProjectType().getProjectCategory() != null && project.getPrimaryProjectType().getProjectCategory().getTitle() != null) {

                sb.append(" ");
                sb.append(project.getPrimaryProjectType().getProjectCategory().getTitle());

                if (project.getPrimaryProjectType().getProjectCategory().getProjectGroup() != null && project.getPrimaryProjectType().getProjectCategory().getProjectGroup().getTitle() != null) {

                    sb.append(" ");
                    sb.append(project.getPrimaryProjectType().getProjectCategory().getProjectGroup().getTitle());
                }
            }
        }

        return sb.toString();
    }

    public String getMapUrl() {

        if (project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    public String getBidTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

        return simpleDateFormat.format(project.getBidDate());
    }

    public String getStartDateString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d");

        return simpleDateFormat.format(project.getBidDate());
    }


    public boolean isUnion() {

        return project.getUnionDesignation() != null && project.getUnionDesignation().length() > 0;
    }

}
