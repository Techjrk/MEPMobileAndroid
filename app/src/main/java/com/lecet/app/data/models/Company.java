package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.interfaces.TrackedObject;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: Company Created: 10/19/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Company extends RealmObject implements TrackedObject {

    @SerializedName("name")
    private String name;

    @SerializedName("address1")
    private String address1;

    @SerializedName("address2")
    private String address2;

    @SerializedName("county")
    private String county;

    @SerializedName("fipsCounty")
    private String fipsCounty;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip5")
    private String zip5;

    @SerializedName("zipPlus4")
    private String zipPlus4;

    @SerializedName("country")
    private String country;

    @SerializedName("ckmsSiteId")
    private String ckmsSiteId;

    @SerializedName("cnCompanysiteUrl")
    private String cnCompanysiteUrl;

    @SerializedName("wwwUrl")
    private String wwwUrl;

    @SerializedName("dcisFactorCntctCode")
    private String dcisFactorCntctCode;

    @SerializedName("dcisFactorCode")
    private String dcisFactorCode;

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    private RealmList<ActivityUpdate> updates;

    private ActivityUpdate recentUpdate;

    @SerializedName("contacts")
    private RealmList<Contact> contacts;

    @SerializedName("projects")
    private RealmList<Project> projects;

    @SerializedName("bids")
    private RealmList<Bid> bids;

    public Company() {
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCounty() {
        return county;
    }

    public String getFipsCounty() {
        return fipsCounty;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip5() {
        return zip5;
    }

    public String getZipPlus4() {
        return zipPlus4;
    }

    public String getCountry() {
        return country;
    }

    public String getCkmsSiteId() {
        return ckmsSiteId;
    }

    public String getCnCompanysiteUrl() {
        return cnCompanysiteUrl;
    }

    public String getWwwUrl() {
        return wwwUrl;
    }

    public String getDcisFactorCntctCode() {
        return dcisFactorCntctCode;
    }

    public String getDcisFactorCode() {
        return dcisFactorCode;
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public ActivityUpdate getRecentUpdate() {
        return recentUpdate;
    }

    public RealmList<Contact> getContacts() {
        return contacts;
    }

    public RealmList<Project> getProjects() {
        return projects;
    }

    public RealmList<Bid> getBids() {
        return bids;
    }

    public String getFullAddress() {

        return address1 + " " + (address2 != null ? address2 : "") + " " + city + " " + state + " " + zipPlus4;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", county='" + county + '\'' +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip5='" + zip5 + '\'' +
                ", zipPlus4='" + zipPlus4 + '\'' +
                ", country='" + country + '\'' +
                ", ckmsSiteId='" + ckmsSiteId + '\'' +
                ", cnCompanysiteUrl='" + cnCompanysiteUrl + '\'' +
                ", wwwUrl='" + wwwUrl + '\'' +
                ", dcisFactorCntctCode='" + dcisFactorCntctCode + '\'' +
                ", dcisFactorCode='" + dcisFactorCode + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", updates=" + updates +
                ", recentUpdate=" + recentUpdate +
                ", contacts=" + contacts +
                ", projects=" + projects +
                ", bids=" + bids +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;

        Company company = (Company) o;

        if (id != company.id) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        if (address1 != null ? !address1.equals(company.address1) : company.address1 != null)
            return false;
        if (address2 != null ? !address2.equals(company.address2) : company.address2 != null)
            return false;
        if (county != null ? !county.equals(company.county) : company.county != null) return false;
        if (fipsCounty != null ? !fipsCounty.equals(company.fipsCounty) : company.fipsCounty != null)
            return false;
        if (city != null ? !city.equals(company.city) : company.city != null) return false;
        if (state != null ? !state.equals(company.state) : company.state != null) return false;
        if (zip5 != null ? !zip5.equals(company.zip5) : company.zip5 != null) return false;
        if (zipPlus4 != null ? !zipPlus4.equals(company.zipPlus4) : company.zipPlus4 != null)
            return false;
        if (country != null ? !country.equals(company.country) : company.country != null)
            return false;
        if (ckmsSiteId != null ? !ckmsSiteId.equals(company.ckmsSiteId) : company.ckmsSiteId != null)
            return false;
        if (cnCompanysiteUrl != null ? !cnCompanysiteUrl.equals(company.cnCompanysiteUrl) : company.cnCompanysiteUrl != null)
            return false;
        if (wwwUrl != null ? !wwwUrl.equals(company.wwwUrl) : company.wwwUrl != null) return false;
        if (dcisFactorCntctCode != null ? !dcisFactorCntctCode.equals(company.dcisFactorCntctCode) : company.dcisFactorCntctCode != null)
            return false;
        if (dcisFactorCode != null ? !dcisFactorCode.equals(company.dcisFactorCode) : company.dcisFactorCode != null)
            return false;
        if (createdAt != null ? !createdAt.equals(company.createdAt) : company.createdAt != null)
            return false;
        if (updatedAt != null ? !updatedAt.equals(company.updatedAt) : company.updatedAt != null)
            return false;
        if (updates != null ? !updates.equals(company.updates) : company.updates != null)
            return false;
        if (recentUpdate != null ? !recentUpdate.equals(company.recentUpdate) : company.recentUpdate != null)
            return false;
        if (contacts != null ? !contacts.equals(company.contacts) : company.contacts != null)
            return false;
        if (projects != null ? !projects.equals(company.projects) : company.projects != null)
            return false;
        return bids != null ? bids.equals(company.bids) : company.bids == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (fipsCounty != null ? fipsCounty.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip5 != null ? zip5.hashCode() : 0);
        result = 31 * result + (zipPlus4 != null ? zipPlus4.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (ckmsSiteId != null ? ckmsSiteId.hashCode() : 0);
        result = 31 * result + (cnCompanysiteUrl != null ? cnCompanysiteUrl.hashCode() : 0);
        result = 31 * result + (wwwUrl != null ? wwwUrl.hashCode() : 0);
        result = 31 * result + (dcisFactorCntctCode != null ? dcisFactorCntctCode.hashCode() : 0);
        result = 31 * result + (dcisFactorCode != null ? dcisFactorCode.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (updates != null ? updates.hashCode() : 0);
        result = 31 * result + (recentUpdate != null ? recentUpdate.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (projects != null ? projects.hashCode() : 0);
        result = 31 * result + (bids != null ? bids.hashCode() : 0);
        return result;
    }
}
