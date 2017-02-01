package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.interfaces.TrackedObject;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: Project Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Project extends RealmObject implements TrackedObject {

    @SerializedName("bidSubmitTo")
    private String bidSubmitTo;

    @SerializedName("state")
    private String state;

    @SerializedName("address1")
    private String address1;

    @SerializedName("address2")
    private String address2;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("projectStage")
    private ProjectStage projectStage;

    @SerializedName("estLow")
    private double estLow;

    @SerializedName("bidDate")
    private Date bidDate;

    @SerializedName("city")
    private String city;

    @SerializedName("primaryProjectTypeId")
    private long primaryProjectTypeId;

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("zip5")
    private String zip5;

    @SerializedName("cnProjectUrl")
    private String cnProjectUrl;

    @SerializedName("title")
    private String title;

    @SerializedName("contractNbr")
    private String contractNbr;

    @SerializedName("numberOfFloorsAboveGround")
    private String numberOfFloorsAboveGround;

    @SerializedName("details")
    private String details;

    @SerializedName("dodgeNumber")
    private String dodgeNumber;

    @SerializedName("lastPublishDate")
    private Date lastPublishDate;

    @SerializedName("contractType")
    private String contractType;

    @SerializedName("projDlvrySys")
    private String projDlvrySys;

    @SerializedName("planInd")
    private String planInd;

    @SerializedName("dodgeVersion")
    private String dodgeVersion;

    @SerializedName("primaryProjectType")
    private PrimaryProjectType primaryProjectType;

    @SerializedName("specAvailable")
    private String specAvailable;

    @SerializedName("fipsCounty")
    private String fipsCounty;

    @SerializedName("targetFinishDate")
    private Date targetFinishDate;

    @SerializedName("priorPublishDate")
    private Date priorPublishDate;

    @SerializedName("numberOfBuildings")
    private String numberOfBuildings;

    @SerializedName("statusProjDlvrySys")
    private String statusProjDlvrySys;

    @SerializedName("estHigh")
    private double estHigh;

    @SerializedName("stdIncludes")
    private String stdIncludes;

    @SerializedName("currencyType")
    private String currencyType;

    @SerializedName("country")
    private String country;

    @SerializedName("geocode")
    private Geocode geocode;

    @SerializedName("zipPlus4")
    private String zipPlus4;

    @SerializedName("projectStageId")
    private String projectStageId;

    @SerializedName("projectNotes")
    private String projectNotes;

    @SerializedName("targetStartDate")
    private Date targetStartDate;

    @SerializedName("geoLocationType")
    private String geoLocationType;

    @SerializedName("county")
    private String county;

    @SerializedName("firstPublishDate")
    private Date firstPublishDate;

    @SerializedName("addendaInd")
    private String addendaInd;

    @SerializedName("availableFrom")
    private String availableFrom;

    @SerializedName("geoType")
    private String geoType;

    @SerializedName("unionDesignation")
    private String unionDesignation;

    @SerializedName("bidTimeZone")
    private String bidTimeZone;

    @SerializedName("bondInformation")
    private String bondInformation;

    @SerializedName("ownerClass")
    private String ownerClass;

    @SerializedName("contacts")
    private RealmList<Contact> contacts;

    @SerializedName("bids")
    private RealmList<Bid> bids;

    private boolean hidden;

    @SerializedName("updates")
    private RealmList<ActivityUpdate> updates;

    private ActivityUpdate recentUpdate;

    public Project() {
    }

    public String getBidSubmitTo() {
        return bidSubmitTo;
    }

    public String getState() {
        return state;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getStatusText() {
        return statusText;
    }

    public ProjectStage getProjectStage() {
        return projectStage;
    }

    public double getEstLow() {
        return estLow;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public String getCity() {
        return city;
    }

    public long getPrimaryProjectTypeId() {
        return primaryProjectTypeId;
    }

    public long getId() {
        return id;
    }

    public String getZip5() {
        return zip5;
    }

    public String getCnProjectUrl() {
        return cnProjectUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContractNbr() {
        return contractNbr;
    }

    public String getNumberOfFloorsAboveGround() {
        return numberOfFloorsAboveGround;
    }

    public String getDetails() {
        return details;
    }

    public String getDodgeNumber() {
        return dodgeNumber;
    }

    public Date getLastPublishDate() {
        return lastPublishDate;
    }

    public String getContractType() {
        return contractType;
    }

    public String getProjDlvrySys() {
        return projDlvrySys;
    }

    public String getPlanInd() {
        return planInd;
    }

    public String getDodgeVersion() {
        return dodgeVersion;
    }

    public PrimaryProjectType getPrimaryProjectType() {
        return primaryProjectType;
    }

    public String getSpecAvailable() {
        return specAvailable;
    }

    public String getFipsCounty() {
        return fipsCounty;
    }

    public Date getTargetFinishDate() {
        return targetFinishDate;
    }

    public Date getPriorPublishDate() {
        return priorPublishDate;
    }

    public String getNumberOfBuildings() {
        return numberOfBuildings;
    }

    public String getStatusProjDlvrySys() {
        return statusProjDlvrySys;
    }

    public double getEstHigh() {
        return estHigh;
    }

    public String getStdIncludes() {
        return stdIncludes;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public String getCountry() {
        return country;
    }

    public Geocode getGeocode() {
        return geocode;
    }

    public String getZipPlus4() {
        return zipPlus4;
    }

    public String getProjectStageId() {
        return projectStageId;
    }

    public String getProjectNotes() {
        return projectNotes;
    }

    public Date getTargetStartDate() {
        return targetStartDate;
    }

    public String getGeoLocationType() {
        return geoLocationType;
    }

    public String getCounty() {
        return county;
    }

    public Date getFirstPublishDate() {
        return firstPublishDate;
    }

    public String getAddendaInd() {
        return addendaInd;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public String getGeoType() {
        return geoType;
    }

    public String getUnionDesignation() {
        return unionDesignation;
    }

    public String getBidTimeZone() {
        return bidTimeZone;
    }

    public String getBondInformation() {
        return bondInformation;
    }

    public String getOwnerClass() {
        return ownerClass;
    }

    public RealmList<Contact> getContacts() {
        return contacts;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {

        this.hidden = hidden;
    }

    public RealmList<Bid> getBids() {
        return bids;
    }

    /** Convenience **/

    public String getFullAddress() {

        String fullAddress  = "";

        if (this.address1 != null) {

            fullAddress = this.address1;
        }

        if (this.state != null) {

            fullAddress = fullAddress + ", " + this.state;
        }

        if (this.zip5 != null) {

            fullAddress = fullAddress + " " + zip5;
        }

        return fullAddress;
    }


    public String getProjectTypes() {

        String projectType = "";

        if (this.primaryProjectType != null ) {

            projectType = primaryProjectType.getTitle();

            if (this.primaryProjectType.getProjectCategory() != null) {

                projectType = projectType + " " + this.primaryProjectType.getProjectCategory().getTitle();

                if (this.primaryProjectType.getProjectCategory().getProjectGroup() != null) {

                    projectType = projectType + " " + this.primaryProjectType.getProjectCategory().getProjectGroup().getTitle();
                }
            }
        }

        return projectType;
    }

    public RealmList<ActivityUpdate> getUpdates() {
        return updates;
    }

    public ActivityUpdate getRecentUpdate() {
        return recentUpdate;
    }

    public void setRecentUpdate(ActivityUpdate recentUpdate) {
        this.recentUpdate = recentUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        return id != project.id;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = bidSubmitTo != null ? bidSubmitTo.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (statusText != null ? statusText.hashCode() : 0);
        result = 31 * result + (projectStage != null ? projectStage.hashCode() : 0);
        temp = Double.doubleToLongBits(estLow);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (bidDate != null ? bidDate.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (int) (primaryProjectTypeId ^ (primaryProjectTypeId >>> 32));
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (zip5 != null ? zip5.hashCode() : 0);
        result = 31 * result + (cnProjectUrl != null ? cnProjectUrl.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (contractNbr != null ? contractNbr.hashCode() : 0);
        result = 31 * result + (numberOfFloorsAboveGround != null ? numberOfFloorsAboveGround.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (dodgeNumber != null ? dodgeNumber.hashCode() : 0);
        result = 31 * result + (lastPublishDate != null ? lastPublishDate.hashCode() : 0);
        result = 31 * result + (contractType != null ? contractType.hashCode() : 0);
        result = 31 * result + (projDlvrySys != null ? projDlvrySys.hashCode() : 0);
        result = 31 * result + (planInd != null ? planInd.hashCode() : 0);
        result = 31 * result + (dodgeVersion != null ? dodgeVersion.hashCode() : 0);
        result = 31 * result + (primaryProjectType != null ? primaryProjectType.hashCode() : 0);
        result = 31 * result + (specAvailable != null ? specAvailable.hashCode() : 0);
        result = 31 * result + (fipsCounty != null ? fipsCounty.hashCode() : 0);
        result = 31 * result + (targetFinishDate != null ? targetFinishDate.hashCode() : 0);
        result = 31 * result + (priorPublishDate != null ? priorPublishDate.hashCode() : 0);
        result = 31 * result + (numberOfBuildings != null ? numberOfBuildings.hashCode() : 0);
        result = 31 * result + (statusProjDlvrySys != null ? statusProjDlvrySys.hashCode() : 0);
        temp = Double.doubleToLongBits(estHigh);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (stdIncludes != null ? stdIncludes.hashCode() : 0);
        result = 31 * result + (currencyType != null ? currencyType.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (geocode != null ? geocode.hashCode() : 0);
        result = 31 * result + (zipPlus4 != null ? zipPlus4.hashCode() : 0);
        result = 31 * result + (projectStageId != null ? projectStageId.hashCode() : 0);
        result = 31 * result + (projectNotes != null ? projectNotes.hashCode() : 0);
        result = 31 * result + (targetStartDate != null ? targetStartDate.hashCode() : 0);
        result = 31 * result + (geoLocationType != null ? geoLocationType.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (firstPublishDate != null ? firstPublishDate.hashCode() : 0);
        result = 31 * result + (addendaInd != null ? addendaInd.hashCode() : 0);
        result = 31 * result + (availableFrom != null ? availableFrom.hashCode() : 0);
        result = 31 * result + (geoType != null ? geoType.hashCode() : 0);
        result = 31 * result + (unionDesignation != null ? unionDesignation.hashCode() : 0);
        result = 31 * result + (bidTimeZone != null ? bidTimeZone.hashCode() : 0);
        result = 31 * result + (bondInformation != null ? bondInformation.hashCode() : 0);
        result = 31 * result + (ownerClass != null ? ownerClass.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (bids != null ? bids.hashCode() : 0);
        result = 31 * result + (hidden ? 1 : 0);
        result = 31 * result + (updates != null ? updates.hashCode() : 0);
        result = 31 * result + (recentUpdate != null ? recentUpdate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Project{" +
                "bidSubmitTo='" + bidSubmitTo + '\'' +
                ", state='" + state + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", statusText='" + statusText + '\'' +
                ", projectStage=" + projectStage +
                ", estLow=" + estLow +
                ", bidDate=" + bidDate +
                ", city='" + city + '\'' +
                ", primaryProjectTypeId=" + primaryProjectTypeId +
                ", id=" + id +
                ", zip5='" + zip5 + '\'' +
                ", cnProjectUrl='" + cnProjectUrl + '\'' +
                ", title='" + title + '\'' +
                ", contractNbr='" + contractNbr + '\'' +
                ", numberOfFloorsAboveGround='" + numberOfFloorsAboveGround + '\'' +
                ", details='" + details + '\'' +
                ", dodgeNumber='" + dodgeNumber + '\'' +
                ", lastPublishDate=" + lastPublishDate +
                ", contractType='" + contractType + '\'' +
                ", projDlvrySys='" + projDlvrySys + '\'' +
                ", planInd='" + planInd + '\'' +
                ", dodgeVersion='" + dodgeVersion + '\'' +
                ", primaryProjectType=" + primaryProjectType +
                ", specAvailable='" + specAvailable + '\'' +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", targetFinishDate=" + targetFinishDate +
                ", priorPublishDate=" + priorPublishDate +
                ", numberOfBuildings='" + numberOfBuildings + '\'' +
                ", statusProjDlvrySys='" + statusProjDlvrySys + '\'' +
                ", estHigh=" + estHigh +
                ", stdIncludes='" + stdIncludes + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", country='" + country + '\'' +
                ", geocode=" + geocode +
                ", zipPlus4='" + zipPlus4 + '\'' +
                ", projectStageId='" + projectStageId + '\'' +
                ", projectNotes='" + projectNotes + '\'' +
                ", targetStartDate=" + targetStartDate +
                ", geoLocationType='" + geoLocationType + '\'' +
                ", county='" + county + '\'' +
                ", firstPublishDate=" + firstPublishDate +
                ", addendaInd='" + addendaInd + '\'' +
                ", availableFrom='" + availableFrom + '\'' +
                ", geoType='" + geoType + '\'' +
                ", unionDesignation='" + unionDesignation + '\'' +
                ", bidTimeZone='" + bidTimeZone + '\'' +
                ", bondInformation='" + bondInformation + '\'' +
                ", ownerClass='" + ownerClass + '\'' +
                ", contacts=" + contacts +
                ", bids=" + bids +
                ", hidden=" + hidden +
                ", updates=" + updates +
                ", recentUpdate=" + recentUpdate +
                '}';
    }
}