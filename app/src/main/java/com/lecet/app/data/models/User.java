package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: User Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class User {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    @SerializedName("street_address")
    private String streetAddress;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip")
    private String zip;

    @SerializedName("rank")
    private String rank;

    @SerializedName("group")
    private String group;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public User() {
    }

    private User(Builder builder) {

        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.streetAddress = builder.streetAddress;
        this.city = builder.city;
        this.state = builder.state;
        this.zip = builder.zip;
        this.rank = builder.rank;
        this.group = builder.group;
        this.avatar = builder.avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCity() {
        return city;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGroup() {
        return group;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRank() {
        return rank;
    }

    public String getState() {
        return state;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getZip() {
        return zip;
    }


    public static class Builder {

        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;
        private String streetAddress;
        private String city;
        private String state;
        private String zip;
        private String rank;
        private String group;
        private String avatar;

        public Builder() {
        }

        Builder firstName(String firstName) {

            this.firstName = firstName;
            return this;
        }

        Builder lastName(String lastName) {

            this.lastName = lastName;
            return this;
        }

        Builder phoneNumber(String phoneNumber) {

            this.phoneNumber = phoneNumber;
            return this;
        }

        Builder email(String email) {

            this.email = email;
            return this;
        }

        Builder streetAddress(String streetAddress) {

            this.streetAddress = streetAddress;
            return this;
        }

        Builder city(String city) {

            this.city = city;
            return this;
        }

        Builder state(String state) {

            this.state = state;
            return this;
        }

        Builder zip(String zip) {

            this.zip = zip;
            return this;
        }

        Builder rank(String rank) {

            this.rank = rank;
            return this;
        }

        Builder group(String group) {

            this.group = group;
            return this;
        }

        Builder avatar(String avatar) {

            this.avatar = avatar;
            return this;
        }

        User build() {

            return new User(this);
        }
    }
}
