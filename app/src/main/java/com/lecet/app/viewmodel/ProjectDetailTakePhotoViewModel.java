package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectDetailTakePhotoViewModel extends BaseObservable {

    private static final String TAG = "ProjDetailTakePhotoVM";

    public ProjectDetailTakePhotoViewModel() {
    }

    public void onClickUsePhoto(View view){
        Log.e(TAG, "onClickUsePhoto: Launch Add Image Activity");
        //Intent intent = new Intent(this, ProjectDetailAddImageActivity.class);
        //this.startActivity(intent);
    }


    public void onClickCancel(View view){
        //finish();
    }
}
