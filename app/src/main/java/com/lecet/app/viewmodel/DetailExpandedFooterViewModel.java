package com.lecet.app.viewmodel;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;

/**
 * File: DetailExpandedFooterViewModel Created: 2/20/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class DetailExpandedFooterViewModel extends DetailFooterViewModel {

    public DetailExpandedFooterViewModel(@NonNull AppCompatActivity appCompatActivity, @NonNull String type, @NonNull String total) {
        super(appCompatActivity, type, total);
    }

    @Override
    public String getTitle() {

        if (isExpanded()) {

            return getAppCompatActivity().getString(R.string.hide);

        } else {

            return getAppCompatActivity().getString(R.string.see_all);
        }
    }
}
