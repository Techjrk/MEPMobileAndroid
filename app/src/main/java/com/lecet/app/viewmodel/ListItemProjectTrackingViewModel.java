package com.lecet.app.viewmodel;

import android.content.Intent;
import android.view.View;

import com.lecet.app.content.ProjectDetailActivity;
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
 * File: ListItemProjectTrackingViewModel Created: 12/2/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ListItemProjectTrackingViewModel extends TrackingListItem<Project> {

    private ProjectDomain projectDomain;
    private String mapApiKey;

    public ListItemProjectTrackingViewModel(ProjectDomain projectDomain, Project object, String mapApiKey, boolean displayUpdate) {
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
        if (object.getGeocode() == null) {

            String mapStr;

            String generatedAddress = generateCenterPointAddress(object);

            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://maps.googleapis.com/maps/api/staticmap");
            sb2.append("?center=");
            sb2.append(generatedAddress);
            sb2.append("&zoom=16");
            sb2.append("&size=200x200");
            sb2.append("&markers=color:blue|");
            sb2.append(generatedAddress);
            sb2.append("&key=" + mapApiKey);
            mapStr = String.format((sb2.toString().replace(' ', '+')), null);

            return mapStr;
        }

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
    public void handleTrackingItemSelected(View view, Project object) {

        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, object.getId());
        view.getContext().startActivity(intent);
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

    private String generateCenterPointAddress(Project project) {

        StringBuilder stringBuilder = new StringBuilder();

        if (project.getAddress1() != null) {
            stringBuilder.append(project.getAddress1());
            stringBuilder.append(",");
        }

        if (project.getAddress2() != null) {
            stringBuilder.append(project.getAddress2());
            stringBuilder.append(",");
        }

        if (project.getCity() != null) {
            stringBuilder.append(project.getCity());
            stringBuilder.append(",");
        }

        if (project.getState() != null) {
            stringBuilder.append(project.getState());
        }

        if (project.getZipPlus4() != null) {
            stringBuilder.append(",");
            stringBuilder.append(project.getZipPlus4());
        }


        return stringBuilder.toString();
    }
}
