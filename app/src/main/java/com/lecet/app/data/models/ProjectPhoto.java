package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project Photo - Realm Object for storing Photos associated with a Project
 * Created by jasonm on 3/9/17.
 */

public class ProjectPhoto extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private boolean text;

    @SerializedName("pending")
    private boolean pending;

    @SerializedName("companyId")
    private long companyId;

    @SerializedName("projectId")
    private long projectId;

    @SerializedName("authorId")
    private long authorId;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("src")
    private String src;

}
