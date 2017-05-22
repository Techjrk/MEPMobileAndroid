package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * File: ProjectNotifyRequest
 * Created: 5/16/17
 * Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectNotifyRequest {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public ProjectNotifyRequest(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectNotifyRequest)) return false;

        ProjectNotifyRequest that = (ProjectNotifyRequest) o;

        if (Double.compare(that.lat, lat) != 0) return false;
        return Double.compare(that.lng, lng) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ProjectNotifyRequest{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
