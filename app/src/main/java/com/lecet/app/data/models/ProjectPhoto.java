package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;


import com.lecet.app.interfaces.ProjectUserCreatedContent;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project Photo - Realm Object for storing Photos associated with a Project
 * Created by jasonm on 3/9/17.
 */

public class ProjectPhoto extends RealmObject implements ProjectUserCreatedContent {

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

    @SerializedName("userId")
    private long authorId;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("src")
    private String src;

    @SerializedName("url")
    private String url;

    @SerializedName("user")
    private User author;

    @SerializedName("geocode")
    private Geocode geocode;

    @SerializedName("fullAddress")
    private String fullAddress;

    public ProjectPhoto(){}

    public ProjectPhoto(long id, String title, String text, boolean pending, long companyId, long projectId, long authorId, Date createdAt, Date updatedAt, String src, String url, Geocode geocode, String fullAddress) {
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
        this.url = url;
        this.geocode = geocode;
        this.fullAddress = fullAddress;
    }

    public ProjectPhoto(long id, String title, String text, long companyId, long projectId, long authorId, Date createdAt, String src, String url) {
        this(id, title, text, false, companyId, projectId, authorId, createdAt, createdAt, src, url, null, null);
    }

    public ProjectPhoto(long id, String title, String text, long companyId, long projectId, long authorId, Date createdAt, String src) {
        this(id, title, text, false, companyId, projectId, authorId, createdAt, createdAt, src, null, null, null);
    }

    public ProjectPhoto(long id, String title, String text, long companyId, long projectId, long authorId, Date createdAt) {
        this(id, title, text, false, companyId, projectId, authorId, createdAt, createdAt, null, null, null, null);
    }

    //Getters And Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public User getAuthor(){
        return author;
    }

    public void setAuthor(User author){
        author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Geocode getGeocode() {
        return geocode;
    }

    public void setGeocode(Geocode geocode) {
        this.geocode = geocode;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public int compareTo(@NonNull ProjectUserCreatedContent other) {
        if (other instanceof ProjectNote) {
            return (int) (createdAt.getTime() - ((ProjectNote) other).getCreatedAt().getTime());
        } else {
            return (int) (id - ((ProjectPhoto) other).getId());
        }
    }

    @Override
    public String toString() {
        return "ProjectPhoto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", pending=" + pending +
                ", companyId=" + companyId +
                ", projectId=" + projectId +
                ", authorId=" + authorId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", url='" + url + '\'' +
                ", author=" + author +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectPhoto that = (ProjectPhoto) o;

        if (id != that.id) return false;
        if (pending != that.pending) return false;
        if (companyId != that.companyId) return false;
        if (projectId != that.projectId) return false;
        if (authorId != that.authorId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null)
            return false;
        if (src != null ? !src.equals(that.src) : that.src != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return author != null ? author.equals(that.author) : that.author == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (pending ? 1 : 0);
        result = 31 * result + (int) (companyId ^ (companyId >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (int) (authorId ^ (authorId >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (src != null ? src.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}
