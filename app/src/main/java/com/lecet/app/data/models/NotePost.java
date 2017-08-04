package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.data.api.request.GeocodeRequest;

/**
 * Created by ludwigvondrake on 4/14/17.
 */

public class NotePost {

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String body;

    @SerializedName("public")
    private boolean isPublic;

    @SerializedName("geocode")
    private GeocodeRequest geocode;

    @SerializedName("fullAddress")
    private String fullAddress;


    public NotePost(String title, String body, boolean isPublic, GeocodeRequest geocode, String fullAddress) {
        this.title = title;
        this.body = body;
        this.isPublic = isPublic;
        this.geocode = geocode;
        this.fullAddress = fullAddress;
    }
}
