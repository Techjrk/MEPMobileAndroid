package com.lecet.app.contentbase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.lecet.app.R;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

/**
 * File: LecetBaseActivity Created: 10/20/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public abstract class LecetBaseActivity extends AppCompatActivity {

    private boolean displayingNetworkAlert;
    private AlertDialog networkAlertDialog;

    public BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager conn = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                onNetworkConnectionChanged(true, networkInfo);

                if (isDisplayingNetworkAlert()) {

                    hideNetworkAlert();
                }

            } else {

                onNetworkConnectionChanged(false, networkInfo);

                showNetworkAlert();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        checkForUpdates();
        checkForCrashes();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isNetworkConnected() && !isDisplayingNetworkAlert()) {

            showNetworkAlert();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkReceiver);
        unregisterManagers();
    }

    /** Hockey App **/

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }


    /** Keyboard management **/

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.FLAG_EDITOR_ACTION:
                onKeyboardEditorActionSelected();
                return true;
            case KeyEvent.KEYCODE_ENTER:
                onKeyboardEditorActionSelected();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }


    /**
     *  Override this method to handle a soft keyboards action button
     *  selection.
     */
    public void onKeyboardEditorActionSelected() {}


    /** Subclasses should override **/

    public abstract void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo);

    /** Network connectivity logic **/

    public boolean isNetworkConnected() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isDisplayingNetworkAlert() {
        return displayingNetworkAlert;
    }

    public void hideNetworkAlert() {

        displayingNetworkAlert = false;

        if (networkAlertDialog != null && networkAlertDialog.isShowing()) {

            networkAlertDialog.dismiss();
        }
    }

    public void showNetworkAlert() {

        if (displayingNetworkAlert) return;

        displayingNetworkAlert = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error_network_title));
        builder.setMessage(getString(R.string.error_network_message));
        builder.setNegativeButton(getString(R.string.ok), null);

        networkAlertDialog = builder.show();
    }
}
