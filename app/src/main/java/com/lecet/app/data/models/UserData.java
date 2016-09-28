package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: UserData Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class UserData {

    @SerializedName("id")
    private String ID;

    @SerializedName("attributes")
    private User attributes;
}
