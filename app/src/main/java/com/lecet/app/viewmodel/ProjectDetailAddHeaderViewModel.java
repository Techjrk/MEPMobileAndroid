package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.lecet.app.utility.Log;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

@Deprecated
public class ProjectDetailAddHeaderViewModel extends BaseObservable {
    private static final String TAG = "DEPRECATED";

    public ProjectDetailAddHeaderViewModel() {
        Log.d(TAG, "Constructor");
    }

    public void onClickAddNote(View view){
        Log.e(TAG, "onClickAddNote: Launch Next Activity");
    }

    public void addPhotoOnClick(){
    }

}
