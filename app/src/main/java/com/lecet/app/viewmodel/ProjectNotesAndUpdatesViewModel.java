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

import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectDetailAddNoteActivity;
import com.lecet.app.content.ProjectDetailTakePhotoActivity;
import com.lecet.app.domain.ProjectDomain;

import java.util.ArrayList;
import java.util.List;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesAndUpdatesViewModel extends BaseObservable {

    private static final String TAG = "ProjectNotesUpdatesVM";

    public static final int RESULT_CODE_PROJECT_ADD_IMAGE = 907;       //TODO - move to activity?
    public static final int RESULT_CODE_PROJECT_CAMERA_IMAGE = 908;    //TODO - move to activity?
    public static final int RESULT_CODE_PROJECT_LIBRARY_IMAGE = 909;   //TODO - move to activity?
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
        Log.e(TAG, "onClickAddNote");
        Intent intent = new Intent(this.fragment.getActivity(), ProjectDetailAddNoteActivity.class);
        fragment.getActivity().startActivity(intent);
    }

    public void onClickAddImage(View view){
        if(canSetup()) {
            Log.e(TAG, "onClickAddImage");
            ProjectDetailActivity activity = (ProjectDetailActivity) this.fragment.getActivity();
            Intent intent = new Intent(activity, ProjectDetailTakePhotoActivity.class);
            intent.putExtra(PROJECT_ID_EXTRA, projectId);
            //activity.startActivityForResult(intent, RESULT_CODE_PROJECT_CAMERA_IMAGE);
            activity.startActivity(intent);
        }
    }

    private boolean canSetup(){
        if(Build.VERSION.SDK_INT >= 23) {
            List<String> permissionNeeded = new ArrayList<String>();//list of permissions that aren't allowed

            if(!hasPermission(Manifest.permission.CAMERA)){//has Camera permissions
               permissionNeeded.add(Manifest.permission.CAMERA);
            }

            if(!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                permissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            Log.e(TAG, "canSetup: NeededPermission(s) = " + permissionNeeded.size());

            if(permissionNeeded.size() > 0) {
                String[] tempList = new String[permissionNeeded.size()];//TODO: write actual converter from List<String> to String[]
                ActivityCompat.requestPermissions(fragment.getActivity(), permissionNeeded.toArray(tempList), REQUEST_CODE_ASK_PERMISSIONS);
            }else {
                return true;//none were needed
            }

            return false;//if less then 1 then there are no permissions so return true. you can now set up
        }
        return true;
    }

    private boolean hasPermission(String permission){
        if(ActivityCompat.checkSelfPermission(fragment.getActivity(), permission) == PackageManager.PERMISSION_DENIED){
            return false;
        }
        return  true;
    }

    //TODO - call & fill in
    public void fetchProjectImages() {
        //
    }
}

