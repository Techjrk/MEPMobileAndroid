package com.lecet.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * File: LecetApplication Created: 10/10/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class LecetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//TODO: Testing - Deleting existing database - Migration needed for new RealmObject created - Noel
        Realm.init(this);

//        RealmConfiguration config = new RealmConfiguration
//                .Builder()
//                .deleteRealmIfMigrationNeeded()
//                .build();
//
//        Realm realm = Realm.getInstance(config);
    }

}
