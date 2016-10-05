package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Project Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Project {

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
    private String bidDate;

    @SerializedName("city")
    private String city;

    @SerializedName("primaryProjectTypeId")
    private long primaryProjectTypeId;

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
    private String lastPublishDate;

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
    private String targetFinishDate;

    @SerializedName("priorPublishDate")
    private String priorPublishDate;

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
    private String targetStartDate;

    @SerializedName("geoLocationType")
    private String geoLocationType;

    @SerializedName("county")
    private String county;

    @SerializedName("firstPublishDate")
    private String firstPublishDate;

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

    public Project() {
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
                ", bidDate='" + bidDate + '\'' +
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
                ", lastPublishDate='" + lastPublishDate + '\'' +
                ", contractType='" + contractType + '\'' +
                ", projDlvrySys='" + projDlvrySys + '\'' +
                ", planInd='" + planInd + '\'' +
                ", dodgeVersion='" + dodgeVersion + '\'' +
                ", primaryProjectType=" + primaryProjectType +
                ", specAvailable='" + specAvailable + '\'' +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", targetFinishDate='" + targetFinishDate + '\'' +
                ", priorPublishDate='" + priorPublishDate + '\'' +
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
                ", targetStartDate='" + targetStartDate + '\'' +
                ", geoLocationType='" + geoLocationType + '\'' +
                ", county='" + county + '\'' +
                ", firstPublishDate='" + firstPublishDate + '\'' +
                ", addendaInd='" + addendaInd + '\'' +
                ", availableFrom='" + availableFrom + '\'' +
                ", geoType='" + geoType + '\'' +
                ", unionDesignation='" + unionDesignation + '\'' +
                ", bidTimeZone='" + bidTimeZone + '\'' +
                ", bondInformation='" + bondInformation + '\'' +
                ", ownerClass='" + ownerClass + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (Double.compare(project.estLow, estLow) != 0) return false;
        if (primaryProjectTypeId != project.primaryProjectTypeId) return false;
        if (id != project.id) return false;
        if (Double.compare(project.estHigh, estHigh) != 0) return false;
        if (bidSubmitTo != null ? !bidSubmitTo.equals(project.bidSubmitTo) : project.bidSubmitTo != null)
            return false;
        if (state != null ? !state.equals(project.state) : project.state != null) return false;
        if (address1 != null ? !address1.equals(project.address1) : project.address1 != null)
            return false;
        if (address2 != null ? !address2.equals(project.address2) : project.address2 != null)
            return false;
        if (statusText != null ? !statusText.equals(project.statusText) : project.statusText != null)
            return false;
        if (projectStage != null ? !projectStage.equals(project.projectStage) : project.projectStage != null)
            return false;
        if (bidDate != null ? !bidDate.equals(project.bidDate) : project.bidDate != null)
            return false;
        if (city != null ? !city.equals(project.city) : project.city != null) return false;
        if (zip5 != null ? !zip5.equals(project.zip5) : project.zip5 != null) return false;
        if (cnProjectUrl != null ? !cnProjectUrl.equals(project.cnProjectUrl) : project.cnProjectUrl != null)
            return false;
        if (title != null ? !title.equals(project.title) : project.title != null) return false;
        if (contractNbr != null ? !contractNbr.equals(project.contractNbr) : project.contractNbr != null)
            return false;
        if (numberOfFloorsAboveGround != null ? !numberOfFloorsAboveGround.equals(project.numberOfFloorsAboveGround) : project.numberOfFloorsAboveGround != null)
            return false;
        if (details != null ? !details.equals(project.details) : project.details != null)
            return false;
        if (dodgeNumber != null ? !dodgeNumber.equals(project.dodgeNumber) : project.dodgeNumber != null)
            return false;
        if (lastPublishDate != null ? !lastPublishDate.equals(project.lastPublishDate) : project.lastPublishDate != null)
            return false;
        if (contractType != null ? !contractType.equals(project.contractType) : project.contractType != null)
            return false;
        if (projDlvrySys != null ? !projDlvrySys.equals(project.projDlvrySys) : project.projDlvrySys != null)
            return false;
        if (planInd != null ? !planInd.equals(project.planInd) : project.planInd != null)
            return false;
        if (dodgeVersion != null ? !dodgeVersion.equals(project.dodgeVersion) : project.dodgeVersion != null)
            return false;
        if (primaryProjectType != null ? !primaryProjectType.equals(project.primaryProjectType) : project.primaryProjectType != null)
            return false;
        if (specAvailable != null ? !specAvailable.equals(project.specAvailable) : project.specAvailable != null)
            return false;
        if (fipsCounty != null ? !fipsCounty.equals(project.fipsCounty) : project.fipsCounty != null)
            return false;
        if (targetFinishDate != null ? !targetFinishDate.equals(project.targetFinishDate) : project.targetFinishDate != null)
            return false;
        if (priorPublishDate != null ? !priorPublishDate.equals(project.priorPublishDate) : project.priorPublishDate != null)
            return false;
        if (numberOfBuildings != null ? !numberOfBuildings.equals(project.numberOfBuildings) : project.numberOfBuildings != null)
            return false;
        if (statusProjDlvrySys != null ? !statusProjDlvrySys.equals(project.statusProjDlvrySys) : project.statusProjDlvrySys != null)
            return false;
        if (stdIncludes != null ? !stdIncludes.equals(project.stdIncludes) : project.stdIncludes != null)
            return false;
        if (currencyType != null ? !currencyType.equals(project.currencyType) : project.currencyType != null)
            return false;
        if (country != null ? !country.equals(project.country) : project.country != null)
            return false;
        if (geocode != null ? !geocode.equals(project.geocode) : project.geocode != null)
            return false;
        if (zipPlus4 != null ? !zipPlus4.equals(project.zipPlus4) : project.zipPlus4 != null)
            return false;
        if (projectStageId != null ? !projectStageId.equals(project.projectStageId) : project.projectStageId != null)
            return false;
        if (projectNotes != null ? !projectNotes.equals(project.projectNotes) : project.projectNotes != null)
            return false;
        if (targetStartDate != null ? !targetStartDate.equals(project.targetStartDate) : project.targetStartDate != null)
            return false;
        if (geoLocationType != null ? !geoLocationType.equals(project.geoLocationType) : project.geoLocationType != null)
            return false;
        if (county != null ? !county.equals(project.county) : project.county != null) return false;
        if (firstPublishDate != null ? !firstPublishDate.equals(project.firstPublishDate) : project.firstPublishDate != null)
            return false;
        if (addendaInd != null ? !addendaInd.equals(project.addendaInd) : project.addendaInd != null)
            return false;
        if (availableFrom != null ? !availableFrom.equals(project.availableFrom) : project.availableFrom != null)
            return false;
        if (geoType != null ? !geoType.equals(project.geoType) : project.geoType != null)
            return false;
        if (unionDesignation != null ? !unionDesignation.equals(project.unionDesignation) : project.unionDesignation != null)
            return false;
        if (bidTimeZone != null ? !bidTimeZone.equals(project.bidTimeZone) : project.bidTimeZone != null)
            return false;
        if (bondInformation != null ? !bondInformation.equals(project.bondInformation) : project.bondInformation != null)
            return false;
        return ownerClass != null ? ownerClass.equals(project.ownerClass) : project.ownerClass == null;

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
        return result;
    }
}