package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;

/**
 * File: LauncherViewModel
 * Created: 8/22/16
 * Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProjectTrackingListViewModel extends BaseObservable {

    private final AppCompatActivity activity;


    public ProjectTrackingListViewModel(AppCompatActivity activity) {

        this.activity = activity;
    }


}
