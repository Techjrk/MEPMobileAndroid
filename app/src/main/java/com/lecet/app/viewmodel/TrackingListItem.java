package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.models.ActivityUpdate;

import java.text.DecimalFormat;

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
    public final ObservableField<String> activityUpdateDetail = new ObservableField<>();

    // UI Related
    public final ObservableBoolean displaySecondaryDetailIcon = new ObservableBoolean();
    public final ObservableInt secondaryDetailIconResourceID = new ObservableInt();
    public final ObservableBoolean displayActivityUpdate = new ObservableBoolean();
    public final ObservableBoolean activityUpdateAvailable = new ObservableBoolean();
    public final ObservableBoolean activityUpdateLayoutExpanded = new ObservableBoolean();
    public final ObservableInt activityUpdateIconResourceID = new ObservableInt();

    private final T object;
    private final Context context;

    public TrackingListItem(Context context, T object, boolean displayUpdate) {

        this.context = context;
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

    public abstract void handleTrackingItemSelected(View view, T object);

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

        if (update == null) return;

        activityUpdateAvailable.set(true);

        switch (update.getModelType()) {
            case "Bid":

                activityUpdateTitle.set(context.getString(R.string.new_bid_placed));

                DecimalFormat formatter = new DecimalFormat("$ #,###");
                activityUpdateMessage.set(formatter.format(update.getBidUpdate().getAmount()));
                activityUpdateDetail.set(update.getBidUpdate().getCompany().getName());
                break;

            case "ProjectStage":

                StringBuilder stageSb = new StringBuilder(context.getString(R.string.new_stage));
                stageSb.append(update.getStageUpdate().getName());
                activityUpdateTitle.set(stageSb.toString());
                break;

            case "ProjectContact":

                activityUpdateTitle.set(context.getString(R.string.new_contact_added));
                activityUpdateMessage.set(update.getContactUpdate().getCompany().getName());
                activityUpdateDetail.set(update.getContactUpdate().getTitle());
                break;

            case "WorkType":
            case "ProjectType":
                break;

            default:
                break;
        }

        activityUpdateIconResourceID.set(activityUpdateIconResourceId());
    }

    ////////////////////////////////////
    // CLICK HANDLERS

    @SuppressWarnings("unused")
    public void onExpandableViewClick(View view) {
        activityUpdateLayoutExpanded.set(!activityUpdateLayoutExpanded.get());
    }

    public void onTrackingItemSelected(View view) {

        handleTrackingItemSelected(view, object);
    }
}
