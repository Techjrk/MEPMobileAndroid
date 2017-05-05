package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;

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
