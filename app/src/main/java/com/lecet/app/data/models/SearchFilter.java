package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchFilter {
 //   @SerializedName("searchFilter")
 //   private SearchFilter searchFilter;

 //   @SerializedName("jurisdictions")
 //   private SearchFilter jurisdictions;
/* TODO: Will check first if the string type is enough for each of the search filter type. If not, the comment class type listed below will be created and will be used.
    private ProjectValue projectValue ;
    private BuildingOrHighway buildingOrHighway ;
    private OwnerType ownerType ;
    private ProjectTypeId projectTypeId ;
    private ProjectStageId projectStageId ;
    private int updatedInLast ;
    private WorkTypeId workTypeId ;
    private Jurisdictions jurisdictions ;
    private List<String> deepJurisdictionId ;
    private Valuation valuation ;
    private UpdatedWithin updatedWithin ;
    private ProjectLocation projectLocation ;
    private String city ;

*/
    @SerializedName("projectValue")
    private Object projectValue ;

    @SerializedName("buildingOrHighway")
    private Object buildingOrHighway ;

    @SerializedName("ownerType")
    private Object ownerType ;

    @SerializedName("projectTypeId")
    private Object projectTypeId ;

    @SerializedName("projectStageId")
    private Object projectStageId ;

    @SerializedName("updatedInLast")
    private int updatedInLast ;

    @SerializedName("workTypeId")
    private Object workTypeId ;

    @SerializedName("jurisdictions")
    private Object jurisdictions ;

    @SerializedName("deepJurisdictionId")
    private List<String> deepJurisdictionId ;

    @SerializedName("valuation")
    private Object valuation ;

    @SerializedName("updatedWithin")
    private Object updatedWithin ;

    @SerializedName("projectLocation")
    private Object projectLocation ;

    @SerializedName("city")
    private String city ;

    public Object getProjectValue() {
        return projectValue;
    }

    public Object getBuildingOrHighway() {
        return buildingOrHighway;
    }

    public Object getOwnerType() {
        return ownerType;
    }

    public Object getProjectTypeId() {
        return projectTypeId;
    }

    public Object getProjectStageId() {
        return projectStageId;
    }

    public int getUpdatedInLast() {
        return updatedInLast;
    }

    public Object getWorkTypeId() {
        return workTypeId;
    }

    public Object getJurisdictions() {
        return jurisdictions;
    }

    public List<String> getDeepJurisdictionId() {
        return deepJurisdictionId;
    }

    public Object getValuation() {
        return valuation;
    }

    public Object getUpdatedWithin() {
        return updatedWithin;
    }

    public Object getProjectLocation() {
        return projectLocation;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "projectValue='" + projectValue + '\'' +
                ", buildingOrHighway='" + buildingOrHighway + '\'' +
                ", ownerType='" + ownerType + '\'' +
                ", projectTypeId='" + projectTypeId + '\'' +
                ", projectStageId='" + projectStageId + '\'' +
                ", updatedInLast=" + updatedInLast +
                ", workTypeId='" + workTypeId + '\'' +
                ", jurisdictions='" + jurisdictions + '\'' +
                ", deepJurisdictionId=" + deepJurisdictionId +
                ", valuation='" + valuation + '\'' +
                ", updatedWithin='" + updatedWithin + '\'' +
                ", projectLocation='" + projectLocation + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilter that = (SearchFilter) o;

        if (updatedInLast != that.updatedInLast) return false;
        if (projectValue != null ? !projectValue.equals(that.projectValue) : that.projectValue != null)
            return false;
        if (buildingOrHighway != null ? !buildingOrHighway.equals(that.buildingOrHighway) : that.buildingOrHighway != null)
            return false;
        if (ownerType != null ? !ownerType.equals(that.ownerType) : that.ownerType != null)
            return false;
        if (projectTypeId != null ? !projectTypeId.equals(that.projectTypeId) : that.projectTypeId != null)
            return false;
        if (projectStageId != null ? !projectStageId.equals(that.projectStageId) : that.projectStageId != null)
            return false;
        if (workTypeId != null ? !workTypeId.equals(that.workTypeId) : that.workTypeId != null)
            return false;
        if (jurisdictions != null ? !jurisdictions.equals(that.jurisdictions) : that.jurisdictions != null)
            return false;
        if (deepJurisdictionId != null ? !deepJurisdictionId.equals(that.deepJurisdictionId) : that.deepJurisdictionId != null)
            return false;
        if (valuation != null ? !valuation.equals(that.valuation) : that.valuation != null)
            return false;
        if (updatedWithin != null ? !updatedWithin.equals(that.updatedWithin) : that.updatedWithin != null)
            return false;
        if (projectLocation != null ? !projectLocation.equals(that.projectLocation) : that.projectLocation != null)
            return false;
        return city != null ? city.equals(that.city) : that.city == null;

    }

    @Override
    public int hashCode() {
        int result = projectValue != null ? projectValue.hashCode() : 0;
        result = 31 * result + (buildingOrHighway != null ? buildingOrHighway.hashCode() : 0);
        result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
        result = 31 * result + (projectTypeId != null ? projectTypeId.hashCode() : 0);
        result = 31 * result + (projectStageId != null ? projectStageId.hashCode() : 0);
        result = 31 * result + updatedInLast;
        result = 31 * result + (workTypeId != null ? workTypeId.hashCode() : 0);
        result = 31 * result + (jurisdictions != null ? jurisdictions.hashCode() : 0);
        result = 31 * result + (deepJurisdictionId != null ? deepJurisdictionId.hashCode() : 0);
        result = 31 * result + (valuation != null ? valuation.hashCode() : 0);
        result = 31 * result + (updatedWithin != null ? updatedWithin.hashCode() : 0);
        result = 31 * result + (projectLocation != null ? projectLocation.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    /*

    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public SearchFilter getJurisdictions() {
        return jurisdictions;
    }

*/


}

