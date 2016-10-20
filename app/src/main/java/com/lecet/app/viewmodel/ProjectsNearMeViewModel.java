package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.lecet.app.domain.ProjectDomain;

import java.util.Calendar;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class ProjectsNearMeViewModel extends BaseObservable {

    private final Activity activity;
    private ProjectDomain projectDomain;

    private String month;
    private String bidsHappeningSoon;
    private Calendar lastDateSelected;

    public ProjectsNearMeViewModel(Activity activity, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectDomain = projectDomain;
    }
}
