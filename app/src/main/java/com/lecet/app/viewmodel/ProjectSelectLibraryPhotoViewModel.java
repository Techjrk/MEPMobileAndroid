package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * Created by jasonm on 3/29/17.
 */
public class ProjectSelectLibraryPhotoViewModel extends BaseObservable {

    private static final String TAG = "ProjSelectLibPhotoVM";

    private Fragment fragment;
    private long projectId;

    public ProjectSelectLibraryPhotoViewModel(Fragment fragment, long projectId) {
        this.fragment = fragment;
        this.projectId = projectId;

        //initImageChooser();
    }

    public void initImageChooser() {
        Log.d(TAG, "initImageChooser");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.getActivity().startActivityForResult(intent, 1);
    }
}
