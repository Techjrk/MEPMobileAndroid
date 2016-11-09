package com.lecet.app.viewmodel;

import android.view.View;

import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectCategory;
import com.lecet.app.data.models.ProjectGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * File: ListItemProjectTrackingViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ListItemProjectTrackingViewModel {

    private final Project project;
    private final String mapsApiKey;
    String projectKeywords;

    public ListItemProjectTrackingViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;

        projectKeywords = generateProjectKeywords();
    }

    public String getProjectName() {

        return project.getTitle();
    }

    public String getProjectKeywords() {
        return this.projectKeywords;
    }

    /*
    Return a String built from a list of the project's PrimaryProjectType, ProjectCategory, and ProjectGroup
     */
    public String generateProjectKeywords() {
        StringBuilder sb = new StringBuilder();
        long id = project.getPrimaryProjectTypeId();
        PrimaryProjectType primaryProjectType = project.getPrimaryProjectType();
        if(primaryProjectType != null) {
            String pptTitle = primaryProjectType.getTitle();
            if (pptTitle != null) sb.append(pptTitle);
            ProjectCategory category = primaryProjectType.getProjectCategory();
            if (category != null) {
                String categoryTitle = category.getTitle();
                if (categoryTitle != null) {
                    sb.append(",");
                    sb.append(categoryTitle);
                }
                ProjectGroup group = category.getProjectGroup();
                if (group != null) {
                    String groupTitle = group.getTitle();
                    if(groupTitle != null) {
                        sb.append(",");
                        sb.append(groupTitle);
                    }
                }
            }
        }

        String str = sb.toString();
        if(str.length() == 0) return null;
        return str;
    }

    public String getProjectNote() {

        return project.getProjectNotes();
    }

    public String getProjectBidDate() {

        if(project.getBidDate() != null) return DateFormat.getDateTimeInstance().format(project.getBidDate());
        return null;
    }

    public String getClientLocation() {

        return project.getCity() + " , " + project.getState();
    }

    public String getMapUrl() {

        if (project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }


    //////////////////////////////////////
    // PROJECT BIDS

    /*public String getBidType() {

        StringBuilder sb = new StringBuilder();
        if (project.getPrimaryProjectType() != null && project.getPrimaryProjectType().getTitle() != null) {

            sb.append(project.getPrimaryProjectType().getTitle());

            if (project.getPrimaryProjectType().getProjectCategory() != null && project.getPrimaryProjectType().getProjectCategory().getTitle() != null) {

                sb.append(" ");
                sb.append(project.getPrimaryProjectType().getProjectCategory().getTitle());

                if (project.getPrimaryProjectType().getProjectCategory().getProjectGroup() != null && project.getPrimaryProjectType().getProjectCategory().getProjectGroup().getTitle() != null) {

                    sb.append(" ");
                    sb.append(project.getPrimaryProjectType().getProjectCategory().getProjectGroup().getTitle());
                }
            }
        }

        return sb.toString();
    }

    public String getBidTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

        return simpleDateFormat.format(project.getBidDate());
    }

    public String getStartDateString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d");

        return simpleDateFormat.format(project.getBidDate());
    }


    public boolean isUnion() {

        return project.getUnionDesignation() != null && project.getUnionDesignation().length() > 0;
    }*/

    public boolean hasBid() {
        //TODO: update
        return project.getBidDate() != null;
    }


    ////////////////////////////////////
    // PROJECT NOTES

    public boolean hasNote() {
        if(project.getProjectNotes() == null || project.getProjectNotes().length() == 0) return false;
        return false;   //TODO - undo
    }



    ////////////////////////////////////
    // CLICK HANDLERS

    public void onBidLayoutClick(View view) {
        //TODO - fill in
    }

    public void onNoteLayoutClick(View view) {
        //TODO - fill in
    }

    /** OnClick handler for clicking the entire Pojo1 List Item view **/

    public void onClick(View view) {

        /*domain.handleItemClick(new Callback<Pojo1>() {
            @Override
            public void onResponse(Call<Pojo1> call, Response<Pojo1> response) {

                if (response.isSuccessful()) {


                } else {


                }
            }

            @Override
            public void onFailure(Call<Pojo1> call, Throwable t) {

            }
        });*/
    }


}
