package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ludwigvondrake on 3/17/17.
 */

public class ProjectAdditionalInfo extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    protected long id;

    protected String title;

    @SerializedName("text")
    protected String text;

    @SerializedName("pending")
    protected boolean pending;

    @SerializedName("companyId")
    protected long companyId;

    @SerializedName("projectId")
    protected long projectId;

    @SerializedName("authorId")
    protected long authorId;



    @SerializedName("createdAt")
    protected Date createdAt;

    @SerializedName("updatedAt")
    protected Date updatedAt;


    public ProjectAdditionalInfo(long id, String title, String text, boolean pending, long companyId, long projectId, long authorId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.pending = pending;
        this.companyId = companyId;
        this.projectId = projectId;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

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
