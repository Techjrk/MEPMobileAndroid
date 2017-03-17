package com.lecet.app.viewmodel;

import android.support.v4.app.Fragment;

import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.domain.ProjectDomain;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesViewModel {

    private final Fragment fragment;
    private final long projectId;
    private final ProjectDomain projectDomain;


    public ProjectNotesViewModel(Fragment fragment, long projectId, ProjectDomain projectDomain) {
        this.fragment = fragment;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
    }

    //TODO - call
    public void fetchProjectNotes() {
        projectDomain.fetchProjectBids(projectId);
    }
}

