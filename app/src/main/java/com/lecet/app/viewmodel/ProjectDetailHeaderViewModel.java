package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Project;

/**
 * File: ProjectDetailHeaderViewModel Created: 11/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailHeaderViewModel {

    private final Project project;


    public ProjectDetailHeaderViewModel(Project project) {
        this.project = project;
    }

    public String getTitle() {

        return project.getTitle();
    }

    public String getAddress() {

        String address = "";

        if (project.getAddress1() != null) {

            address = project.getAddress1();
        }

        if (project.getState() != null) {

            address = address + " " + project.getState();
        }

        return address;
    }
}
