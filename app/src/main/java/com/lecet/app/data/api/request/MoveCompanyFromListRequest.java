package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * File: MoveCompanyFromListRequest Created: 11/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class MoveCompanyFromListRequest {

    @SerializedName("itemIds")
    private List<Long> itemIds;

    public MoveCompanyFromListRequest(@NonNull List<Long> itemIds) {

        this.itemIds = itemIds;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveCompanyFromListRequest)) return false;

        MoveCompanyFromListRequest that = (MoveCompanyFromListRequest) o;

        return itemIds.equals(that.itemIds);

    }

    @Override
    public int hashCode() {
        return itemIds.hashCode();
    }

    @Override
    public String toString() {
        return "MoveProjectFromListRequest{" +
                "itemIds=" + itemIds +
                '}';
    }
}

