package com.lecet.app.viewmodel;

import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Company;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.DateUtility;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * File: ListItemCompanyTrackingViewModel Created: 11/29/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ListItemCompanyTrackingViewModel extends TrackingListItem<Company> {

    private String mapsApiKey;
    private ProjectDomain projectDomain;

    public ListItemCompanyTrackingViewModel(ProjectDomain projectDomain, Company company, String mapsApiKey, boolean showUpdates) {
        super(company, showUpdates);

        this.mapsApiKey = mapsApiKey;
        this.projectDomain = projectDomain;
        init(company);
    }

    @Override
    public String generateTitle(Company object) {
        return object.getName();
    }

    @Override
    public String generatePrimaryDetail(Company object) {
        return generateAddress(object);
    }

    @Override
    public String generateSecondaryDetail(Company object) {
        return generateLocale(object);
    }

    @Override
    public String generateMapUrl(Company object) {
        return mapUrl(object);
    }

    @Override
    public boolean showActivityUpdate(Company object) {

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
        return false;
    }

    private String mapUrl(Company company) {

        String mapStr;

        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://maps.googleapis.com/maps/api/staticmap");
        sb2.append("?center=");
        sb2.append(company.getAddress1() + ",");
        sb2.append(company.getAddress2() + ",");
        sb2.append(company.getCity() + ",");
        sb2.append(company.getState());
        sb2.append("&zoom=16");
        sb2.append("&size=200x200");
        sb2.append("&markers=color:blue|");
        sb2.append("&key=" + mapsApiKey);
        mapStr = String.format((sb2.toString().replace(' ', '+')), null);

        return mapStr;
    }


    private String generateAddress(Company company) {

        StringBuilder sb = new StringBuilder();
        if (company.getAddress1() != null) sb.append(" " + company.getAddress1());
        if (company.getAddress2() != null) sb.append(" " + company.getAddress2());

        return sb.toString();
    }

    private String generateLocale(Company company) {

        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        if (company.getCity() != null) sb.append(company.getCity());
        if (company.getCity() != null && company.getState() != null) sb.append(", ");
        if (company.getState() != null) sb.append(company.getState());
        if (company.getZip5() != null) sb.append(" " + company.getZip5());

        return sb.toString();
    }

}
