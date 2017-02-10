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

    @SerializedName("faxNumber")
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

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFax() {
        return fax;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public String getTitle() {
        return title;
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

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", fax='" + fax + '\'' +
                ", address='" + address + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", organization='" + organization + '\'' +
                ", title='" + title + '\'' +
                '}';
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
        if (fax != null ? !fax.equals(user.fax) : user.fax != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (address2 != null ? !address2.equals(user.address2) : user.address2 != null)
            return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (state != null ? !state.equals(user.state) : user.state != null) return false;
        if (zip != null ? !zip.equals(user.zip) : user.zip != null) return false;
        if (organization != null ? !organization.equals(user.organization) : user.organization != null)
            return false;
        return title != null ? title.equals(user.title) : user.title == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
