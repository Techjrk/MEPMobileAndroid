package com.lecet.app.viewmodel;

import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectCategory;
import com.lecet.app.data.models.ProjectGroup;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.DateUtility;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * File: NewListItemProjectTrackingViewModel Created: 12/2/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class NewListItemProjectTrackingViewModel extends TrackingListItem<Project> {

    private ProjectDomain projectDomain;
    private String mapApiKey;

    public NewListItemProjectTrackingViewModel(ProjectDomain projectDomain, Project object, String mapApiKey, boolean displayUpdate) {
        super(object, displayUpdate);
        this.projectDomain = projectDomain;
        this.mapApiKey = mapApiKey;
        init(object);
    }

    @Override
    public String generateTitle(Project object) {
        return object.getTitle();
    }

    @Override
    public String generatePrimaryDetail(Project object) {
        return generateAddress(object);
    }

    @Override
    public String generateSecondaryDetail(final Project object) {

        String keywords = generateProjectKeywords(object.getPrimaryProjectType());

        if (keywords == null) {

            if (keywords == null) {

                final RealmChangeListener<RealmResults<PrimaryProjectType>> listener = new RealmChangeListener<RealmResults<PrimaryProjectType>>() {
                    @Override
                    public void onChange(RealmResults<PrimaryProjectType> element) {

                        if (element.size() > 0) {
                            PrimaryProjectType result = element.first();
                            String keywords = generateProjectKeywords(result);
                            secondaryDetail.set(keywords);
                            element.removeChangeListeners();
                        }
                    }
                };

                projectDomain.fetchProjectTypeAsync(object.getPrimaryProjectTypeId(), listener);
            }

        }

        return keywords;
    }

    @Override
    public String generateMapUrl(Project object) {
        if (object.getGeocode() == null) return null;

        String mapStr = String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=200x200&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", object.getGeocode().getLat(), object.getGeocode().getLng(),
                object.getGeocode().getLat(), object.getGeocode().getLng(), mapApiKey);

        return mapStr;
    }

    @Override
    public boolean showActivityUpdate(Project object) {

        if (object.getRecentUpdate() != null) {

            return true;

        } else {

            // Run an async fetch to see if there are any updates that might be available
            projectDomain.fetchProjectActivityUpdates(object.getId(), DateUtility.addDays(-1), new RealmChangeListener<RealmResults<ActivityUpdate>>() {
                @Override
                public void onChange(RealmResults<ActivityUpdate> element) {

                    if (element.size() > 0) {

                        ActivityUpdate update = element.first();
                        refreshActivityUpdateDisplay(update);
                        element.removeChangeListeners();
                    }

                }
            });

            return false;
        }
    }

    @Override
    public boolean showSecondaryDetailIcon() {
        return true;
    }

    @Override
    public String activityTitle() {
        return getObject().getRecentUpdate().getModelTitle();
    }

    @Override
    public String activityMessage() {
        return getObject().getRecentUpdate().getSummary();
    }

    @Override
    public int activityUpdateIconResourceId() {
        return super.activityUpdateIconResourceId();
    }

    private String generateAddress(Project project) {

        StringBuilder sb = new StringBuilder();
        if (project.getCity() != null) sb.append(project.getCity());
        if (project.getCity() != null && project.getState() != null) sb.append(", ");
        if (project.getState() != null) sb.append(project.getState());

        return sb.toString();
    }

    /**
     * Return a String built from a list of the project's PrimaryProjectType, ProjectCategory, and
     * ProjectGroup
     **/
    private String generateProjectKeywords(PrimaryProjectType primaryProjectType) {
        StringBuilder sb = new StringBuilder();

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
}
