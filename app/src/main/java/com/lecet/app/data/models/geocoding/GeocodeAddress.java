package com.lecet.app.data.models.geocoding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jasonm on 5/30/17.
 */

public class GeocodeAddress {

    @SerializedName("results")
    @Expose
    private List<GeocodeResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<GeocodeResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodeResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GeocodeAddress{" +
                "results=" + results +
                ", status='" + status + '\'' +
                '}';
    }
}