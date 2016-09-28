package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * File: CreateUserRequest Created: 8/26/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class CreateUserRequest implements Parcelable {

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

    @SerializedName("avatar")
    private String avatar;

    public CreateUserRequest(){

    }

    protected CreateUserRequest(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        streetAddress = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        avatar = in.readString();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(streetAddress);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(avatar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreateUserRequest> CREATOR = new Creator<CreateUserRequest>() {
        @Override
        public CreateUserRequest createFromParcel(Parcel in) {
            return new CreateUserRequest(in);
        }

        @Override
        public CreateUserRequest[] newArray(int size) {
            return new CreateUserRequest[size];
        }
    };

}
