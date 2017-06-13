package com.lecet.app.data.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.interfaces.TrackedObject;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: Project Created: 10/5/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Project extends RealmObject implements TrackedObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("dodgeNumber")
    private String dodgeNumber;

    @SerializedName("dodgeVersion")
    private String dodgeVersion;

    @SerializedName("title")
    private String title;

    @SerializedName("address1")
    private String address1;

    @SerializedName("address2")
    private String address2;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip5")
    private String zip5;

    @SerializedName("zipPlus4")
    private String zipPlus4;

    @SerializedName("county")
    private String county;

    @SerializedName("fipsCounty")
    private String fipsCounty;

    @SerializedName("country")
    private String country;

    @SerializedName("estLow")
    private double estLow;

    @SerializedName("estHigh")
    private double estHigh;

    @SerializedName("bidSubmitTo")
    private String bidSubmitTo;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("projectStage")
    private ProjectStage projectStage;

    @SerializedName("bidDate")
    private Date bidDate;

    @SerializedName("primaryProjectTypeId")
    private long primaryProjectTypeId;

    @SerializedName("cnProjectUrl")
    private String cnProjectUrl;

    @SerializedName("contractNbr")
    private String contractNbr;

    @SerializedName("numberOfFloorsAboveGround")
    private String numberOfFloorsAboveGround;

    @SerializedName("details")
    private String details;

    @SerializedName("lastPublishDate")
    private Date lastPublishDate;

    @SerializedName("contractType")
    private String contractType;

    @SerializedName("projDlvrySys")
    private String projDlvrySys;

    @SerializedName("planInd")
    private String planInd;

    @SerializedName("primaryProjectType")
    private PrimaryProjectType primaryProjectType;

    @SerializedName("specAvailable")
    private String specAvailable;

    @SerializedName("targetFinishDate")
    private Date targetFinishDate;

    @SerializedName("priorPublishDate")
    private Date priorPublishDate;

    @SerializedName("numberOfBuildings")
    private String numberOfBuildings;

    @SerializedName("statusProjDlvrySys")
    private String statusProjDlvrySys;

    @SerializedName("stdIncludes")
    private String stdIncludes;

    @SerializedName("currencyType")
    private String currencyType;

    @SerializedName("geocode")
    private Geocode geocode;

    @SerializedName("projectStageId")
    private String projectStageId;

    @SerializedName("projectNotes")
    private String projectNotes;

    @SerializedName("targetStartDate")
    private Date targetStartDate;

    @SerializedName("geoLocationType")
    private String geoLocationType;

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

    @SerializedName("updates")
    private RealmList<ActivityUpdate> updates;

    @SerializedName("userNotes")
    private RealmList<ProjectNote> userNotes;

    @SerializedName("images")
    private RealmList<ProjectPhoto> images;

    private boolean hidden;

    private ActivityUpdate recentUpdate;

    private boolean mbsItem; // Mobile bids happening soon

    private boolean mraItem; // Mobile projects recently added

    private boolean mruItem; // Mobile projects recently updated



    public Project() {
    }

    public RealmList<ProjectNote> getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(RealmList<ProjectNote> userNotes) {
        this.userNotes = userNotes;
    }

    public RealmList<ProjectPhoto> getImages() {
        return images;
    }

    public void setImages(RealmList<ProjectPhoto> images) {
        this.images = images;
    }

    public String getBidSubmitTo() {
        return bidSubmitTo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public String getEstLowStr() {
        return Double.toString(estLow);
    }

    public void setEstLow(double estLow) {
        this.estLow = estLow;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public void setZip5(String zip5) {
        this.zip5 = zip5;
    }

    public String getCnProjectUrl() {
        return cnProjectUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setEstHigh(double estHigh) {
        this.estHigh = estHigh;
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

    public void setGeocode(Geocode geocode) {
        this.geocode = geocode;
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

    public void setTargetStartDate(Date targetStartDate) {
        this.targetStartDate = targetStartDate;
    }

    public String getGeoLocationType() {
        return geoLocationType;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public boolean isMbsItem() {
        return mbsItem;
    }

    public void setMbsItem(boolean mbsItem) {
        this.mbsItem = mbsItem;
    }

    public boolean isMraItem() {
        return mraItem;
    }

    public void setMraItem(boolean mraItem) {
        this.mraItem = mraItem;
    }

    public boolean isMruItem() {
        return mruItem;
    }

    public void setMruItem(boolean mruItem) {
        this.mruItem = mruItem;
    }

    public RealmList<Bid> getBids() {
        return bids;
    }

    /**
     * Convenience
     **/

    public String getFullAddress() {

        String fullAddress = "";

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

        if (this.primaryProjectType != null) {

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

    public void updateProject(Realm realm, Project project, Boolean hidden) {

        this.bidSubmitTo = project.bidSubmitTo;
        this.state = project.getState();
        this.address1 = project.address1;
        this.address2 = project.getAddress2();
        this.statusText = project.getStatusText();
        if (project.getProjectStage() != null) {
            this.projectStage = realm.copyToRealmOrUpdate(project.getProjectStage());
        }
        this.estLow = project.getEstLow();
        this.bidDate = project.getBidDate();
        this.city = project.getCity();
        this.primaryProjectTypeId = project.getPrimaryProjectTypeId();
        this.zip5 = project.getZip5();
        this.cnProjectUrl = project.getCnProjectUrl();
        this.title = project.getTitle();
        this.contractNbr = project.getContractNbr();
        this.numberOfFloorsAboveGround = project.getNumberOfFloorsAboveGround();
        this.details = project.getDetails();
        this.dodgeNumber = project.getDodgeNumber();
        this.lastPublishDate = project.getLastPublishDate();
        this.contractType = project.getContractType();
        this.projDlvrySys = project.getProjDlvrySys();
        this.planInd = project.getPlanInd();
        this.dodgeVersion = project.getDodgeVersion();
        if (project.getPrimaryProjectType() != null) {
            this.primaryProjectType = realm.copyToRealmOrUpdate(project.getPrimaryProjectType());
        }
        this.specAvailable = project.getSpecAvailable();
        this.fipsCounty = project.getFipsCounty();
        this.targetFinishDate = project.getTargetFinishDate();
        this.priorPublishDate = project.getPriorPublishDate();
        this.numberOfBuildings = project.getNumberOfBuildings();
        this.statusProjDlvrySys = project.getStatusProjDlvrySys();
        this.estHigh = project.getEstHigh();
        this.stdIncludes = project.getStdIncludes();
        this.currencyType = project.getCurrencyType();
        this.country = project.getCountry();
        if (project.getGeocode() != null) {
            this.geocode = realm.copyToRealm(project.getGeocode());
        }
        this.zipPlus4 = project.getZipPlus4();
        this.projectStageId = project.getProjectStageId();
        this.projectNotes = project.getProjectNotes();
        this.targetStartDate = project.getTargetStartDate();
        this.geoLocationType = project.getGeoLocationType();
        this.county = project.getCounty();
        this.firstPublishDate = project.getFirstPublishDate();
        this.addendaInd = project.getAddendaInd();
        this.availableFrom = project.getAvailableFrom();
        this.geoType = project.getGeoType();
        this.unionDesignation = project.getUnionDesignation();
        this.bidTimeZone = project.getBidTimeZone();
        this.bondInformation = project.getBondInformation();
        this.ownerClass = project.getOwnerClass();
        if (project.getContacts() != null) {

            List<Contact> managedContacts = realm.copyToRealmOrUpdate(project.getContacts());
            for (Contact contact : managedContacts) {
                this.contacts.add(contact);
            }
        }
        if (project.getBids() != null) {

            List<Bid> managedBids = realm.copyToRealmOrUpdate(project.getBids());
            for (Bid bid : managedBids) {
                this.bids.add(bid);
            }
        }
        if (project.getUpdates() != null) {

            List<ActivityUpdate> managedUpdates = realm.copyToRealmOrUpdate(project.getUpdates());
            for (ActivityUpdate update : managedUpdates) {
                this.updates.add(update);
            }
        }

        if (hidden != null) {

            this.hidden = hidden.booleanValue();
        }
    }

    public void updateProject(Realm realm, Project project, @Nullable Boolean isHidden, @Nullable Boolean mbsItem, @Nullable Boolean mraItem, @Nullable Boolean mruItem) {

        this.bidSubmitTo = project.bidSubmitTo;
        this.state = project.getState();
        this.address1 = project.address1;
        this.address2 = project.getAddress2();
        this.statusText = project.getStatusText();
        if (project.getProjectStage() != null) {
            this.projectStage = realm.copyToRealmOrUpdate(project.getProjectStage());
        }
        this.estLow = project.getEstLow();
        this.bidDate = project.getBidDate();
        this.city = project.getCity();
        this.primaryProjectTypeId = project.getPrimaryProjectTypeId();
        this.zip5 = project.getZip5();
        this.cnProjectUrl = project.getCnProjectUrl();
        this.title = project.getTitle();
        this.contractNbr = project.getContractNbr();
        this.numberOfFloorsAboveGround = project.getNumberOfFloorsAboveGround();
        this.details = project.getDetails();
        this.dodgeNumber = project.getDodgeNumber();
        this.lastPublishDate = project.getLastPublishDate();
        this.contractType = project.getContractType();
        this.projDlvrySys = project.getProjDlvrySys();
        this.planInd = project.getPlanInd();
        this.dodgeVersion = project.getDodgeVersion();
        if (project.getPrimaryProjectType() != null) {
            this.primaryProjectType = realm.copyToRealmOrUpdate(project.getPrimaryProjectType());
        }
        this.specAvailable = project.getSpecAvailable();
        this.fipsCounty = project.getFipsCounty();
        this.targetFinishDate = project.getTargetFinishDate();
        this.priorPublishDate = project.getPriorPublishDate();
        this.numberOfBuildings = project.getNumberOfBuildings();
        this.statusProjDlvrySys = project.getStatusProjDlvrySys();
        this.estHigh = project.getEstHigh();
        this.stdIncludes = project.getStdIncludes();
        this.currencyType = project.getCurrencyType();
        this.country = project.getCountry();
        if (project.getGeocode() != null) {
            this.geocode = realm.copyToRealm(project.getGeocode());
        }
        this.zipPlus4 = project.getZipPlus4();
        this.projectStageId = project.getProjectStageId();
        this.projectNotes = project.getProjectNotes();
        this.targetStartDate = project.getTargetStartDate();
        this.geoLocationType = project.getGeoLocationType();
        this.county = project.getCounty();
        this.firstPublishDate = project.getFirstPublishDate();
        this.addendaInd = project.getAddendaInd();
        this.availableFrom = project.getAvailableFrom();
        this.geoType = project.getGeoType();
        this.unionDesignation = project.getUnionDesignation();
        this.bidTimeZone = project.getBidTimeZone();
        this.bondInformation = project.getBondInformation();
        this.ownerClass = project.getOwnerClass();
        if (project.getContacts() != null) {

            List<Contact> managedContacts = realm.copyToRealmOrUpdate(project.getContacts());
            for (Contact contact : managedContacts) {
                this.contacts.add(contact);
            }
        }
        if (project.getBids() != null) {

            List<Bid> managedBids = realm.copyToRealmOrUpdate(project.getBids());
            for (Bid bid : managedBids) {
                this.bids.add(bid);
            }
        }
        if (project.getUpdates() != null) {

            List<ActivityUpdate> managedUpdates = realm.copyToRealmOrUpdate(project.getUpdates());
            for (ActivityUpdate update : managedUpdates) {
                this.updates.add(update);
            }
        }

        if (isHidden != null) {

            this.hidden = isHidden.booleanValue();
        }

        if (mbsItem != null) {

            this.mbsItem = mbsItem.booleanValue();
        }

        if (mraItem != null) {

            this.mraItem = mraItem.booleanValue();
        }

        if (mruItem != null) {

            this.mruItem = mruItem.booleanValue();
        }

    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", dodgeNumber='" + dodgeNumber + '\'' +
                ", dodgeVersion='" + dodgeVersion + '\'' +
                ", title='" + title + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip5='" + zip5 + '\'' +
                ", zipPlus4='" + zipPlus4 + '\'' +
                ", county='" + county + '\'' +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", country='" + country + '\'' +
                ", estLow=" + estLow +
                ", estHigh=" + estHigh +
                ", bidSubmitTo='" + bidSubmitTo + '\'' +
                ", statusText='" + statusText + '\'' +
                ", projectStage=" + projectStage +
                ", bidDate=" + bidDate +
                ", primaryProjectTypeId=" + primaryProjectTypeId +
                ", cnProjectUrl='" + cnProjectUrl + '\'' +
                ", contractNbr='" + contractNbr + '\'' +
                ", numberOfFloorsAboveGround='" + numberOfFloorsAboveGround + '\'' +
                ", details='" + details + '\'' +
                ", lastPublishDate=" + lastPublishDate +
                ", contractType='" + contractType + '\'' +
                ", projDlvrySys='" + projDlvrySys + '\'' +
                ", planInd='" + planInd + '\'' +
                ", primaryProjectType=" + primaryProjectType +
                ", specAvailable='" + specAvailable + '\'' +
                ", targetFinishDate=" + targetFinishDate +
                ", priorPublishDate=" + priorPublishDate +
                ", numberOfBuildings='" + numberOfBuildings + '\'' +
                ", statusProjDlvrySys='" + statusProjDlvrySys + '\'' +
                ", stdIncludes='" + stdIncludes + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", geocode=" + geocode +
                ", projectStageId='" + projectStageId + '\'' +
                ", projectNotes='" + projectNotes + '\'' +
                ", targetStartDate=" + targetStartDate +
                ", geoLocationType='" + geoLocationType + '\'' +
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
                ", updates=" + updates +
                ", userNotes=" + userNotes +
                ", images=" + images +
                ", hidden=" + hidden +
                ", recentUpdate=" + recentUpdate +
                ", mbsItem=" + mbsItem +
                ", mraItem=" + mraItem +
                ", mruItem=" + mruItem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != project.id) return false;
        if (Double.compare(project.estLow, estLow) != 0) return false;
        if (Double.compare(project.estHigh, estHigh) != 0) return false;
        if (primaryProjectTypeId != project.primaryProjectTypeId) return false;
        if (hidden != project.hidden) return false;
        if (mbsItem != project.mbsItem) return false;
        if (mraItem != project.mraItem) return false;
        if (mruItem != project.mruItem) return false;
        if (dodgeNumber != null ? !dodgeNumber.equals(project.dodgeNumber) : project.dodgeNumber != null)
            return false;
        if (dodgeVersion != null ? !dodgeVersion.equals(project.dodgeVersion) : project.dodgeVersion != null)
            return false;
        if (title != null ? !title.equals(project.title) : project.title != null) return false;
        if (address1 != null ? !address1.equals(project.address1) : project.address1 != null)
            return false;
        if (address2 != null ? !address2.equals(project.address2) : project.address2 != null)
            return false;
        if (city != null ? !city.equals(project.city) : project.city != null) return false;
        if (state != null ? !state.equals(project.state) : project.state != null) return false;
        if (zip5 != null ? !zip5.equals(project.zip5) : project.zip5 != null) return false;
        if (zipPlus4 != null ? !zipPlus4.equals(project.zipPlus4) : project.zipPlus4 != null)
            return false;
        if (county != null ? !county.equals(project.county) : project.county != null) return false;
        if (fipsCounty != null ? !fipsCounty.equals(project.fipsCounty) : project.fipsCounty != null)
            return false;
        if (country != null ? !country.equals(project.country) : project.country != null)
            return false;
        if (bidSubmitTo != null ? !bidSubmitTo.equals(project.bidSubmitTo) : project.bidSubmitTo != null)
            return false;
        if (statusText != null ? !statusText.equals(project.statusText) : project.statusText != null)
            return false;
        if (projectStage != null ? !projectStage.equals(project.projectStage) : project.projectStage != null)
            return false;
        if (bidDate != null ? !bidDate.equals(project.bidDate) : project.bidDate != null)
            return false;
        if (cnProjectUrl != null ? !cnProjectUrl.equals(project.cnProjectUrl) : project.cnProjectUrl != null)
            return false;
        if (contractNbr != null ? !contractNbr.equals(project.contractNbr) : project.contractNbr != null)
            return false;
        if (numberOfFloorsAboveGround != null ? !numberOfFloorsAboveGround.equals(project.numberOfFloorsAboveGround) : project.numberOfFloorsAboveGround != null)
            return false;
        if (details != null ? !details.equals(project.details) : project.details != null)
            return false;
        if (lastPublishDate != null ? !lastPublishDate.equals(project.lastPublishDate) : project.lastPublishDate != null)
            return false;
        if (contractType != null ? !contractType.equals(project.contractType) : project.contractType != null)
            return false;
        if (projDlvrySys != null ? !projDlvrySys.equals(project.projDlvrySys) : project.projDlvrySys != null)
            return false;
        if (planInd != null ? !planInd.equals(project.planInd) : project.planInd != null)
            return false;
        if (primaryProjectType != null ? !primaryProjectType.equals(project.primaryProjectType) : project.primaryProjectType != null)
            return false;
        if (specAvailable != null ? !specAvailable.equals(project.specAvailable) : project.specAvailable != null)
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
        if (geocode != null ? !geocode.equals(project.geocode) : project.geocode != null)
            return false;
        if (projectStageId != null ? !projectStageId.equals(project.projectStageId) : project.projectStageId != null)
            return false;
        if (projectNotes != null ? !projectNotes.equals(project.projectNotes) : project.projectNotes != null)
            return false;
        if (targetStartDate != null ? !targetStartDate.equals(project.targetStartDate) : project.targetStartDate != null)
            return false;
        if (geoLocationType != null ? !geoLocationType.equals(project.geoLocationType) : project.geoLocationType != null)
            return false;
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
        if (ownerClass != null ? !ownerClass.equals(project.ownerClass) : project.ownerClass != null)
            return false;
        if (contacts != null ? !contacts.equals(project.contacts) : project.contacts != null)
            return false;
        if (bids != null ? !bids.equals(project.bids) : project.bids != null) return false;
        if (updates != null ? !updates.equals(project.updates) : project.updates != null)
            return false;
        if (userNotes != null ? !userNotes.equals(project.userNotes) : project.userNotes != null)
            return false;
        if (images != null ? !images.equals(project.images) : project.images != null) return false;
        return recentUpdate != null ? recentUpdate.equals(project.recentUpdate) : project.recentUpdate == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (dodgeNumber != null ? dodgeNumber.hashCode() : 0);
        result = 31 * result + (dodgeVersion != null ? dodgeVersion.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip5 != null ? zip5.hashCode() : 0);
        result = 31 * result + (zipPlus4 != null ? zipPlus4.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (fipsCounty != null ? fipsCounty.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        temp = Double.doubleToLongBits(estLow);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(estHigh);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (bidSubmitTo != null ? bidSubmitTo.hashCode() : 0);
        result = 31 * result + (statusText != null ? statusText.hashCode() : 0);
        result = 31 * result + (projectStage != null ? projectStage.hashCode() : 0);
        result = 31 * result + (bidDate != null ? bidDate.hashCode() : 0);
        result = 31 * result + (int) (primaryProjectTypeId ^ (primaryProjectTypeId >>> 32));
        result = 31 * result + (cnProjectUrl != null ? cnProjectUrl.hashCode() : 0);
        result = 31 * result + (contractNbr != null ? contractNbr.hashCode() : 0);
        result = 31 * result + (numberOfFloorsAboveGround != null ? numberOfFloorsAboveGround.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (lastPublishDate != null ? lastPublishDate.hashCode() : 0);
        result = 31 * result + (contractType != null ? contractType.hashCode() : 0);
        result = 31 * result + (projDlvrySys != null ? projDlvrySys.hashCode() : 0);
        result = 31 * result + (planInd != null ? planInd.hashCode() : 0);
        result = 31 * result + (primaryProjectType != null ? primaryProjectType.hashCode() : 0);
        result = 31 * result + (specAvailable != null ? specAvailable.hashCode() : 0);
        result = 31 * result + (targetFinishDate != null ? targetFinishDate.hashCode() : 0);
        result = 31 * result + (priorPublishDate != null ? priorPublishDate.hashCode() : 0);
        result = 31 * result + (numberOfBuildings != null ? numberOfBuildings.hashCode() : 0);
        result = 31 * result + (statusProjDlvrySys != null ? statusProjDlvrySys.hashCode() : 0);
        result = 31 * result + (stdIncludes != null ? stdIncludes.hashCode() : 0);
        result = 31 * result + (currencyType != null ? currencyType.hashCode() : 0);
        result = 31 * result + (geocode != null ? geocode.hashCode() : 0);
        result = 31 * result + (projectStageId != null ? projectStageId.hashCode() : 0);
        result = 31 * result + (projectNotes != null ? projectNotes.hashCode() : 0);
        result = 31 * result + (targetStartDate != null ? targetStartDate.hashCode() : 0);
        result = 31 * result + (geoLocationType != null ? geoLocationType.hashCode() : 0);
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
        result = 31 * result + (updates != null ? updates.hashCode() : 0);
        result = 31 * result + (userNotes != null ? userNotes.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (hidden ? 1 : 0);
        result = 31 * result + (recentUpdate != null ? recentUpdate.hashCode() : 0);
        result = 31 * result + (mbsItem ? 1 : 0);
        result = 31 * result + (mraItem ? 1 : 0);
        result = 31 * result + (mruItem ? 1 : 0);
        return result;
    }
}