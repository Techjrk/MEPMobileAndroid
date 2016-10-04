package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Access Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class Access {

    @SerializedName("id")
    private String id;

    @SerializedName("ttl")
    private long ttl;

    @SerializedName("userId")
    private long userId;

    @SerializedName("created")
    private String created;

    public Access() {
    }

    public String getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getCreated() {
        return created;
    }

    public long getTtl() {
        return ttl;
    }
}
