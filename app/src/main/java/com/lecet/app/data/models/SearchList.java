package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Noel Anonas getdevsinc on 11/21/16.
 */

public class SearchList extends RealmObject {

    @SerializedName("code")
    private String code;
    @SerializedName("action")
    private boolean action;

   @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int userId;
    @SerializedName("projectId")
    private int projectId;
    @SerializedName("companyId")
    private int companyId;
    @SerializedName("projectTrackingListId")
    private int projectTrackingListId;
    @SerializedName("companyTrackingListId")
    private int companyTrackingListId;
    @SerializedName("createdAt")
    private String createdAt;

    public boolean isAction() {
        return action;
    }

    public int getUserId() {
        return userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getProjectTrackingListId() {
        return projectTrackingListId;
    }

    public int getCompanyTrackingListId() {
        return companyTrackingListId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @SerializedName("updatedAt")
    private  String updatedAt;

//    @SerializedName("robjects")
//    private String [] robjects;
//    private RealmList<SearchObject> robjects;
//    private SearchObject robjects;

    public int getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    public String getCode() {
        return code;
    }

/*
    public SearchObject getRobjects() {
        return robjects;
    }

    @Override

    public String toString() {
        return ".. SearchList Result.. {" +robjects+ '}';
    }
*/

}
