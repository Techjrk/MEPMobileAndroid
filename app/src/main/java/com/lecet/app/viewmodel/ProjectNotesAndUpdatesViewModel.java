package com.lecet.app.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectNotesAdapter;
import com.lecet.app.content.ProjectAddImageActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectAddNoteActivity;
import com.lecet.app.content.ProjectNotesAndUpdatesFragment;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ProjectAdditionalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectImageChooserActivity.PROJECT_REPLACE_IMAGE_EXTRA;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesAndUpdatesViewModel extends BaseObservableViewModel {

    private static final String TAG = "ProjectNotesUpdatesVM";

	public static final int NOTE_REQUEST_CODE = 880;
    public static final int RESULT_CODE_PROJECT_ADD_IMAGE = 991;
    public static final int RESULT_CODE_PROJECT_CAMERA_IMAGE = 992;
    public static final int RESULT_CODE_PROJECT_LIBRARY_IMAGE = 993;
	public static final int REQUEST_CODE_NEW_IMAGE = 994;
    public static final int REQUEST_CODE_REPLACE_IMAGE = 995;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1115;

    private final Fragment fragment;
    private final long projectId;
    private final ProjectDomain projectDomain;
    private final  ProjectNotesAndUpdatesFragment.ProjectNotesFragmentListener listener;

    private ProjectNotesAdapter projectNotesAdapter;
    private List<ProjectAdditionalData> additionalNotes;

    private Call<List<ProjectNote>> additonalNotesCall;
    private Call<List<ProjectPhoto>> additonalImagesCall;

    public ProjectNotesAndUpdatesViewModel(Fragment fragment, long projectId, ProjectDomain projectDomain, ProjectNotesAndUpdatesFragment.ProjectNotesFragmentListener listener) {
        super((AppCompatActivity) fragment.getActivity());

        this.fragment = fragment;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
        this.listener = listener;
    }

    /* Lifecycle */

    public void onCreateView(View parent) {

        initNotesRecyclerView(parent);
        getAdditionalNotes(false);
        getImages();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NOTE_REQUEST_CODE) {
            getAdditionalNotes(true);
            getImages();
        }
    }

    /* OnClicks */

    public void onClickAddNote(View view){
        Log.d(TAG, "onClickAddNote: Launch Add Note Activity");
        Intent intent = new Intent(this.fragment.getActivity(), ProjectAddNoteActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        fragment.startActivityForResult(intent, NOTE_REQUEST_CODE);
    }

    public void onClickAddImage(View view){
        if(canSetup()) {
            Log.d(TAG, "onClickAddImage");
            ProjectDetailActivity activity = (ProjectDetailActivity) this.fragment.getActivity();

            Intent intent = new Intent(activity, ProjectAddImageActivity.class);
            intent.putExtra(PROJECT_ID_EXTRA, projectId);
            intent.putExtra(PROJECT_REPLACE_IMAGE_EXTRA, false);
            activity.startActivityForResult(intent, NOTE_REQUEST_CODE);
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
            Log.d(TAG, "canSetup: NeededPermission(s) = " + permissionNeeded.size());

            if(permissionNeeded.size() > 0) {
                String[] tempList = permissionNeeded.toArray(new String[permissionNeeded.size()]);
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

    private void initNotesRecyclerView(View parent) {

        AppCompatActivity activity = getActivityWeakReference().get();

        additionalNotes = new ArrayList<>();
        projectNotesAdapter = new ProjectNotesAdapter(additionalNotes, activity);

        //Scope Limit the Location Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext());

        RecyclerView recyclerView = (RecyclerView) parent.findViewById(R.id.recycler_view_project_notes);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(projectNotesAdapter);
    }


    public void getAdditionalNotes(final boolean refresh) {
        if (refresh) {
            additionalNotes.clear();
        }

        additonalNotesCall = projectDomain.fetchProjectNotes(projectId, new Callback<List<ProjectNote>>() {
            @Override
            public void onResponse(Call<List<ProjectNote>> call, Response<List<ProjectNote>> response) {

                AppCompatActivity activity = getActivityWeakReference().get();

                if (response.isSuccessful()) {

                    List<ProjectNote> responseBody = response.body();

                    // Notify listener of notes count so we can update map in project detail.
                    listener.onPhotosAndNotesUpdated(responseBody != null ? responseBody.size() : 0);

                    if (responseBody != null && additionalNotes != null) {
                        additionalNotes.addAll(responseBody);
                        projectNotesAdapter.notifyDataSetChanged();
                    }

                    if (additionalNotes != null) {
                        Collections.sort(additionalNotes);
                        Collections.reverse(additionalNotes);
                        projectNotesAdapter.notifyDataSetChanged();
                    }

                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(activity.getString(R.string.error_network_title),
                            response.message());
                }

            }

            @Override
            public void onFailure(Call<List<ProjectNote>> call, Throwable t) {

                AppCompatActivity activity = getActivityWeakReference().get();

                dismissProgressDialog();

                showCancelAlertDialog(activity.getString(R.string.error_network_title),
                        activity.getString(R.string.error_network_message));
            }
        });

    }

    private void getImages() {

        additonalImagesCall = projectDomain.fetchProjectImages(projectId, new Callback<List<ProjectPhoto>>() {
            @Override
            public void onResponse(Call<List<ProjectPhoto>> call, Response<List<ProjectPhoto>> response) {

                AppCompatActivity activity = getActivityWeakReference().get();

                if (response.isSuccessful()) {

                    Log.d(TAG, "getAdditionalImages: onResponse");

                    List<ProjectPhoto> responseBody = response.body();

                    // Notify listener of notes count so we can update map in project detail.
                    listener.onPhotosAndNotesUpdated(responseBody != null ? responseBody.size() : 0);

                    if (responseBody != null && additionalNotes != null) {
                        additionalNotes.addAll(responseBody);
                        projectNotesAdapter.notifyDataSetChanged();
                    }

                    if (additionalNotes != null) {
                        Collections.sort(additionalNotes);
                        Collections.reverse(additionalNotes);
                        projectNotesAdapter.notifyDataSetChanged();
                    }

                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(activity.getString(R.string.error_network_title),
                            response.message());
                }

            }

            @Override
            public void onFailure(Call<List<ProjectPhoto>> call, Throwable t) {

                AppCompatActivity activity = getActivityWeakReference().get();

                dismissProgressDialog();

                showCancelAlertDialog(activity.getString(R.string.error_network_title),
                        activity.getString(R.string.error_network_message));
            }
        });
    }


    public void getAdditionalImages(final boolean refresh) {

        additonalImagesCall = projectDomain.fetchProjectImages(projectId, new Callback<List<ProjectPhoto>>() {
            @Override
            public void onResponse(Call<List<ProjectPhoto>> call, Response<List<ProjectPhoto>> response) {
                Log.d(TAG, "getAdditionalImages: onResponse");

                if (response.isSuccessful()) {

                    List<ProjectPhoto> responseBody = response.body();

                    // Notify listener of notes count so we can update map in project detail.
                    listener.onPhotosAndNotesUpdated(responseBody != null ? responseBody.size() : 0);

                    if (refresh) {
                        additionalNotes.clear();
                    }

                    if (responseBody != null && additionalNotes != null) {
                        additionalNotes.addAll(responseBody);
                        projectNotesAdapter.notifyDataSetChanged();
                    }

                    if (additionalNotes != null) {
                        Collections.sort(additionalNotes);
                        Collections.reverse(additionalNotes);
                        projectNotesAdapter.notifyDataSetChanged();
                    }

                } else {

                    AppCompatActivity activity = getActivityWeakReference().get();

                    dismissProgressDialog();

                    showCancelAlertDialog(activity.getString(R.string.error_network_title),
                            response.message());
                }

            }

            @Override
            public void onFailure(Call<List<ProjectPhoto>> call, Throwable t) {

                AppCompatActivity activity = getActivityWeakReference().get();

                dismissProgressDialog();

                showCancelAlertDialog(activity.getString(R.string.error_network_title),
                        activity.getString(R.string.error_network_message));
            }
        });
    }

}

