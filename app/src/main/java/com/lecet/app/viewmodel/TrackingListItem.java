package com.lecet.app.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.models.ActivityUpdate;

import io.realm.RealmObject;

/**
 * File: TestTrackingListItem Created: 12/2/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public abstract class TrackingListItem<T extends RealmObject> {

    // Primary Fields
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> primaryDetail = new ObservableField<>();
    public final ObservableField<String> secondaryDetail = new ObservableField<>();
    public final ObservableField<String> mapUrlString = new ObservableField<>();

    // Update Related
    public final ObservableField<String> activityUpdateTitle = new ObservableField<>();
    public final ObservableField<String> activityUpdateMessage = new ObservableField<>();

    // UI Related
    public final ObservableBoolean displaySecondaryDetailIcon = new ObservableBoolean();
    public final ObservableInt secondaryDetailIconResourceID = new ObservableInt();
    public final ObservableBoolean displayActivityUpdate = new ObservableBoolean();
    public final ObservableBoolean activityUpdateAvailable = new ObservableBoolean();
    public final ObservableBoolean activityUpdateLayoutExpanded = new ObservableBoolean();
    public final ObservableInt activityUpdateIconResourceID = new ObservableInt();


    private final T object;

    public TrackingListItem(T object, boolean displayUpdate) {

        this.object = object;
        displayActivityUpdate.set(displayUpdate);
        activityUpdateLayoutExpanded.set(false);
        //init(object);
    }

    public T getObject() {
        return object;
    }

    public abstract String generateTitle(T object);

    public abstract String generatePrimaryDetail(T object);

    public abstract String generateSecondaryDetail(T object);

    public abstract String generateMapUrl(T object);

    public abstract boolean showActivityUpdate(T object);

    public abstract boolean showSecondaryDetailIcon();

    public void init(T object) {
        title.set(generateTitle(object));
        primaryDetail.set(generatePrimaryDetail(object));
        secondaryDetail.set(generateSecondaryDetail(object));
        mapUrlString.set(generateMapUrl(object));
        displaySecondaryDetailIcon.set(showSecondaryDetailIcon());

        if (showSecondaryDetailIcon()) {

            secondaryDetailIconResourceID.set(secondaryDetailIconResourceId());
        }

        if (showActivityUpdate(object)) {

            activityUpdateAvailable.set(true);
            activityUpdateTitle.set(activityTitle());
            activityUpdateMessage.set(activityMessage());
            activityUpdateIconResourceID.set(activityUpdateIconResourceId());
        }
    }


    @DrawableRes
    public int secondaryDetailIconResourceId() {

        return R.drawable.ic_yellow_marker;
    }

    public String activityTitle() {
        return null;
    }

    public String activityMessage() {
        return null;
    }

    @DrawableRes
    public int activityUpdateIconResourceId() {

        return R.drawable.ic_yellow_marker;
    }

    public void refreshActivityUpdateDisplay(ActivityUpdate update) {

        activityUpdateAvailable.set(update != null);
        activityUpdateTitle.set(update.getModelTitle());
        activityUpdateMessage.set(update.getSummary());
        activityUpdateIconResourceID.set(activityUpdateIconResourceId());
    }

    ////////////////////////////////////
    // CLICK HANDLERS

    @SuppressWarnings("unused")
    public void onExpandableViewClick(View view) {
        activityUpdateLayoutExpanded.set(!activityUpdateLayoutExpanded.get());
    }
}
