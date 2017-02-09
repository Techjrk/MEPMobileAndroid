package com.lecet.app.interfaces;

import com.lecet.app.data.models.Project;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * File: MRADataSource Created: 10/24/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MRADataSource {
    void refreshRecentlyAddedProjects(LecetCallback<TreeMap<Long, TreeSet<Project>>> callback);
}

