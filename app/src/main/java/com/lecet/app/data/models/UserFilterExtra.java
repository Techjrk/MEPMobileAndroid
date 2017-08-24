package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by getdevsinc on 8/22/17.
 */
//Note: This class is used to store/hold the selected extra or multi-item part of Project Type, Jurisdiction and Stage and this class will be used as RealmList type for the section mentioned.
public class UserFilterExtra extends RealmObject {
    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    @SerializedName("valueStage")
    private String valueStage;

    public String getValueStage() {
        return valueStage;
    }

    public void setValueStage(String valueStage) {
        this.valueStage = valueStage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserFilterExtra{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", valueStage='" + valueStage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFilterExtra that = (UserFilterExtra) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return valueStage != null ? valueStage.equals(that.valueStage) : that.valueStage == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (valueStage != null ? valueStage.hashCode() : 0);
        return result;
    }
}
