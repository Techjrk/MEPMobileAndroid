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

//    public UpdateUserProfileRequest(User user) {
//
//        this.id  = user.getId();
//        this.firstName = user.getFirstName();
//        this.lastName = user.getLastName();
//        this.phoneNumber = user.getPhoneNumber();
//        this.email = user.getEmail();
//        this.fax = user.getFax();
//        this.address = user.getAddress();
//        this.address2 = user.getAddress2();
//        this.city = user.getCity();
//        this.state = user.getState();
//        this.zip = user.getZip();
//        this.organization = user.getOrganization();
//        this.title = user.getTitle();
//    }

    private UpdateUserProfileRequest(Builder builder) {

        this.id  = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.fax = builder.fax;
        this.address = builder.address;
        this.address2 = builder.address2;
        this.city = builder.city;
        this.state = builder.state;
        this.zip = builder.zip;
        this.organization = builder.organization;
        this.title = builder.title;
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

    public static class Builder {

        private long id;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;
        private String fax;
        private String address;
        private String address2;
        private String city;
        private String state;
        private String zip;
        private String organization;
        private String title;


        public Builder(long id) {

            this.id = id;
        }

        public Builder firstName(String firstName) {

            this.firstName = firstName;

            return this;
        }

        public Builder lastName(String lastName) {

            this.lastName = lastName;

            return this;
        }

        public Builder phoneNumber(String phoneNumber) {

            this.phoneNumber = phoneNumber;

            return this;
        }

        public Builder email (String email) {

            this.email = email;

            return this;
        }

        public Builder fax (String fax) {

            this.fax = fax;

            return this;
        }

        public Builder address(String address) {

            this.address = address;

            return this;
        }

        public Builder address2(String address2) {

            this.address2 = address2;

            return this;
        }

        public Builder city(String city) {

            this.city = city;

            return this;
        }

        public Builder state(String state) {

            this.state = state;

            return this;
        }

        public Builder zip(String zip) {

            this.zip = zip;

            return this;
        }

        public Builder organization(String organization) {

            this.organization = organization;

            return this;
        }

        public Builder title(String title) {

            this.title = title;

            return this;
        }

        public UpdateUserProfileRequest build() {

            return new UpdateUserProfileRequest(this);
        }
    }
}
