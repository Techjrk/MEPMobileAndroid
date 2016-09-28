package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Pojo2 Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class Pojo2 {

    @SerializedName("id")
    private int ID;

    @SerializedName("course_id")
    private int courseID;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("holes")
    private int holes;

    @SerializedName("cart")
    private int cart;

    @SerializedName("walk")
    private int walk;

    @SerializedName("ride")
    private int ride;

    @SerializedName("caddy_qty")
    private int caddyQty;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updateAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    public Pojo2() {
    }

    public int getID() {
        return ID;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getHoles() {
        return holes;
    }

    public int getCart() {
        return cart;
    }

    public int getWalk() {
        return walk;
    }

    public int getRide() {
        return ride;
    }

    public int getCaddyQty() {
        return caddyQty;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }
}
