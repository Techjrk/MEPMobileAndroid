package com.lecet.app.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.ProjectDetailAddImageActivity;
import com.lecet.app.content.ProjectDetailAddNoteActivity;
import com.lecet.app.content.ProjectDetailTakePhotoActivity;
import com.lecet.app.domain.ProjectDomain;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesAndUpdatesViewModel extends BaseObservable {

    private static final String TAG = "ProjectNotesUpdatesVM";
    private final int REQUEST_CODE_ASK_PERMISSIONS = 1115;
    private final Fragment fragment;
    private final long projectId;
    private final ProjectDomain projectDomain;

    public ProjectNotesAndUpdatesViewModel(Fragment fragment, long projectId, ProjectDomain projectDomain) {
        this.fragment = fragment;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
    }

    public void onClickAddNote(View view){
        Log.e(TAG, "onClickAddNote: Launch Add Note Activity");
        Intent intent = new Intent(this.fragment.getActivity(), ProjectDetailAddNoteActivity.class);
        fragment.getActivity().startActivity(intent);
    }

    public void onClickAddImage(View view){
        if(canSetup()) {
            Log.e(TAG, "onClickAddImage: Launch Take Photo Activity");
            Intent intent = new Intent(this.fragment.getActivity(), ProjectDetailTakePhotoActivity.class);
            fragment.getActivity().startActivity(intent);
        }
    }

    private boolean canSetup(){
        if(Build.VERSION.SDK_INT >= 23) {

            int hasCameraPermission = ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA);
            Log.e(TAG, "canSetup: Returning: " + (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));
            if(hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                return (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
            }
            return true;
        }
        return true;
    }

    //TODO - call & fill in
    public void fetchProjectImages() {
        //
    }
}

