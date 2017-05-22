package com.lecet.app;

import android.app.Application;

import io.realm.Realm;

/**
 * File: LecetApplication Created: 10/10/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class LecetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

    }


}
