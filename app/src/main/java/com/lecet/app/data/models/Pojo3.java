package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Pojo3 Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class Pojo3 {

    @SerializedName("id")
    private int ID;

    @SerializedName("teetime_id")
    private int teeTimeID;

    @SerializedName("user_id")
    private int userID;

    @SerializedName("caddy_user_id")
    private int caddyUserID;

    @SerializedName("is_lead")
    private int isLead;

    @SerializedName("walking")
    private int walking;

    @SerializedName("caddy_agree")
    private String caddyAgree;

    @SerializedName("caddy")
    private int caddy;

    @SerializedName("riding")
    private int riding;

    @SerializedName("message")
    private String message;

    @SerializedName("tee_time")
    private Pojo2 pojo2;

    public Pojo3() {
    }

    public int getID() {
        return ID;
    }

    public int getTeeTimeID() {
        return teeTimeID;
    }

    public int getUserID() {
        return userID;
    }

    public int getCaddyUserID() {
        return caddyUserID;
    }

    public int getIsLead() {
        return isLead;
    }

    public int getWalking() {
        return walking;
    }

    public String getCaddyAgree() {
        return caddyAgree;
    }

    public int getCaddy() {
        return caddy;
    }

    public int getRiding() {
        return riding;
    }

    public String getMessage() {
        return message;
    }

    public Pojo2 getPojo2() {
        return pojo2;
    }
}
