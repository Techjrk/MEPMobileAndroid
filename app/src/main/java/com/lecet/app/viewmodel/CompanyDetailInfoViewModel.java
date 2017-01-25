package com.lecet.app.viewmodel;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * File: CompanyDetailInfoViewModel Created: 1/24/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailInfoViewModel {

    @Retention(SOURCE)
    @IntDef({INFO_PHONE, INFO_EMAIL, INFO_WEB})
    public @interface InfoType {}
    public static final int INFO_PHONE = 0;
    public static final int INFO_EMAIL = 1;
    public static final int INFO_WEB = 2;

    private final AppCompatActivity appCompatActivity;
    private final String info;
    @InfoType
    private final int infoType;


    public CompanyDetailInfoViewModel(AppCompatActivity appCompatActivity, String info, @InfoType int infoType) {
        this.appCompatActivity = appCompatActivity;
        this.info = info;
        this.infoType = infoType;
    }

    public String getInfo() {
        return info;
    }

    public Drawable getInfoDrawable() {

        switch (infoType) {

            case INFO_PHONE:
                return ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_phone);
            case INFO_EMAIL:
                return ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_email);
            case INFO_WEB:
                return ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_globe);
            default:
                return ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_phone);
        }
    }

}
