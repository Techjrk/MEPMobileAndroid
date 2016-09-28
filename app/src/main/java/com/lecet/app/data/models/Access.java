package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Access Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class Access {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("expires_in")
    private String expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    public Access() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
