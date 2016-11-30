package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Company;

/**
 * File: ListItemCompanyTrackingViewModel Created: 11/29/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ListItemCompanyTrackingViewModel extends ListItemTrackingViewModel {

    private Company company;

    public ListItemCompanyTrackingViewModel(Company company, String mapsApiKey, boolean showUpdates) {
        super(mapsApiKey, showUpdates, null);

        this.company = company;

        init();
    }

    @Override
    public String getItemName() {
        return company.getName();
    }

    @Override
    public String generateDetailSecondary() {
        return generateLocale();
    }

    @Override
    public String generateDetailPrimary() {
        return generateAddress();
    }

    @Override
    public String getMapUrl() {

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
        sb2.append("&key=" + getMapsApiKey());
        mapStr = String.format((sb2.toString().replace(' ', '+')), null);

        return mapStr;
    }

    @Override
    public boolean displaySecondaryDetail() {
        return false;
    }

    private String generateAddress() {

        StringBuilder sb = new StringBuilder();
        if (company.getAddress1() != null) sb.append(" " + company.getAddress1());
        if (company.getAddress2() != null) sb.append(" " + company.getAddress2());
        setDetail1(sb.toString());

        return sb.toString();
    }

    private String generateLocale() {

        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        if (company.getCity() != null) sb.append(company.getCity());
        if (company.getCity() != null && company.getState() != null) sb.append(", ");
        if (company.getState() != null) sb.append(company.getState());
        if (company.getZip5() != null) sb.append(" " + company.getZip5());

        return sb.toString();
    }
}
