package com.lecet.app.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.data.models.UserData;

/**
 * File: UserResponse Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class UserResponse {

    @SerializedName("data")
    private UserData data;

    public UserData getData() {
        return data;
    }
}
