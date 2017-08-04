package com.lecet.app.data.api.response;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.PrimaryProjectType;

import java.util.List;

/**
 * File: ProjectTrackingListDetailResponse Created: 11/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectTrackingListDetailResponse {

    @SerializedName("primaryProjectType")
    private PrimaryProjectType primaryProjectType;

    @SerializedName("updates")
    private List<ActivityUpdate> projectUpdates;

    public PrimaryProjectType getPrimaryProjectType() {
        return primaryProjectType;
    }

    public List<ActivityUpdate> getProjectUpdates() {
        return projectUpdates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTrackingListDetailResponse)) return false;

        ProjectTrackingListDetailResponse that = (ProjectTrackingListDetailResponse) o;

        if (primaryProjectType != null ? !primaryProjectType.equals(that.primaryProjectType) : that.primaryProjectType != null)
            return false;
        return projectUpdates != null ? projectUpdates.equals(that.projectUpdates) : that.projectUpdates == null;

    }

    @Override
    public int hashCode() {
        int result = primaryProjectType != null ? primaryProjectType.hashCode() : 0;
        result = 31 * result + (projectUpdates != null ? projectUpdates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectTrackingListDetailResponse{" +
                "primaryProjectType=" + primaryProjectType +
                ", projectUpdates=" + projectUpdates +
                '}';
    }
}
