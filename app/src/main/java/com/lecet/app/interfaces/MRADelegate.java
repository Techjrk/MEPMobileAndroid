package com.lecet.app.interfaces;

import com.lecet.app.domain.BidDomain;

/**
 * File: MRADelegate Created: 10/24/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MRADelegate {
    void mraBidGroupSelected(@BidDomain.BidGroup int group);
}
