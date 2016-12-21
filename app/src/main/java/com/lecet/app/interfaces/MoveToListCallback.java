package com.lecet.app.interfaces;

import io.realm.RealmObject;

/**
 * File: MoveToListCallback Created: 12/12/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface MoveToListCallback<T extends RealmObject & TrackingListObject> {

    void onTrackingListClicked(T trackingList);
}
