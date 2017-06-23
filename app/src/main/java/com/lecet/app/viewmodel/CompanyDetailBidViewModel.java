package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private final String STANDARD_PRE_BID_MARKER = "ic_standard_marker_pre_bid_kjselm.png";
    private final String STANDARD_PRE_BID_MARKER_UPDATE = "ic_standard_marker_pre_bid_update_p25ao6.png";
    private final String STANDARD_POST_BID_MARKER = "ic_standard_marker_post_bid_kkabhe.png";
    private final String STANDARD_POST_BID_MARKER_UPDATE = "ic_standard_marker_post_bid_update_exicg2.png";
    private final String CUSTOM_PRE_BID_MARKER = "ic_custom_pin_marker_pre_bid_tdocru.png";
    private final String CUSTOM_PRE_BID_MARKER_UPDATE = "ic_custom_pin_marker_pre_bid_update_xzjl40.png";
    private final String CUSTOM_POST_BID_MARKER = "ic_custom_pin_marker_post_bid_iwa8we.png";
    private final String CUSTOM_POST_BID_MARKER_UPDATE = "ic_custom_pin_marker_post_bid_update_kzkxrw.png";
    private final String url = "http://res.cloudinary.com/djakoy1gr/image/upload/v1498123162/";
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

    public void onBidSelected(View view) {
        if(project == null){
            //TODO: Handle gracefully
            return;
        }
        Intent intent = new Intent(activity, ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        activity.startActivity(intent);
    }

}
