package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.BR;
import com.lecet.app.R;

/**
 * File: DetailFooterViewModel Created: 1/25/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class DetailFooterViewModel extends BaseObservable {

    private final AppCompatActivity appCompatActivity;
    private final String type;
    private final String total;
    private boolean expanded;

    public DetailFooterViewModel(@NonNull AppCompatActivity appCompatActivity, @NonNull String type, @NonNull String total) {

        this.appCompatActivity = appCompatActivity;
        this.type = type;
        this.total = total;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    @Bindable
    public boolean isExpanded() {

        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    public String getTitle() {

        return appCompatActivity.getString(R.string.see_all) + " " + total + " " + type;
    }


}
