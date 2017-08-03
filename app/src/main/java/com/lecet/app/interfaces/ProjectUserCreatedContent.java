package com.lecet.app.interfaces;

import com.lecet.app.data.models.User;

import java.util.Date;

/**
 * Created by jasonm on 8/3/17.
 */

public interface ProjectUserCreatedContent extends Comparable<ProjectUserCreatedContent> {

    long getId();

    void setId(long id);

    String getTitle();

    void setTitle(String title);

    String getText();

    void setText(String text);

    long getCompanyId();

    void setCompanyId(long companyId);

    long getProjectId();

    void setProjectId(long projectId);

    long getAuthorId();

    void setAuthorId(long authorId);

    User getAuthor();

    void setAuthor(User author);

    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    Date getUpdatedAt();

    void setUpdatedAt(Date updatedAt);

}
