package com.lecet.app.viewmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchResult;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemRecentViewModel {

    //TODO - support either Project or Company data
    private final Project project;
    private final String mapsApiKey;
    private final String code = "this123code";

    private SearchViewModel viewModel;
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

    /**
     * old code - for reference
     * public SearchItemRecentViewModel(Project project, String mapsApiKey) {
     * this.project = project;
     * this.mapsApiKey = mapsApiKey;
     * }
     */
    public SearchItemRecentViewModel(SearchViewModel viewModel, Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        this.viewModel = viewModel;
    }


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

    ////////////////////////////////////
    // CLICK HANDLERS

    public void onClick(View view) {
        Toast.makeText(viewModel.getActivity(), "onClick: \nProject id: " + project.getId(),Toast.LENGTH_SHORT).show();
    }

    public void setItemClickListener(final SearchResult item, View view) {
        final LinearLayout mapviewsection = (LinearLayout) view.findViewById(R.id.mapsectionview);

        mapviewsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(viewModel.getActivity(), "Is this section already implemented?",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setItemClickListener(final Project item, View view) {
        final ImageView mapviewsection = (ImageView) view.findViewById(R.id.query_map_section_view);

        mapviewsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(viewModel.getActivity(), "Already Implemented?",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

