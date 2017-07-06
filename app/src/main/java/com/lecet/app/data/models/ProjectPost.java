package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lecet.app.BR;
import com.lecet.app.data.api.request.GeocodeRequest;

/**
 * Created by jasonm
 * See http://lecet.dt-staging.com/explorer/#!/Project/Project_createInstance
 */

public class ProjectPost extends BaseObservable {

    @SerializedName("title")
    private String title;

    @SerializedName("dodgeNumber")
    private String dodgeNumber;

    @SerializedName("dodgeVersion")
    private long dodgeVersion;

    @SerializedName("firstPublishDate")
    private String firstPublishDate;

    @SerializedName("lastPublishDate")
    private String lastPublishDate;

    @SerializedName("priorPublishDate")
    private String priorPublishDate;

    @SerializedName("cnProjectUrl")
    private String cnProjectUrl;

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

    @SerializedName("country")
    private String country;

    @SerializedName("zip5")
    private String zip5;

    @SerializedName("zipPlus4")
    private String zipPlus4;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("statusProjDlvrySys")
    private String statusProjDlvrySys;

    @SerializedName("currencyType")
    private String currencyType;

    @SerializedName("estLow")
    private double estLow;

    @SerializedName("estHigh")
    private double estHigh;

    @SerializedName("bidDate")
    private String bidDate;

    @SerializedName("bidTimeZone")
    private String bidTimeZone;

    @SerializedName("bidSubmitTo")
    private String bidSubmitTo;

    @SerializedName("contractNbr")
    private String contractNbr;

    @SerializedName("projDlvrySys")
    private String projDlvrySys;

    @SerializedName("targetStartDate")
    private String targetStartDate;

    @SerializedName("targetFinishDate")
    private String targetFinishDate;

    @SerializedName("ownerClass")
    private String ownerClass;

    @SerializedName("availableFrom")
    private String availableFrom;

    @SerializedName("addendaInd")
    private boolean addendaInd;

    @SerializedName("planInd")
    private boolean planInd;

    @SerializedName("specAvailable")
    private boolean specAvailable;

    @SerializedName("details")
    private String details;

    @SerializedName("bondInformation")
    private String bondInformation;

    @SerializedName("geoLocationType")
    private String geoLocationType;    // takes ROOFTOP, RANGE_INTERPOLATED, GEOMETRIC_CENTER, or APPROXIMATE

    @SerializedName("geoType")
    private String geoType;

    @SerializedName("unionDesignation")
    private String unionDesignation;   //takes U, N, NA or null

    @SerializedName("projectNotes")
    private String projectNotes;

    @SerializedName("stdIncludes")
    private String stdIncludes;

    @SerializedName("contractType")
    private String contractType;

    @SerializedName("numberOfBuildings")
    private int numberOfBuildings;

    @SerializedName("numberOfFloorsAboveGround")
    private int numberOfFloorsAboveGround;

    /*@SerializedName("id")
    private long id;*/

    @SerializedName("primaryProjectTypeId")
    private long primaryProjectTypeId;

    @SerializedName("projectStageId")
    private int projectStageId;

    @SerializedName("jurisdictionCityId")
    private int jurisdictionCityId;

    @SerializedName("geocode")
    private GeocodeRequest geocode;


    public ProjectPost(double latitude, double longitude) {
        this.geocode = new GeocodeRequest();
        geocode.setLat(latitude);
        geocode.setLng(longitude);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDodgeNumber() {
        return dodgeNumber;
    }

    public void setDodgeNumber(String dodgeNumber) {
        this.dodgeNumber = dodgeNumber;
    }

    public long getDodgeVersion() {
        return dodgeVersion;
    }

    public void setDodgeVersion(long dodgeVersion) {
        this.dodgeVersion = dodgeVersion;
    }

    public String getFirstPublishDate() {
        return firstPublishDate;
    }

    public void setFirstPublishDate(String firstPublishDate) {
        this.firstPublishDate = firstPublishDate;
    }

    public String getLastPublishDate() {
        return lastPublishDate;
    }

    public void setLastPublishDate(String lastPublishDate) {
        this.lastPublishDate = lastPublishDate;
    }

    public String getPriorPublishDate() {
        return priorPublishDate;
    }

    public void setPriorPublishDate(String priorPublishDate) {
        this.priorPublishDate = priorPublishDate;
    }

    public String getCnProjectUrl() {
        return cnProjectUrl;
    }

    public void setCnProjectUrl(String cnProjectUrl) {
        this.cnProjectUrl = cnProjectUrl;
    }

    @Bindable
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
        notifyPropertyChanged(BR.address1);
    }

    @Bindable
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
        notifyPropertyChanged(BR.address2);
    }

    @Bindable
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
        notifyPropertyChanged(BR.county);
    }

    @Bindable
    public String getFipsCounty() {
        return fipsCounty;
    }

    public void setFipsCounty(String fipsCounty) {
        this.fipsCounty = fipsCounty;
        notifyPropertyChanged(BR.fipsCounty);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }

    @Bindable
    public String getZip5() {
        return zip5;
    }

    public void setZip5(String zip5) {
        this.zip5 = zip5;
        notifyPropertyChanged(BR.zip5);
    }

    @Bindable
    public String getZipPlus4() {
        return zipPlus4;
    }

    public void setZipPlus4(String zipPlus4) {
        this.zipPlus4 = zipPlus4;
        notifyPropertyChanged(BR.zipPlus4);
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusProjDlvrySys() {
        return statusProjDlvrySys;
    }

    public void setStatusProjDlvrySys(String statusProjDlvrySys) {
        this.statusProjDlvrySys = statusProjDlvrySys;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Bindable
    public double getEstLow() {
        return estLow;
    }

    public void setEstLow(double estLow) {
        this.estLow = estLow;
        notifyPropertyChanged(BR.estLow);
    }

    public String getEstLowStr() {
        if(this.estLow == 0) return "0";

        return Double.toString(Math.floor(estLow));
    }

    public void setEstLowStr(String estLow) {
        if (estLow != null && !estLow.isEmpty()) {
            this.estLow = Double.parseDouble(estLow);
        }
    }

    public double getEstHigh() {
        return estHigh;
    }

    public void setEstHigh(int estHigh) {
        this.estHigh = estHigh;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }

    public String getBidTimeZone() {
        return bidTimeZone;
    }

    public void setBidTimeZone(String bidTimeZone) {
        this.bidTimeZone = bidTimeZone;
    }

    public String getBidSubmitTo() {
        return bidSubmitTo;
    }

    public void setBidSubmitTo(String bidSubmitTo) {
        this.bidSubmitTo = bidSubmitTo;
    }

    public String getContractNbr() {
        return contractNbr;
    }

    public void setContractNbr(String contractNbr) {
        this.contractNbr = contractNbr;
    }

    public String getProjDlvrySys() {
        return projDlvrySys;
    }

    public void setProjDlvrySys(String projDlvrySys) {
        this.projDlvrySys = projDlvrySys;
    }

    @Bindable
    public String getTargetStartDate() {
        return targetStartDate;
    }

    public void setTargetStartDate(String targetStartDate) {
        this.targetStartDate = targetStartDate;
        notifyPropertyChanged(BR.targetStartDate);
    }

    public String getTargetFinishDate() {
        return targetFinishDate;
    }

    public void setTargetFinishDate(String targetFinishDate) {
        this.targetFinishDate = targetFinishDate;
    }

    public String getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(String ownerClass) {
        this.ownerClass = ownerClass;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public boolean isAddendaInd() {
        return addendaInd;
    }

    public void setAddendaInd(boolean addendaInd) {
        this.addendaInd = addendaInd;
    }

    public boolean isPlanInd() {
        return planInd;
    }

    public void setPlanInd(boolean planInd) {
        this.planInd = planInd;
    }

    public boolean isSpecAvailable() {
        return specAvailable;
    }

    public void setSpecAvailable(boolean specAvailable) {
        this.specAvailable = specAvailable;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBondInformation() {
        return bondInformation;
    }

    public void setBondInformation(String bondInformation) {
        this.bondInformation = bondInformation;
    }

    public String getGeoLocationType() {
        return geoLocationType;
    }

    public void setGeoLocationType(String geoLocationType) {
        this.geoLocationType = geoLocationType;
    }

    public String getGeoType() {
        return geoType;
    }

    public void setGeoType(String geoType) {
        this.geoType = geoType;
    }

    public String getUnionDesignation() {
        return unionDesignation;
    }

    public void setUnionDesignation(String unionDesignation) {
        this.unionDesignation = unionDesignation;
    }

    public String getProjectNotes() {
        return projectNotes;
    }

    public void setProjectNotes(String projectNotes) {
        this.projectNotes = projectNotes;
    }

    public String getStdIncludes() {
        return stdIncludes;
    }

    public void setStdIncludes(String stdIncludes) {
        this.stdIncludes = stdIncludes;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public int getNumberOfBuildings() {
        return numberOfBuildings;
    }

    public void setNumberOfBuildings(int numberOfBuildings) {
        this.numberOfBuildings = numberOfBuildings;
    }

    public int getNumberOfFloorsAboveGround() {
        return numberOfFloorsAboveGround;
    }

    public void setNumberOfFloorsAboveGround(int numberOfFloorsAboveGround) {
        this.numberOfFloorsAboveGround = numberOfFloorsAboveGround;
    }

    @Bindable
    public long getPrimaryProjectTypeId() {
        return primaryProjectTypeId;
    }

    public void setPrimaryProjectTypeId(long primaryProjectTypeId) {
        this.primaryProjectTypeId = primaryProjectTypeId;
        notifyPropertyChanged(BR.primaryProjectTypeId);
    }

    @Bindable
    public int getProjectStageId() {
        return projectStageId;
    }

    public void setProjectStageId(int projectStageId) {
        this.projectStageId = projectStageId;
        notifyPropertyChanged(BR.projectStageId);
    }

    @Bindable
    public int getJurisdictionCityId() {
        return jurisdictionCityId;
    }

    public void setJurisdictionCityId(int jurisdictionCityId) {
        this.jurisdictionCityId = jurisdictionCityId;
        notifyPropertyChanged(BR.jurisdictionCityId);
    }

    public GeocodeRequest getGeocode() {
        return geocode;
    }

    public void setGeocode(GeocodeRequest geocode) {
        this.geocode = geocode;
    }

    public void setGeocode(Geocode geocode){
        if(geocode == null){
            this.geocode = null;
        }
        this.geocode.setLng(geocode.getLng());
        this.geocode.setLat(geocode.getLat());
    }

    public String toConvertedString() {
        return "ProjectPost{" +
                "\"title\":\"" + title + "\"" +
                ",\"geocode\":{\"lat\":" + geocode.getLat() + ",\"lng\":" + geocode.getLng() + "}" +
                "}";

    }

    @Override
    public String toString() {
        return "ProjectPost{" +
                "title='" + title + '\'' +
                ", geocode=" + geocode +
                ", dodgeNumber='" + dodgeNumber + '\'' +
                ", dodgeVersion=" + dodgeVersion +
                ", firstPublishDate='" + firstPublishDate + '\'' +
                ", lastPublishDate='" + lastPublishDate + '\'' +
                ", priorPublishDate='" + priorPublishDate + '\'' +
                ", cnProjectUrl='" + cnProjectUrl + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", county='" + county + '\'' +
                ", fipsCounty='" + fipsCounty + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zip5='" + zip5 + '\'' +
                ", zipPlus4='" + zipPlus4 + '\'' +
                ", statusText='" + statusText + '\'' +
                ", statusProjDlvrySys='" + statusProjDlvrySys + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", estLow=" + estLow +
                ", estHigh=" + estHigh +
                ", bidDate='" + bidDate + '\'' +
                ", bidTimeZone='" + bidTimeZone + '\'' +
                ", bidSubmitTo='" + bidSubmitTo + '\'' +
                ", contractNbr='" + contractNbr + '\'' +
                ", projDlvrySys='" + projDlvrySys + '\'' +
                ", targetStartDate='" + targetStartDate + '\'' +
                ", targetFinishDate='" + targetFinishDate + '\'' +
                ", ownerClass='" + ownerClass + '\'' +
                ", availableFrom='" + availableFrom + '\'' +
                ", addendaInd=" + addendaInd +
                ", planInd=" + planInd +
                ", specAvailable=" + specAvailable +
                ", details='" + details + '\'' +
                ", bondInformation='" + bondInformation + '\'' +
                ", geoLocationType='" + geoLocationType + '\'' +
                ", geoType='" + geoType + '\'' +
                ", unionDesignation='" + unionDesignation + '\'' +
                ", projectNotes='" + projectNotes + '\'' +
                ", stdIncludes='" + stdIncludes + '\'' +
                ", contractType='" + contractType + '\'' +
                ", numberOfBuildings=" + numberOfBuildings +
                ", numberOfFloorsAboveGround=" + numberOfFloorsAboveGround +
                ", primaryProjectTypeId=" + primaryProjectTypeId +
                ", projectStageId=" + projectStageId +
                ", jurisdictionCityId=" + jurisdictionCityId +
                ", fipsCountyId=" + jurisdictionCityId +
                '}';
    }


}

