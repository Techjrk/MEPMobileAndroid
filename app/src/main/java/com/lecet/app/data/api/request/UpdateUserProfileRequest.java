package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.data.models.User;

/**
 * File: UpdateUserProfileRequest Created: 1/27/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class UpdateUserProfileRequest {

    @SerializedName("id")
    private long id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    @SerializedName("fax")
    private String fax;

    @SerializedName("address")
    private String address;

    @SerializedName("address2")
    private String address2;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip")
    private String zip;

    @SerializedName("organization")
    private String organization;

    @SerializedName("title")
    private String title;

    public UpdateUserProfileRequest(User user) {

        this.id  = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.fax = user.getFax();
        this.address = user.getAddress();
        this.address2 = user.getAddress2();
        this.city = user.getCity();
        this.state = user.getState();
        this.zip = user.getZip();
        this.organization = user.getOrganization();
        this.title = user.getTitle();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getOrganization() {
        return organization;
    }

    public String getTitle() {
        return title;
    }
}
