package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by domandtom on 8/17/16.
 */

public class Pojo1 {

    @SerializedName("id")
    private String ID;

    @SerializedName("name")
    private String name;

    public Pojo1(){}

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
