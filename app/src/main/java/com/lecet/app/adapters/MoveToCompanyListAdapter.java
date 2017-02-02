package com.lecet.app.adapters;

import android.content.Context;

import com.lecet.app.R;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.interfaces.MTMMenuCallback;
import com.lecet.app.interfaces.MoveToListCallback;

import io.realm.RealmResults;

/**
 * File: MoveToCompanyListAdapter Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MoveToCompanyListAdapter extends MoveToAdapter<CompanyTrackingList> {


    public MoveToCompanyListAdapter(Context context, String title, RealmResults<CompanyTrackingList> trackingLists, MoveToListCallback<CompanyTrackingList> callback) {
        super(context, title, trackingLists, callback);
    }

    @Override
    public String itemPrimaryDetail(CompanyTrackingList object) {
        return object.getName();
    }

    @Override
    public String itemSecondaryDetail(CompanyTrackingList object) {
        return String.format(getContext().getString(R.string.mtm_menu_number_company), object.getCompanies().size());
    }
}
