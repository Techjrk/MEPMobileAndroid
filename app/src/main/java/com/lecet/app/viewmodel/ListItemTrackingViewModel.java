package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.content.TrackingListActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectCategory;
import com.lecet.app.data.models.ProjectGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private final String listType;


    private final String NEW_BID_PLACED = "A new bid has been placed";      // TODO - Externalize, which will require Context
    private final String NEW_NOTE_ADDED = "A new note has been added";
    private final String BID_PLACED_AT  = "A bid was placed at";
    private final String STAGE_UPDATED  = "The project stage is now";
    private final long RECENT_BID_MS = 1000 * 60 * 60 * 24 * 14;            // ms * secs * mins * hrs * days

    private final Project project;
    private final Company company;
    private final String mapsApiKey;

    private String detail1;
    private String detail2;
    private final boolean showUpdates;
    private boolean showExpandableView;
    private boolean expandableViewExpanded;

    private int expandableViewIconId;
    private String expandableViewTitle = "";
    private String expandableViewMessage = "";

    /**
     * Constructor for Project Tracking List
     */
    public ListItemTrackingViewModel(String listType, Project project, String mapsApiKey, boolean showUpdates) {
        this.listType = listType;
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        this.showUpdates = showUpdates;
        this.company = null;

        setExpandableMode();

        // detail TextView 1
        StringBuilder sb = new StringBuilder();
        if(project.getCity() != null) sb.append(project.getCity());
        if(project.getCity() != null && project.getState() != null) sb.append(", ");
        if(project.getState() != null) sb.append(project.getState());
        setDetail1(sb.toString());

        // detail TextView 2
        setDetail2(generateProjectKeywords());
    }

    /**
     * Constructor for Company Tracking List
     */
    public ListItemTrackingViewModel(String listType, Company company, String mapsApiKey, boolean showUpdates) {
        this.listType = listType;
        this.company = company;
        this.mapsApiKey = mapsApiKey;
        this.showUpdates = showUpdates;
        this.project = null;

        setExpandableMode();

        // detail TextView 1
        StringBuilder sb = new StringBuilder();
        if(company.getAddress1() != null) sb.append(" " + company.getAddress1());
        if(company.getAddress2() != null) sb.append(" " + company.getAddress2());
        setDetail1(sb.toString());


        // detail TextView 2
        sb = new StringBuilder();
        sb.append(" ");
        if(company.getCity() != null) sb.append(company.getCity());
        if(company.getCity() != null && company.getState() != null) sb.append(", ");
        if(company.getState() != null) sb.append(company.getState());
        if(company.getZip5() != null) sb.append(" " + company.getZip5());
        setDetail2(sb.toString());
    }

    public String getItemName() {

        if(isProjectList()) return project.getTitle();
        else if(isCompanyList()) return company.getName();
        else return null;
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
        if(isProjectList() && recentBid()) {
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
            if (isProjectList()) {
                setExpandableViewMessage(project.getProjectNotes());
            } else if (isCompanyList()) {
                //TODO: fill in - D is working on details
                setExpandableViewMessage("COMPANY PROJECT NOTES");
            }
        }

        // Stage Update Mode
        else if(hasStageUpdate()) {
            setExpandableMode(EXPANDABLE_MODE_STAGE);
            setExpandableViewIconId(R.drawable.ic_add_note);    //TODO - change to a 'stage' icon, TBD
            setExpandableViewTitle(STAGE_UPDATED);
            if(isProjectList()) {
                setExpandableViewMessage(project.getProjectStage().getName());
            }
            else if(isCompanyList()) {
                setExpandableViewMessage("COMPANY STAGE MESSAGE");
                //TODO - fill in
            }
        }

        // Hidden
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

    /*public String getLocation() {

        if(isProjectList()) return project.getCity() + " , " + project.getState();
        else if(isCompanyList()) return company.getCity() + " , " + company.getState();
        return null;
    }*/

    public String getMapUrl() {

        String mapStr = null;
        if(isProjectList())
        {
            if (project.getGeocode() == null) return null;
            mapStr = String.format(Locale.getDefault(), "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=200x200&" +
                            "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                            project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
        }
        else if(isCompanyList()) {
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/staticmap");
            sb.append("?zoom=16");
            sb.append("&size=200x200");
            sb.append("&markers=color:blue|");
            if(company.getAddress1() != null) sb.append(company.getAddress1() + ",");
            if(company.getAddress2() != null) sb.append(company.getAddress2() + ",");
            if(company.getCity() != null)     sb.append(company.getCity() + ",");
            if(company.getState() != null)    sb.append(company.getState());
            sb.append("&key=" + mapsApiKey);
            //mapStr = String.format((sb.toString().replace(" ", "+")), null);
            mapStr = sb.toString().replace(" ", "+");
        }

        Log.d(TAG, "getMapUrl: mapStr: " + mapStr);

        return mapStr;
    }

    private boolean hasNote() {

        // Project List
        if(isProjectList())
        {
            if (project.getProjectNotes() == null || project.getProjectNotes().length() == 0) {
                return false;
            }
        }

        // Company List
        else if(isCompanyList()) {
            return false;   //TODO - check if we need to implement Company notes
        }

        return true;
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

    private boolean isProjectList() {
        return listType.equals(TrackingListActivity.TRACKING_LIST_TYPE_PROJECT);
    }

    private boolean isCompanyList() {
        return listType.equals(TrackingListActivity.TRACKING_LIST_TYPE_COMPANY);
    }


    ///////////////////////////////
    // BINDINGS

    @Bindable
    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    @Bindable
    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

    @Bindable
    public Boolean getShowDetail2Icon() {
        return isProjectList();
    }

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
