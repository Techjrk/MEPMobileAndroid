package com.lecet.app.viewmodel;

import android.content.Intent;
import android.view.View;

import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.Project;

import java.text.SimpleDateFormat;

/**
 * File: DashboardProjectItemViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class DashboardProjectItemViewModel {

    private final Project project;
    private final String mapsApiKey;
    private final String STANDARD_PRE_BID_MARKER = "ic_standard_marker_pre_bid_kjselm.png";
    private final String STANDARD_PRE_BID_MARKER_UPDATE = "ic_standard_marker_pre_bid_update_p25ao6.png";
    private final String STANDARD_POST_BID_MARKER = "ic_standard_marker_post_bid_kkabhe.png";
    private final String STANDARD_POST_BID_MARKER_UPDATE = "ic_standard_marker_post_bid_update_exicg2.png";
    private final String CUSTOM_PRE_BID_MARKER = "ic_custom_pin_marker_pre_bid_tdocru.png";
    private final String CUSTOM_PRE_BID_MARKER_UPDATE = "ic_custom_pin_marker_pre_bid_update_xzjl40.png";
    private final String CUSTOM_POST_BID_MARKER = "ic_custom_pin_marker_post_bid_iwa8we.png";
    private final String CUSTOM_POST_BID_MARKER_UPDATE = "ic_custom_pin_marker_post_bid_update_kzkxrw.png";
    private final String url = "http://res.cloudinary.com/djakoy1gr/image/upload/v1498123162/";
    public DashboardProjectItemViewModel(Project project, String mapsApiKey) {
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

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=20&size=400x400&" +
                        "markers=icon:"+getMarkerIcon(project)+"|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    private String getMarkerIcon(Project project) {
        StringBuilder urlBuilder = new StringBuilder(url);
        boolean hasUpdates = projectHasUpdates(project);

        // for standard projects, i.e. with Dodge numbers
        if(project.getDodgeNumber() != null) {

            if (project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? STANDARD_PRE_BID_MARKER_UPDATE : STANDARD_PRE_BID_MARKER);
            }

            // style marker for pre-bid or post-bid color
            else {
                if (project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? STANDARD_PRE_BID_MARKER_UPDATE : STANDARD_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? STANDARD_POST_BID_MARKER_UPDATE : STANDARD_POST_BID_MARKER);
                }
            }
        }

        // for custom (user-created) projects, which have no Dodge number
        else {
            // pre-bid user-created projects
            if(project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? CUSTOM_PRE_BID_MARKER_UPDATE : CUSTOM_PRE_BID_MARKER);

            }

            // post-bid user-created projects
            else {
                if(project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? CUSTOM_PRE_BID_MARKER_UPDATE : CUSTOM_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? CUSTOM_POST_BID_MARKER_UPDATE : CUSTOM_POST_BID_MARKER);
                }
            }
        }
        return urlBuilder.toString();
    }
    private boolean projectHasUpdates(Project project) {
        if(project == null) return false;
        if(project.getUserNotes() == null || project.getImages() == null) {
            return false;
        }
        return (project.getImages().size() > 0 || project.getUserNotes().size() > 0);
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

    public String getUnionDesignation() {

        if (project == null) return "?";

        return project.getUnionDesignation();
    }

    public void onItemClick(View view) {

        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        view.getContext().startActivity(intent);
    }
}
