package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * File: MoveProjectFromListRequest Created: 11/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MoveProjectFromListRequest {

    @SerializedName("projectIds")
    private List<Long> projectIds;

    public MoveProjectFromListRequest(@NonNull List<Long> projectIds) {

        this.projectIds = projectIds;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveProjectFromListRequest)) return false;

        MoveProjectFromListRequest that = (MoveProjectFromListRequest) o;

        return projectIds.equals(that.projectIds);

    }

    @Override
    public int hashCode() {
        return projectIds.hashCode();
    }

    @Override
    public String toString() {
        return "MoveProjectFromListRequest{" +
                "projectIds=" + projectIds +
                '}';
    }
}
