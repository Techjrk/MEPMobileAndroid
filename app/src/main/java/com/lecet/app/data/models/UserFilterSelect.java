package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by getdevsinc on 8/17/17.
 */

public class UserFilterSelect extends RealmObject{
    @PrimaryKey
    @SerializedName("id")
    private long id;

    //Note: Display values for the project fields (with Select-end as name). Checked/actual value item uses simple variable name.
    @SerializedName("locationSelect")
    private String locationSelect;

    @SerializedName("typeIdSelect")
    private String typeIdSelect;

    @SerializedName("typeIdInt")
    private String typeIdInt; //checked values for type id

    @SerializedName("valueSelect")
    private String valueSelect;

    @SerializedName("valueMin")
    private String valueMin; //actual value for value min

    @SerializedName("valueMax")
    private String valueMax; //actual value for value max

    @SerializedName("updatedWithinSelect")
    private String updatedWithinSelect;

    @SerializedName("updatedWithin")
    private String updatedWithin; //checked values for updated within

    @SerializedName("biddingWithinSelect")
    private String biddingWithinSelect;

    @SerializedName("biddingWithin")
    private String biddingWithin; //checked values for bidding within

    @SerializedName("jurisdictionSelect")
    private String jurisdictionSelect;

    @SerializedName("stageSelect")
    private String stageSelect;

    @SerializedName("bhSelect")
    private String bhSelect;

    @SerializedName("bh")
    private String bh; //checked values for BH

    @SerializedName("ownerTypeSelect")
    private String ownerTypeSelect;

    @SerializedName("ownerType")
    private String ownerType; // values for ownerType

    @SerializedName("workTypeSelect")
    private String workTypeSelect;

    @SerializedName("workType")
    private String workType; //checked values for workType

    //Note: Display values for the company fields (with c-first and Select-end as name)

    @SerializedName("cLocationSelect")
    private String cLocationSelect;

    @SerializedName("cValueSelect")
    private String cValueSelect; //real values for value

    @SerializedName("cJurisdictionSelect")
    private String cJurisdictionSelect;

    @SerializedName("cBiddingWithinSelect")
    private String cBiddingWithinSelect;

    @SerializedName("cTypeSelect")
    private String cTypeSelect;

    //Note: Final filter result values after processing the selected filter

    @SerializedName("updatedWithinResult")
    private String updatedWithinResult;

    @SerializedName("biddingWithinResult")
    private String biddingWithinResult;

    @SerializedName("valueResult")
    private String valueResult;

    @SerializedName("typeResult")
    private String typeResult;

    @SerializedName("jurisdictionResult")
    private String jurisdictionResult;

    @SerializedName("stageResult")
    private String stageResult;

    @SerializedName("bhResult")
    private String bhResult;

    @SerializedName("ownerTypeResult")
    private String ownerTypeResult;

    @SerializedName("workTypeResult")
    private String workTypeResult;

    //Note: This is for single & multi-selected complex items


    //A. Project Type

    @SerializedName("typeKey")
    private RealmList<UserFilterName> typeKey;

    @SerializedName("typeValue")
    private RealmList<UserFilterName> typeValue;

    //B. Stage

    @SerializedName("stageKey")
    private RealmList<UserFilterName> stageKey;

    @SerializedName("stageValue")
    private RealmList<UserFilterName> stageValue;

    //C. Jurisdiction

    @SerializedName("jurisdictionKey")
    private RealmList<UserFilterName> jurisdictionKey;

    @SerializedName("jurisdictionValue")
    private RealmList<UserFilterName> jurisdictionValue;

    //Note: instance method
    public void clear() {
        setBh("");
        setBhResult("");
        setBhSelect("");
        setBiddingWithin("");
        setBiddingWithinResult("");
        setBiddingWithinSelect("");
        setcBiddingWithinSelect("");
        setcJurisdictionSelect("");
        setcLocationSelect("");
        setcTypeSelect("");
        setcValueSelect("");
        setJurisdictionKey(null);
        setJurisdictionResult("");
        setJurisdictionSelect("");
        setJurisdictionValue(null);
        setLocationSelect("");
        setOwnerType("");
        setOwnerTypeResult("");
        setOwnerTypeSelect("");
        setStageKey(null);
        setStageResult("");
        setStageSelect("");
        setStageValue(null);
        setTypeIdInt("");
        setTypeIdSelect("");
        setTypeKey(null);
        setTypeResult("");
        setTypeValue(null);
        setUpdatedWithin("");
        setUpdatedWithinResult("");
        setUpdatedWithinSelect("");
        setValueMax("");
        setValueMin("");
        setValueResult("");
        setValueSelect("");
        setWorkType("");
        setWorkTypeResult("");
        setWorkTypeSelect("");
    }

    //Note: Setter/Getter methods

    public String getLocationSelect() {
        return locationSelect;
    }

    public void setLocationSelect(String locationSelect) {
        this.locationSelect = locationSelect;
    }

    public String getTypeIdSelect() {
        return typeIdSelect;
    }

    public void setTypeIdSelect(String typeIdSelect) {
        this.typeIdSelect = typeIdSelect;
    }

    public String getTypeIdInt() {
        return typeIdInt;
    }

    public void setTypeIdInt(String typeIdInt) {
        this.typeIdInt = typeIdInt;
    }

    public String getValueSelect() {
        return valueSelect;
    }

    public void setValueSelect(String valueSelect) {
        this.valueSelect = valueSelect;
    }

    public String getValueMin() {
        return valueMin;
    }

    public void setValueMin(String valueMin) {
        this.valueMin = valueMin;
    }

    public String getValueMax() {
        return valueMax;
    }

    public void setValueMax(String valueMax) {
        this.valueMax = valueMax;
    }

    public String getUpdatedWithinSelect() {
        return updatedWithinSelect;
    }

    public void setUpdatedWithinSelect(String updatedWithinSelect) {
        this.updatedWithinSelect = updatedWithinSelect;
    }

    public String getUpdatedWithin() {
        return updatedWithin;
    }

    public void setUpdatedWithin(String updatedWithin) {
        this.updatedWithin = updatedWithin;
    }

    public String getBiddingWithinSelect() {
        return biddingWithinSelect;
    }

    public void setBiddingWithinSelect(String biddingWithinSelect) {
        this.biddingWithinSelect = biddingWithinSelect;
    }

    public String getBiddingWithin() {
        return biddingWithin;
    }

    public void setBiddingWithin(String biddingWithin) {
        this.biddingWithin = biddingWithin;
    }

    public String getJurisdictionSelect() {
        return jurisdictionSelect;
    }

    public void setJurisdictionSelect(String jurisdictionSelect) {
        this.jurisdictionSelect = jurisdictionSelect;
    }

    public String getStageSelect() {
        return stageSelect;
    }

    public void setStageSelect(String stageSelect) {
        this.stageSelect = stageSelect;
    }

    public String getBhSelect() {
        return bhSelect;
    }

    public void setBhSelect(String bhSelect) {
        this.bhSelect = bhSelect;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getOwnerTypeSelect() {
        return ownerTypeSelect;
    }

    public void setOwnerTypeSelect(String ownerTypeSelect) {
        this.ownerTypeSelect = ownerTypeSelect;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getWorkTypeSelect() {
        return workTypeSelect;
    }

    public void setWorkTypeSelect(String workTypeSelect) {
        this.workTypeSelect = workTypeSelect;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getcLocationSelect() {
        return cLocationSelect;
    }

    public void setcLocationSelect(String cLocationSelect) {
        this.cLocationSelect = cLocationSelect;
    }

    public String getcValueSelect() {
        return cValueSelect;
    }

    public void setcValueSelect(String cValueSelect) {
        this.cValueSelect = cValueSelect;
    }

    public String getcJurisdictionSelect() {
        return cJurisdictionSelect;
    }

    public void setcJurisdictionSelect(String cJurisdictionSelect) {
        this.cJurisdictionSelect = cJurisdictionSelect;
    }

    public String getcBiddingWithinSelect() {
        return cBiddingWithinSelect;
    }

    public void setcBiddingWithinSelect(String cBiddingWithinSelect) {
        this.cBiddingWithinSelect = cBiddingWithinSelect;
    }

    public String getcTypeSelect() {
        return cTypeSelect;
    }

    public void setcTypeSelect(String cTypeSelect) {
        this.cTypeSelect = cTypeSelect;
    }

    public String getUpdatedWithinResult() {
        return updatedWithinResult;
    }

    public void setUpdatedWithinResult(String updatedWithinResult) {
        this.updatedWithinResult = updatedWithinResult;
    }

    public String getBiddingWithinResult() {
        return biddingWithinResult;
    }

    public void setBiddingWithinResult(String biddingWithinResult) {
        this.biddingWithinResult = biddingWithinResult;
    }

    public String getValueResult() {
        return valueResult;
    }

    public void setValueResult(String valueResult) {
        this.valueResult = valueResult;
    }

    public String getTypeResult() {
        return typeResult;
    }

    public void setTypeResult(String typeResult) {
        this.typeResult = typeResult;
    }

    public String getJurisdictionResult() {
        return jurisdictionResult;
    }

    public void setJurisdictionResult(String jurisdictionResult) {
        this.jurisdictionResult = jurisdictionResult;
    }

    public String getStageResult() {
        return stageResult;
    }

    public void setStageResult(String stageResult) {
        this.stageResult = stageResult;
    }

    public String getBhResult() {
        return bhResult;
    }

    public void setBhResult(String bhResult) {
        this.bhResult = bhResult;
    }

    public String getOwnerTypeResult() {
        return ownerTypeResult;
    }

    public void setOwnerTypeResult(String ownerTypeResult) {
        this.ownerTypeResult = ownerTypeResult;
    }

    public String getWorkTypeResult() {
        return workTypeResult;
    }

    public void setWorkTypeResult(String workTypeResult) {
        this.workTypeResult = workTypeResult;
    }

    public RealmList<UserFilterName> getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(RealmList<UserFilterName> typeKey) {
        this.typeKey = typeKey;
    }

    public RealmList<UserFilterName> getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(RealmList<UserFilterName> typeValue) {
        this.typeValue = typeValue;
    }

    public RealmList<UserFilterName> getStageKey() {
        return stageKey;
    }

    public void setStageKey(RealmList<UserFilterName> stageKey) {
        this.stageKey = stageKey;
    }

    public RealmList<UserFilterName> getStageValue() {
        return stageValue;
    }

    public void setStageValue(RealmList<UserFilterName> stageValue) {
        this.stageValue = stageValue;
    }

    public RealmList<UserFilterName> getJurisdictionKey() {
        return jurisdictionKey;
    }

    public void setJurisdictionKey(RealmList<UserFilterName> jurisdictionKey) {
        this.jurisdictionKey = jurisdictionKey;
    }

    public RealmList<UserFilterName> getJurisdictionValue() {
        return jurisdictionValue;
    }

    public void setJurisdictionValue(RealmList<UserFilterName> jurisdictionValue) {
        this.jurisdictionValue = jurisdictionValue;
    }

    @Override
    public String toString() {
        return "UserFilterSelect{" +
                "id=" + id +
                ", locationSelect='" + locationSelect + '\'' +
                ", typeIdSelect='" + typeIdSelect + '\'' +
                ", typeIdInt='" + typeIdInt + '\'' +
                ", valueSelect='" + valueSelect + '\'' +
                ", valueMin='" + valueMin + '\'' +
                ", valueMax='" + valueMax + '\'' +
                ", updatedWithinSelect='" + updatedWithinSelect + '\'' +
                ", updatedWithin='" + updatedWithin + '\'' +
                ", biddingWithinSelect='" + biddingWithinSelect + '\'' +
                ", biddingWithin='" + biddingWithin + '\'' +
                ", jurisdictionSelect='" + jurisdictionSelect + '\'' +
                ", stageSelect='" + stageSelect + '\'' +
                ", bhSelect='" + bhSelect + '\'' +
                ", bh='" + bh + '\'' +
                ", ownerTypeSelect='" + ownerTypeSelect + '\'' +
                ", ownerType='" + ownerType + '\'' +
                ", workTypeSelect='" + workTypeSelect + '\'' +
                ", workType='" + workType + '\'' +
                ", cLocationSelect='" + cLocationSelect + '\'' +
                ", cValueSelect='" + cValueSelect + '\'' +
                ", cJurisdictionSelect='" + cJurisdictionSelect + '\'' +
                ", cBiddingWithinSelect='" + cBiddingWithinSelect + '\'' +
                ", cTypeSelect='" + cTypeSelect + '\'' +
                ", updatedWithinResult='" + updatedWithinResult + '\'' +
                ", biddingWithinResult='" + biddingWithinResult + '\'' +
                ", valueResult='" + valueResult + '\'' +
                ", typeResult='" + typeResult + '\'' +
                ", jurisdictionResult='" + jurisdictionResult + '\'' +
                ", stageResult='" + stageResult + '\'' +
                ", bhResult='" + bhResult + '\'' +
                ", ownerTypeResult='" + ownerTypeResult + '\'' +
                ", workTypeResult='" + workTypeResult + '\'' +
                ", typeKey=" + typeKey +
                ", typeValue=" + typeValue +
                ", stageKey=" + stageKey +
                ", stageValue=" + stageValue +
                ", jurisdictionKey=" + jurisdictionKey +
                ", jurisdictionValue=" + jurisdictionValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFilterSelect that = (UserFilterSelect) o;

        if (id != that.id) return false;
        if (locationSelect != null ? !locationSelect.equals(that.locationSelect) : that.locationSelect != null)
            return false;
        if (typeIdSelect != null ? !typeIdSelect.equals(that.typeIdSelect) : that.typeIdSelect != null)
            return false;
        if (typeIdInt != null ? !typeIdInt.equals(that.typeIdInt) : that.typeIdInt != null)
            return false;
        if (valueSelect != null ? !valueSelect.equals(that.valueSelect) : that.valueSelect != null)
            return false;
        if (valueMin != null ? !valueMin.equals(that.valueMin) : that.valueMin != null)
            return false;
        if (valueMax != null ? !valueMax.equals(that.valueMax) : that.valueMax != null)
            return false;
        if (updatedWithinSelect != null ? !updatedWithinSelect.equals(that.updatedWithinSelect) : that.updatedWithinSelect != null)
            return false;
        if (updatedWithin != null ? !updatedWithin.equals(that.updatedWithin) : that.updatedWithin != null)
            return false;
        if (biddingWithinSelect != null ? !biddingWithinSelect.equals(that.biddingWithinSelect) : that.biddingWithinSelect != null)
            return false;
        if (biddingWithin != null ? !biddingWithin.equals(that.biddingWithin) : that.biddingWithin != null)
            return false;
        if (jurisdictionSelect != null ? !jurisdictionSelect.equals(that.jurisdictionSelect) : that.jurisdictionSelect != null)
            return false;
        if (stageSelect != null ? !stageSelect.equals(that.stageSelect) : that.stageSelect != null)
            return false;
        if (bhSelect != null ? !bhSelect.equals(that.bhSelect) : that.bhSelect != null)
            return false;
        if (bh != null ? !bh.equals(that.bh) : that.bh != null) return false;
        if (ownerTypeSelect != null ? !ownerTypeSelect.equals(that.ownerTypeSelect) : that.ownerTypeSelect != null)
            return false;
        if (ownerType != null ? !ownerType.equals(that.ownerType) : that.ownerType != null)
            return false;
        if (workTypeSelect != null ? !workTypeSelect.equals(that.workTypeSelect) : that.workTypeSelect != null)
            return false;
        if (workType != null ? !workType.equals(that.workType) : that.workType != null)
            return false;
        if (cLocationSelect != null ? !cLocationSelect.equals(that.cLocationSelect) : that.cLocationSelect != null)
            return false;
        if (cValueSelect != null ? !cValueSelect.equals(that.cValueSelect) : that.cValueSelect != null)
            return false;
        if (cJurisdictionSelect != null ? !cJurisdictionSelect.equals(that.cJurisdictionSelect) : that.cJurisdictionSelect != null)
            return false;
        if (cBiddingWithinSelect != null ? !cBiddingWithinSelect.equals(that.cBiddingWithinSelect) : that.cBiddingWithinSelect != null)
            return false;
        if (cTypeSelect != null ? !cTypeSelect.equals(that.cTypeSelect) : that.cTypeSelect != null)
            return false;
        if (updatedWithinResult != null ? !updatedWithinResult.equals(that.updatedWithinResult) : that.updatedWithinResult != null)
            return false;
        if (biddingWithinResult != null ? !biddingWithinResult.equals(that.biddingWithinResult) : that.biddingWithinResult != null)
            return false;
        if (valueResult != null ? !valueResult.equals(that.valueResult) : that.valueResult != null)
            return false;
        if (typeResult != null ? !typeResult.equals(that.typeResult) : that.typeResult != null)
            return false;
        if (jurisdictionResult != null ? !jurisdictionResult.equals(that.jurisdictionResult) : that.jurisdictionResult != null)
            return false;
        if (stageResult != null ? !stageResult.equals(that.stageResult) : that.stageResult != null)
            return false;
        if (bhResult != null ? !bhResult.equals(that.bhResult) : that.bhResult != null)
            return false;
        if (ownerTypeResult != null ? !ownerTypeResult.equals(that.ownerTypeResult) : that.ownerTypeResult != null)
            return false;
        if (workTypeResult != null ? !workTypeResult.equals(that.workTypeResult) : that.workTypeResult != null)
            return false;
        if (typeKey != null ? !typeKey.equals(that.typeKey) : that.typeKey != null) return false;
        if (typeValue != null ? !typeValue.equals(that.typeValue) : that.typeValue != null)
            return false;
        if (stageKey != null ? !stageKey.equals(that.stageKey) : that.stageKey != null)
            return false;
        if (stageValue != null ? !stageValue.equals(that.stageValue) : that.stageValue != null)
            return false;
        if (jurisdictionKey != null ? !jurisdictionKey.equals(that.jurisdictionKey) : that.jurisdictionKey != null)
            return false;
        return jurisdictionValue != null ? jurisdictionValue.equals(that.jurisdictionValue) : that.jurisdictionValue == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (locationSelect != null ? locationSelect.hashCode() : 0);
        result = 31 * result + (typeIdSelect != null ? typeIdSelect.hashCode() : 0);
        result = 31 * result + (typeIdInt != null ? typeIdInt.hashCode() : 0);
        result = 31 * result + (valueSelect != null ? valueSelect.hashCode() : 0);
        result = 31 * result + (valueMin != null ? valueMin.hashCode() : 0);
        result = 31 * result + (valueMax != null ? valueMax.hashCode() : 0);
        result = 31 * result + (updatedWithinSelect != null ? updatedWithinSelect.hashCode() : 0);
        result = 31 * result + (updatedWithin != null ? updatedWithin.hashCode() : 0);
        result = 31 * result + (biddingWithinSelect != null ? biddingWithinSelect.hashCode() : 0);
        result = 31 * result + (biddingWithin != null ? biddingWithin.hashCode() : 0);
        result = 31 * result + (jurisdictionSelect != null ? jurisdictionSelect.hashCode() : 0);
        result = 31 * result + (stageSelect != null ? stageSelect.hashCode() : 0);
        result = 31 * result + (bhSelect != null ? bhSelect.hashCode() : 0);
        result = 31 * result + (bh != null ? bh.hashCode() : 0);
        result = 31 * result + (ownerTypeSelect != null ? ownerTypeSelect.hashCode() : 0);
        result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
        result = 31 * result + (workTypeSelect != null ? workTypeSelect.hashCode() : 0);
        result = 31 * result + (workType != null ? workType.hashCode() : 0);
        result = 31 * result + (cLocationSelect != null ? cLocationSelect.hashCode() : 0);
        result = 31 * result + (cValueSelect != null ? cValueSelect.hashCode() : 0);
        result = 31 * result + (cJurisdictionSelect != null ? cJurisdictionSelect.hashCode() : 0);
        result = 31 * result + (cBiddingWithinSelect != null ? cBiddingWithinSelect.hashCode() : 0);
        result = 31 * result + (cTypeSelect != null ? cTypeSelect.hashCode() : 0);
        result = 31 * result + (updatedWithinResult != null ? updatedWithinResult.hashCode() : 0);
        result = 31 * result + (biddingWithinResult != null ? biddingWithinResult.hashCode() : 0);
        result = 31 * result + (valueResult != null ? valueResult.hashCode() : 0);
        result = 31 * result + (typeResult != null ? typeResult.hashCode() : 0);
        result = 31 * result + (jurisdictionResult != null ? jurisdictionResult.hashCode() : 0);
        result = 31 * result + (stageResult != null ? stageResult.hashCode() : 0);
        result = 31 * result + (bhResult != null ? bhResult.hashCode() : 0);
        result = 31 * result + (ownerTypeResult != null ? ownerTypeResult.hashCode() : 0);
        result = 31 * result + (workTypeResult != null ? workTypeResult.hashCode() : 0);
        result = 31 * result + (typeKey != null ? typeKey.hashCode() : 0);
        result = 31 * result + (typeValue != null ? typeValue.hashCode() : 0);
        result = 31 * result + (stageKey != null ? stageKey.hashCode() : 0);
        result = 31 * result + (stageValue != null ? stageValue.hashCode() : 0);
        result = 31 * result + (jurisdictionKey != null ? jurisdictionKey.hashCode() : 0);
        result = 31 * result + (jurisdictionValue != null ? jurisdictionValue.hashCode() : 0);
        return result;
    }

}
