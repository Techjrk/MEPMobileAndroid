package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.data.api.response.UserResponse;

/**
 * Created by domandtom on 8/17/16.
 */

public class Pojo4 {

    @SerializedName("id")
    private String ID;

    @SerializedName("user")
    private UserResponse data;

    @SerializedName("checkin_time")
    private String checkInTime;

    @SerializedName("checkout_time")
    private String checkOutTime;

    public Pojo4() {
    }

    public String getID() {
        return ID;
    }

    public UserResponse getData() {
        return data;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }
}
