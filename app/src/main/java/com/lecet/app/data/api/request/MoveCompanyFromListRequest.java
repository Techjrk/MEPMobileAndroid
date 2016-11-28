package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * File: MoveCompanyFromListRequest Created: 11/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MoveCompanyFromListRequest {

    @SerializedName("companyIds")
    private List<Long> companyIds;

    public MoveCompanyFromListRequest(@NonNull List<Long> companyIds) {

        this.companyIds = companyIds;
    }

    public List<Long> getCompanyIds() {
        return companyIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveCompanyFromListRequest)) return false;

        MoveCompanyFromListRequest that = (MoveCompanyFromListRequest) o;

        return companyIds.equals(that.companyIds);

    }

    @Override
    public int hashCode() {
        return companyIds.hashCode();
    }

    @Override
    public String toString() {
        return "MoveProjectFromListRequest{" +
                "projectIds=" + companyIds +
                '}';
    }
}

