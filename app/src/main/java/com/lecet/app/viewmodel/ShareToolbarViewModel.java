package com.lecet.app.viewmodel;

import android.app.Dialog;
import android.databinding.BaseObservable;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Display;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.MoveToListCallback;
import com.lecet.app.interfaces.TrackedObject;
import com.lecet.app.interfaces.TrackingListObject;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * File: ShareToolbarViewModel Created: 1/5/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public abstract class ShareToolbarViewModel<T extends RealmObject & TrackedObject, U extends RealmObject & TrackingListObject> extends BaseObservable implements MoveToListCallback<U> {

    @Retention(SOURCE)
    @IntDef({NAVIGATION_MODE_TRACK, NAVIGATION_MODE_SHARE, NAVIGATION_MODE_HIDE})
    private @interface NavigationMode {
    }

    private static final int NAVIGATION_MODE_TRACK = 0;
    private static final int NAVIGATION_MODE_SHARE = 1;
    private static final int NAVIGATION_MODE_HIDE = 2;

    private final AppCompatActivity appCompatActivity;
    private final TrackingListDomain trackingListDomain;

    @NavigationMode
    private int selectedMode;
    private U currentTrackingList;

    private ListPopupWindow sharePopupWindow;
    private ListPopupWindow mtmPopupWindow;
    private MoveToAdapter mtmAdapter;

    private Dialog hideDialog;

    public ShareToolbarViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain) {

        this.appCompatActivity = appCompatActivity;
        this.trackingListDomain = trackingListDomain;
    }

    public abstract MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<U> lists);

    public abstract RealmResults<U> getUserTrackingListsExcludingCurrentList(U currentTrackingList);

    public abstract RealmList<T> getTrackedItems(U trackingList);


    @SuppressWarnings("unused")
    public void onTrackSelected(View view) {

        if (selectedMode == NAVIGATION_MODE_TRACK) return;

        selectedMode = NAVIGATION_MODE_TRACK;
        showTrackWindow();
    }

    @SuppressWarnings("unused")
    public void onShareSelected(View view) {

        if (selectedMode == NAVIGATION_MODE_SHARE) return;

        selectedMode = NAVIGATION_MODE_SHARE;
        showShareWindow();
    }

    @SuppressWarnings("unused")
    public void onHideSelected(View view) {

        if (selectedMode == NAVIGATION_MODE_HIDE) return;

        selectedMode = NAVIGATION_MODE_HIDE;
        showHideDialog();
    }

    /* PopupWindow */

    private void dismissWindow() {

        if (mtmPopupWindow != null && mtmPopupWindow.isShowing()) {

            mtmPopupWindow.dismiss();
        }

        if (sharePopupWindow != null && sharePopupWindow.isShowing()) {

            sharePopupWindow.dismiss();
        }
    }

    private void showTrackWindow() {

        dismissWindow();
        dismissDialog();
    }

    private void showShareWindow() {

        dismissWindow();
        dismissDialog();
    }

    /* Tracking List */

    private void toogleMoveMenu(View view) {
        if (mtmPopupWindow == null) {
            createMoveMenu(view);
        } else {
            mtmAdapter.setTrackingLists(getUserTrackingListsExcludingCurrentList(currentTrackingList));
        }
        mtmPopupWindow.show();
    }

    private void createMoveMenu(View anchor) {
        if (mtmPopupWindow == null) {
            mtmPopupWindow = new ListPopupWindow(appCompatActivity);

            mtmAdapter = getMoveToListAdapter(appCompatActivity, appCompatActivity.getResources().getString(R.string.move_to), this, getUserTrackingListsExcludingCurrentList(currentTrackingList));

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
        }
    }


    /* Dialog */

    private void dismissDialog() {

        if (hideDialog != null && hideDialog.isShowing()) {

            hideDialog.dismiss();
        }
    }

    private void showHideDialog() {

        dismissWindow();
        dismissDialog();
    }

    /* MoveToListCallback */
    public void onTrackingListClicked(U trackingList) {

        dismissWindow();
        handleMoveItemsClicked(getSelectedItems(), trackingList);
    }

    /* Tracking List Management */

    public List<Long> getTrackedIds(List<T> selectedItems) {

        List<Long> ids = new ArrayList<>(selectedItems.size());

        for (T trackedObject : selectedItems) {
            ids.add(trackedObject.getId());
        }

        return ids;
    }

    public List<Long> getRetainedIds(T currentTrackedObject, List<Long> currentIds) {

        currentIds.remove(currentTrackedObject.getId());

        return currentIds;
    }

    public List<Long> getAddedIds(List<T> selectedItems, U trackingList) {
        RealmList<T> trackedItems = getTrackedItems(trackingList);

        List<Long> currentIds = getTrackedIds(trackedItems);

        for (T trackedObject : selectedItems) {

            currentIds.add(trackedObject.getId());
        }

        return currentIds;
    }
}
