package com.lecet.app.data.api.request;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * File: Geocode Created: 10/5/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class GeocodeRequest {

    @SerializedName("lng")
    private double lng;

    @SerializedName("lat")
    private double lat;


    public GeocodeRequest() {
    }

    public GeocodeRequest(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString() {
        return "Geocode{" +
                "lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeocodeRequest geocode = (GeocodeRequest) o;

        if (Double.compare(geocode.lng, lng) != 0) return false;
        return Double.compare(geocode.lat, lat) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lng);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
