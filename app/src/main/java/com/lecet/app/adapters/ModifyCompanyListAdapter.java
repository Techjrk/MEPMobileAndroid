package com.lecet.app.adapters;


import android.content.Context;

import com.lecet.app.data.models.Company;

import io.realm.RealmResults;

/**
 * File: ModifyCompanyListAdapter Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ModifyCompanyListAdapter extends ModifyListAdapter<RealmResults<Company>, Company> {

    public ModifyCompanyListAdapter(Context context, RealmResults<Company> companies) {
        super(context, companies);
    }

    @Override
    public long getObjectId(Company object) {
        return object.getId();
    }

    @Override
    public String getPrimaryDetail(Company object) {
        return object.getName();
    }

    @Override
    public String getSecondaryDetail(Company object) {
        return generateLocale(object);
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
