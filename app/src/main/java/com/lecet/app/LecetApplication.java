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
Realm realm;
        Realm.init(this);
//
/*
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);

*/
    }


}
