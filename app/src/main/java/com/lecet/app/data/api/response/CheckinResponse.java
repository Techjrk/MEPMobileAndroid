package com.lecet.app.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.data.models.Pojo4;

/**
 * Created by domandtom on 8/17/16.
 */

public class CheckinResponse {

    @SerializedName("data")
    private Pojo4 data;

    public CheckinResponse(){}

    public Pojo4 getData() {
        return data;
    }
}
