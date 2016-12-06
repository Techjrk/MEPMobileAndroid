package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Project;

import java.text.SimpleDateFormat;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemRecentViewModel {

    //TODO - support either Project or Company data
    private final Project project;
    private final String mapsApiKey;
    private final String code ="this123code";

    public String  getTitle(){
        if (project == null) return "--";
        return project.getTitle();
    }
    public String getAddress() {
        if (project == null) return "--";

        return  project.getAddress1();
    }
    public String getCode() {
        return code;
    }

    public SearchItemRecentViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
    }


    public String getProjectName() {

        return project.getTitle();
    }

    public String getClientLocation() {

        return project.getCity() + " , " + project.getState();
    }


    public String getMapUrl() {

        if (project == null || project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }


}
