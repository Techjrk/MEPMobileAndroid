package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project Note - Realm Object for storing Notes associated with a Project
 * Created by jasonm on 3/9/17.
 */

public class ProjectNote extends ProjectAdditionalInfo {
    //VARIABLES
    @SerializedName("isPublic")
    private boolean isPublic = true;

    public ProjectNote(long id, String title, String text, boolean pending, long companyId, long projectId, long authorId, Date createdAt, Date updatedAt) {
        super(id, title, text, pending, companyId, projectId, authorId, createdAt, updatedAt);
    }

    //VARIABLES
    public boolean isPublic() {return isPublic;}

    public void setPublic(boolean aPublic) {isPublic = aPublic;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title = title;}

    public String getText() {return text;}

    public void setText(String text) {this.text = text;}

    public boolean isPending() {return pending;}

    public void setPending(boolean pending) {this.pending = pending;}

    public long getCompanyId() {return companyId;}

    public void setCompanyId(long companyId) {this.companyId = companyId;}

    public long getProjectId() {return projectId;}

    public void setProjectId(long projectId) {this.projectId = projectId;}

    public long getAuthorId() {return authorId;}

    public void setAuthorId(long authorId) {this.authorId = authorId;}

    public Date getCreatedAt() {return createdAt;}

    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}

    public Date getUpdatedAt() {return updatedAt;}

    public void setUpdatedAt(Date updatedAt) {this.updatedAt = updatedAt;}

    @Override
    public String toString() {
        return "ProjectNote{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isPublic=" + isPublic +
                ", pending=" + pending +
                ", companyId=" + companyId +
                ", projectId=" + projectId +
                ", authorId=" + authorId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectNote that = (ProjectNote) o;

        if (id != that.id) return false;
        if (isPublic != that.isPublic) return false;
        if (pending != that.pending) return false;
        if (companyId != that.companyId) return false;
        if (projectId != that.projectId) return false;
        if (authorId != that.authorId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (isPublic ? 1 : 0);
        result = 31 * result + (pending ? 1 : 0);
        result = 31 * result + (int) (companyId ^ (companyId >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (int) (authorId ^ (authorId >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

}
