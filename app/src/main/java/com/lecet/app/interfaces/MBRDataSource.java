package com.lecet.app.interfaces;

import com.lecet.app.data.models.Bid;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * File: MBRDataSource Created: 10/11/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MBRDataSource {
    void refreshRecentlyMadeBids(LecetCallback<TreeMap<Long, TreeSet<Bid>>> callback);
}