package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectCategory;
import com.lecet.app.data.models.ProjectGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lecet.app.BR;

/**
 * File: ListItemProjectTrackingViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ListItemProjectTrackingViewModel extends BaseObservable {

    private static final String TAG = "ListItemProjTrackingVM";

    private final String NEW_BID_PLACED = "A new bid has been placed";
    private final String NEW_NOTE_ADDED = "A new note has been added";
    private final String BID_PLACED_AT  = "A bid was placed at";
    private final long RECENT_BID_MS = 1000 * 60 * 60 * 24 * 14; // ms * secs * mins * hrs * days

    private final Project project;
    private final String mapsApiKey;
    private String projectKeywords;
    private boolean expandableViewExpanded;
    private String expandableViewTitle = "";
    private String expandableViewMessage = "";

    public ListItemProjectTrackingViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;

        setExpandableViewTitle(generateExpandableViewTitle());
        setExpandableViewMessage(generateExpandableViewMessage());
        projectKeywords = generateProjectKeywords();
    }

    public String getProjectName() {

        return project.getTitle();
    }

    public String getProjectKeywords() {
        return this.projectKeywords;
    }

    /**
     Return a String built from a list of the project's PrimaryProjectType, ProjectCategory, and ProjectGroup
     **/
    public String generateProjectKeywords() {
        StringBuilder sb = new StringBuilder();
        long id = project.getPrimaryProjectTypeId();
        PrimaryProjectType primaryProjectType = project.getPrimaryProjectType();
        if(primaryProjectType != null) {
            String pptTitle = primaryProjectType.getTitle();
            if (pptTitle != null) sb.append(pptTitle);
            ProjectCategory category = primaryProjectType.getProjectCategory();
            if (category != null) {
                String categoryTitle = category.getTitle();
                if (categoryTitle != null) {
                    sb.append(",");
                    sb.append(categoryTitle);
                }
                ProjectGroup group = category.getProjectGroup();
                if (group != null) {
                    String groupTitle = group.getTitle();
                    if(groupTitle != null) {
                        sb.append(",");
                        sb.append(groupTitle);
                    }
                }
            }
        }

        String str = sb.toString();
        if(str.length() == 0) return null;
        return str;
    }

    /*public String getProjectNote() {

        return project.getProjectNotes();
    }

    public String getProjectBidDate() {

        if(project.getBidDate() != null) return DateFormat.getDateTimeInstance().format(project.getBidDate());
        return null;
    }*/

    private String generateExpandableViewTitle() {

        // Bid
        if(recentBid()) {
            return NEW_BID_PLACED; //TODO - externalize... need a Context to get resources though
        }

        // Note, if no bid
        else if(hasNote()) {
            return NEW_NOTE_ADDED;
        }
        return null;
    }

    private String generateExpandableViewMessage() {

        // Bid
       if (recentBid()) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm aaa");
            String formattedDate = sdf.format(project.getBidDate());
            return BID_PLACED_AT + " " + formattedDate;
        }

        // Note (if no bid)
        else if(hasNote()) {
            return project.getProjectNotes();
        }
        return null;
    }

    private boolean recentBid() {
        if(project.getBidDate() == null) return false;

        Date now = new Date();
        long timeSinceBid = now.getTime() - project.getBidDate().getTime();
        boolean isRecent = timeSinceBid < RECENT_BID_MS;
        return (isRecent);
    }

    public String getClientLocation() {

        return project.getCity() + " , " + project.getState();
    }

    public String getMapUrl() {

        if (project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    @Bindable
    public boolean getExpandableViewExpanded() {
        return expandableViewExpanded;
    }

    public void setExpandableViewExpanded(boolean expandableViewExpanded) {
        this.expandableViewExpanded = expandableViewExpanded;
        notifyPropertyChanged(BR.expandableViewExpanded);
    }

    @Bindable
    public String getExpandableViewTitle() {
        return expandableViewTitle;
    }

    public void setExpandableViewTitle(String expandableViewTitle) {
        this.expandableViewTitle = expandableViewTitle;
        notifyPropertyChanged(BR.expandableViewTitle);
    }

    @Bindable
    public String getExpandableViewMessage() {
        return expandableViewMessage;
    }

    public void setExpandableViewMessage(String expandableViewMessage) {
        this.expandableViewMessage = expandableViewMessage;
        notifyPropertyChanged(BR.expandableViewMessage);
    }


    public boolean showExpandableView() {
        return (recentBid() || hasNote());
    }


    ////////////////////////////////////
    // PROJECT NOTES

    public boolean hasNote() {
        if (project.getProjectNotes() == null || project.getProjectNotes().length() == 0) {
            return false;
        }
        else return true;
    }


    ////////////////////////////////////
    // CLICK HANDLERS

    public void onExpandableViewClick(View view) {
        //Log.d(TAG, "onExpandableViewClick: " + view);
        setExpandableViewExpanded(!expandableViewExpanded);
    }

    public void onClick(View view) {
        //TODO - fill in
        Log.d(TAG, "onClick: " + view);
    }


}
