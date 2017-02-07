package com.lecet.app.contentbase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.content.LauncherActivity;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.lang.ref.WeakReference;

import io.realm.Realm;
import retrofit2.Response;

/**
 * File: BaseViewModel Created: 2/6/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class BaseObservableViewModel extends BaseObservable {

    private static final int HTTP_UNAUTHORIZED = 401;

    private final WeakReference<AppCompatActivity> activityWeakReference;

    private ProgressDialog progressDialog;
    private Dialog alertDialog;


    public BaseObservableViewModel(AppCompatActivity appCompatActivity) {

        this.activityWeakReference = new WeakReference<AppCompatActivity>(appCompatActivity);
    }

    public WeakReference<AppCompatActivity> getActivityWeakReference() {
        return activityWeakReference;
    }

    /** Dialogs **/

    public void dismissAlertDialog() {

        if (alertDialog != null && alertDialog.isShowing()) {

            alertDialog.dismiss();
        }
    }

    public void showProgressDialog(String title, String message) {

        // If activity is not alive. Don't do anything.
        if (! isActivityAlive()) return;

        AppCompatActivity appCompatActivity = activityWeakReference.get();

        dismissAlertDialog();
        dismissProgressDialog();

        progressDialog = ProgressDialog.show(appCompatActivity, title, message, true, false);
    }

    public void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    public void showCancelAlertDialog(String title, String message) {

        showCancelAlertDialog(title, message, null);
    }

    public void showCancelAlertDialog(String title, String message, DialogInterface.OnClickListener listener) {

        // If activity is not alive. Don't do anything.
        if (! isActivityAlive()) return;

        AppCompatActivity appCompatActivity = activityWeakReference.get();

        dismissProgressDialog();
        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(appCompatActivity.getString(R.string.ok), listener);

        alertDialog = builder.show();
    }

    public void showTwoButtonAlertDialog(String title, String message, String negativeTitle, DialogInterface.OnClickListener negativeListener,
                                         String positiveTitle, DialogInterface.OnClickListener positiveListener) {

        // If activity is not alive. Don't do anything.
        if (! isActivityAlive()) return;

        AppCompatActivity appCompatActivity = activityWeakReference.get();

        dismissProgressDialog();
        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negativeTitle, negativeListener);
        builder.setPositiveButton(positiveTitle, positiveListener);

        alertDialog = builder.show();
    }

    /** Activity LifeCycle **/

    public boolean isActivityAlive() {

        AppCompatActivity activity = activityWeakReference.get();

        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
    }

    /** Session Management **/

    public <T> boolean  isSessionUnauthorized(@NonNull Response<T> retrofitResponse) {

        return retrofitResponse.code() == HTTP_UNAUTHORIZED;
    }

    public void handleUnAuthorizedUser() {

        clearSessionData();
        redirectUnauthorizedUser();
    }

    public void redirectUnauthorizedUser() {

        AppCompatActivity appCompatActivity = activityWeakReference.get();

        if (appCompatActivity == null) return;

        Intent i = new Intent(appCompatActivity, LauncherActivity.class);
        // Clear activity stack
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public void clearSessionData() {

        final AppCompatActivity appCompatActivity = activityWeakReference.get();

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all data in realm and clear shard preferences
                realm.deleteAll();

                LecetSharedPreferenceUtil.getInstance(appCompatActivity).clearPreferences();
            }
        });
    }

    public void displayUnauthorizedUserDialog() {

        AppCompatActivity appCompatActivity = activityWeakReference.get();
        String title = appCompatActivity.getString(R.string.app_name);
        String message = appCompatActivity.getString(R.string.error_unauthorized_user);

        showCancelAlertDialog(title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                handleUnAuthorizedUser();
            }
        });
    }
}
