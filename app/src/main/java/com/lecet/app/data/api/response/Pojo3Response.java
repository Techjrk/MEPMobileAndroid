package com.lecet.app.data.api.response;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.data.models.Pojo3;

import java.util.List;

/**
 * File: Pojo3Response Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class Pojo3Response {

    @SerializedName("pojo3s")
    private List<Pojo3> pojo3s;

    public Pojo3Response(){}

    public List<Pojo3> getPojo3s() {
        return pojo3s;
    }
}
