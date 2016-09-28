package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * LogoutRequest
 * Created by domandtom on 8/17/16.
 */

public class LogoutRequest {

    @SerializedName("uuid")
    private String uuid;

    public LogoutRequest(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

}
