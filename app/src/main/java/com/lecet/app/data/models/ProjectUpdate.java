package com.lecet.app.data.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ProjectUpdate Created: 11/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectUpdate extends RealmObject {

    @PrimaryKey
    private long id;

    private String changeIndicator;
    private String attributeName;
    private String attributeValue;
    private String modelType;
    private long modelId;
    private String modelTitle;
    private String projectTitle;
    private String summary;
    private long companyId;
    private long contactId;
    private long projectId;

    private Bid bidUpdate;
    private Contact contactUpdate;
    private ProjectStage stageUpdate;
    private Project projectUpdate;

    public ProjectUpdate() {
    }

    public String getChangeIndicator() {
        return changeIndicator;
    }

    public void setChangeIndicator(String changeIndicator) {
        this.changeIndicator = changeIndicator;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public Bid getBidUpdate() {
        return bidUpdate;
    }

    public void setBidUpdate(Bid bidUpdate) {
        this.bidUpdate = bidUpdate;
    }

    public Contact getContactUpdate() {
        return contactUpdate;
    }

    public void setContactUpdate(Contact contactUpdate) {
        this.contactUpdate = contactUpdate;
    }

    public ProjectStage getStageUpdate() {
        return stageUpdate;
    }

    public void setStageUpdate(ProjectStage stageUpdate) {
        this.stageUpdate = stageUpdate;
    }

    public Project getProjectUpdate() {
        return projectUpdate;
    }

    public void setProjectUpdate(Project projectUpdate) {
        this.projectUpdate = projectUpdate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectUpdate)) return false;

        ProjectUpdate that = (ProjectUpdate) o;

        if (modelId != that.modelId) return false;
        if (id != that.id) return false;
        if (companyId != that.companyId) return false;
        if (contactId != that.contactId) return false;
        if (projectId != that.projectId) return false;
        if (changeIndicator != null ? !changeIndicator.equals(that.changeIndicator) : that.changeIndicator != null)
            return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        if (attributeValue != null ? !attributeValue.equals(that.attributeValue) : that.attributeValue != null)
            return false;
        if (modelType != null ? !modelType.equals(that.modelType) : that.modelType != null)
            return false;
        if (modelTitle != null ? !modelTitle.equals(that.modelTitle) : that.modelTitle != null)
            return false;
        if (projectTitle != null ? !projectTitle.equals(that.projectTitle) : that.projectTitle != null)
            return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (bidUpdate != null ? !bidUpdate.equals(that.bidUpdate) : that.bidUpdate != null)
            return false;
        if (contactUpdate != null ? !contactUpdate.equals(that.contactUpdate) : that.contactUpdate != null)
            return false;
        if (stageUpdate != null ? !stageUpdate.equals(that.stageUpdate) : that.stageUpdate != null)
            return false;
        return projectUpdate != null ? projectUpdate.equals(that.projectUpdate) : that.projectUpdate == null;

    }

    @Override
    public int hashCode() {
        int result = changeIndicator != null ? changeIndicator.hashCode() : 0;
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (attributeValue != null ? attributeValue.hashCode() : 0);
        result = 31 * result + (modelType != null ? modelType.hashCode() : 0);
        result = 31 * result + (int) (modelId ^ (modelId >>> 32));
        result = 31 * result + (modelTitle != null ? modelTitle.hashCode() : 0);
        result = 31 * result + (projectTitle != null ? projectTitle.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (companyId ^ (companyId >>> 32));
        result = 31 * result + (int) (contactId ^ (contactId >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (bidUpdate != null ? bidUpdate.hashCode() : 0);
        result = 31 * result + (contactUpdate != null ? contactUpdate.hashCode() : 0);
        result = 31 * result + (stageUpdate != null ? stageUpdate.hashCode() : 0);
        result = 31 * result + (projectUpdate != null ? projectUpdate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectUpdate{" +
                "changeIndicator='" + changeIndicator + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", modelType='" + modelType + '\'' +
                ", modelId=" + modelId +
                ", modelTitle='" + modelTitle + '\'' +
                ", projectTitle='" + projectTitle + '\'' +
                ", summary='" + summary + '\'' +
                ", id=" + id +
                ", companyId=" + companyId +
                ", contactId=" + contactId +
                ", projectId=" + projectId +
                ", bidUpdate=" + bidUpdate +
                ", contactUpdate=" + contactUpdate +
                ", stageUpdate=" + stageUpdate +
                ", projectUpdate=" + projectUpdate +
                '}';
    }
}
