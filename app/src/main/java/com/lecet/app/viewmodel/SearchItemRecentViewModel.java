package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchRecyclerViewAdapter;
import com.lecet.app.data.models.Project;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemRecentViewModel {

    //TODO - support either Project or Company data
    private final Project project;
    private final String mapsApiKey;
    private final String code = "this123code";
    private AppCompatActivity activity;

    public String getTitle() {
        if (project == null) return "--";
        return project.getTitle();
    }

    public String getAddress() {
        if (project == null) return "--";

        return project.getAddress1();
    }

    public String getCode() {
        return code;
    }

    /** old code - for reference
        public SearchItemRecentViewModel(Project project, String mapsApiKey) {
            this.project = project;
            this.mapsApiKey = mapsApiKey;


        } */
    public SearchItemRecentViewModel(AppCompatActivity activity, Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        this.activity = activity;

    }

/*
    public SearchItemRecentViewModel(Project project, String mapsApiKey, SearchRecyclerViewAdapter.OnItemClickListener listener) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
    }
*/


    public String getProjectName() {

        return project.getTitle();
    }

    public String getClientLocation() {

        return project.getCity() + " , " + project.getState();
    }


    public String getMapUrl() {

        if (project == null || project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }


    public ScrollView getMSE1SectionView() {
        return (ScrollView) this.activity.findViewById(R.id.mse1section);
    }

    public ScrollView getMSE2SectionView() {
        return (ScrollView) this.activity.findViewById(R.id.mse2section);
    }
}
