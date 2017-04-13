package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.repacked.kotlin.Deprecated;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


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
