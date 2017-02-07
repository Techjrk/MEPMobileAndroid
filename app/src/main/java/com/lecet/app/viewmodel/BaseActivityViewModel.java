package com.lecet.app.viewmodel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;

/**
 * File: BaseActivityViewModel Created: 1/30/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class BaseActivityViewModel extends BaseObservable {

    private ProgressDialog progressDialog;
    private Dialog alertDialog;


    public void dismissAlertDialog() {

        if (alertDialog != null && alertDialog.isShowing()) {

            alertDialog.dismiss();
        }
    }

    public void showProgressDialog(AppCompatActivity appCompatActivity, String title, String message) {

        dismissAlertDialog();
        dismissProgressDialog();

        progressDialog = ProgressDialog.show(appCompatActivity, title, message, true, false);
    }

    public void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    public void showCancelAlertDialog(AppCompatActivity appCompatActivity, String title, String message) {

        dismissProgressDialog();
        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(appCompatActivity.getString(R.string.ok), null);

        alertDialog = builder.show();
    }

    public void showTwoButtonAlertDialog(AppCompatActivity appCompatActivity, String title, String message, String negativeTitle, DialogInterface.OnClickListener negativeListener,
                                          String positiveTitle, DialogInterface.OnClickListener positiveListener) {

        dismissProgressDialog();
        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negativeTitle, negativeListener);
        builder.setPositiveButton(positiveTitle, positiveListener);

        alertDialog = builder.show();
    }

}
