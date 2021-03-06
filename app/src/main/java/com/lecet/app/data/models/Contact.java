package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: Contact Created: 10/19/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Contact extends RealmObject implements Serializable{

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("contactId")
    private long contactId;

    @SerializedName("companyId")
    private long companyId;

    @SerializedName("projectId")
    private long projectId;

    @SerializedName("contactTypeId")
    private long contactTypeId;

    @SerializedName("company")
    private Company company;

    @SerializedName("contactType")
    private ContactType contactType;

    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

    @SerializedName("fipsCounty")
    private String fipsCounty;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    public Contact() {}

    public long getId() {
        return id;
    }

    public long getContactId() {
        return contactId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getProjectId() {
        return projectId;
    }

    public long getContactTypeId() {
        return contactTypeId;
    }

    public Company getCompany() {
        return company;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getFipsCounty() {
        return fipsCounty;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", companyId=" + companyId +
                ", projectId=" + projectId +
                ", contactTypeId=" + contactTypeId +
                ", company=" + company +
                ", contactType=" + contactType +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (id != contact.id) return false;
        if (contactId != contact.contactId) return false;
        if (companyId != contact.companyId) return false;
        if (projectId != contact.projectId) return false;
        if (contactTypeId != contact.contactTypeId) return false;
        if (company != null ? !company.equals(contact.company) : contact.company != null)
            return false;
        if (contactType != null ? !contactType.equals(contact.contactType) : contact.contactType != null)
            return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (title != null ? !title.equals(contact.title) : contact.title != null) return false;
        if (fipsCounty != null ? !fipsCounty.equals(contact.fipsCounty) : contact.fipsCounty != null)
            return false;
        if (email != null ? !email.equals(contact.email) : contact.email != null) return false;
        return phoneNumber != null ? phoneNumber.equals(contact.phoneNumber) : contact.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (contactId ^ (contactId >>> 32));
        result = 31 * result + (int) (companyId ^ (companyId >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (int) (contactTypeId ^ (contactTypeId >>> 32));
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (contactType != null ? contactType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (fipsCounty != null ? fipsCounty.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
