package com.lecet.app.viewmodel;

import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectCategory;
import com.lecet.app.data.models.ProjectGroup;
import com.lecet.app.domain.ProjectDomain;

/**
 * File: ListItemProjectTrackingViewModel Created: 11/29/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ListItemProjectTrackingViewModel extends ListItemTrackingViewModel {

    private Project project;
    private ProjectDomain projectDomain;

    @Deprecated
    public ListItemProjectTrackingViewModel(Project project, String mapsApiKey, boolean showUpdates) {
        super(mapsApiKey, showUpdates, project.getRecentUpdate());

        this.project = project;

        init();
    }

    public ListItemProjectTrackingViewModel(ProjectDomain projectDomain , Project project, String mapsApiKey, boolean showUpdates) {
        super(mapsApiKey, showUpdates, project.getRecentUpdate());

        this.projectDomain = projectDomain;
        this.project = project;

        init();
    }

    @Override
    public String getItemName() {
        return project.getTitle();
    }

    @Override
    public String generateDetailPrimary() {
        return generateAddress();
    }

    @Override
    public String generateDetailSecondary() {
        return generateProjectKeywords();
    }


    @Override
    public String getMapUrl() {

        if (project.getGeocode() == null) return null;

        String mapStr = String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=200x200&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), getMapsApiKey());

        return mapStr;
    }

    @Override
    public boolean displaySecondaryDetail() {
        return true;
    }

    /**
     * Return a String built from a list of the project's PrimaryProjectType, ProjectCategory, and
     * ProjectGroup
     **/
    private String generateProjectKeywords() {
        StringBuilder sb = new StringBuilder();
        long id = project.getPrimaryProjectTypeId();
        PrimaryProjectType primaryProjectType = project.getPrimaryProjectType();
        if (primaryProjectType != null) {
            String pptTitle = primaryProjectType.getTitle();
            if (pptTitle != null) sb.append(pptTitle);
            ProjectCategory category = primaryProjectType.getProjectCategory();
            if (category != null) {
                String categoryTitle = category.getTitle();
                if (categoryTitle != null) {
                    sb.append(", ");
                    sb.append(categoryTitle);
                }
                ProjectGroup group = category.getProjectGroup();
                if (group != null) {
                    String groupTitle = group.getTitle();
                    if (groupTitle != null) {
                        sb.append(", ");
                        sb.append(groupTitle);
                    }
                }
            }
        }

        String str = sb.toString();
        if (str.length() == 0) return null;
        return str;
    }

    private String generateAddress() {

        StringBuilder sb = new StringBuilder();
        if (project.getCity() != null) sb.append(project.getCity());
        if (project.getCity() != null && project.getState() != null) sb.append(", ");
        if (project.getState() != null) sb.append(project.getState());
        setDetail1(sb.toString());

        return sb.toString();
    }
}
