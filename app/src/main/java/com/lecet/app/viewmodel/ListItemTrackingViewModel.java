package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectCategory;
import com.lecet.app.data.models.ProjectGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lecet.app.BR;

/**
 * File: ListItemTrackingViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ListItemTrackingViewModel extends BaseObservable {

    private static final String TAG = "ListItemProjTrackingVM";

    public static final String EXPANDABLE_MODE_GONE = "gone";
    public static final String EXPANDABLE_MODE_BID = "bid";
    public static final String EXPANDABLE_MODE_NOTE = "note";
    public static final String EXPANDABLE_MODE_STAGE = "stage";

    private String expandableMode = EXPANDABLE_MODE_GONE;


    private final String NEW_BID_PLACED = "A new bid has been placed";      // TODO - Externalize, which will require Context
    private final String NEW_NOTE_ADDED = "A new note has been added";
    private final String BID_PLACED_AT  = "A bid was placed at";
    private final String STAGE_UPDATED  = "The project stage is now";
    private final long RECENT_BID_MS = 1000 * 60 * 60 * 24 * 14;            // ms * secs * mins * hrs * days

    private final Project project;
    private final String mapsApiKey;
    private String projectKeywords;
    private final boolean showUpdates;
    private boolean showExpandableView;
    private boolean expandableViewExpanded;

    private int expandableViewIconId;
    private String expandableViewTitle = "";
    private String expandableViewMessage = "";

    public ListItemTrackingViewModel(Project project, String mapsApiKey, boolean showUpdates) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        this.showUpdates = showUpdates;

        setExpandableMode();
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
                    sb.append(", ");
                    sb.append(categoryTitle);
                }
                ProjectGroup group = category.getProjectGroup();
                if (group != null) {
                    String groupTitle = group.getTitle();
                    if(groupTitle != null) {
                        sb.append(", ");
                        sb.append(groupTitle);
                    }
                }
            }
        }

        String str = sb.toString();
        if(str.length() == 0) return null;
        return str;
    }

    private void setExpandableMode() {
        // Bid mode
        if(recentBid()) {
            setExpandableMode(EXPANDABLE_MODE_BID);
            setExpandableViewIconId(R.drawable.ic_add);
            setExpandableViewTitle(NEW_BID_PLACED);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm aaa");
            String formattedDate = sdf.format(project.getBidDate());
            setExpandableViewMessage(BID_PLACED_AT + " " + formattedDate);
        }
        // Note Mode
        else if(hasNote()) {
            setExpandableMode(EXPANDABLE_MODE_BID);
            setExpandableViewIconId(R.drawable.ic_add_note);
            setExpandableViewTitle(NEW_NOTE_ADDED);
            setExpandableViewMessage(project.getProjectNotes());
        }
        // Stage Update Mode
        else if(hasStageUpdate()) {
            setExpandableMode(EXPANDABLE_MODE_STAGE);
            setExpandableViewIconId(R.drawable.ic_add_note);    //TODO - change to a 'stage' icon, TBD
            setExpandableViewTitle(STAGE_UPDATED);
            setExpandableViewMessage(project.getProjectStage().getName());
        }
        // Hide
        else {
            setExpandableMode(EXPANDABLE_MODE_GONE);
            setExpandableViewTitle("");
            setExpandableViewMessage("");
        }

        // set the showExpandableView to true if this item is in a mode such as Bid, Note, or Stage
        setShowExpandableView(expandableMode == EXPANDABLE_MODE_BID || expandableMode == EXPANDABLE_MODE_NOTE|| expandableMode == EXPANDABLE_MODE_STAGE);

        //Log.d(TAG, "setExpandableMode: " + project.getTitle() + ", " + expandableMode);
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

    private boolean hasNote() {
        if (project.getProjectNotes() == null || project.getProjectNotes().length() == 0) {
            return false;
        }
        else return true;
    }

    private boolean hasStageUpdate() {
        //TODO - fill in to return true if there is a project stage update
        return false;
    }

    public String getExpandableMode() {
        return expandableMode;
    }

    public void setExpandableMode(String expandableMode) {
        this.expandableMode = expandableMode;
    }


    ///////////////////////////////
    // BINDINGS

    @Bindable
    public boolean getShowUpdates() {
        return showUpdates;
    }

    @Bindable
    public boolean getShowExpandableView() {
        return showExpandableView;
    }

    public void setShowExpandableView(boolean showExpandableView) {
        this.showExpandableView = showExpandableView;
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
    public int getExpandableViewIconId() {
        return expandableViewIconId;
    }

    public void setExpandableViewIconId(int expandableViewIconId) {
        this.expandableViewIconId = expandableViewIconId;
        //notifyPropertyChanged(BR.expandableViewIconId);
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
