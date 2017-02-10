package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: Bid Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Bid extends RealmObject {

    @SerializedName("awardInd")
    private boolean awardInd;

    @SerializedName("createDate")
    private Date createDate;

    @SerializedName("amount")
    private double amount;

    @SerializedName("bidderRole")
    private String bidderRole;

    @SerializedName("rank")
    private int rank;

    @SerializedName("fipsCounty")
    private String fipsCounty;

    @SerializedName("zip5")
    private String zip5;

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("companyId")
    private long companyId;

    @SerializedName("contactId")
    private long contactId;

    @SerializedName("projectId")
    private long projectId;

    @SerializedName("project")
    private Project project;

    @SerializedName("contact")
    private Contact contact;

    @SerializedName("company")
    private Company company;

    private boolean mbrItem; // Mobile bids recently made

    public Bid() {
    }


    public boolean isAwardInd() {
        return awardInd;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getBidderRole() {
        return bidderRole;
    }

    public int getRank() {
        return rank;
    }

    public String getFipsCounty() {
        return fipsCounty;
    }

    public String getZip5() {
        return zip5;
    }

    public long getId() {
        return id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getContactId() {
        return contactId;
    }

    public long getProjectId() {
        return projectId;
    }

    public Project getProject() {
        return project;
    }

    public Contact getContact() {
        return contact;
    }

    public Company getCompany() {
        return company;
    }

    public boolean isMbrItem() {
        return mbrItem;
    }

    public void setMbrItem(boolean mbrItem) {
        this.mbrItem = mbrItem;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "awardInd=" + awardInd +
                ", createDate=" + createDate +
                ", amount=" + amount +
                ", bidderRole='" + bidderRole + '\'' +
                ", rank=" + rank +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", zip5='" + zip5 + '\'' +
                ", id=" + id +
                ", companyId=" + companyId +
                ", contactId=" + contactId +
                ", projectId=" + projectId +
                ", project=" + project +
                ", contact=" + contact +
                ", company=" + company +
                ", mbrItem=" + mbrItem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;

        Bid bid = (Bid) o;

        if (awardInd != bid.awardInd) return false;
        if (Double.compare(bid.amount, amount) != 0) return false;
        if (rank != bid.rank) return false;
        if (id != bid.id) return false;
        if (companyId != bid.companyId) return false;
        if (contactId != bid.contactId) return false;
        if (projectId != bid.projectId) return false;
        if (mbrItem != bid.mbrItem) return false;
        if (createDate != null ? !createDate.equals(bid.createDate) : bid.createDate != null)
            return false;
        if (bidderRole != null ? !bidderRole.equals(bid.bidderRole) : bid.bidderRole != null)
            return false;
        if (fipsCounty != null ? !fipsCounty.equals(bid.fipsCounty) : bid.fipsCounty != null)
            return false;
        if (zip5 != null ? !zip5.equals(bid.zip5) : bid.zip5 != null) return false;
        if (project != null ? !project.equals(bid.project) : bid.project != null) return false;
        if (contact != null ? !contact.equals(bid.contact) : bid.contact != null) return false;
        return company != null ? company.equals(bid.company) : bid.company == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (awardInd ? 1 : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (bidderRole != null ? bidderRole.hashCode() : 0);
        result = 31 * result + rank;
        result = 31 * result + (fipsCounty != null ? fipsCounty.hashCode() : 0);
        result = 31 * result + (zip5 != null ? zip5.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (companyId ^ (companyId >>> 32));
        result = 31 * result + (int) (contactId ^ (contactId >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (mbrItem ? 1 : 0);
        return result;
    }
}
