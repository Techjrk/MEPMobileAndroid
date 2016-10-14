package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * File: RecentBidItemViewModel Created: 10/13/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class RecentBidItemViewModel {

    private final Bid bid;

    public RecentBidItemViewModel(Bid bid) {
        this.bid = bid;
    }

    public String getBidAmount() {

        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(bid.getAmount());
    }


    public String getBidCompany() {

        return null;
    }

    public String getClientCompany() {

        return null;
    }

    public String getClientLocation() {

        return null;
    }

    public String getBidType() {

        return null;
    }

    public String getMapUrl() {

        return null;
    }

    public String getBidDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM d");

        return simpleDateFormat.format(bid.getCreateDate());
    }

    public String getStartDateString() {

        return null;
    }

    public boolean isUnion() {

        Project project = bid.getProject();

        return project.getUnionDesignation() != null && project.getUnionDesignation().length() > 0;
    }


}
