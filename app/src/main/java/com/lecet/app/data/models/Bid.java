package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Bid Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Bid {

    @SerializedName("awardInd")
    private boolean awardInd;

    @SerializedName("createDate")
    private String createDate;

    @SerializedName("amount")
    private long amount;

    @SerializedName("bidderRole")
    private String bidderRole;

    @SerializedName("rank")
    private int rank;

    @SerializedName("fipsCounty")
    private String fipsCounty;

    @SerializedName("zip5")
    private String zip5;

    @SerializedName("id")
    private long id;

    @SerializedName("companyId")
    private long companyId;

    @SerializedName("contactId")
    private long contactId;

    @SerializedName("projectId")
    private long projectId;


    public Bid(){}

    public boolean isAwardInd() {
        return awardInd;
    }

    public String getCreateDate() {
        return createDate;
    }

    public long getAmount() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        if (awardInd != bid.awardInd) return false;
        if (amount != bid.amount) return false;
        if (rank != bid.rank) return false;
        if (id != bid.id) return false;
        if (companyId != bid.companyId) return false;
        if (contactId != bid.contactId) return false;
        if (projectId != bid.projectId) return false;
        if (!createDate.equals(bid.createDate)) return false;
        if (!bidderRole.equals(bid.bidderRole)) return false;
        if (!fipsCounty.equals(bid.fipsCounty)) return false;
        return zip5 != null ? zip5.equals(bid.zip5) : bid.zip5 == null;

    }

    @Override
    public int hashCode() {
        int result = (awardInd ? 1 : 0);
        result = 31 * result + createDate.hashCode();
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        result = 31 * result + bidderRole.hashCode();
        result = 31 * result + rank;
        result = 31 * result + fipsCounty.hashCode();
        result = 31 * result + (zip5 != null ? zip5.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (companyId ^ (companyId >>> 32));
        result = 31 * result + (int) (contactId ^ (contactId >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "awardInd=" + awardInd +
                ", createDate='" + createDate + '\'' +
                ", amount=" + amount +
                ", bidderRole='" + bidderRole + '\'' +
                ", rank=" + rank +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", zip5='" + zip5 + '\'' +
                ", id=" + id +
                ", companyId=" + companyId +
                ", contactId=" + contactId +
                ", projectId=" + projectId +
                '}';
    }
}
