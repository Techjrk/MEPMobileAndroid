package com.lecet.app.viewmodel;

import android.app.Dialog;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ListPopupWindow;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * File: ShareToolbarViewModel Created: 1/5/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ShareToolbarViewModel {

    @Retention(SOURCE)
    @IntDef({NAVIGATION_MODE_TRACK, NAVIGATION_MODE_SHARE, NAVIGATION_MODE_HIDE})
    private  @interface NavigationMode {}
    private static final int NAVIGATION_MODE_TRACK = 0;
    private static final int NAVIGATION_MODE_SHARE = 1;
    private static final int NAVIGATION_MODE_HIDE = 2;

    @NavigationMode
    private int selectedMode;


    private ListPopupWindow listPopupWindow;
    private Dialog hideDialog;

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

        if (listPopupWindow != null && listPopupWindow.isShowing()) {

            listPopupWindow.dismiss();
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
}
