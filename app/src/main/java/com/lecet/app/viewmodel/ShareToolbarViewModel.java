package com.lecet.app.viewmodel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.MoveToListCallback;
import com.lecet.app.interfaces.TrackedObject;
import com.lecet.app.interfaces.TrackingListObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * File: ShareToolbarViewModel Created: 1/5/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public abstract class ShareToolbarViewModel<T extends RealmObject & TrackedObject, U extends RealmObject & TrackingListObject> extends BaseObservable implements MoveToListCallback<U> {

    private final AppCompatActivity appCompatActivity;
    private final TrackingListDomain trackingListDomain;
    private T trackedObject;

    private ProgressDialog progressDialog;
    private ListPopupWindow mtmPopupWindow;
    private MoveToAdapter mtmAdapter;
    private String hideButtonTitle;

    private Dialog hideDialog;
    private AlertDialog alertDialog;


    public ShareToolbarViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, T trackedObject) {

        this.appCompatActivity = appCompatActivity;
        this.trackingListDomain = trackingListDomain;
        this.trackedObject = trackedObject;
    }

    public abstract MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<U> lists);

    public abstract U getAssociatedTrackingList(T trackedObject);

    public abstract RealmResults<U> getUserTrackingListsExcludingCurrentList(U currentTrackingList);

    public abstract RealmResults<U> getAllUserTrackingLists();

    public abstract RealmList<T> getTrackedItems(U trackingList);

    public abstract void removeTrackedObjectFromTrackingList(long trackingListId, List<Long> trackedIds);

    public abstract void addTrackedObjectToTrackingList(long trackingListId, List<Long> trackedIds);

    public abstract void onShareObjectSelected(T trackedObject);

    public abstract void onHideObjectSelected(T trackedObject);

    public TrackingListDomain getTrackingListDomain() {
        return trackingListDomain;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public T getTrackedObject() {
        return trackedObject;
    }

    public void setTrackedObject(T trackedObject) {
        this.trackedObject = trackedObject;
    }

    @SuppressWarnings("unused")
    public void onTrackSelected(View view) {

        showTrackWindow(view);
    }

    @SuppressWarnings("unused")
    public void onShareSelected(View view) {

        showShareWindow(view);
    }

    @SuppressWarnings("unused")
    public void onHideSelected(View view) {

        showHideDialog();
    }

    /* PopupWindow */

    private void dismissWindow() {

        if (mtmPopupWindow != null && mtmPopupWindow.isShowing()) {

            mtmPopupWindow.dismiss();
        }
    }

    private void showTrackWindow(View view) {

        dismissWindow();
        dismissDialog();
        toogleMoveMenu(view);
    }

    private void showShareWindow(View view) {

        dismissWindow();
        dismissDialog();
        onShareObjectSelected(trackedObject);

        // We have no way of knowing if the share intent was dismissed, so let's clear the group on selection
        clearRadioGroup();
    }

    /* Tracking List */

    private void toogleMoveMenu(View view) {
        if (mtmPopupWindow == null) {
            createMoveMenu(view);
        } else {

            U currentTrackingList = getAssociatedTrackingList(trackedObject);

            if (currentTrackingList == null) {

                mtmAdapter.setTrackingLists(getAllUserTrackingLists());

            } else {

                mtmAdapter.setTrackingLists(getUserTrackingListsExcludingCurrentList(currentTrackingList));
            }

        }
        mtmPopupWindow.show();
    }

    private void createMoveMenu(View anchor) {
        if (mtmPopupWindow == null) {
            mtmPopupWindow = new ListPopupWindow(appCompatActivity);

            U currentTrackingList = getAssociatedTrackingList(trackedObject);
            RealmResults<U> trackingLists;

            if (currentTrackingList == null) {

                trackingLists = getAllUserTrackingLists();

            } else {

                trackingLists = getUserTrackingListsExcludingCurrentList(currentTrackingList);
            }

            mtmAdapter = getMoveToListAdapter(appCompatActivity, appCompatActivity.getResources().getString(R.string.move_to), this, trackingLists);

            Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - appCompatActivity.getResources().getDimensionPixelSize(R.dimen.mtm_sort_menu_space);
            int[] coordinates = new int[2];
            anchor.getLocationOnScreen(coordinates);
            int offset = (int) (coordinates[0]
                    - (appCompatActivity.getResources().getDimensionPixelSize(R.dimen.mtm_sort_menu_space) / 2.0));
            mtmPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.more_menu_upsidedown_background));
            mtmPopupWindow.setAnchorView(anchor);
            mtmPopupWindow.setModal(true);
            mtmPopupWindow.setWidth(width);
            mtmPopupWindow.setHorizontalOffset(-offset);
            mtmPopupWindow.setAdapter(mtmAdapter);
            mtmPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    clearRadioGroup();
                    mtmPopupWindow = null;
                }
            });
        }
    }

    /* Radio Group */
    public void clearRadioGroup() {

        RadioGroup radioGroup = (RadioGroup) appCompatActivity.findViewById(R.id.share_radio_group);
        radioGroup.clearCheck();
    }

    /* Popup Window */

    private void dismissDialog() {

        if (hideDialog != null && hideDialog.isShowing()) {

            hideDialog.dismiss();
        }
    }

    private void showHideDialog() {

        dismissWindow();
        dismissDialog();
        onHideObjectSelected(trackedObject);
    }

    /* Alert Dialog */

    public void dismissAlertDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public void showAlertDialog(String title, String message) {

        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(appCompatActivity.getString(R.string.ok), null);

        alertDialog = builder.show();
    }

    /* Progress Dialog */
    public void showProgressDialog(String title, String message) {

        dismissProgressDialog();

        progressDialog = ProgressDialog.show(appCompatActivity, title, message, true, false);
    }

    public void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    /* MoveToListCallback */
    public void onTrackingListClicked(U trackingList) {

        dismissWindow();
        handleTrackingListSelected(trackedObject, trackingList);
    }

    /* Hide Button **/
    @Bindable
    public String getHideButtonTitle() {

        return hideButtonTitle;
    }

    public void setHideButtonTitle(String hideButtonTitle) {
        this.hideButtonTitle = hideButtonTitle;
        notifyPropertyChanged(BR.hideButtonTitle);
    }

    /* Tracking List Management */

    public void handleTrackingListSelected(T trackedObject, U trackingList) {

        // Remove from any existing tracking list, then add to new tracking list
        U existingTrackingList = getAssociatedTrackingList(trackedObject);

        if (existingTrackingList != null) {

            List<Long> retainedIds = getRetainedIds(trackedObject, existingTrackingList);
            removeTrackedObjectFromTrackingList(existingTrackingList.getId(), retainedIds);
        }

        List<Long> trackedIds = getAddedIds(trackedObject, trackingList);
        addTrackedObjectToTrackingList(trackingList.getId(), trackedIds);
    }

    public List<Long> getTrackedIds(List<T> selectedItems) {

        List<Long> ids = new ArrayList<>(selectedItems.size());

        for (T trackedObject : selectedItems) {
            ids.add(trackedObject.getId());
        }

        return ids;
    }

    public List<Long> getRetainedIds(T currentTrackedObject, U trackingList) {

        RealmList<T> trackedItems = getTrackedItems(trackingList);
        List<Long> currentIds = getTrackedIds(trackedItems);
        currentIds.remove(currentTrackedObject.getId());

        return currentIds;
    }

    public List<Long> getAddedIds(T currentTrackedObject, U trackingList) {

        RealmList<T> trackedItems = getTrackedItems(trackingList);
        List<Long> currentIds = getTrackedIds(trackedItems);
        currentIds.add(currentTrackedObject.getId());

        return currentIds;
    }


}
