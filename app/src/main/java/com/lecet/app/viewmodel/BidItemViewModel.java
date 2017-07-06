package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.view.View;

import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.SearchDomain;

import java.text.DecimalFormat;

import io.realm.Realm;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * This code is copyright (c) 2016 Dom & Tom Inc.
 * This View Model is used
 */

public class BidItemViewModel extends BaseObservable {

    private Project project;
    private Company company;
    private Contact contact;
    private String mapsApiKey;
    private boolean isClientLocation2;
    private boolean hasStarCard;
    private SearchDomain searchDomain;
    private Location currentLocation;

    /*Created dummy server and upload these images on it and access it with piccasso*/
    public static final String STANDARD_PRE_BID_MARKER = "ic_standard_marker_pre_bid_kjselm.png";
    public static final String STANDARD_PRE_BID_MARKER_UPDATE = "ic_standard_marker_pre_bid_update_p25ao6.png";
    public static final String STANDARD_POST_BID_MARKER = "ic_standard_marker_post_bid_kkabhe.png";
    public static final String STANDARD_POST_BID_MARKER_UPDATE = "ic_standard_marker_post_bid_update_exicg2.png";
    public static final String CUSTOM_PRE_BID_MARKER = "ic_custom_pin_marker_pre_bid_tdocru.png";
    public static final String CUSTOM_PRE_BID_MARKER_UPDATE = "ic_custom_pin_marker_pre_bid_update_xzjl40.png";
    public static final String CUSTOM_POST_BID_MARKER = "ic_custom_pin_marker_post_bid_iwa8we.png";
    public static final String CUSTOM_POST_BID_MARKER_UPDATE = "ic_custom_pin_marker_post_bid_update_kzkxrw.png";
    public static final String url = "http://res.cloudinary.com/djakoy1gr/image/upload/v1498123162/";
    private final double METERS_PER_MILE = 1609.34;
    private final DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");

    public BidItemViewModel(Project project, String mapsApiKey, Location currentLocation) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;

        searchDomain = new SearchDomain(LecetClient.getInstance(), Realm.getDefaultInstance());
        this.currentLocation = currentLocation;
        setStarCardStatus();

    }

    private void setStarCardStatus() {
        if(project == null) return;
        if(project.getImages() == null) return;
        if(project.getUserNotes() == null) return;

        if (project.getImages().size() > 0 || project.getUserNotes().size() > 0) {
            setHasStarCard(true);
        }
    }

    ////////////////////////////////////
    // PROJECT
    @Bindable
    public String getDistToCurrentLocation(){
        if(currentLocation == null || project.getGeocode() == null)
            return "";
        Location projectLocation = new Location("project_location");
        projectLocation.setLatitude(project.getGeocode().getLat());
        projectLocation.setLongitude(project.getGeocode().getLng());
        double distanceBetweenDouble = meterToMiles(currentLocation.distanceTo(projectLocation));

        if(distanceBetweenDouble < 10){ //add decimal places when distanceBetweenDouble > 10 miles
            decimalFormat.setMinimumFractionDigits(0);
            decimalFormat.setMaximumFractionDigits(2);
        }
        else{ //remove decimal places when distanceBetweenDouble > 10 miles
            decimalFormat.setMinimumFractionDigits(0);
            decimalFormat.setMaximumFractionDigits(0);
        }
        String distanceBetween = decimalFormat.format(distanceBetweenDouble);
        return distanceBetween + " miles away";
    }

    private double meterToMiles(double meters){
        return meters / METERS_PER_MILE;
    }

    @Bindable
    public boolean getHasStarCard() {
        return hasStarCard;
    }

    public void setHasStarCard(boolean hasStarCard) {
        this.hasStarCard = hasStarCard;
    }

    public String getTitle() {
        return project != null ? project.getTitle() : company != null ? company.getName() : "Unknown";
    }

    public String getProjectName() {
        return project.getTitle();
    }

    public String getProjectEstLow(){
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat.format(project.getEstLow());
    }
    public String getClientLocation() {
        return project != null ? project.getCity() + " , " + project.getState() : company != null ? company.getAddress1() : "";
    }

    public String getClientLocation2() {
       /* if (project != null && project.getAddress2() != null && !project.getAddress2().trim().equals(""))
            setIsClientLocation2(true);
        else setIsClientLocation2(false);*/
       String address="";
       if (project != null ) {
            setIsClientLocation2(true);
           if (project.getAddress1() != null && !project.getAddress1().trim().equals("")) {
                address = project.getAddress1();
           }
           if (project.getAddress2() != null && !project.getAddress2().trim().equals("")) {
               address += ", "+ project.getAddress2();
           }
          if ((project.getAddress1() == null || project.getAddress1().trim().equals("")) && (project.getAddress2() == null || project.getAddress2().trim().equals(""))) {
              setIsClientLocation2(false);
          }
        } else setIsClientLocation2(false);
        return project != null ? address : "";
    }

    @Bindable
    public boolean getIsClientLocation2() {
        return isClientLocation2;
    }

    @Bindable
    public void setIsClientLocation2(boolean clientLocation2) {
        isClientLocation2 = clientLocation2;
    }

    ////////////////////////////////////
    // MAP IMAGE URL - UNIVERSAL

    public String getMapUrl() {

        // project map
        if (project != null) {

            if (project.getGeocode() == null) {
                return null;
            } else

                return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=20&size=400x400&" +
                                "markers=icon:"+getMarkerIcon(project)+"|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                        project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
        }

        // company map
        if (company != null) {
            if (company.getCity() == null || company.getState() == null) {
                return null;
            }

            String mapStr = null;
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/staticmap");
            sb.append("?zoom=16");
            sb.append("&size=200x200");
            sb.append("&markers=color:blue|");
            if (company.getAddress1() != null) sb.append(company.getAddress1() + ",");
            if (company.getAddress2() != null) sb.append(company.getAddress2() + ",");
            if (company.getCity() != null) sb.append(company.getCity() + ",");
            if (company.getState() != null) sb.append(company.getState());
            sb.append("&key=" + mapsApiKey);
            mapStr = sb.toString().replace(" ", "+");
            return mapStr;
        }

        return null;
    }

    ////////////////////////////////////
    // CLICK HANDLERS


    //event for clicking the Saved Search Project Detail item
    public void onProjectClick(View view) {
        if (project == null) return;

        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        view.getContext().startActivity(intent);
    }

    private boolean projectHasUpdates(Project project) {
        if(project == null) return false;
        if(project.getUserNotes() == null || project.getImages() == null) {
            return false;
        }
        return (project.getImages().size() > 0 || project.getUserNotes().size() > 0);
    }

    private String getMarkerIcon(Project project) {
        StringBuilder urlBuilder = new StringBuilder(url);
        boolean hasUpdates = projectHasUpdates(project);

        // for standard projects, i.e. with Dodge numbers
        if(project.getDodgeNumber() != null) {

            if (project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? STANDARD_PRE_BID_MARKER_UPDATE : STANDARD_PRE_BID_MARKER);
            }

            // style marker for pre-bid or post-bid color
            else {
                if (project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? STANDARD_PRE_BID_MARKER_UPDATE : STANDARD_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? STANDARD_POST_BID_MARKER_UPDATE : STANDARD_POST_BID_MARKER);
                }
            }
        }

        // for custom (user-created) projects, which have no Dodge number
        else {
            // pre-bid user-created projects
            if(project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? CUSTOM_PRE_BID_MARKER_UPDATE : CUSTOM_PRE_BID_MARKER);

            }

            // post-bid user-created projects
            else {
                if(project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? CUSTOM_PRE_BID_MARKER_UPDATE : CUSTOM_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? CUSTOM_POST_BID_MARKER_UPDATE : CUSTOM_POST_BID_MARKER);
                }
            }
        }
        return urlBuilder.toString();
    }
}

