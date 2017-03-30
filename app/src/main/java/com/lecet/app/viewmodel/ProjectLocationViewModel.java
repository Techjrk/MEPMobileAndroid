package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;

import com.lecet.app.domain.ProjectDomain;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectLocationViewModel extends BaseObservable {

    private final Fragment fragment;
    private final long projectId;
    private final ProjectDomain projectDomain;

    public ProjectLocationViewModel(Fragment fragment, long projectId, ProjectDomain projectDomain) {
        this.fragment = fragment;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
    }

    // TODO: 3/10/17 fill in location functionality from existing activity
}

