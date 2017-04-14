package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

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

    public NotePost(String title, String body, boolean isPublic) {
        this.title = title;
        this.body = body;
        this.isPublic = isPublic;
    }
}
