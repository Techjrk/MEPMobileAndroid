package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by domandtom on 8/17/16.
 */

public class CheckInRequest {

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("course_id")
    private int course_id;

    public CheckInRequest(String uuid, int course_id) {
        this.uuid = uuid;
        this.course_id = course_id;
    }

    public String getUuid() {
        return uuid;
    }

    public int getCourse_id() {
        return course_id;
    }
}
