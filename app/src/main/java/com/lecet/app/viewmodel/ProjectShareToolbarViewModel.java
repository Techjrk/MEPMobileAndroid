package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToProjectListAdapter;
import com.lecet.app.content.LecetConfirmDialogFragment;
import com.lecet.app.content.MainActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MoveToListCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectShareToolbarViewModel Created: 1/9/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectShareToolbarViewModel extends ShareToolbarViewModel<Project, ProjectTrackingList> implements LecetConfirmDialogFragment.ConfirmDialogListener {


    public ProjectShareToolbarViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, Project trackedObject) {
        super(appCompatActivity, trackingListDomain, trackedObject);
        setHideButtonTitle(getTrackedObject().isHidden() ? getAppCompatActivity().getString(R.string.unhide) : getAppCompatActivity().getString(R.string.hide));
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<ProjectTrackingList> lists) {
        return new MoveToProjectListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public ProjectTrackingList getAssociatedTrackingList(Project trackedObject) {

        RealmResults<ProjectTrackingList> results = getTrackingListDomain().fetchProjectTrackingListsContainingProject(trackedObject.getId());
        if (results.size() > 0) {

            return results.first();
        }

        return null;
    }

    @Override
    public RealmResults<ProjectTrackingList> getUserTrackingListsExcludingCurrentList(ProjectTrackingList currentTrackingList) {
        return getTrackingListDomain().fetchProjectTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }

    @Override
    public RealmResults<ProjectTrackingList> getAllUserTrackingLists() {
        return getTrackingListDomain().fetchUserProjectTrackingList();
    }

    @Override
    public RealmList<Project> getTrackedItems(ProjectTrackingList trackingList) {
        return trackingList.getProjects();
    }

    @Override
    public void removeTrackedObjectFromTrackingList(long trackingListId, List<Long> trackedIds) {

        final ProjectTrackingList prevList = getAssociatedTrackingList(getTrackedObject());

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        getTrackingListDomain().syncProjectTrackingList(trackingListId, trackedIds, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    getTrackingListDomain().copyToRealmTransaction(response.body());

                    List<Long> selectedItems = new ArrayList<>();
                    selectedItems.add(getTrackedObject().getId());

                    // Remove items from Tracking list relationship
                    if (prevList != null) {
                        asyncDeleteProjects(prevList.getId(), selectedItems);
                    }

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    @Override
    public void addTrackedObjectToTrackingList(long trackingListId, List<Long> trackedIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        getTrackingListDomain().syncProjectTrackingList(trackingListId, trackedIds, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    getTrackingListDomain().copyToRealmTransaction(response.body());
                    dismissProgressDialog();

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    /* Delete */
    private void asyncDeleteProjects(long trackingListId, List<Long> toBeDeletedIds) {

        getTrackingListDomain().deleteProjectsFromTrackingListAsync(trackingListId, toBeDeletedIds, new LecetCallback<ProjectTrackingList>() {
            @Override
            public void onSuccess(ProjectTrackingList result) {

                dismissProgressDialog();
            }

            @Override
            public void onFailure(int code, String message) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), message);
            }
        });
    }

    @Override
    public void onShareObjectSelected(Project trackedObject) {

        String projectUrl = LecetClient.ENDPOINT + "project/" + trackedObject.getId();

        StringBuilder sb = new StringBuilder();
        sb.append("DODGE NUMBER : ");
        sb.append(trackedObject.getDodgeNumber());
        sb.append("/n");
        sb.append("WEB LINK : ");
        sb.append(projectUrl);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/plain");
        getAppCompatActivity().startActivity(Intent.createChooser(sendIntent, getAppCompatActivity().getResources().getText(R.string.share_project)));
    }

    @Override
    public void onHideObjectSelected(Project trackedObject) {

        String message = String.format(trackedObject.isHidden() ? getAppCompatActivity().getString(R.string.you_are_about_unhide) : getAppCompatActivity().getString(R.string.you_are_about_hide), getAppCompatActivity().getString(R.string.project));
        String positive = String.format(trackedObject.isHidden() ? getAppCompatActivity().getString(R.string.unhide_blank) :  getAppCompatActivity().getString(R.string.hide_blank), getAppCompatActivity().getString(R.string.project));

        LecetConfirmDialogFragment dialogFragment = LecetConfirmDialogFragment.newInstance(message, positive, getAppCompatActivity().getString(android.R.string.cancel));

        dialogFragment.setCallbackListener(this);
        dialogFragment.show(getAppCompatActivity().getSupportFragmentManager(), LecetConfirmDialogFragment.TAG);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        clearRadioGroup();

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), "");

        final Project project = getTrackedObject();
        if (project.isHidden()) {

            getTrackingListDomain().getProjectDomain().unhideProject(project.getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        getTrackingListDomain().getProjectDomain().setProjectHidden(project, false);
                        setHideButtonTitle(getAppCompatActivity().getString(R.string.hide));
                        dismissProgressDialog();

                    } else {

                        dismissProgressDialog();
                        showAlertDialog(getAppCompatActivity().getString(R.string.app_name), response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    dismissProgressDialog();
                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
                }
            });

        } else {

            getTrackingListDomain().getProjectDomain().hideProject(project.getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        getTrackingListDomain().getProjectDomain().setProjectHidden(project, true);
                        setHideButtonTitle(getAppCompatActivity().getString(R.string.unhide));
                        dismissProgressDialog();

                        // Finish the activity and clear the stack
                        Intent intent = new Intent(getAppCompatActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getAppCompatActivity().startActivity(intent);
                        getAppCompatActivity().finish();

                    } else {

                        dismissProgressDialog();
                        showAlertDialog(getAppCompatActivity().getString(R.string.app_name), response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    dismissProgressDialog();
                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
                }
            });
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        clearRadioGroup();
        dialog.dismiss();
    }

    @Override
    public void onDialogCancel(DialogFragment dialog) {

        clearRadioGroup();
    }

}
