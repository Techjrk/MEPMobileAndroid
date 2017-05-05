package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasonm on 4/14/17.
 */

public class PhotoPost {

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String body;

    @SerializedName("public")
    private boolean isPublic;

    @SerializedName("file")
    private String file;    // a base-64-encoded String representing the image file data

    public PhotoPost(String title, String body, boolean isPublic, String file) {
        this.title = title;
        this.body = body;
        this.isPublic = isPublic;
        this.file = file;
    }
}
