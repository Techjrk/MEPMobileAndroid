package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectDetailTakePhotoViewModel extends BaseObservable {

    private static final String TAG = "ProjDetailTakePhotoVM";

    private Activity activity;
    private long projectId;

    public ProjectDetailTakePhotoViewModel(Activity activity, long projectId) {
        this.activity = activity;
        this.projectId = projectId;
    }

    // removed this as there is no Use button at the top of the Activity which uses this ViewModel
    /*public void onClickUsePhoto(View view){
        Log.e(TAG, "onClickUsePhoto: Launch Add Image Activity");
        //Intent intent = new Intent(this, ProjectDetailAddImageActivity.class);
        //this.startActivity(intent);
    }*/


    public void onClickCancel(View view) {
        Log.e(TAG, "onClickCancel: onClickCancel called");
        activity.onBackPressed();
    }
}
