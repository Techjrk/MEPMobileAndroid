package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.utility.Log;

import java.text.SimpleDateFormat;

/**
 * File: ListItemTrackingViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public abstract class ListItemTrackingViewModel extends BaseObservable {

    private static final String TAG = "ListItemProjTrackingVM";

    public static final String EXPANDABLE_MODE_GONE = "gone";
    public static final String EXPANDABLE_MODE_BID = "Bid";
    public static final String EXPANDABLE_MODE_NOTE = "note";
    public static final String EXPANDABLE_MODE_STAGE = "ProjectStage";
    public static final String EXPANDABLE_MODE_CONTACT = "ProjectContact";

    private String expandableMode = EXPANDABLE_MODE_GONE;

    private final String NEW_BID_PLACED = "A new bid has been placed";      // TODO - Externalize, which will require Context
    private final String NEW_NOTE_ADDED = "A new note has been added";
    private final String BID_PLACED_AT = "A bid was placed at";
    private final String STAGE_UPDATED = "The project stage is now";
    private final long RECENT_BID_MS = 1000 * 60 * 60 * 24 * 14;            // ms * secs * mins * hrs * days

    private final String mapsApiKey;
    private final ActivityUpdate update;

    private String detail1;
    private String detail2;
    private final boolean showUpdates;
    private boolean showExpandableView;
    private boolean expandableViewExpanded;

    private int expandableViewIconId;
    private String expandableViewTitle = "";
    private String expandableViewMessage = "";


    public ListItemTrackingViewModel(String mapsApiKey, boolean showUpdates, ActivityUpdate update) {

        this.mapsApiKey = mapsApiKey;
        this.showUpdates = showUpdates;
        this.update = update;
    }

    public abstract String getItemName();

    public abstract String generateDetailSecondary();

    public abstract String generateDetailPrimary();

    public abstract String getMapUrl();

    public abstract boolean displaySecondaryDetail();

    /**
     * Init
     **/
    public void init() {

        setExpandableMode();
        setDetail1(generateDetailPrimary());
        setDetail2(generateDetailSecondary());
    }

    /**
     * Getters
     **/
    public String getMapsApiKey() {
        return mapsApiKey;
    }

    private void setExpandableMode() {

        if (update != null && update.getModelType() != null) {

            // Bid mode
            if (update.getModelType().matches(EXPANDABLE_MODE_BID)) {
                setExpandableMode(EXPANDABLE_MODE_BID);
                setExpandableViewIconId(R.drawable.ic_add);
                setExpandableViewTitle(NEW_BID_PLACED);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm aaa");
                String formattedDate = sdf.format(update.getBidUpdate().getCreateDate());
                setExpandableViewMessage(BID_PLACED_AT + " " + formattedDate);
            }

            // Note Mode
            else if (update.getModelType().matches(EXPANDABLE_MODE_NOTE)) {
                setExpandableMode(EXPANDABLE_MODE_BID);
                setExpandableViewIconId(R.drawable.ic_add_note);
                setExpandableViewTitle(NEW_NOTE_ADDED);
                setExpandableViewMessage(update.getSummary());
            }

            // Stage Update Mode
            else if (update.getModelType().matches(EXPANDABLE_MODE_STAGE)) {
                setExpandableMode(EXPANDABLE_MODE_STAGE);
                setExpandableViewIconId(R.drawable.ic_update_stage);
                setExpandableViewTitle(STAGE_UPDATED);
                setExpandableViewMessage(update.getStageUpdate().getName());
            }

            // Stage Contact Mode
            else if (update.getModelType().matches(EXPANDABLE_MODE_CONTACT)) {
                setExpandableMode(EXPANDABLE_MODE_CONTACT);
                setExpandableViewIconId(R.drawable.ic_update_contact);
                setExpandableViewTitle(update.getSummary());
                setExpandableViewMessage(update.getContactUpdate().getCompany().getName());
            }

        } else {

            setExpandableMode(EXPANDABLE_MODE_GONE);
            setExpandableViewTitle("");
            setExpandableViewMessage("");
        }

        // set the showExpandableView to true if this item is in a mode such as Bid, Note, or Stage
        setShowExpandableView(expandableMode.equals(EXPANDABLE_MODE_BID) || expandableMode.equals(EXPANDABLE_MODE_NOTE) || expandableMode.equals(EXPANDABLE_MODE_STAGE));
    }

    @Bindable
    public String getExpandableMode() {
        return expandableMode;
    }

    public void setExpandableMode(String expandableMode) {
        this.expandableMode = expandableMode;
    }

    ///////////////////////////////
    // BINDINGS

    @Bindable
    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
        notifyPropertyChanged(BR.detail1);
    }

    @Bindable
    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
        notifyPropertyChanged(BR.detail2);
    }

    @Bindable
    public Boolean getShowDetail2Icon() {
        return displaySecondaryDetail();
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
        notifyPropertyChanged(BR.showExpandableView);
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
        notifyPropertyChanged(BR.expandableViewIconId);
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
        Log.d(TAG, "onClick: " + view);
    }


}
