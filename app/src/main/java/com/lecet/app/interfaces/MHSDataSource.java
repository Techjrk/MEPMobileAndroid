package com.lecet.app.interfaces;

import com.lecet.app.data.models.Project;

/**
 * File: MHSDataSource Created: 10/11/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MHSDataSource {

    void refreshProjectsHappeningSoon(LecetCallback<Project[]> callback);
}
