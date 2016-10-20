package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: User Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class User extends RealmObject {

    @PrimaryKey
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
        this.address = builder.address;
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

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
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
        private String address;
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

        Builder address(String streetAddress) {

            this.address = streetAddress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (address2 != null ? !address2.equals(user.address2) : user.address2 != null)
            return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (state != null ? !state.equals(user.state) : user.state != null) return false;
        if (zip != null ? !zip.equals(user.zip) : user.zip != null) return false;
        if (rank != null ? !rank.equals(user.rank) : user.rank != null) return false;
        if (group != null ? !group.equals(user.group) : user.group != null) return false;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
        if (createdAt != null ? !createdAt.equals(user.createdAt) : user.createdAt != null)
            return false;
        return updatedAt != null ? updatedAt.equals(user.updatedAt) : user.updatedAt == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", rank='" + rank + '\'' +
                ", group='" + group + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
