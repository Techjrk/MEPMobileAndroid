package com.lecet.app.viewmodel;

import android.content.Intent;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
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
            projectDomain.fetchCompanyActivityUpdates(object.getId(), DateUtility.addDays(-1), new RealmChangeListener<RealmResults<ActivityUpdate>>() {
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

    @Override
    public void handleTrackingItemSelected(View view, Company object) {

        Intent intent = new Intent(view.getContext(), CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, object.getId());
        view.getContext().startActivity(intent);
    }

    private String mapUrl(Company company) {

        String mapStr;

        String generatedAddress = generateCenterPointAddress(company);

        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://maps.googleapis.com/maps/api/staticmap");
        sb2.append("?center=");
        sb2.append(generatedAddress);
        sb2.append("&zoom=16");
        sb2.append("&size=200x200");
        sb2.append("&markers=color:blue|");
        sb2.append(generatedAddress);
        sb2.append("&key=" + mapsApiKey);
        mapStr = String.format((sb2.toString().replace(' ', '+')), null);

        return mapStr;
    }

    private String generateCenterPointAddress(Company company) {

        StringBuilder stringBuilder = new StringBuilder();

        if (company.getAddress1() != null) {
            stringBuilder.append(company.getAddress1());
            stringBuilder.append(",");
        }

        if (company.getAddress2() != null) {
            stringBuilder.append(company.getAddress2());
            stringBuilder.append(",");
        }

        if (company.getCity() != null) {
            stringBuilder.append(company.getCity());
            stringBuilder.append(",");
        }

        if (company.getState() != null) {
            stringBuilder.append(company.getState());
        }

        if (company.getZipPlus4() != null) {
            stringBuilder.append(",");
            stringBuilder.append(company.getZipPlus4());
        }


        return stringBuilder.toString();
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
