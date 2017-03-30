package com.lecet.app.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.Project;

/**
 * File: MapInfoWindowViewModel Created: 2/22/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class MapInfoWindowViewModel extends BaseObservable {

    private final Context context;
    private final Project project;

    private String projectTitle;
    private String projectLocation;
    private String address1;
    private String address2;
    private boolean postBid;
    private String bidStatus;

    public MapInfoWindowViewModel(Context context, Project project) {

        this.context = context;
        this.project = project;

        setProjectTitle(project.getTitle());
        setProjectLocation(String.format("%s, %s", project.getCity(), project.getState()));
        setAddress1(project.getAddress1());
        setAddress2(project.getAddress2());

        if (project.getProjectStage() == null) {
            setPostBid(false);
        } else {
            setPostBid(project.getProjectStage().getParentId() != 102);
        }

        setBidStatus(isPostBid() ? context.getString(R.string.post_bid) : context.getString(R.string.pre_bid));
    }

    @Bindable
    public String getProjectTitle() {

        return this.projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        notifyPropertyChanged(BR.projectTitle);
    }

    @Bindable
    public String getProjectLocation() {

        return this.projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
        notifyPropertyChanged(BR.projectLocation);
    }

    @Bindable
    public String getAddress1() {

        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
        notifyPropertyChanged(BR.address1);
    }

    @Bindable
    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
        notifyPropertyChanged(BR.address2);
    }

    @Bindable
    public boolean isPostBid() {

        return this.postBid;
    }

    public void setPostBid(boolean postBid) {
        this.postBid = postBid;
        notifyPropertyChanged(BR.postBid);
    }

    @Bindable
    public String getBidStatus() {
        return this.bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
        notifyPropertyChanged(BR.bidStatus);
    }

    @DrawableRes
    public int getBidBackground() {

        return isPostBid() ? R.drawable.post_bid_window_title_background : R.drawable.pre_bid_window_title_background;
    }

    /* OnClick */

    public void onDirectionsClicked(View view) {

        double latitude = project.getGeocode().getLat();
        double longitude = project.getGeocode().getLng();

        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s"
                , latitude, longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }
}
