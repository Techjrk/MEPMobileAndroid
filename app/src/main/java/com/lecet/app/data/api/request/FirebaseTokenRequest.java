package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

/**
 * File: FirebaseTokenRequest
 * Created: 5/22/17
 * Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class FirebaseTokenRequest {

    @SerializedName("deviceType")
    private String deviceType;

    @SerializedName("deviceToken")
    private String deviceToken;

    public FirebaseTokenRequest(@NonNull String deviceType, @NonNull String deviceToken) {
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FirebaseTokenRequest)) return false;

        FirebaseTokenRequest that = (FirebaseTokenRequest) o;

        if (!deviceType.equals(that.deviceType)) return false;
        return deviceToken.equals(that.deviceToken);

    }

    @Override
    public int hashCode() {
        int result = deviceType.hashCode();
        result = 31 * result + deviceToken.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FirebaseTokenRequest{" +
                "deviceType='" + deviceType + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}
