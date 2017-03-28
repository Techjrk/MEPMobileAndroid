package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.ProjectDetailAddNoteActivity;
import com.lecet.app.domain.ProjectDomain;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesViewModel {

    private static final String TAG = "ProjectNotesViewModel";

    private final Fragment fragment;
    private final long projectId;
    private final ProjectDomain projectDomain;

    public ProjectNotesViewModel(Fragment fragment, long projectId, ProjectDomain projectDomain) {
        this.fragment = fragment;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
    }

    public void onClickAddNote(View view){
        Log.e(TAG, "onClickAddNote: Launch Add Note Activity");
        Intent intent = new Intent(this.fragment.getActivity(), ProjectDetailAddNoteActivity.class);
        fragment.getActivity().startActivity(intent);
    }


    //TODO - call
    public void fetchProjectNotes() {
        projectDomain.fetchProjectBids(projectId);
    }
}

