package com.lecet.app.interfaces;

import com.lecet.app.domain.BidDomain;

/**
 * File: MRUDelegate Created: 10/24/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MRUDelegate {
    void mruBidGroupSelected(@BidDomain.BidGroup int group);
}
