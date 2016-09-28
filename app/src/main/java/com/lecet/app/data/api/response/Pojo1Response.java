package com.lecet.app.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.data.models.Pojo1;

import java.util.List;

/**
 * Created by domandtom on 8/17/16.
 */

public class Pojo1Response {

    @SerializedName("data")
    private List<Pojo1> data;

    public Pojo1Response() {}

    public List<Pojo1> getData() {
        return data;
    }
}
