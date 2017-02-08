package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by domandtom on 11/21/16.
 */

public class SearchResult {

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

    @SerializedName("updatedAt")
    private  String updatedAt;

    @SerializedName("project")
    private Project project;

    @SerializedName("company")
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

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

    public int getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Project getProject() {
        return project;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setProjectTrackingListId(int projectTrackingListId) {
        this.projectTrackingListId = projectTrackingListId;
    }

    public void setCompanyTrackingListId(int companyTrackingListId) {
        this.companyTrackingListId = companyTrackingListId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (action != that.action) return false;
        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (projectId != that.projectId) return false;
        if (companyId != that.companyId) return false;
        if (projectTrackingListId != that.projectTrackingListId) return false;
        if (companyTrackingListId != that.companyTrackingListId) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null)
            return false;
        return project != null ? project.equals(that.project) : that.project == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (action ? 1 : 0);
        result = 31 * result + id;
        result = 31 * result + userId;
        result = 31 * result + projectId;
        result = 31 * result + companyId;
        result = 31 * result + projectTrackingListId;
        result = 31 * result + companyTrackingListId;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "code='" + code + '\'' +
                ", action=" + action +
                ", id=" + id +
                ", userId=" + userId +
                ", projectId=" + projectId +
                ", companyId=" + companyId +
                ", projectTrackingListId=" + projectTrackingListId +
                ", companyTrackingListId=" + companyTrackingListId +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", project=" + project +
                '}';
    }
}
