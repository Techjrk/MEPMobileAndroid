package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.BidDomain;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import io.realm.RealmList;

/**
 * File: RecentBidItemViewModel Created: 10/13/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class RecentBidItemViewModel {

    private static final String TAG = "RecentBidItemViewModel";

    private final BidDomain bidDomain;
    private final Bid bid;
    private final String mapsApiKey;
    private final AppCompatActivity activity;
    private final Project project;
    public RecentBidItemViewModel(BidDomain bidDomain, Bid bid, String mapsApiKey, AppCompatActivity activity) {
        this.bidDomain = bidDomain;
        this.bid = bid;
        this.mapsApiKey = mapsApiKey;
        this.activity = activity;

        if (bid.getProject() != null) {

            this.project = bid.getProject();

        } else {

            this.project = bidDomain.getBidProject(bid.getProjectId());
        }
    }

    public String getBidAmount() {

        DecimalFormat formatter = new DecimalFormat("$ #,###");

        return formatter.format(bid.getAmount());
    }

    public String getBidCompany() {

        Contact contact = bid.getContact();

        if (contact != null) {

            Company company = contact.getCompany();

            if (company != null) {

                return company.getName();
            }

        } else {

            Company company = bidDomain.getBidCompany(bid.getCompanyId());

            if (company != null) {

                return company.getName();
            }
        }

        return "[ not provided ]";
    }

    public String getProjectName() {

        if (project != null) {

            return project.getTitle();

        }

        return "[ not provided ]";
    }

    public String getClientLocation() {

        if (project != null) {

            return project.getCity() + " , " + project.getState();
        }

        return "[ not provided ]";
    }

    public String getBidType() {

        StringBuilder sb = new StringBuilder();
        if (project != null && project.getPrimaryProjectType() != null && project.getPrimaryProjectType().getTitle() !=null) {

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

    public String getMapUrl() {

        if (project == null || project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=20&size=400x400&" +
                        "markers=icon:"+getMarkerIcon(project)+"|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }
    private String getMarkerIcon(Project project) {
        StringBuilder urlBuilder = new StringBuilder(BidItemViewModel.url);
        boolean hasUpdates = projectHasUpdates(project);

        // for standard projects, i.e. with Dodge numbers
        if(project.getDodgeNumber() != null) {

            if (project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? BidItemViewModel.STANDARD_PRE_BID_MARKER_UPDATE : BidItemViewModel.STANDARD_PRE_BID_MARKER);
            }

            // style marker for pre-bid or post-bid color
            else {
                if (project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.STANDARD_PRE_BID_MARKER_UPDATE : BidItemViewModel.STANDARD_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.STANDARD_POST_BID_MARKER_UPDATE : BidItemViewModel.STANDARD_POST_BID_MARKER);
                }
            }
        }

        // for custom (user-created) projects, which have no Dodge number
        else {
            // pre-bid user-created projects
            if(project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? BidItemViewModel.CUSTOM_PRE_BID_MARKER_UPDATE : BidItemViewModel.CUSTOM_PRE_BID_MARKER);

            }

            // post-bid user-created projects
            else {
                if(project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.CUSTOM_PRE_BID_MARKER_UPDATE : BidItemViewModel.CUSTOM_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.CUSTOM_POST_BID_MARKER_UPDATE : BidItemViewModel.CUSTOM_POST_BID_MARKER);
                }
            }
        }
        return urlBuilder.toString();
    }
    private boolean projectHasUpdates(Project project) {
        if(project == null) return false;
        if(project.getUserNotes() == null || project.getImages() == null) {
            return false;
        }
        return (project.getImages().size() > 0 || project.getUserNotes().size() > 0);
    }
    public String getStartDateString() {

        if (project != null) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d");

            if (project.getBidDate() == null) return "[ not provided ]";

            return simpleDateFormat.format(project.getBidDate());
        }

        return "[ not provided ]";
    }

    public boolean isUnion() {

        if (project == null) return false;

        return project.getUnionDesignation() != null && project.getUnionDesignation().length() > 0;
    }

    public String getUnionDesignation() {

        if (project == null) return "?";

        return project.getUnionDesignation();
    }

    /** OnClick **/
    public void onItemClick(View view) {

        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, bid.getProjectId());
        activity.startActivity(intent);
    }
}
