package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

import io.realm.ObjectChangeSet;
import io.realm.RealmObjectChangeListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ListItemHiddenProjectViewModel Created: 1/31/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ListItemHiddenProjectViewModel extends CompanyDetailProjectViewModel {

    private final Context context;
    private final ProjectDomain projectDomain;

    private String hideTitle;
    private boolean hidden;

    @ColorRes
    int hiddenTextViewColor;

    private RealmObjectChangeListener<Project> objectChangeListener;
    private Project resultProject;

    public ListItemHiddenProjectViewModel(Context context, ProjectDomain projectDomain, Project project, String mapsApiKey) {
        super(project, mapsApiKey);
        this.context = context;
        this.projectDomain = projectDomain;


        // We will make an async call to get the Project and listen for any updates to update the UI
        resultProject =  projectDomain.fetchProjectAsync(project.getId(), new RealmObjectChangeListener<Project>() {
            @Override
            public void onChange(Project object, ObjectChangeSet changeSet) {
                if (changeSet == null) {
                    // The first time async returns with an null changeSet.
                    updateFields(object);
                    resultProject.removeAllChangeListeners();
                } else {
                    // Called on every update.
                    updateFields(object);
                    resultProject.removeAllChangeListeners();
                }
            }
        });
    }

    public void setObjectChangeListener(RealmObjectChangeListener<Project> objectChangeListener) {
        this.objectChangeListener = objectChangeListener;
    }

    @Bindable
    public String getHideTitle() {

        return hideTitle;
    }

    public void setHideTitle(String hideTitle) {
        this.hideTitle = hideTitle;
        notifyPropertyChanged(BR.hideTitle);
    }

    @Bindable
    @ColorRes
    public int getHiddenTextViewColor() {

        return hiddenTextViewColor;
    }

    public void setHiddenTextViewColor(int hiddenTextViewColor) {
        this.hiddenTextViewColor = hiddenTextViewColor;
    }

    private void updateFields(Project project) {

        hidden = project.isHidden();
        setHideTitle(hidden ? context.getString(R.string.unhide) : context.getString(R.string.hide));
        setHiddenTextViewColor(hidden ? R.color.lecetHideBlue : R.color.lecetUnhideBlue);
    }

    public void onHideSelected(View view) {

        if (hidden) {

            projectDomain.unhideProject(getProject().getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        hidden = false;
                        projectDomain.setProjectHiddenAsync(getProject(), hidden);
                        setHideTitle(context.getString(R.string.hide));
                        setHiddenTextViewColor(R.color.lecetHideBlue);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                }
            });

        } else {

            projectDomain.hideProject(getProject().getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        hidden = true;
                        projectDomain.setProjectHiddenAsync(getProject(), hidden);
                        setHideTitle(context.getString(R.string.unhide));
                        setHiddenTextViewColor(R.color.lecetUnhideBlue);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void removeChangeListener() {

        if (resultProject != null) {
            resultProject.removeAllChangeListeners();
        }
    }
}
