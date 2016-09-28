package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasonm on 8/24/16.
 */

public class SetPasswordRequest {

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("password")
    private String password;

    public SetPasswordRequest(String uuid, String password) {
        this.uuid = uuid;
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPassword() {
        return password;
    }
}
