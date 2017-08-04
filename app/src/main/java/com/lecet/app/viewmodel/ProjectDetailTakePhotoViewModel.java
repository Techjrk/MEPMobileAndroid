package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.lecet.app.utility.Log;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectDetailTakePhotoViewModel extends BaseObservable {

    private static final String TAG = "ProjDetailTakePhotoVM";

    private Activity activity;
    private long projectId;

    public ProjectDetailTakePhotoViewModel(Activity activity, long projectId){
        this.activity = activity;
        this.projectId = projectId;
    }

    public void onClickCancel(View view){
        Log.e(TAG, "onClickCancel: onClickCancel called");
         activity.onBackPressed();
    }
}
