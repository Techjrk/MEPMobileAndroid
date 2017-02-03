package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

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
    private boolean isHidden;

    @ColorRes
    int hiddenTextViewColor;

    public ListItemHiddenProjectViewModel(Context context, ProjectDomain projectDomain, Project project, String mapsApiKey) {
        super(project, mapsApiKey);
        this.context = context;
        this.projectDomain = projectDomain;
        this.isHidden = getProject().isHidden();
        setHideTitle(isHidden ? context.getString(R.string.unhide) : context.getString(R.string.hide));
        setHiddenTextViewColor(isHidden ? R.color.lecetHideBlue : R.color.lecetUnhideBlue);
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

    public void onHideSelected(View view) {

        if (isHidden) {
            projectDomain.unhideProject(getProject().getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        isHidden = false;
                        projectDomain.setProjectHidden(getProject(), isHidden);
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
                        isHidden = true;
                        projectDomain.setProjectHidden(getProject(), isHidden);
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
}
