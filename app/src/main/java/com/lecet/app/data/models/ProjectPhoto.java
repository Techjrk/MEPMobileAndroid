package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.interfaces.ProjectAdditionalData;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project Photo - Realm Object for storing Photos associated with a Project
 * Created by jasonm on 3/9/17.
 */

public class ProjectPhoto implements ProjectAdditionalData {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("pending")
    private boolean pending;

    @SerializedName("companyId")
    private long companyId;

    @SerializedName("projectId")
    private long projectId;

    @SerializedName("authorId")
    private long authorId;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("src")
    private String src;

    public ProjectPhoto(){}//TODO: Understand why this is not working.

    public ProjectPhoto(long id, String title, String text, boolean pending, long companyId, long projectId, long authorId, Date createdAt, Date updatedAt, String src) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.pending = pending;
        this.companyId = companyId;
        this.projectId = projectId;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.src = src;
    }

    public ProjectPhoto(long id, String title, String text, long companyId, long projectId, long authorId, Date createdAt, String src) {
        this(id, title, text, false, companyId, projectId, authorId, createdAt, createdAt, src);
    }

    public ProjectPhoto(long id, String title, String text, long companyId, long projectId, long authorId, Date createdAt) {
        this(id, title, text, false, companyId, projectId, authorId, createdAt, createdAt, null);
    }

    //GETTERS AND SETTERS
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

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

    public String getSrc() {return src;}

    public void setSrc(String src) {this.src = src;}
}
