package com.lecet.app.interfaces;

import com.lecet.app.data.models.Project;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * File: MRUDataSource Created: 10/24/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MRUDataSource {
    void refreshRecentlyUpdatedProjects(LecetCallback<TreeMap<Long, TreeSet<Project>>> callback);
}
