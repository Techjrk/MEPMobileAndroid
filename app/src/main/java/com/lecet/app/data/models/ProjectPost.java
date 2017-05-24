package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasonm
 * See http://lecet.dt-staging.com/explorer/#!/Project/Project_createInstance
 */

public class ProjectPost {

    @SerializedName("title")
    private String title;

    @SerializedName("public")
    private boolean isPublic;

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
    private int estLow;

    @SerializedName("estHigh")
    private int estHigh;

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

    //@SerializedName("geocode")
    //private String geocode;     //TODO - required type in API is GeoPoint

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

    @SerializedName("id")
    private long id;

    @SerializedName("primaryProjectTypeId")
    private int primaryProjectTypeId;

    @SerializedName("projectStageId")
    private int projectStageId;

    @SerializedName("jurisdictionCityId")
    private int jurisdictionCityId;

    //TODO - add GeoPoint obj

    //@SerializedName("latitude")
    //private double latitude;

    //@SerializedName("longitude")
    //private double longitude;

    @SerializedName("geocode")
    private Geocode geocode;

    public ProjectPost(String title, double latitude, double longitude) {
        this.title = title;
        //this.latitude = latitude;
        //this.longitude = longitude;

        this.geocode = new Geocode();
        geocode.setLat(latitude);
        geocode.setLng(longitude);
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
                ", geoCode=" + geocode +
                ", isPublic=" + isPublic +
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
                ", id=" + id +
                ", primaryProjectTypeId=" + primaryProjectTypeId +
                ", projectStageId=" + projectStageId +
                ", jurisdictionCityId=" + jurisdictionCityId +
                '}';
    }



}
