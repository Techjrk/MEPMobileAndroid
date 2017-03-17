package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project Photo - Realm Object for storing Photos associated with a Project
 * Created by jasonm on 3/9/17.
 */

public class ProjectPhoto extends ProjectAdditionalInfo {
    //VARIABLES
    @SerializedName("src")
    private String src;

    public ProjectPhoto(long id, String title, String text, boolean pending, long companyId, long projectId, long authorId, Date createdAt, Date updatedAt, String src) {
        super(id, title, text, pending, companyId, projectId, authorId, createdAt, updatedAt);
        this.src = src;
    }

    public ProjectPhoto(long id, String title, String text, long companyId, long projectId, long authorId, Date createdAt, String src){
        super(id, title, text, false, companyId, projectId, authorId, createdAt, createdAt);
        this.src = src;
    }

    //GETTERS AND SETTERS
    public String getSrc() {return src;}

    public void setSrc(String src) {
        this.src = src;
    }

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
}
